package com.example.oscar.asteroides;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Vector;

public class AlmacenaPuntuacionesFicheroExterno implements AlmacenPuntuaciones{
    private static  String FICHERO= Environment.getExternalStorageDirectory()+ "/oscar/puntuaciones.txt";
    private Context context;
    public AlmacenaPuntuacionesFicheroExterno(Context contexto) {
        context=contexto;

    }
    @Override
    public void guardarPuntuacion(int puntos, String nombre, long fecha) {
        try
        {
            String estadoSD=Environment.getExternalStorageState();
            if(!estadoSD.equals(Environment.MEDIA_MOUNTED))
            {
                Toast.makeText(context,"No puedo escribir en la memoria externa",Toast.LENGTH_SHORT).show();
                return;
            }
            FileOutputStream f= new FileOutputStream(FICHERO,true);
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
            String estadoSD=Environment.getExternalStorageState();
            if(!estadoSD.equals(Environment.MEDIA_MOUNTED))
            {
                Toast.makeText(context,"No puedo leer la memoria externa",Toast.LENGTH_SHORT).show();
                return result;
            }
            FileInputStream f=new FileInputStream(FICHERO);
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
