package com.example.oscar.asteroides;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends  AppCompatActivity implements GestureOverlayView.OnGesturePerformedListener {
    private Button bAcercaDe;
    private GestureLibrary libreria;
    private MediaPlayer mp;
    private boolean efectosSonido=true;
    //private Button bSalir;
    public static AlmacenPuntuaciones almacen = new AlmacenPuntuacionesArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bAcercaDe = (Button) findViewById(R.id.Button03);
        bAcercaDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lanzarAcercaDe(null);
            }
        });
        libreria = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!libreria.load()) {
            finish();
        }
        GestureOverlayView gestureView = (GestureOverlayView) findViewById(R.id.gestures);
        gestureView.addOnGesturePerformedListener(this);
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
        efectosSonido=pref.getBoolean("musica",true);
        if(efectosSonido) {
            mp = MediaPlayer.create(this, R.raw.audio);
            mp.start();
        }
        //lanzo el servicio
        startService(new Intent(MainActivity.this,ServicioMusica.class));
        //seleccion del tipo de almacenamiento de las puntuaciones
        SeleccionaAlmacen(pref);
    }

    public void lanzarAcercaDe(View view) {
        Intent i = new Intent(this, AcercaDe.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.acction_settings) {
            lanzarPreferencias(null);
            return true;
        }
        if (id == R.id.acercaDe) {
            lanzarAcercaDe(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void lanzarPreferencias(View view) {
        Intent i = new Intent(this, Preferencias.class);
        startActivity(i);
    }

    public void mostrarPreferencias(View view) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String s = "musica:" + pref.getBoolean("musica", true)
                + "Graficos:" + pref.getString("graficos", "?");
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();

    }

    public void lanzarPuntuaciones(View view) {
        Intent i = new Intent(this, Puntuaciones.class);
        startActivity(i);
    }

    public void lanzarJuego(View view) {
        Intent i = new Intent(this, Juego.class);
        startActivityForResult(i,1234);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1234 && resultCode==RESULT_OK && data!=null)
        {
            int puntuacion=data.getExtras().getInt("puntuacion");
            String nombre="yo";
            //Mejor leerlo desde un AlertDialog.Builder
            almacen.guardarPuntuacion(puntuacion,nombre, System.currentTimeMillis());
            lanzarPuntuaciones(null);
        }
    }

    @Override
    public void onGesturePerformed(GestureOverlayView ov, Gesture gesture) {
        ArrayList<Prediction> predictions = libreria.recognize(gesture);
        if (predictions.size() > 0) {
            String comando = predictions.get(0).name;
            if (comando.equals("play")) {
                lanzarJuego(null);
            } else if (comando.equals("configurar")) {
                lanzarPreferencias(null);
            } else if (comando.equals("acerca_de")) {
                lanzarAcercaDe(null);
            } else if (comando.equals("cancelar")) {
                finish();
            }
        }

    }

    //funciones del sclo de vida
    @Override
    protected void onPause() {
        super.onPause();
        if(efectosSonido) {
            mp.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(efectosSonido) {
            mp.start();
        }
    }

    @Override
    protected void onDestroy() {
        if(efectosSonido) {
            mp.stop();
        }
        stopService(new Intent(MainActivity.this,ServicioMusica.class));
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle estado) {
        super.onSaveInstanceState(estado);

        if (mp != null && efectosSonido) {
            int pos = mp.getCurrentPosition();
            estado.putInt("posicion", pos);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle estado) {
        super.onRestoreInstanceState(estado);
        if(efectosSonido) {
            if (estado != null && mp != null) {
                int pos = estado.getInt("posicion");
                mp.seekTo(pos);
            }
        }
    }
    private void SeleccionaAlmacen(SharedPreferences pref)
    {
        int tipoPuntuacion=Integer.parseInt( pref.getString("TipoPuntuaciones","0"));
        switch (tipoPuntuacion) {
            case 0:
                almacen = new AlmacenPuntuacionesArray();
                break;
            case 1:
                almacen= new AlmacenaPuntuacionesPreferencias(this);
                break;
            case 2:
                almacen = new AlmacenaPuntuacionesFicheroInterno(this);
                break;
            case 3:
                almacen = new AlmacenaPuntuacionesFicheroExterno(this);
                break;
            case 4:
                almacen = new AlmacenaPuntuacionesFicheroExtApl(this);
                break;
            case 5:
                almacen = new AlmacenaPuntuacionesRecursoRaw(this);
                break;
            case 6:
                almacen = new AlmacenaPuntuacionesRecursoAssets(this);
                break;
            case 7:
                almacen = new AlmacenaPuntuacionesXML_SAX(this);
                break;
            case 8:
                almacen = new AlmacenaPuntuacionesXML_DOM(this);
                break;
            case 9:
                almacen = new AlmacenaPuntuacionesSQLite(this);
                break;
            case 10:
                almacen = new AlmacenaPuntuacionesSQLiteRel(this);
                break;
            case 11:
                almacen = new AlmacenaPunanesProvider(this);
                break;
            case 12:
                almacen = new AlmacenaPuntuacionesSocket();
                break;
        }
    }
}