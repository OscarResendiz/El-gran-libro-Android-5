package com.example.oscar.asteroides;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Vector;

public class AlmacenaPuntuacionesPreferencias implements AlmacenPuntuaciones {
    private static  String PREFERENCIAS="puntuaciones";
    private Context context;
    public  AlmacenaPuntuacionesPreferencias(Context contexto)
    {
        context=contexto;
    }
    @Override
    public void guardarPuntuacion(int puntos, String nombre, long fecha) {
        SharedPreferences preferencias=context.getSharedPreferences(PREFERENCIAS,Context.MODE_APPEND);
        SharedPreferences.Editor editor=preferencias.edit();
        for(int n=9;n>=1;n--)
        {
            editor.putString("puntuacion"+n,preferencias.getString("puntuacion"+(n-1),""));
        }
        editor.putString("puntuacion0",puntos+" "+nombre);
        editor.commit();
    }

    @Override
    public Vector<String> listaPuntuaciones(int cantidad) {
        Vector<String> result=new Vector<String>();
        SharedPreferences preferencias=context.getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
        for(int n=0;n<=9;n++)
        {
            String s=preferencias.getString("puntuacion"+n,"");
            if(s!="")
            {
                result.add(s);
            }

        }
        return result;
    }
}
