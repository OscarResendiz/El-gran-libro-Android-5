package oscar.com.despertador;

import android.media.MediaPlayer;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.widget.Toast;

import java.io.File;
import java.util.List;

//se encarga de reproducir la lista de canciones de la alarma
public class HiloMusical extends Thread {
    private int idAlarma;
    private boolean ejecutando;
    private boolean detener;
    private DataBase Db;
    List<Cancion> canciones;
    private boolean reproducioendo;
    private MediaPlayer reproductor;
    private StatusReproduccion statusReproduccion;

    public HiloMusical(int id, DataBase db) {
        Db = db;
        detener = false;
        ejecutando = false;
        idAlarma = id;
        Alarma alarma = Db.getAlarma(idAlarma);
        canciones = Db.getCancionesLista(alarma.getIdLista());
        reproductor = new MediaPlayer();
        reproductor.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                                public void onCompletion(MediaPlayer mp) {
                                                    Stop();
                                                    statusReproduccion.incrementaPosicion();
                                                    reproducioendo = false;
                                                }
                                            }
        );
        statusReproduccion=db.getPosicionReproduccion(1,idAlarma);
        statusReproduccion.setMaxinmo(canciones.size());
        statusReproduccion.setDataBase(db);
    }

    public boolean esTuId(int id) {
        if (id == idAlarma)
            return true;
        return false;
    }

    public boolean Corriendo() {
        //indica si se esta reproduciendo la musioa
        return reproductor.isPlaying();//this.isAlive();
    }

    public void Stop() {
        reproductor.stop();
        SystemClock.sleep(500);
    }

    public void Play() {
        //se debe de incializar la musica
        if(!this.isAlive()) {
            this.start();
        }
        else
        {
            if(isReproducioendo()==false)
                reproductor.start();
        }
    }

    public boolean isReproducioendo()
    {
        if(reproductor==null)
            return false;
        return reproductor.isPlaying();
        //return reproducioendo;
    }

    @Override
    public void run() {
        ejecutando = true;
        reproducioendo = false;
        try {
            while (!detener) {
                //mientras no se pida la opcion de detener
                if (!reproducioendo) {
                    if (canciones.size() > 0) {
                            Cancion cancion = canciones.get(statusReproduccion.getPosicion());
                        File f = new File(cancion.getPath());
                        if (f.exists()) {
                            reproductor.reset();
                            reproductor.setDataSource(cancion.getPath());
                            reproductor.prepare();
                            reproductor.start();
                            reproducioendo = true;
                        } else {
                            statusReproduccion.incrementaPosicion();
                        }
                    }
                }
                //me espero un segundo sin actividad
                SystemClock.sleep(1000);
            }
        } catch (Exception e) {
            String s=e.getMessage();
            Toast.makeText(null,s,Toast.LENGTH_LONG);
        }
        ejecutando=false;
    }

}

