package osadchukdm.task4;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class secondActivity extends AppCompatActivity {

    private final String LOG = "Activity Second";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Log.d(LOG,"OnCreate");

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
