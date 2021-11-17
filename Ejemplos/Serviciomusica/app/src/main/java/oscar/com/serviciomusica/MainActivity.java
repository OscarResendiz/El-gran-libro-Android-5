package oscar.com.serviciomusica;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
private int ID_NOTIFICACION_AUXILIO=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button arrancar=(Button)findViewById(R.id.boton_arrancar);
        Button detener=(Button)findViewById(R.id.boton_detener);
        Button socorro=(Button)findViewById(R.id.boton_socorro);
        arrancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MainActivity.this,ServicioMusica.class));
            }
        });
        detener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(MainActivity.this,ServicioMusica.class));
            }
        });
        socorro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sonido="android.resource://"+getPackageName()+"/"+R.raw.auxilio;
                //sonido=R.raw.auxilio+"";
                /////////////////////creando una mortificacion/////////////////////////////
                NotificationCompat.Builder notific=new NotificationCompat.Builder(MainActivity.this)
                        .setContentTitle("Socorro")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentText("Ups")
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),android.R.drawable.ic_media_play))
                        .setWhen(System.currentTimeMillis()+1000*60*60)
                        .setContentInfo("Auxilio")
                        .setSound(Uri.parse(sonido))
//                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setVibrate(new long[]{50,100,50,100,50,100,50,300,50,300,50,300,50,100,50,100,50,100})
                        .setTicker("Me rroban");
            //muestro la mortificacion
                //se muestra la notificacion
                NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(ID_NOTIFICACION_AUXILIO,notific.build());

            }
        });
    }
}
