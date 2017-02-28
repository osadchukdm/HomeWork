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

    private final int CMD_START = 1;
    private final int CMD_STOP = 2;
    private final int CMD_SHOW_NOTIFICATION = 3;

    private final String TAG = SchedulerService.class.getCanonicalName();

    private Timer timer;
    private TimerTask timerTask;
    //private final int CHECK_INTERVAL = 30 * 60 * 1000;
    private final int CHECK_INTERVAL = 10 * 1000;
    private String serviceMessage;


    @Override
    public void onCreate() {

        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    private void startTimer() {

        if (timer == null) {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    showNotification(serviceMessage);
                }
            };
            timer.schedule(timerTask, 0, CHECK_INTERVAL);
            Log.d(TAG, "timer started");
        } else {
            Log.d(TAG, "timer already started");
        }
    }

    private void stopTimer() {

        if (timer != null) {
            timerTask.cancel();
            timer.purge();
            timer.cancel();
            timer = null;
            stopSelf();
            Log.d(TAG, "timer stopped");
        } else {
            Log.d(TAG, "timer not running");
        }
    }

    private void showNotification(String message) {

        Intent notifyIntent = new Intent(getApplicationContext(),
                SettingsActivity.class);

        PendingIntent contentIntent = PendingIntent.getActivity
                (getApplicationContext(), 0,notifyIntent,
                        PendingIntent.FLAG_ONE_SHOT);
        Notification.Builder builder = new Notification.Builder(
                getApplicationContext());
        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentText(message);
        Notification notify = builder.build();
        notify.defaults |= Notification.DEFAULT_ALL;
        NotificationManager notificationManager = (NotificationManager)
                getApplicationContext().getSystemService(
                        Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notify);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        int command = 0;
        if (intent != null) {
            command = intent.getIntExtra("cmd", 0);
        }

        switch (command) {
            case CMD_START: {
                serviceMessage = intent.getStringExtra("message");
                startTimer();
                break;
            }
            case CMD_STOP: {
                stopTimer();
                break;
            }

            case CMD_SHOW_NOTIFICATION: {
                serviceMessage = intent.getStringExtra("message");
                showNotification(serviceMessage);
                break;
            }

        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return null;
    }


}
