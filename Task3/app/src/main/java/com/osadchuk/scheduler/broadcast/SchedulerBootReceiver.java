package com.osadchuk.scheduler.broadcast;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.osadchuk.scheduler.R;
import com.osadchuk.scheduler.service.SchedulerService;

public class SchedulerBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // restart service
        SharedPreferences preferences = context.getSharedPreferences("prefs",
                Context.MODE_PRIVATE);
        String message = preferences.getString("message", context.getString(R.string.default_notification_message));
        Intent serviceIntent = new Intent(context, SchedulerService.class);
        serviceIntent.putExtra("message", message);
        context.startService(serviceIntent);
    }
}
