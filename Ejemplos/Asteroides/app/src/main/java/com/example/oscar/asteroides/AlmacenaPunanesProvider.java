package com.example.oscar.asteroides;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.util.Vector;

public class AlmacenaPunanesProvider implements AlmacenPuntuaciones {
    private Activity activity;
    public AlmacenaPunanesProvider(Activity atv)
    {
        activity=atv;
    }
    @Override
    public void guardarPuntuacion(int puntos, String nombre, long fecha) {
        Uri uri=Uri.parse("content://org.example.puntuacionesprovider/puntuaciones");
        ContentValues valores=new ContentValues();
        valores.put("nombre",nombre);
        valores.put("puntos",puntos);
        valores.put("fecha",fecha);
        try {
            activity.getContentResolver().insert(uri,valores);
        }
        catch (Exception e)
        {
            Toast.makeText(activity,"verificar que este intalado org.example.puntuacionesprovider.PuntuacionesProvider",Toast.LENGTH_LONG).show();
            Log.e("asteroides","Error "+e.toString(),e);
        }
    }

    @Override
    public Vector<String> listaPuntuaciones(int cantidad) {
        Vector<String> result = new Vector<String>();
        Uri uri = Uri.parse("content://org.example.puntuacionesprovider/puntuaciones");
        try {
            Cursor cursor = activity.getContentResolver().query(uri, null, null, null, "fecha desc");
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
                    int puntos = cursor.getInt(cursor.getColumnIndex("puntos"));
                    result.add(puntos + " " + nombre);
                }
            }
        }
        catch (Exception e)
        {
            Toast.makeText(activity,e.toString(),Toast.LENGTH_LONG).show();
        }
        return result;
    }
}
