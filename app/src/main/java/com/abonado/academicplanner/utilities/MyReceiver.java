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
    String channel_id2 = "test2";
    static int notification;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");

        if(intent.getAction() != null) {

            String action = intent.getAction();


            if (action.equals("startDateNotify")){

                Toast.makeText(context, intent.getStringExtra("startAsmntKey"),
                        Toast.LENGTH_LONG).show();
                createNotificationChannel(context, channel_id);
                Notification n = new NotificationCompat.Builder(context, channel_id)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentText(intent.getStringExtra("startAsmntKey"))
                        .setContentTitle("Start Date").build();
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(notification++, n);
            }

            if (action.equals("endDateNotify")) {
                Toast.makeText(context, intent.getStringExtra("endAsmntKey"),
                        Toast.LENGTH_LONG).show();
                createNotificationChannel(context, channel_id2);
                Notification n = new NotificationCompat.Builder(context, channel_id2)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentText(intent.getStringExtra("endAsmntKey"))
                        .setContentTitle("End Date").build();
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(notification++, n);

            }






        }

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