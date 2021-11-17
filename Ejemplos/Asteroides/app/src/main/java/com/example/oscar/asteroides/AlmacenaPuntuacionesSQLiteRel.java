package com.example.oscar.asteroides;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Vector;

public class AlmacenaPuntuacionesSQLiteRel extends SQLiteOpenHelper implements AlmacenPuntuaciones{
    public AlmacenaPuntuacionesSQLiteRel(Context context)
    {
        // se crea la segunda version de la base de datos
        super(context,"puntuaciones",null,2);
    }
    //---------------------metodos de SQLiteHelper-------------------------------------
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table usuarios(usu_id integer primary key autoincrement, nombre text, correo text)");
        db.execSQL("create table puntuaciones2(punt_id integer primary key autoincrement, puntos integer, fecha long, usuario integer, foreign key (usuario) references usuarios(usu_id) )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion==1 && newVersion==2)
        {
            onCreate(db);
            Cursor cursor=db.rawQuery("select puntos, nombre, fecha from puntuaciones ",null);
            while (cursor.moveToNext())
            {
                guardaPuntuacion(db,cursor.getInt(0),cursor.getString(1),cursor.getInt(2));
            }
            cursor.close();
            db.execSQL("drop table puntuaciones");
        }
    }
    //-------------------metodos de AlmacenPuntuaciones----------------------------------------
    @Override
    public Vector<String> listaPuntuaciones(int cantidad) {
        Vector<String> result= new Vector<String>();
        SQLiteDatabase db=getReadableDatabase();
        String comando="select puntos, nombre from puntuaciones2, usuarios where usuario=usu_id order by puntos desc limit "+cantidad;
        Cursor cursor=db.rawQuery(comando,null);
        while (cursor.moveToNext())
        {
            result.add(cursor.getInt(cursor.getColumnIndex("puntos"))+" "+cursor.getString(cursor.getColumnIndex("nombre")));
        }
        cursor.close();
        db.close();
        return  result;
    }
    @Override
    public void guardarPuntuacion(int puntos, String nombre, long fecha) {
        SQLiteDatabase db=getWritableDatabase();
        guardaPuntuacion(db,puntos,nombre,fecha);
    }
    public void guardaPuntuacion(SQLiteDatabase db,int puntos, String nombre, long fecha)
    {
        int usuario=buscaInserta(db,nombre);
        db.execSQL("pragma foreign_key=on");
        db.execSQL("insert into puntuaciones2 values(null,"+puntos+","+fecha+","+usuario+")");
    }
    private int buscaInserta(SQLiteDatabase db, String nombre) {
        Cursor cursor = db.rawQuery("select usu_id from usuarios where nombre='" + nombre + "'", null);
        if (cursor.moveToNext()) {
            int result = cursor.getInt(0);
            cursor.close();
            return result;
        }
        cursor.close();
        db.execSQL("insert into usuarios values(null,'" + nombre + "','correo@dominio.mx')");
        return buscaInserta(db, nombre);
    }
}
