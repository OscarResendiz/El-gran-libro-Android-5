package oscar.com.serviciomusica;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class ServicioMusica extends Service {
    MediaPlayer reproductor;
    private static final int ID_NOTIFICACION_CREAR=1;

    @Override
    public void onCreate() {
        Toast.makeText(this, "Servicio creado", Toast.LENGTH_SHORT).show();
        reproductor = MediaPlayer.create(this, R.raw.audio);
    }

    @Override
    public int onStartCommand(Intent intenc, int flags, int idArranque) {
        /////////////////////creando una mortificacion/////////////////////////////
        NotificationCompat.Builder notific=new NotificationCompat.Builder(this)
        .setContentTitle("Creando servicio de musica")
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentText("Informacion adicional")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),android.R.drawable.ic_media_play))
                .setWhen(System.currentTimeMillis()+1000*60*60)
                .setContentInfo("mas informacion")
                .setTicker("Texto en la barra de estado");

        //se asigna la actividad a lanzar cuando se clike sobre la notificacion
        PendingIntent intencionPendiente=PendingIntent.getActivity(this,0,new Intent(this,MainActivity.class),0);
        notific.setContentIntent(intencionPendiente);
        //se muestra la notificacion
        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID_NOTIFICACION_CREAR,notific.build());
        //////////////////////////////////////////////////////////////////////////
        Toast.makeText(this, "Servicio arrancado" + idArranque, Toast.LENGTH_SHORT).show();
        reproductor.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Servicio detenido", Toast.LENGTH_SHORT).show();
        reproductor.stop();
        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(ID_NOTIFICACION_CREAR);
    }

    @Override
    public IBinder onBind(Intent intencion) {
        return null;
    }
}
