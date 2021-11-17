package com.example.oscar.masvistas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText entrada;
    private TextView salida;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        entrada=(EditText)findViewById(R.id.entrada);
        salida=(TextView)findViewById(R.id.salida);
    }
    public void  sePulsa(View view)
    {
       // Toast.makeText(this,"Pulsado",Toast.LENGTH_SHORT).show();
        salida.setText(String.valueOf(Float.parseFloat(entrada.getText().toString())*2.0));
    }
    public void sePulsa0(View view)
    {
        entrada.setText(entrada.getText()+(String)view.getTag());
    }

}
