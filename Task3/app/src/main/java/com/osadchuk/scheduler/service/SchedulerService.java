package com.osadchuk.scheduler.service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.osadchuk.scheduler.R;
import com.osadchuk.scheduler.activity.SettingsActivity;
import java.util.Timer;
import java.util.TimerTask;



public class SchedulerService extends Service {

    private final String TAG = SchedulerService.class.getCanonicalName();
    //private final int CHECK_INTERVAL = 30 * 60 * 1000;
    private final int CHECK_INTERVAL = 10 * 1000;
    private final int NOTIFICATION_ID=0;
    private final int PENDING_SETTING=0;
    private final int START_TIME=0;
    private Timer timer;
    private TimerTask timerTask;

    @Override
    public void onCreate() {

        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    private void showNotification(String message) {

        Context notifyContext=getApplicationContext();
        Intent notifyIntent = new Intent(notifyContext, SettingsActivity.class);

        PendingIntent contentIntent = PendingIntent.getActivity
                (notifyContext, PENDING_SETTING,notifyIntent,PENDING_SETTING);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(notifyContext);

        builder.setContentIntent(contentIntent)
                .setContentTitle(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis());

        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager)
                notifyContext.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final String message;

        Log.d(TAG, "onStartCommand");

        if(intent==null) {
            SharedPreferences preferences = getApplicationContext().getSharedPreferences("prefs",
                    Context.MODE_PRIVATE);
            message = preferences.getString("message", getApplicationContext().
                    getString(R.string.default_notification_message));
        }else
            message=intent.getStringExtra("message");

        timer = new Timer();
        timerTask = new TimerTask() {

            @Override
            public void run() {
                        showNotification(message);
            }
        };

        timer.schedule(timerTask, START_TIME, CHECK_INTERVAL);

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if (timer != null) {
            timerTask.cancel();
            timer.purge();
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return null;
    }

}
