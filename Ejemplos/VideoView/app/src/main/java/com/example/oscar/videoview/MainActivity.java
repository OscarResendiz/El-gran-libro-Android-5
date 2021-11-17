package com.example.oscar.videoview;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    private VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoView=(VideoView)findViewById(R.id.videoView);
        //de forma alternativa si queremos un streaming usar
       // videoView.setVideoURI(Uri.parse("https://www.youtube.com/watch?v=PT0X-dDanXI"));
        videoView.setVideoPath("/storage/sdcard0/video.mp4");
        videoView.start();
        videoView.requestFocus();
    }
}
