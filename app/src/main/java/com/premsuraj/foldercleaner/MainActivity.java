package com.premsuraj.foldercleaner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.premsuraj.foldercleaner.model.DataModelManager;

public class MainActivity extends AppCompatActivity {
    boolean mHasPermission = false;
    ProgressBar mProgressBar;
    private DataModelManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        if(hasPermission()) {
            mHasPermission = true;
            scheduleJob();
        } else {
            mHasPermission = false;
            askPermission();
        }

        findViewById(R.id.folders_to_clean).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra(ListActivity.KEY_WHICH, ListActivity.ITEMS.FOLDERS);
                startActivity(intent);
            }
        });

        findViewById(R.id.types_to_ignore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra(ListActivity.KEY_WHICH, ListActivity.ITEMS.IGNORED);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        EditText edtDaysToKeep = (EditText) findViewById(R.id.edt_days_to_keep);
        try {
            getDataModelManager().get().setDaysToKeep(Integer.parseInt(edtDaysToKeep.getText().toString()));
        } catch (Exception ignored) {

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUi();
    }

    private void scheduleJob() {
        if(CleanerScheduler.schedule(MainActivity.this)) {
            createSuccessSnackbar("Job scheduled successfully");
        } else {
            createErrorSnackbar("Failed to schedule task");
        }
    }

    private void createSuccessSnackbar(String text) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_main),
                text,
                Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void createErrorSnackbar(String text) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_main),
                text,
                Snackbar.LENGTH_LONG);
        TextView textView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.RED);
        snackbar.show();
    }

    private boolean hasPermission() {
        if(Build.VERSION.SDK_INT >= 23) {
            return checkSelfPermission(Manifest.permission_group.STORAGE) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(this,
                new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(permissions.length == 0) {
            mHasPermission = false;
        } else if(grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            mHasPermission = true;
            scheduleJob();
        } else {
            mHasPermission = false;
        }
        updateUi();
    }

    private void updateUi() {
        if(mHasPermission) {
            setupButtonWithPermission();
        } else {
            setupButtonWithoutPermission();
        }

        int daysToKeep = getDataModelManager().get().getDaysToKeep();
        EditText edtDaysToKeep = (EditText) findViewById(R.id.edt_days_to_keep);
        edtDaysToKeep.setText("" + daysToKeep);
    }

    private DataModelManager getDataModelManager() {
        if(mDataManager != null) {
            return mDataManager;
        }

        mDataManager = new DataModelManager(this);
        return mDataManager;
    }

    private void setupButtonWithPermission() {
        Button cleanNow = (Button) findViewById(R.id.btn_clean_now);
        cleanNow.setText(R.string.clean_now);
        cleanNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressBar.setVisibility(View.VISIBLE);
                new CleanerTask(MainActivity.this, new CleanerTask.Listener() {
                    @Override
                    public void onCleanerTaskDone(boolean isSuccess, int filesCleaned) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        if(isSuccess) {
                            createSuccessSnackbar("Cleaned " + filesCleaned + " files");
                        } else {
                            createErrorSnackbar("Failed to clean files");
                        }
                    }
                }).execute();
            }
        });
    }

    private void setupButtonWithoutPermission() {
        Button cleanNow = (Button) findViewById(R.id.btn_clean_now);
        cleanNow.setText(R.string.grant_permission);
        cleanNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askPermission();
            }
        });
    }
}
