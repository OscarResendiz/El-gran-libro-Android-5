package com.example.oscar.gestures;

import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GestureOverlayView.OnGesturePerformedListener{
    private GestureLibrary libreria;
    private TextView salida;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        libreria=GestureLibraries.fromRawResource(this,R.raw.gestures);
        if(!libreria.load())
        {
            finish();
        }
        GestureOverlayView gestureView=(GestureOverlayView) findViewById(R.id.gestures);
        gestureView.addOnGesturePerformedListener(this);
        salida=(TextView)findViewById(R.id.salida);
    }

    @Override
    public void onGesturePerformed(GestureOverlayView ov, Gesture gesture) {
        ArrayList<Prediction> predictions=libreria.recognize(gesture);
        salida.setText("");
        for (Prediction prediction :predictions)
        {
            salida.setText(prediction.name+" "+prediction.score+"\n");
        }
    }
}
