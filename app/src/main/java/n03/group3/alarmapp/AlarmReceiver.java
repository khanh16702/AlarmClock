package n03.group3.alarmapp;

import static android.content.Context.ACTIVITY_SERVICE;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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
        }
        else if (string.equals("off")) {
            context.stopService(mIntent);
            MainActivity.activeAlarm = "";
        }
    }
}
