package osadchukdm.task4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private final String LOG = "Activity First";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(LOG,"OnCreate");

        Button button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        secondActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG,"OnStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG,"OnResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG,"OnPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG,"OnStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LOG,"OnRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG,"OnDestroy");
    }
}
