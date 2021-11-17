package com.example.oscar.grabadora;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.IOException;

public class GrabadoaActivity extends AppCompatActivity {

    private static final String LOG_TAG="Grabadora";
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private static String fichero= Environment.getExternalStorageDirectory()+"/audio.3gp";
    private Button bGrabar,bDetener,bReproducir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grabadoa);
        bGrabar=(Button)findViewById(R.id.bGrabar);
        bDetener=(Button)findViewById(R.id.bDetener);
        bReproducir=(Button)findViewById(R.id.bReproducir);
        bDetener.setEnabled(false);
        bReproducir.setEnabled(false);
    }
    public void grabar(View view)
    {
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setOutputFile(fichero);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try
        {
            mediaRecorder.prepare();
        }
        catch (IOException e)
        {
            Log.e(LOG_TAG,"Fallo en grabacion:"+e.getMessage());
        }
        mediaRecorder.start();
        bGrabar.setEnabled(false);
        bDetener.setEnabled(true);
        bReproducir.setEnabled(false);
    }
    public void detenerGrabacion(View view)
    {
        mediaRecorder.stop();
        mediaRecorder.release();
        bGrabar.setEnabled(true);
        bDetener.setEnabled(false);
        bReproducir.setEnabled(true);
    }
    public void reproducir(View view)
    {
        mediaPlayer=new MediaPlayer();
        try
        {
            mediaPlayer.setDataSource(fichero);
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                                    public void onCompletion(MediaPlayer mp) {
                                                        //code to disable button here
                                                        Toast.makeText(GrabadoaActivity.this,"FIBN",Toast.LENGTH_LONG);
                                                    }
                                                }
                );
        }
        catch (IOException e)
        {
            Log.e(LOG_TAG,"Fallo en reproduccion:"+e.getMessage());
        }
    }
}
