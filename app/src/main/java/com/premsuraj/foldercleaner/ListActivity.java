package com.premsuraj.foldercleaner;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.premsuraj.foldercleaner.model.DataModel;
import com.premsuraj.foldercleaner.model.DataModelManager;

import java.util.List;

public class ListActivity extends AppCompatActivity implements ListAdapter.OnDeleteClickListener {

    static abstract class ITEMS {
        public static final int FOLDERS = 1;
        public static final int IGNORED = 2;
    }

    public static final String KEY_WHICH = "which_to_show";

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DataModelManager mDataManager;
    private DataModel mDataModel;

    private int which = ITEMS.FOLDERS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        which = getIntent().getIntExtra(KEY_WHICH, ITEMS.FOLDERS);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mDataManager = new DataModelManager(this);
        mDataModel = mDataManager.get();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(which == ITEMS.FOLDERS)
            mRecyclerView.setAdapter(new ListAdapter(mDataModel.getFoldersToClean(), this));
        else if(which == ITEMS.IGNORED)
            mRecyclerView.setAdapter(new ListAdapter(mDataModel.getTypesToIgnore(), this));
    }

    @Override
    public void onDeleteClicked(final int position, String itemValue) {
        final String item;
        if(which == ITEMS.FOLDERS) {
            final List<String> foldersToClean = mDataModel.getFoldersToClean();
            item = foldersToClean.remove(position);
            ((ListAdapter) mRecyclerView.getAdapter()).setItems(foldersToClean);
            mRecyclerView.getAdapter().notifyItemRemoved(position);
            mDataModel.setFoldersToClean(foldersToClean);
        } else if(which == ITEMS.IGNORED) {
            final List<String> typesToIgnore = mDataModel.getTypesToIgnore();
            item  = typesToIgnore.remove(position);
            ((ListAdapter) mRecyclerView.getAdapter()).setItems(typesToIgnore);
            mRecyclerView.getAdapter().notifyItemRemoved(position);
            mDataModel.setTypesToIgnore(typesToIgnore);
        } else {
            return;
        }

        final Snackbar snackbar = Snackbar.make(mRecyclerView, "Deleted", Snackbar.LENGTH_LONG);
        snackbar.setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(which == ITEMS.FOLDERS) {
                    final List<String> foldersToClean = mDataModel.getFoldersToClean();
                    foldersToClean.add(position, item);
                    ((ListAdapter) mRecyclerView.getAdapter()).setItems(foldersToClean);
                    mRecyclerView.getAdapter().notifyItemInserted(position);
                    mDataModel.setFoldersToClean(foldersToClean);
                } else if(which == ITEMS.IGNORED) {
                    final List<String> typesToIgnore = mDataModel.getTypesToIgnore();
                    typesToIgnore.add(position, item);
                    ((ListAdapter) mRecyclerView.getAdapter()).setItems(typesToIgnore);
                    mRecyclerView.getAdapter().notifyItemInserted(position);
                    mDataModel.setTypesToIgnore(typesToIgnore);
                }
            }
        }).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDataManager.update(mDataModel);
    }
}
