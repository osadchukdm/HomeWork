package com.osadchuk.scheduler.activity;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.osadchuk.scheduler.R;
import com.osadchuk.scheduler.service.SchedulerService;

public class SettingsActivity extends AppCompatActivity {
    private final String TAG = SettingsActivity.class.getCanonicalName();
    private EditText newMessage;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        newMessage = (EditText) findViewById(R.id.message);

        preferences = getApplicationContext()
                .getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String message = preferences.getString("message", getString(R.string.default_notification_message));
        newMessage.setText(message);



        Button button =(Button)findViewById(R.id.apply);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String messageService = newMessage.getText().toString();

                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("message", messageService);
                editor.apply();
                sendNotification(messageService);
                finish();
            }
        });
    }

    private void sendNotification(String message) {
        Log.d(TAG,message);
        Intent intent = new Intent(getApplicationContext(), SchedulerService.class);
        intent.putExtra("message",message);
        stopService(intent);
        startService(intent);
    }
}
