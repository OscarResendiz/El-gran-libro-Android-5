package com.example.oscar.asteroides;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Vector;

public class AlmacenaPuntuacionesFicheroInterno implements AlmacenPuntuaciones {
    private static  String FICHERO= "oscar/puntuaciones.txt";
    private Context context;
    public AlmacenaPuntuacionesFicheroInterno(Context contexto) {
        context=contexto;

    }
    @Override
    public void guardarPuntuacion(int puntos, String nombre, long fecha) {
        try
        {
            FileOutputStream f=context.openFileOutput(FICHERO,Context.MODE_APPEND);
            String texto=puntos+" "+nombre+"\n";
            f.write(texto.getBytes());
            f.close();
        }
        catch (Exception e)
        {
            Log.e("Asterorides", e.getMessage(), e);
        }
    }

    @Override
    public Vector<String> listaPuntuaciones(int cantidad) {
        Vector<String> result=new Vector<String>();
        try
        {
            FileInputStream f=context.openFileInput(FICHERO);
            BufferedReader entrada=new BufferedReader(new InputStreamReader(f));
            int n=0;
            String linea;
            do {
                linea=entrada.readLine();
                if(linea!=null)
                {
                    result.add(linea);
                    n++;
                }
            }
            while (n<cantidad && linea!=null);
        }
        catch (Exception e)
        {
            Log.e("Asterorides", e.getMessage(), e);
        }
        return result;
    }
}
