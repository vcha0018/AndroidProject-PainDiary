package com.monash.paindiary.helper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.navigation.NavDeepLinkBuilder;

import com.monash.paindiary.R;
import com.monash.paindiary.activities.AppActivity;

public class ReminderBroadcast extends BroadcastReceiver {
    private final String NOTIFICATION_CHANNEL = "notifyPainEntry";
    @Override
    public void onReceive(Context context, Intent intent) {
        setNotificationTemp(context);

        MediaPlayer mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_NOTIFICATION_URI);
        mediaPlayer.start();
    }

    private void setNotificationTemp(Context context) {
        createNotificationChannel(context);

        // Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(context, AppActivity.class);
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);

        Bundle bundle = new Bundle();
        bundle.putBoolean("isFromNotification", true);
        // Get the PendingIntent containing the entire back stack
        PendingIntent pendingIntent = new NavDeepLinkBuilder(context)
                .setComponentName(AppActivity.class)
                .setGraph(R.navigation.mobile_navigation)
                .setDestination(R.id.nav_pain_data_entry_fragment)
                .setArguments(bundle)
                .createPendingIntent();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.ic_baseline_doorbell_24)
                .setContentTitle("Pain entry Reminder")
                .setContentText("Add your pain entry for today.")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        notificationManagerCompat.notify(0, builder.build());
    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_CHANNEL, importance);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
