package com.osadchuk.scheduler.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.osadchuk.scheduler.R;
import com.osadchuk.scheduler.service.SchedulerService;

public class MainActivity extends AppCompatActivity {

    private final int CMD_START = 1;
    private final int CMD_STOP = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonStart=(Button)findViewById(R.id.start);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences preferences = getApplicationContext().
                        getSharedPreferences("prefs", Context.MODE_PRIVATE);
                String message = preferences.getString("message", getString(R.string.default_notification_message));

                Intent intent = new Intent(getApplicationContext(), SchedulerService.class);
                intent.putExtra("cmd", CMD_START);
                intent.putExtra("message", message);
                startService(intent);
            }
        });

        Button buttonStop=(Button)findViewById(R.id.stop);
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(getApplicationContext(), SchedulerService.class);
                intent.putExtra("cmd", CMD_STOP);
                startService(intent);*/
                stopService(
                        new Intent(MainActivity.this, SchedulerService.class));

            }
        });
    }
}
