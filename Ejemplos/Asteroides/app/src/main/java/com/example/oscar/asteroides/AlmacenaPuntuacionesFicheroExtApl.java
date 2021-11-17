package com.example.oscar.asteroides;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Vector;

public class AlmacenaPuntuacionesFicheroExtApl implements AlmacenPuntuaciones{
    private static  String DIRECTORIO= Environment.getExternalStorageDirectory()+"/Android/data/com.example.oscar.asteroides/files";
    private static  String FICHERO= Environment.getExternalStorageDirectory()+"/Android/data/com.example.oscar.asteroides/files/puntuaciones.txt";
    private Context context;
    public AlmacenaPuntuacionesFicheroExtApl(Context contexto) {
        context=contexto;
       // String directorio=Environment.getDataDirectory()+"/Android/data/com.example.oscar.asteroides/files/";
        String directorio=Environment.getDataDirectory()+"/com.example.oscar.asteroides/files/";
        directorio=DIRECTORIO+"/com.example.oscar.asteroides/files/";
        File ruta=new File(directorio);
        if(!ruta.exists())
        {
            ruta.mkdirs();
        }

    }
    @Override
    public void guardarPuntuacion(int puntos, String nombre, long fecha) {
        try
        {
            String estadoSD=Environment.getExternalStorageState();
            if(!estadoSD.equals(Environment.MEDIA_MOUNTED))
            {
                Toast.makeText(context, "No puedo escribir en la memoria externa", Toast.LENGTH_SHORT).show();
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
