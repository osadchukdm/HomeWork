package osadchukdm.task_service;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);
        final EditText messageText=(EditText)findViewById(R.id.message);
        messageText.setText(R.string.notificationText);

        final Button setText=(Button)findViewById(R.id.setText);
        setText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(messageText.getText()==null){
                    Toast.makeText(getApplicationContext(),"Enter message",Toast.LENGTH_SHORT);
                }
                else {

                }
            }
        });

    }
}
