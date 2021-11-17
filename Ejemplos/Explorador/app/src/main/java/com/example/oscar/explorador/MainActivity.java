package com.example.oscar.explorador;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends Activity {
    private ArrayList<String> SD_CARDS;
    private TextView rutas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rutas = (TextView) findViewById(R.id.textView1);
        SD_CARDS = getAllsds();
        String sd = "";
        for (int i=0;i<SD_CARDS.size();i++)
        {
            sd += SD_CARDS.get(i);
            sd += "\n";
        }
        rutas.setText(sd);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
     //   getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//        if (id == R.id.action_settings) {
  //          return true;
    //    }
        return super.onOptionsItemSelected(item);
    }
    private ArrayList<String> getAllsds() {
        ArrayList<String> tmp = new ArrayList<String>(); // Array de todas las rutas

        String rutas[] = { "/mnt/", "/storage/" }; // Rutas posibles

        for (int i = 0; i < rutas.length; i++) {
            File file = new File(rutas[i]);// Ruta actual ( listamos de uno a uno )
            if (!file.exists()) // Si no existe pasamos a la siguiente
                continue;
            System.out.println("Ruta : " + rutas[i]); // Imprimimos para "depurar"
            String f[] = file.list(); // Contenido de ruta actual
            for (int o = 0; o < f.length; o++) { // Recorremos el contenido de la ruta actual
                if (f[o].indexOf("sd") != -1) { // Vemos si contiene sd en el nombre si es asi es almacenamiento
                    String ruta = rutas[i] + f[o]; // Creamos una Ruta,esta ruta es la original
                    File fd = new File(ruta); // Creamos un nuevo File para evitar rutas repetidas basadas en symlinks
                    String toAdd;
                    try {
                        System.out.println("Canocial :"
                                + fd.getCanonicalPath().toString()); // Esta seria la ruta original
                        toAdd = fd.getCanonicalPath().toString();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        toAdd = null;
                        continue;
                    }
                    if (!tmp.contains(toAdd)) { // Si se repitiera no lo añadimos
                        System.out.println("A añadir :" + toAdd);
                        tmp.add(toAdd);
                    }
                }
            }
        }

        return tmp; // Regresamos el Array de almacenamientos
    }
}
