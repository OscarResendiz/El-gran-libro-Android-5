package oscar.com.despertador;

import android.media.MediaPlayer;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HiloReproductor {
    private MediaPlayer reproductor;
    private List<Cancion> canciones=null;
    private static HiloReproductor hiloReproductor;
    private int codigoIdentificador; //codigo que se va a ocupar para saber quien tiene el control
    private IReproductor vista;
    private int iconoplayActual;
    private DataBase db;
    private StatusReproduccion statusReproduccion;
    public List<Cancion> getCanciones() {
        return canciones;
    }
    public void setVista(IReproductor v)
    {
        vista=v;
        if(codigoIdentificador!=vista.getId())
        {
            //se cambio la vista, por lo que hay que parar el reproductor
            stop();
            //me traigo la lista de canciones
            canciones=vista.getCacnciones();
            codigoIdentificador=vista.getId();
        }
        //aplico el icono
        vista.setPlayIcon(iconoplayActual);
        //me traigo los datos de la lista de reproduccion
        if(db==null)
            db=new DataBase(vista.getContexto());
        statusReproduccion=db.getPosicionReproduccion(vista.getTipo(),vista.getId());
        statusReproduccion.setDataBase(db);
        statusReproduccion.setMaxinmo(canciones.size());

    }
    private HiloReproductor()
    {
        //para evitar que sea instanciada desde afuera
        codigoIdentificador=-1;
        canciones=new ArrayList<Cancion>();
        reproductor = new MediaPlayer();
        iconoplayActual=android.R.drawable.ic_media_play;
        reproductor.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                                public void onCompletion(MediaPlayer mp) {
                                                    siguiente();
                                                }
                                            }
        );
    }
    public static HiloReproductor getReproductor()
    {
        if(hiloReproductor==null)
            hiloReproductor=new HiloReproductor();
        return hiloReproductor;
    }
    public void anterior()
    {
        stop();
        statusReproduccion.decrenetaPosicion();
        play();
    }
    public boolean isPlaying()
    {
        return reproductor.isPlaying();
    }
    public void play()
    {
        try {
            Cancion cancion = canciones.get(statusReproduccion.getPosicion());
            reproductor.setDataSource(cancion.getPath());
            reproductor.prepare();
            reproductor.start();
            iconoplayActual = android.R.drawable.ic_media_pause;
            vista.setPlayIcon(iconoplayActual);
        }
        catch (Exception e)
        {
            //throw e;
            //Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    public void stop()
    {
        reproductor.stop();
        reproductor.reset();
        iconoplayActual=android.R.drawable.ic_media_play;
        vista.setPlayIcon(iconoplayActual);
    }

    public void siguiente()
    {
        stop();
        statusReproduccion.incrementaPosicion();
        play();

    }
    public int getIconoplayActual()
    {
        return  iconoplayActual;
    }
}
