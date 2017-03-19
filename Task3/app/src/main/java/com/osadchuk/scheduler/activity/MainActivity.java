package com.osadchuk.scheduler.activity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.osadchuk.scheduler.R;
import com.osadchuk.scheduler.service.SchedulerService;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button buttonStart=(Button)findViewById(R.id.start);

        final Button buttonStop=(Button)findViewById(R.id.stop);

        if(isServiceRunning(SchedulerService.class)) {
            buttonStart.setEnabled(false);
            buttonStop.setEnabled(true);
        }else {
            buttonStart.setEnabled(true);
            buttonStop.setEnabled(false);

        }

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isServiceRunning(SchedulerService.class)) {
                    SharedPreferences preferences = getApplicationContext().
                            getSharedPreferences("prefs", Context.MODE_PRIVATE);
                    String message = preferences.getString("message", getString(R.string.default_notification_message));

                    Intent intent = new Intent(getApplicationContext(), SchedulerService.class);

                    intent.putExtra("message", message);
                    startService(intent);
                    buttonStart.setEnabled(false);
                    buttonStop.setEnabled(true);
                }
            }
        });


        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isServiceRunning(SchedulerService.class)) {
                    stopService(
                            new Intent(MainActivity.this, SchedulerService.class));
                    buttonStart.setEnabled(true);
                    buttonStop.setEnabled(false);
                }
            }
        });
    }
    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager)
                getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service :
                manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
