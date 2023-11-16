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

public class Receiver extends BroadcastReceiver {

    String assessments_channel_id = "assessments";
    String course_channel_id = "courses";
    String term_channel_id = "terms";
    static int notification;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");

        if(intent.getAction() != null) {

            String action = intent.getAction();


            if (action.equals("asmntStartDateNotify")){

                Toast.makeText(context, intent.getStringExtra("startAsmntKey"),
                        Toast.LENGTH_LONG).show();
                createNotificationChannel(context, assessments_channel_id);
                Notification n = new NotificationCompat.Builder(context, assessments_channel_id)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentText(intent.getStringExtra("startAsmntKey"))
                        .setContentTitle("ASSESSMENT START Date").build();
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(notification++, n);
            }

            if (action.equals("asmntEndDateNotify")) {
                Toast.makeText(context, intent.getStringExtra("endAsmntKey"),
                        Toast.LENGTH_LONG).show();
                createNotificationChannel(context, assessments_channel_id);
                Notification n = new NotificationCompat.Builder(context, assessments_channel_id)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentText(intent.getStringExtra("endAsmntKey"))
                        .setContentTitle("ASSESSMENT END Date").build();
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(notification++, n);

            }

            if (action.equals("courseStartDateNotify")){

                Toast.makeText(context, intent.getStringExtra("startCourseKey"),
                        Toast.LENGTH_LONG).show();
                createNotificationChannel(context, course_channel_id);
                Notification n = new NotificationCompat.Builder(context, course_channel_id)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentText(intent.getStringExtra("startCourseKey"))
                        .setContentTitle("COURSE START Date").build();
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(notification++, n);
            }

            if(action.equals("courseEndDateNotify")){

                Toast.makeText(context, intent.getStringExtra("courseEndKey"),
                        Toast.LENGTH_LONG).show();
                createNotificationChannel(context, course_channel_id);
                Notification n = new NotificationCompat.Builder(context, course_channel_id)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentText(intent.getStringExtra("courseEndKey"))
                        .setContentTitle("COURSE END Date").build();
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(notification++, n);

            }

            if(action.equals("termStartDateNotify")){

                Toast.makeText(context, intent.getStringExtra("startTermKey"),
                        Toast.LENGTH_LONG).show();
                createNotificationChannel(context, term_channel_id);
                Notification n = new NotificationCompat.Builder(context, term_channel_id)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentText(intent.getStringExtra("startTermKey"))
                        .setContentTitle("TERM START Date").build();
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(notification++, n);

            }

            if(action.equals("termEndDateNotify")){

                Toast.makeText(context, intent.getStringExtra("termEndKey"),
                        Toast.LENGTH_LONG).show();
                createNotificationChannel(context, term_channel_id);
                Notification n = new NotificationCompat.Builder(context, term_channel_id)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentText(intent.getStringExtra("termEndKey"))
                        .setContentTitle("TERM END Date").build();
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