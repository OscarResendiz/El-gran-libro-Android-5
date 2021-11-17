package com.example.oscar.comunicacionactividades;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CVerificar extends Activity {
    private Button BAceptar;
    private Button BRechazar;
    private TextView TNombre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_verificacion);
        InicializaCoponentes();
        Bundle extras=getIntent().getExtras();
        String nombre=extras.getString("Nombre");
        TNombre.setText("Hola "+nombre+"\nAceptas las condiciones");
    }
    //se encarga de inicializar los componentes
    private void InicializaCoponentes()
    {
        //cargo los componenetes
        BAceptar=(Button)findViewById(R.id.BAceptar);
        BRechazar=(Button) findViewById(R.id.BRechazar);
        TNombre=(TextView)findViewById(R.id.TNombre);
        //aplico los eventos
        BAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Aceptar(null);
            }
        });
        BRechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rechazar(null);
            }
        });
    }
    private void Aceptar(View view)
    {
        Respuesta("Acepta");
    }
    private  void Rechazar(View view)
    {
        Respuesta("Rechaza");

    }
    private void Respuesta(String res)
    {
        Intent i=new Intent();
        i.putExtra("resultado",res);
        setResult(RESULT_OK,i);
        finish();
    }
}
