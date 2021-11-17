package com.example.oscar.transition;

import android.graphics.drawable.TransitionDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        ImageView image=new ImageView(this);
        setContentView(image);
        TransitionDrawable trasicion=(TransitionDrawable)getResources().getDrawable(R.drawable.transicion);
        image.setImageDrawable(trasicion);
        trasicion.startTransition(2000);
    }
}
