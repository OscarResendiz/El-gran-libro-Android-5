package servicioremoto.example.org.servicioremoto;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class ServicioRemoto extends Service
{
    MediaPlayer reproductor;
    public ServicioRemoto() {
    }
    @Override
    public void onCreate()
    {
        super.onCreate();
        reproductor=MediaPlayer.create(ServicioRemoto.this,R.raw.audio);
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return this.binder;
    }
    private final IServicioMusica.Stub binder=new IServicioMusica.Stub()
    {
        public  String reproduce(String mensaje)
        {
            reproductor.start();
            return mensaje;
        }
        public void setPosicion(int ms)
        {
            reproductor.seekTo(ms);;
        }
        public int getPosicion()
        {
            return reproductor.getCurrentPosition();
        }
    };

}
