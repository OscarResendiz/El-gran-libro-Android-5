package com.example.oscar.asteroides;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Vector;

public class AlmacenaPuntuacionesSQLite extends SQLiteOpenHelper implements AlmacenPuntuaciones {
    public AlmacenaPuntuacionesSQLite(Context context)
    {
        super(context,"puntuaciones",null,1); //se crea la base de datos
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //se crea la estructura de la base de datos
        db.execSQL("create table puntuaciones(_id integer primary key autoincrement,puntos integer, nombre text, fecha long)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //en caso de que se actualice la tabla aqui se ponen las instrucciones para modificar la estructura
    }

    @Override
    public void guardarPuntuacion(int puntos, String nombre, long fecha) {
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("insert into puntuaciones values(null,"+puntos+",'"+nombre+"',"+fecha+")");;
        db.close();
    }

    @Override
    public Vector<String> listaPuntuaciones(int cantidad) {
        Vector<String> result= new Vector<String>();
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.rawQuery("select puntos, nombre from puntuaciones order by puntos desc limit "+cantidad,null);
        while (cursor.moveToNext())
        {
            result.add(cursor.getInt(0)+" "+cursor.getString(1));
        }
        cursor.close();;
        db.close();;
        return result;
    }
}
