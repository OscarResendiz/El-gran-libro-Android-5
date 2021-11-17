package com.example.oscar.asteroides;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.example.oscar.asteroides.AlmacenPuntuaciones;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

public class AlmacenaPuntuacionesSocket implements AlmacenPuntuaciones {
    private String ip;
    public AlmacenaPuntuacionesSocket()
    {
        ip="192.168.42.31";
        ip="158.42.146.127";
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
    }
    @Override
    public void guardarPuntuacion(int puntos, String nombre, long fecha) {
        try
        {
            Socket sk=new Socket(ip,1234);
            BufferedReader entrada=new BufferedReader(new InputStreamReader( sk.getInputStream()));
            PrintWriter salida=new PrintWriter(new OutputStreamWriter(sk.getOutputStream()));
            salida.println(puntos+" "+nombre);
            String respuesta=entrada.readLine();
            if(!respuesta.equals("OK"))
            {
                Log.e("Asteroides", "Error respuesta del servidor incorrecta");
            }
            sk.close();
        }
        catch (Exception e)
        {
            Log.e("Asteroides", e.toString(),e);
        }
    }

    @Override
    public Vector<String> listaPuntuaciones(int cantidad) {
        Vector<String> result=new Vector<String>();
        try
        {
            Socket sk=new Socket(ip,1234);
            BufferedReader entrada=new BufferedReader(new InputStreamReader( sk.getInputStream()));
            PrintWriter salida=new PrintWriter(new OutputStreamWriter(sk.getOutputStream()));
            salida.println("PUNTUACIONES");
            int n=0;
            String respuesta;
            do {
                respuesta=entrada.readLine();
                if(respuesta!=null)
                {
                    result.add(respuesta);
                    n++;
                }
            }while (n<cantidad && respuesta!=null);
            sk.close();
        }
        catch (Exception e)
        {
            Log.e("Asteroides", e.toString(),e);
        }
        return  result;
    }
}
