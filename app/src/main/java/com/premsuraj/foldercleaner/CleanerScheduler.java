package com.premsuraj.foldercleaner;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.text.format.DateUtils;

/**
 * Created by Premsuraj
 */

public class CleanerScheduler {

    public static boolean schedule(Activity activity) {
        JobScheduler scheduler = (JobScheduler) activity.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        JobInfo.Builder jobBuilder = new JobInfo.Builder(1,
                new ComponentName(activity.getPackageName(), CleanerJob.class.getName()));
        jobBuilder.setPersisted(true)
                .setRequiresCharging(true)
                .setOverrideDeadline(DateUtils.DAY_IN_MILLIS);
//                .setRequiresDeviceIdle(true);

        return (scheduler.schedule(jobBuilder.build()) ==  JobScheduler.RESULT_SUCCESS);
    }
}
