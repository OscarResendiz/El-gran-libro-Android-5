package com.example.oscar.videoplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class videoPlayer extends AppCompatActivity implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, SurfaceHolder.Callback
{
    private MediaPlayer mediaPlayer;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private EditText editText;
    private ImageButton bPlay,bPause, bStop,bLog;
    private TextView logTextView;
    private boolean pause;
    private String path;
    private  int savePos=0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        surfaceView=(SurfaceView)findViewById(R.id.surfaceView);
        surfaceHolder=surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        editText=(EditText) findViewById(R.id.path);
        editText.setText("http://personales.gan.upv.es/~jtomas/video.3gp");
        logTextView=(TextView)findViewById(R.id.log);
        bPlay=(ImageButton) findViewById(R.id.play);
        bPlay.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         if(mediaPlayer!=null)
                                         {
                                             if(pause)
                                             {
                                                 mediaPlayer.start();
                                             }
                                             else
                                             {
                                                 playVideo();
                                             }
                                         }
                                     }
                                 }
        );
        bPause=(ImageButton) findViewById(R.id.pause);
        bPause.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         if(mediaPlayer!=null)
                                         {
                                             pause=true;
                                                 mediaPlayer.pause();
                                         }
                                     }
                                 }
        );
        bStop=(ImageButton) findViewById(R.id.stop);
        bStop.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         if(mediaPlayer!=null)
                                         {
                                             pause=false;
                                             mediaPlayer.stop();
                                         }
                                     }
                                 }
        );
        bLog=(ImageButton) findViewById(R.id.logButon);
        bLog.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         if(logTextView.getVisibility()==TextView.VISIBLE)
                                         {
                                             logTextView.setVisibility(TextView.INVISIBLE);
                                         }
                                         else
                                         {
                                             logTextView.setVisibility(TextView.VISIBLE);
                                         }
                                     }
                                 }
        );
        log("");
    }
    private void playVideo()
    {
        try

        {
            pause = false;
            path = editText.getText().toString();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(path);
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.prepare();
            //mediaPlayer.prepareAsync();
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.seekTo(savePos);
        }
        catch (Exception e)
        {
            log("ERROR: "+ e.getMessage());
        }
    }
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        log("onBufferingUpdate percent: "+ percent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        log("onCompletion called");
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        log("onPrepared called");
        int mVideoWidth=mediaPlayer.getVideoWidth();
        int mVideoHeight=mediaPlayer.getVideoHeight();
        if(mVideoWidth!=0 &&mVideoHeight!=0)
        {
            surfaceHolder.setFixedSize(mVideoWidth,mVideoHeight);
            mediaPlayer.start();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        log("Entramos en surfaceCreated");
                playVideo();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        log("Entramos en surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        log("Entramos en surfaceDestroyed");
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(mediaPlayer!=null)
        {
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }
    @Override
    public void  onPause()
    {
        super.onPause();
        if(mediaPlayer!=null && !pause)
        {
            mediaPlayer.pause();
        }
    }
    @Override
    public void onResume()
    {
        super.onResume();
        if(mediaPlayer!=null && !pause)
        {
            mediaPlayer.start();
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle guardarEstado)
    {
        super.onSaveInstanceState(guardarEstado);
        if(mediaPlayer!=null)
        {
            int pos=mediaPlayer.getCurrentPosition();
            guardarEstado.putString("ruta",path);
            guardarEstado.putInt("posicion", pos);
        }
    }
    @Override
    protected void onRestoreInstanceState(Bundle recEstado)
    {
        super.onRestoreInstanceState(recEstado);
        if(mediaPlayer!=null)
        {
            path=recEstado.getString("ruta");
            savePos=recEstado.getInt("posicion");
        }
    }
    private void  log(String s)
    {
        logTextView.append(s+"\n");
    }
}
