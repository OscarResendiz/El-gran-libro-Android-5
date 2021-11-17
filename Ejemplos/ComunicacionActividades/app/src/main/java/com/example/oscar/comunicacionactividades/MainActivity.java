package com.example.oscar.comunicacionactividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button BVerificar;
    private EditText TNombre;
    private TextView TResultado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BVerificar=(Button)findViewById(R.id.BVerificar);
        BVerificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Verificar(null);
            }
        });
        TNombre=(EditText)findViewById(R.id.TNombre);
        TResultado=(TextView)findViewById(R.id.TResultado);
    }
    private void Verificar(View view)
    {
        Intent i;
        i = new Intent(this,CVerificar.class);
        String s= (String) TNombre.getText().toString();
        i.putExtra("Nombre",s);
        startActivityForResult(i,123);
    }
    //funcion que regresa el resultado de la actividad
    @Override
    protected void onActivityResult(int codigo,int resultado, Intent datos)
    {
        if(codigo!=123)
            return; //no es mi codigo
        if(resultado!= RESULT_OK) {
            TResultado.setText("Te vatearon");
            return; //fue rechazado
        }
        String s=datos.getExtras().getString("resultado");
        TResultado.setText(s);
    }
}
