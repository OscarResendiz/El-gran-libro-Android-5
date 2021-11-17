package org.example.puntuacionesprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PuntuacionesSQLiteHelper extends SQLiteOpenHelper {
    public PuntuacionesSQLiteHelper(Context context)
    {
        super(context,"puntuaciones",null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //se crea la estructura de la base de datos
        db.execSQL("create table puntuaciones(_id integer primary key autoincrement,puntos integer, nombre text, fecha long)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
