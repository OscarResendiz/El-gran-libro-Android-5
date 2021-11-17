package com.example.oscar.asteroides;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import java.util.List;
import java.util.Vector;
//import android.R;
public class VistaJuego extends View implements SensorEventListener
{
    private Activity padre;
    private int puntuacion;
    ////////asteroides/////
    private Vector<Grafico> asteroides; //vector con los asteroides
    private  int numAsteroides=5; //numero inicial de asteroides
    private int numFragmentos=3; // fragmnentos en que se divide
    ////////NAVE////////////

    public void setPadre(Activity padre) {
        this.padre = padre;
    }

    private Grafico nave; //grafico de la nave
    private int giroNave; //incremento de direccion
    private double aceleracionNave; //aumento de velocidad
    private static final int  MAX_VELOCIDAD_NAVE=20;
    //incremento estandar de giro y aceleracion
    private static final int PASO_GIRO_NAVE=5;
    private static final float PASO_ACELRACION_NAVE=0.5f;
    //////THREAD Y TIEMPO////////////
    private ThreadJuego thread=new ThreadJuego();
    //cada cuando queremos procesar el cambio (ms)
    private static  int PERIODO_PROCESO=50;
    //cuando se realizo el ultimo proceso
    private long ultimoProceso=0;
    private  float mX=0,mY=0;
    protected boolean disparo=false;
    //para el manejo de los sensores
    private boolean hayValorInicial=false;
    private float valorInicialX,valorInicialY;
    SensorManager sensorManager;
    Sensor orientationSensor;
    /////MISISL////
    private Vector<Grafico> misiles;
    private static  int PASO_VELOCIDAD_MISIL=12;
    private  Vector<Integer> tiempoMisiles;
    private Drawable drawableMisil;
    /////SONIDOS/////////
    //private MediaPlayer mpDisparo,mpExplosion;
    private SoundPool soundPool;
    int idDisparo,idExplosion;
    private Drawable drawableAsteroide[]= new Drawable[3];
    private boolean efectosSonido=true;
    public VistaJuego(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        Drawable drawableNave;
        //asteroides fragmentados

        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getContext());
        String tipog=pref.getString("Graficos","1");
        if(tipog.equals("0"))
        {
            //asteroides con vectores
            Path pathAsteroide=new Path();
            pathAsteroide.moveTo((float)0.3,(float)0.0);
            pathAsteroide.lineTo((float)0.6,(float)0.0);
            pathAsteroide.lineTo((float)0.6,(float)0.3);
            pathAsteroide.lineTo((float)0.8,(float)0.2);
            pathAsteroide.lineTo((float)1.0,(float)0.4);
            pathAsteroide.lineTo((float)0.8,(float)0.6);
            pathAsteroide.lineTo((float)0.9,(float)0.9);
            pathAsteroide.lineTo((float)0.8,(float)1.0);
            pathAsteroide.lineTo((float)0.4,(float)1.0);
            pathAsteroide.lineTo((float)0.0,(float)0.6);
            pathAsteroide.lineTo((float)0.0,(float)0.2);
            pathAsteroide.lineTo((float)0.3,(float)0.0);
            for(int i=0;i<3;i++)
            {
                ShapeDrawable dAsteroide=new ShapeDrawable(new PathShape(pathAsteroide,1,1));
                dAsteroide.getPaint().setColor(Color.WHITE);
                dAsteroide.getPaint().setStyle(Paint.Style.STROKE);
                dAsteroide.setIntrinsicWidth(50-i*14);
                dAsteroide.setIntrinsicHeight(50-1*14);
                drawableAsteroide[i]=dAsteroide;
            }
            setBackgroundColor(Color.BLACK);
            setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        }
        else
        {
            //cargamos las imagenes de los asterodes
            drawableAsteroide[0]=context.getResources().getDrawable(R.drawable.asteroide1);
            drawableAsteroide[1]=context.getResources().getDrawable(R.drawable.asteroide2);
            drawableAsteroide[2]=context.getResources().getDrawable(R.drawable.asteroide3);

//            drawableAsteroide=context.getResources().getDrawable(R.drawable.asteroide1);
            setLayerType(View.LAYER_TYPE_HARDWARE,null);
        }
        asteroides=new Vector<Grafico>();
        for (int i=0; i<numAsteroides;i++)
        {
            Grafico asteroide=new Grafico(this,drawableAsteroide[0]);
            asteroide.setIncY(Math.random()*4-2);
            asteroide.setIncX(Math.random()*4-2);
            asteroide.setAngulo((int)(Math.random()*360));
            asteroide.setRotacion((int)(Math.random()*8-4));
            asteroides.add(asteroide);
        }
        //nave
        if(tipog.equals("0"))
        {
            Path pathNave=new Path();
            pathNave.moveTo((float)0.0,(float)0.0);
            pathNave.lineTo((float)1.0,(float)0.5);
            pathNave.lineTo((float)0.5,(float)0.1);
            pathNave.lineTo((float)0.1,(float)0.0);
            ShapeDrawable dNave=new ShapeDrawable(new PathShape(pathNave,1,1));
            dNave.getPaint().setColor(Color.WHITE);
            dNave.getPaint().setStyle(Paint.Style.STROKE);
            dNave.setIntrinsicWidth(50);
            dNave.setIntrinsicHeight(50);
            drawableNave=dNave;
        }
        else {
            drawableNave = context.getResources().getDrawable(R.drawable.nave);
        }
        nave=new Grafico(this,drawableNave);
        //misisl
        if(tipog.equals("0"))
        {
            ShapeDrawable dMisil=new ShapeDrawable(new RectShape());
            dMisil.getPaint().setColor(Color.WHITE);
            dMisil.getPaint().setStyle(Paint.Style.STROKE);
            dMisil.setIntrinsicWidth(15);
            dMisil.setIntrinsicHeight(3);
            drawableMisil=dMisil;
        }
        else {
            drawableMisil = context.getResources().getDrawable(R.drawable.misil1);
        }
        misiles=new Vector<Grafico>();
        tiempoMisiles=new Vector<Integer>();

