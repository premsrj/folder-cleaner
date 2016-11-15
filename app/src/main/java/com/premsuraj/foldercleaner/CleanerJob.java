package com.premsuraj.foldercleaner;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;

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
                Notifier.notify(CleanerJob.this, "Files cleaned", "Cleaned " + filesCleaned + " files");
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
