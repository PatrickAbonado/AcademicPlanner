package com.abonado.academicplanner.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.abonado.academicplanner.R;

public class MyReceiver extends BroadcastReceiver {

    String channel_id = "test";
    static int notificationId;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");

        Toast.makeText(context, intent.getStringExtra("startAsmntKey"),
                Toast.LENGTH_LONG).show();
        createNotificationChannel(context, channel_id);
        Notification n = new NotificationCompat.Builder(context,channel_id)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentText(intent.getStringExtra("startAsmntKey"))
                .setContentTitle("NotificationTest").build();
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId++, n);


        Toast.makeText(context, intent.getStringExtra("endAsmntKey"),
                Toast.LENGTH_LONG).show();
        createNotificationChannel(context, channel_id);
        n = new NotificationCompat.Builder(context,channel_id)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentText(intent.getStringExtra("endAsmntKey"))
                .setContentTitle("NotificationTest").build();
        notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId++, n);

    }

    private void createNotificationChannel(Context context, String CHANNEL_ID) {


        /* CharSequence name="mychannelname";
        String description="mychanneldescription";*/

        CharSequence name = context.getResources().getString(R.string.channel_name);

        String description = context.getString(R.string.channel_description);

        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);

        channel.setDescription(description);

        NotificationManager notificationManager =
                context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

    }
}