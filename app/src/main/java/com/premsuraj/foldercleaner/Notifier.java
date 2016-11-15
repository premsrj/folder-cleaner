package com.premsuraj.foldercleaner;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Created by Premsuraj
 */

public class Notifier {

    public static void notify(Context context, String title, String message) {

        Intent openActivityIntent = new Intent(context, MainActivity.class);
        openActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent mainActivityPendingIntent = PendingIntent.getActivity(context, 101, openActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        final Notification notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setOngoing(false)
                .setAutoCancel(true)
                .setContentIntent(mainActivityPendingIntent).build();

        NotificationManagerCompat.from(context).notify(12, notificationBuilder);
    }
}
