package com.example.oscar.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import static android.util.Log.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Hola oscar","Entramos en onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        TextView texto= new TextView(this);
  //      texto.setText("hola oscar");
    //    setContentView(texto);

    }
}
