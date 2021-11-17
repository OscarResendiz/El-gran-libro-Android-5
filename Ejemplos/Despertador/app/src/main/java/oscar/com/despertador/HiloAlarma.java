package oscar.com.despertador;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



import android.content.Context;
import android.icu.util.Calendar;
import android.os.SystemClock;
import android.text.format.Time;

import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

//clase que se ejecuta y verifica que alarmas son las que estan activas y las lanza en el momento en que hay que hacerlo
public class HiloAlarma extends Thread
{
    DataBase Db;
    Context Contexto;
    List<Alarma> Alarmas;
    List<HiloMusical> AlarmasEjecutando;
    private boolean corriendo;
    private static HiloAlarma hiloAlarma=null;
    private boolean pausa;
    private boolean pausado;
    //para el manejo de eventos
    private ArrayList listeners;

    //implemento un singleton para evitar que se ejecute varias veces
    public static void InciaAlarma(Context context)
    {
        if(hiloAlarma==null) {
            hiloAlarma = new HiloAlarma(context);
            hiloAlarma.start();
        }
        else
        {
            if(!hiloAlarma.isAlive())
            {
                hiloAlarma.start();
            }
        }
    }
    private HiloAlarma(Context context)
    {
        listeners=new ArrayList();
        AlarmasEjecutando=new ArrayList<HiloMusical>();
        Contexto=context;
        Db=new DataBase(Contexto);
        corriendo=false;
    }
    public boolean Corriendo()
    {
        return corriendo;
    }
    public void Detener()
    {
        corriendo=false;
    }
    @Override
    public void run()
    {
            //me traigo las alarmas
        Time today = new Time(Time.getCurrentTimezone());
        int dia;
        int hora;
        int minuto;
        corriendo=true;
        pausa=false;
        while (corriendo) {
            //obtngo la hora actual
            while (pausa)
            {
                pausado=true;
                SystemClock.sleep(100);
            }
            today.setToNow();
            //recorro la lista de alarmas y veo si hay que ejecutarla
            Alarmas=Db.getAlarmas();
            for (Alarma alarma:Alarmas)
                if (alarma.hayQueActivar(today)) {
                    boolean encontrado = false;
                    //veo si ya se esta ejecutando
                    for (HiloMusical hm : AlarmasEjecutando)
                        if (hm.esTuId(alarma.getIdAlarma())) {
                            if (hm.Corriendo() == false) {
                                //como ya no esta corriendo, lo vuelvo a inicializar
                                hm.Play();
                                        avisaAlarma(alarma);
                            }
                            encontrado = true;
                        }
                    if (encontrado == false) {
                        //no lo encontre, por lo que lo creo
                        HiloMusical hm2 = new HiloMusical(alarma.getIdAlarma(), Db);
                        AlarmasEjecutando.add(hm2);//se incializa la musica automaticamente
                        hm2.Play();
                        avisaAlarma(alarma);
                    }
                }
            //me espero un segundo sin actividad
            SystemClock.sleep(1000);

        }
    }
    public static HiloAlarma getControlador()
    {
        return hiloAlarma;
    }
    public boolean AlarmaEjecutando(int idAlarma) {
        //regresa true si la alarma se tocando musica
        for(HiloMusical hm:AlarmasEjecutando)
        {
            if(hm.esTuId(idAlarma))
            {
                return  hm.isReproducioendo();
            }
        }
        return false;
    }

    public  void Detener(int idAlarma)
    {
        pausado=false;
        pausa=true;
        while (pausado==false)
        {
            SystemClock.sleep(100);
        }
        for(HiloMusical hm:AlarmasEjecutando)
        {
            if(hm.esTuId(idAlarma))
            {
                hm.Stop();
            }
        }
        pausa=false;
    }

    public void Play(int idAlarma)
    {
        pausado=false;
        pausa=true;
        while (pausado==false)
        {
            SystemClock.sleep(100);
        }
        for(HiloMusical hm:AlarmasEjecutando)
        {
            if(hm.esTuId(idAlarma))
            {
                hm.Play();
                return;
            }
        }
        HiloMusical hm2=new HiloMusical(idAlarma,Db);
        AlarmasEjecutando.add(hm2);//se incializa la musica automaticamente
        hm2.Play();
        pausa=false;
    }
    public void addAlarmaVenetListener(IAlarmaEventListener listener)
    {
        listeners.add(listener);
    }
    private void avisaAlarma(Alarma alarma)
    {

        ListIterator li = listeners.listIterator();
        while (li.hasNext()) {
            IAlarmaEventListener listener=(IAlarmaEventListener)li.next();
            AlarmaEventObject arg=new AlarmaEventObject(this,alarma);
            listener.OnAlarma(arg);
        }
    }
}
