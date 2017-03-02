package com.osadchuk.scheduler.service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.osadchuk.scheduler.R;
import com.osadchuk.scheduler.activity.SettingsActivity;
import java.util.Timer;
import java.util.TimerTask;

public class SchedulerService extends Service {

    private final String TAG = SchedulerService.class.getCanonicalName();

    private Timer timer;
    private TimerTask timerTask;
    //private final int CHECK_INTERVAL = 30 * 60 * 1000;
    private final int CHECK_INTERVAL = 10 * 1000;

    @Override
    public void onCreate() {

        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    private void showNotification(String message) {
        Context notifyContext=getApplicationContext();
        Intent notifyIntent = new Intent(notifyContext, SettingsActivity.class);

        PendingIntent contentIntent = PendingIntent.getActivity
                (notifyContext, 0,notifyIntent, PendingIntent.FLAG_ONE_SHOT);

        Notification.Builder builder = new Notification.Builder(notifyContext);
        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentText(message);
        Notification notify = builder.build();
        NotificationManager notificationManager = (NotificationManager)
                notifyContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notify);
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                showNotification(intent.getStringExtra("message"));
            }
        };
        timer.schedule(timerTask, 0, CHECK_INTERVAL);

        return super.onStartCommand(intent, flags, startId);
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
