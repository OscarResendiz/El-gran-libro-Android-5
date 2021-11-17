package com.example.oscar.usarpermiso;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void verVideo(View view)
    {
        Intent i=new Intent();
        i.setClassName("com.payperview.servicios","com.payperview.servicios.VerVideo");
        startActivity(i);

    }
}
