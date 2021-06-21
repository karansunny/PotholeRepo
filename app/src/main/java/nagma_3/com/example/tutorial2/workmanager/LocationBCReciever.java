package nagma_3.com.example.tutorial2.workmanager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class LocationBCReciever extends BroadcastReceiver {

    public static final String ACTION_TRACK = "nagma_3.com.example.tutorial2.workmanager.ACTION_TRACK";
    public static final String ACTION_STOP = "nagma_3.com.example.tutorial2.workmanager.ACTION_STOP";
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    boolean connected = true;
    NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Action: " + intent.getAction(), Toast.LENGTH_SHORT).show();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context , default_notification_channel_id ) ;
        builder.setContentTitle( "Location - Tracking" ) ;
        String action = intent.getAction() ;
        Log. e ( "Location" , action) ;
        assert action != null;
        builder.setContentText("Started") ;
        builder.setSmallIcon(android.R.drawable.ic_lock_idle_alarm) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
        Notification notification = builder.build() ;
        notificationManager = (NotificationManager) context.getSystemService(Context. NOTIFICATION_SERVICE ) ;

        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            int importance = NotificationManager. IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }

        assert notificationManager != null;
        if (ACTION_TRACK.equals(intent.getAction())) {
            notificationManager.notify( 1 , notification);
            connected = false;
        } else {
            notificationManager.cancelAll();
            connected = true;
        }

        if (ACTION_TRACK.equals(intent.getAction())) {
            Log.e("MyAlarmReceiverALARM--",ACTION_TRACK);

            Intent i = new Intent(context, TrackLocationService.class);
            i.putExtra("foo", "bar");
            context.startService(i);

        }
        else
        {
            Log.e("MyAlarmReceiverElse--",ACTION_STOP);
            Intent i = new Intent(context, TrackLocationService.class);
            context.stopService(i);
        }
    }
}
