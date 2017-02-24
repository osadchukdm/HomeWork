package osadchukdm.task_service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class Service_message extends Service {

    private final String LOG = "Service";
    private static final int NOTIFY_ID = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG,"OnCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG,"OnStart");
        Context context=getApplicationContext();

        Intent intentActivity = new
                Intent(context, MessageActivity.class);
        startActivity(intentActivity);


        Notification.Builder builder = new Notification.Builder(context);

        /*builder.setContentIntent(contentIntent)
                .setTicker(R.string.notificationText)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(false)
                .setContentText(R.string.notificationText);

        Notification notification = builder.build();*/

       /* NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, notification);
*/
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        Log.d(LOG,"OnDestroy");
        super.onDestroy();
    }

    public Service_message() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