        //sensores
        boolean usarSensores=pref.getBoolean("sensores",false);
        if(usarSensores== true)
        {
            InicializaSensores(context);
        }
        //sonidos
        //me traigo la configuracion de sonido
        efectosSonido=pref.getBoolean("musica",false);
        if(efectosSonido) {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
            idDisparo = soundPool.load(context, R.raw.disparo, 0);
            idExplosion = soundPool.load(context, R.raw.explosion, 0);
        }
        //me traigo el numero de asteroides que hay que dividir
        numFragmentos=Integer.parseInt( pref.getString("fragmentos","3"));
    }

    public ThreadJuego getThread() {
        return thread;
    }

    @Override
    protected void onSizeChanged(int ancho, int alto,int ancho_anterior, int alto_anterior)
    {
        super.onSizeChanged(ancho, alto,ancho_anterior, alto_anterior);
        //nave
        nave.setCenY(alto/2);
        nave.setCenX(ancho/2);
        //una ves que conocemos nuestro ancho y alto+
        for (Grafico asteroide: asteroides)
        {
            do {
                asteroide.setCenX((int) (Math.random() * ancho));
                asteroide.setCenY((int) (Math.random() * alto));
            }
            while (asteroide.distancia(nave)<(ancho+alto)/5);
        }
        ultimoProceso=System.currentTimeMillis();
        thread.start();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        synchronized (asteroides) {
            for (Grafico asteroide : asteroides) {
                asteroide.dibujaGrafico(canvas);
            }
        }
        //dijamos la nave
        nave.dibujaGrafico(canvas);
        //dibujamos los misiles
        for (Grafico misil : misiles)
        {
            misil.dibujaGrafico(canvas);
        }
    }
    protected void  actualizaFisica() {
        long ahora = System.currentTimeMillis();
        if (ultimoProceso + PERIODO_PROCESO > ahora) {
            //salir si el periodo de proceso no se ha cumplico
            return;
        }
        //para una ejecucionen tiempo real calculamos retardo
        double retardo = (ahora - ultimoProceso) / PERIODO_PROCESO;
        ultimoProceso = ahora;
        //actualizamos velocidad y direccion de la nave a partir de giro nave y aceleracion nave (segun la entrada del jugador)
        nave.setAngulo((int) (nave.getAngulo() + giroNave * retardo));
        double nincX = nave.getIncX() + aceleracionNave * Math.cos(Math.toRadians(nave.getAngulo())) * retardo;
        double nincY = nave.getIncY() + aceleracionNave * Math.sin(Math.toRadians(nave.getAngulo())) * retardo;
        //actualizamos si el modulo de la velocidad no excelde el maximo
        if (Math.hypot(nincX, nincY) <= MAX_VELOCIDAD_NAVE) {
            nave.setIncX(nincX);
            nave.setIncY(nincY);
        }
        nave.incremetaPos(retardo);//actualizamos la posicion
        //aahora tambien los asteroides
        for (Grafico asteroide : asteroides) {
            asteroide.incremetaPos(retardo);
        }
        //actualizamos la posicion del misisl
        for (int m = misiles.size() - 1; m >= 0; m--) {
            Grafico misil = misiles.get(m);
            misil.incremetaPos(retardo);
            tiempoMisiles.set(m, tiempoMisiles.get(m) - 1);
            if (tiempoMisiles.get(m) < 0) {
                //termino su tiempo de vida, por lque hay que destruirlo
                tiempoMisiles.remove(m);
                misiles.remove(m);
            } else {
                //verifico si ha colicionado con algun asteroide
                for (int i = 0; i < asteroides.size(); i++) {
                    if (misil.verificaColicion(asteroides.elementAt(i))) {
                        destruyeAsteroide(i);
                        //tambien destruyo el misil
                        misiles.remove(m);
                        break;
                    }
                }
            }
        }
        for (Grafico asteroide: asteroides)
        {
            if(asteroide.verificaColicion(nave))
            {
                salir();
            }
        }
    }
    private void  destruyeAsteroide(int i)
    {
        synchronized (asteroides) {
            int tam;
            if(asteroides.get(i).getDrawable()!=drawableAsteroide[2])
            {
                if(asteroides.get(i).getDrawable()==drawableAsteroide[1])
                {
                    tam=2;
                }
                else
                {
                    tam=1;
                }
                for (int n=0;n<numFragmentos;n++)
                {
                    Grafico asteroide=new Grafico(this,drawableAsteroide[tam]);
                    asteroide.setCenX(asteroides.get(i).getCenX());
                    asteroide.setCenY(asteroides.get(i).getCenY());
                    asteroide.setIncX(Math.random()*7-2-tam);
                    asteroide.setIncY(Math.random()*7-2-tam);
                    asteroide.setAngulo((int)(Math.random()*360));
                    asteroide.setRotacion((int)(Math.random()*4-4));
                    asteroides.add(asteroide);
                }
            }
            asteroides.remove(i);
            puntuacion+=1000;
        }
        //mpExplosion.start();
        if(efectosSonido) {
            soundPool.play(idExplosion, 1, 1, 0, 0, 1);
        }
        if(asteroides.isEmpty())
        {
            salir();
        }
    }
    @Override
    public boolean onKeyDown(int codigoTecla, KeyEvent evento)
    {
        super.onKeyDown(codigoTecla,evento);
        //suponemos que vamos  a procesar la pulsacion
        boolean procesada=true;
        switch (codigoTecla)
        {
            case KeyEvent.KEYCODE_DPAD_UP:
                aceleracionNave=+PASO_ACELRACION_NAVE;
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                giroNave=-PASO_GIRO_NAVE;
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                giroNave=+PASO_GIRO_NAVE;
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                activaMisil();
                break;
                default:
                    //si estamos aqui: no hay pulsacion que nos interese
                    procesada=false;
                    break;
        }
        return procesada;
    }
    @Override
    public boolean onKeyUp(int codigoTecla, KeyEvent evento)
    {
        super.onKeyUp(codigoTecla, evento);
        boolean procesada=true;
        //suponemos que vamos a procesar la pulsacion
        switch (codigoTecla)
        {
            case KeyEvent.KEYCODE_DPAD_UP:
                aceleracionNave=0;
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                giroNave=0;
                break;
                default:
                    //si estamos aqui no hay pulsacion que nos interese
                    procesada=false;
                    break;
        }
        return procesada;
    }
    private void  activaMisil()
    {

        Grafico misil=new Grafico(this,drawableMisil);
        misil.setCenX(nave.getCenX());
        misil.setCenY(nave.getCenY());
        misil.setAngulo(nave.getAngulo());
        misil.setIncX(Math.cos(Math.toRadians(misil.getAngulo()))*PASO_VELOCIDAD_MISIL);
        misil.setIncY(Math.sin(Math.toRadians(misil.getAngulo()))*PASO_VELOCIDAD_MISIL);
        int tiempoMisil=(int)Math.min(this.getWidth()/Math.abs(misil.getIncX()),this.getHeight()/Math.abs(misil.getIncY()))-2;
        misiles.add(misil);
        tiempoMisiles.add(tiempoMisil);
        //mpDisparo.start();
        if(efectosSonido) {
            soundPool.play(idDisparo, 1, 1, 1, 0, 1);
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        super.onTouchEvent(event);
        float x=event.getX();
        float y=event.getY();
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                disparo=true;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx=Math.abs(x-mX);
                float dy=Math.abs(y-mY);
                if(dy<6 && dx>6)
                {
                    giroNave=Math.round((x-mX)/2);
                    disparo=false;
                }
                else  if(dx<6 && dy>6)
                {
                    aceleracionNave=Math.round((mY-y)/25);
                    disparo=false;
                }
                break;
            case MotionEvent.ACTION_UP:
                giroNave=0;
                aceleracionNave=0;
                if(disparo)
                {
                    activaMisil();
                }
                break;
        }
        mX=x;
        mY=y;
        return true;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i)
    {

    }
    private void InicializaSensores(Context context)
    {
        //manejo de los sensores
        sensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
        List<Sensor> listaSensores = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (!listaSensores.isEmpty()) {
            orientationSensor = listaSensores.get(0);
        }
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        float valorX=sensorEvent.values[0]; //X
        float valorY=sensorEvent.values[1]; //Y
        if(!hayValorInicial)
        {
            valorInicialX=valorX;
            valorInicialY=valorY;
            hayValorInicial=true;
        }
        giroNave=(int)(valorX-valorInicialX)/2;
        aceleracionNave=(int)(valorY-valorInicialY)/2;
    }
    private void activarSensores()
    {
        if(orientationSensor!=null) {
            sensorManager.registerListener(this, orientationSensor, SensorManager.SENSOR_DELAY_GAME);
        }
    }
    private void salir()
    {
        Bundle bundle=new Bundle();
        bundle.putInt("puntuacion",puntuacion);
        Intent intent=new Intent();
        intent.putExtras(bundle);
        padre.setResult(Activity.RESULT_OK,intent);
        padre.finish();
    }
    private void desactivarSensores()
    {
        if(sensorManager!=null) {
            sensorManager.unregisterListener(this);
        }
    }
    class ThreadJuego extends Thread {
        private boolean pausa, corriendo;

        @Override
        public void run() {
            corriendo=true;
            while (corriendo) {
                actualizaFisica();
                synchronized (this)
                {
                    while (pausa)
                    {
                        try {
                            wait();
                        }
                        catch (Exception e)
                        {

                        }
                    }
                }
            }
        }

        public synchronized void pausar() {
            pausa = true;
            desactivarSensores();
        }

        public synchronized void reanudar() {
            pausa=false;
            activarSensores();
            notify(); //para sacar del wait() en run
        }
        public void detener()
        {
            corriendo=false;
            if(pausa)
                reanudar();
        }
    }
}
