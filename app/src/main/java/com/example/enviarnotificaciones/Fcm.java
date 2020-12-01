package com.example.enviarnotificaciones;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Fcm extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("token","mi token es: "+s);
        guardartoken(s);
    }
    private void guardartoken(String s){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("token");
        ref.child("user").setValue(s);
    }
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String from= remoteMessage.getFrom();
        Log.e("TAG","mensaje recibido de: "+from);
        /*if(remoteMessage.getNotification() != null) {

            Log.e("TAG", "el titulo es: " + remoteMessage.getNotification().getTitle());
            Log.e("TAG", "el detalle es: " + remoteMessage.getNotification().getBody());

        }*/
        if(remoteMessage.getData().size()>0){
            Log.e("TAG","mi titulo es: "+remoteMessage.getData().get("titulo"));
            Log.e("TAG","mi detalle es: "+remoteMessage.getData().get("detalle"));

             String titulo= remoteMessage.getData().get("titulo");
            String detalle= remoteMessage.getData().get("detalle");
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ){
                mayorqueoreo(titulo,detalle);
            }
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O ){
                menorqueoreo(titulo, detalle);
            }
        }

    }



    private void mayorqueoreo(String titulo, String detalle){
        String id="mensaje";
        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder= new NotificationCompat.Builder(this,id);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel nc = new NotificationChannel(id,"nuevo",NotificationManager.IMPORTANCE_HIGH);
            nc.setShowBadge(true);
            assert nm != null;
            nm.createNotificationChannel(nc);
        }
        builder.setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(titulo)
                .setSmallIcon(R.drawable.baseline_sms_24)
                .setContentText(detalle)
                .setContentIntent(clicknoti())
                .setContentInfo("nuevo")
                .setColor(Color.BLUE)
                .setDefaults(Notification.DEFAULT_SOUND)
        ;
        Random random = new Random();
        int idNotify=random.nextInt(8000);

        assert nm != null;
        nm.notify(idNotify,builder.build());

    }
    private void menorqueoreo(String titulo, String detalle) {
        String id="mensaje";
        NotificationCompat.Builder builder= new NotificationCompat.Builder(getApplicationContext(),id);
        builder.setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(titulo)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(detalle)
                .setContentIntent(clicknoti())
                .setContentInfo("nuevo")
                .setColor(Color.BLUE);

        Random random = new Random();
        int idNotify=random.nextInt(8000);
        NotificationManagerCompat nm= NotificationManagerCompat.from(getApplicationContext());
        //NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        assert nm != null;
        nm.notify(idNotify,builder.build());
    }
    public PendingIntent clicknoti(){
        Intent nf = new Intent(getApplicationContext(),MainActivity.class);
        nf.putExtra("color","rojo");
        nf.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(this,0,nf,0);
    }

}
