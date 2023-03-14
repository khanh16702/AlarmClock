package n03.group3.alarmapp;

import static android.content.Context.ACTIVITY_SERVICE;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isRunning = false;
        String string = intent.getExtras().getString("extra");

        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (Music.class.getName().equals(service.service.getClassName())) {
                isRunning = true;
            }
        }
        Intent mIntent = new Intent(context, Music.class);
        if (string.equals("on") && !isRunning) {
            context.startService(mIntent);
            MainActivity.activeAlarm = intent.getExtras().getString("active");

            Intent openAppIntent = new Intent(context, NotificationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context, 0, openAppIntent, 0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "alarmChannel")
                    .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                    .setContentTitle("Wake Up Wake Up!!!")
                    .setContentText(intent.getExtras().getString("alarmName"))
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent);
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            notificationManagerCompat.notify(123, builder.build());
        }
        else if (string.equals("off")) {
            context.stopService(mIntent);
            MainActivity.activeAlarm = "";
        }
    }
}
