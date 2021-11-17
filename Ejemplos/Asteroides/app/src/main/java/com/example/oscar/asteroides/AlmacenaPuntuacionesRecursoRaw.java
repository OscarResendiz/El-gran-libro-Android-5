package com.example.oscar.asteroides;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

public class AlmacenaPuntuacionesRecursoRaw  implements AlmacenPuntuaciones {
    private Context context;
    public AlmacenaPuntuacionesRecursoRaw(Context contexto) {
        context=contexto;

    }
    @Override
    public void guardarPuntuacion(int puntos, String nombre, long fecha) {

    }

    @Override
    public Vector<String> listaPuntuaciones(int cantidad) {
        Vector<String> result=new Vector<String>();
        try
        {
            InputStream f=context.getResources().openRawResource(R.raw.puntuaciones);
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
