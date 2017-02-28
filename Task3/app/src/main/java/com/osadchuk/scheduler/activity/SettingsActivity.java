package com.osadchuk.scheduler.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.osadchuk.scheduler.R;
import com.osadchuk.scheduler.service.SchedulerService;

public class SettingsActivity extends AppCompatActivity {

    private final int CMD_SHOW_NOTIFICATION = 3;

    private EditText newMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        newMessage = (EditText) findViewById(R.id.message);

        SharedPreferences preferences = getApplicationContext()
                .getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String message = preferences.getString
                ("message", getString(R.string.default_notification_message));
        newMessage.setText(message);

        Button button =(Button)findViewById(R.id.apply);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = newMessage.getText().toString();

                SharedPreferences preferences = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("message", message);
                editor.commit();
                sendNotification(message);
                finish();
            }
        });

    }

    private void sendNotification(String message) {
        Intent intent = new Intent(getApplicationContext(), SchedulerService.class);
        intent.putExtra("message", message);
        intent.putExtra("cmd", CMD_SHOW_NOTIFICATION);
        startService(intent);
    }
}
