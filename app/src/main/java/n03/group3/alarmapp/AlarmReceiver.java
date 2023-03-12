package n03.group3.alarmapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver  extends BroadcastReceiver {
    MediaPlayer mp;
    @Override
    public void onReceive(Context context, Intent intent) {
        mp = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
        String activate = intent.getExtras().getString("activateAlarm");
        if (activate.equals("on")) {
            mp.setLooping(true);
            mp.start();
        }
        else {
            mp.stop();
            mp.reset();
        }

        Intent toNotificationIntent = new Intent(context, NotificationActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, toNotificationIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "alarmChannel")
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle("Wake Up Wake Up!!!")
                .setContentText("Another day has come! Ready for it?")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123, builder.build());
    }
}
