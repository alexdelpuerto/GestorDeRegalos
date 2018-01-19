package regalos.example.com.gestorderegalos.Firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Random;
import regalos.example.com.gestorderegalos.MainActivity;
import regalos.example.com.gestorderegalos.R;

/**
 * Created by Alejandro on 15/11/2017.
 */

public class FirebaseMensajes extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String cabecera = remoteMessage.getData().get("cabecera");
        String cuerpo = remoteMessage.getData().get("cuerpo");

        mostrarNotificacion(cabecera, cuerpo);
    }

    private void mostrarNotificacion(String cabecera, String cuerpo){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(cabecera)
                .setContentText(cuerpo)
                .setSmallIcon(R.mipmap.ic_launcher_round);

        Intent i = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, i, 0);

        builder.setContentIntent(contentIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Random random = new Random();
        notificationManager.notify(random.nextInt(), builder.build());

    }
}
