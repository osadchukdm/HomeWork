package osadchukdm.task1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.text);
        textView.setText(getText(getString(R.string.fileName)));
    }

    public String getText(String name) {

        if (name == null)
            return null;

        if (name.isEmpty())
            return null;

        String poem = "";
        StringBuilder builder = new StringBuilder();

        try {
            BufferedReader bufer = new BufferedReader(
                    new InputStreamReader(getAssets().open(name)));
            while ((poem = bufer.readLine()) != null) {
                builder.append(poem+"\n");
            }
            poem = builder.toString();
        } catch (IOException e) {
            poem=getString(R.string.error);
        }

        return poem;
    }
}