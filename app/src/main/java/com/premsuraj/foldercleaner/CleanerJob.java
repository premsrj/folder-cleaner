package com.premsuraj.foldercleaner;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import com.premsuraj.foldercleaner.model.DataModelManager;

/**
 * Created by Premsuraj
 */

public class CleanerJob extends JobService {
    private JobParameters mJobParameters;
    private AsyncTask mCleanerTask;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        this.mJobParameters = jobParameters;
        this.mCleanerTask = new CleanerTask(this, new CleanerTask.Listener() {
            @Override
            public void onCleanerTaskDone(boolean isSuccess, int filesCleaned) {
                jobFinished(mJobParameters, true);
                if (filesCleaned > 0)
                    Notifier.notify(CleanerJob.this, "Files cleaned", "Cleaned " + filesCleaned + " files");

                try {
                    SharedPreferences preferences = getSharedPreferences(DataModelManager.SHARED_PREF_NAME, MODE_PRIVATE);
                    long lastTime = preferences.getLong("lastclean", 0);
                    if (lastTime != 0) {
                        long timeSinceSecs = (lastTime - System.currentTimeMillis()) / 1000;

                        Bundle params = new Bundle();
                        params.putLong(FirebaseAnalytics.Param.VALUE, timeSinceSecs);
                        FirebaseAnalytics.getInstance(CleanerJob.this)
                                .logEvent("seconds_since_clean", params);
                    }
                    preferences.edit().putLong("lastclean", System.currentTimeMillis()).apply();
                } catch (Exception e) {
                    FirebaseCrash.report(e);
                }
            }
        }).execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        mCleanerTask.cancel(true);
        return true;
    }
}
