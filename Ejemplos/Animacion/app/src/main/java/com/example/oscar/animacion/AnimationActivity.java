package com.example.oscar.animacion;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AnimationActivity extends AppCompatActivity {
AnimationDrawable animacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        animacion=(AnimationDrawable )getResources().getDrawable(R.drawable.animacion);
        ImageView vista=new ImageView(this);
        vista.setBackgroundColor(Color.WHITE);
        vista.setImageDrawable(animacion);
        vista.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        animacion.start();
                    }
                }
        );
        setContentView(vista);
    }
}
