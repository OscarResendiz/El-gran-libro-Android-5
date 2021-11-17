package com.example.oscar.mislugares;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscar on 21/02/2018.
 */

public class Lugares {
    //para localizacion
    final static String TAG="MisLugares";
    protected static GeoPunto posicionActual=new GeoPunto(0,0);
    public Lugares()
    {
    }
    static Lugar elemento(int id)
    {
        Lugar lugar=null;
        SQLiteDatabase bd=lugaresDB.getReadableDatabase();
        Cursor cursor=bd.rawQuery("select * from lugares where _id="+id,null);
        if(cursor.moveToNext())
        {
            lugar=new Lugar();
            lugar.setNombre(cursor.getString(cursor.getColumnIndex("nombre")));
            lugar.setDireccion(cursor.getString(cursor.getColumnIndex("direccion")));
            lugar.setPosicion(new GeoPunto(cursor.getInt(cursor.getColumnIndex("longitud")),cursor.getInt(cursor.getColumnIndex("latitud"))));
            lugar.setTipo(TipoLugar.values()[cursor.getInt(cursor.getColumnIndex("tipo"))]);
            lugar.setFoto(cursor.getString(cursor.getColumnIndex("foto")));
            lugar.setTelefono(cursor.getInt(cursor.getColumnIndex("telefono")));
            lugar.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            lugar.setComentario(cursor.getString(cursor.getColumnIndex("comentario")));
            lugar.setFecha(cursor.getLong(cursor.getColumnIndex("fecha")));
            lugar.setValoracion(cursor.getInt(cursor.getColumnIndex("valoracion")));
        }
        cursor.close();
        bd.close();;
        return lugar;
    }
    static void anyade(Lugar lugar)
    {
    }
    private static  LugaresDB lugaresDB;

    static int nuevo()
    {
        int id=-1;
        Lugar lugar=new Lugar();
        SQLiteDatabase db=lugaresDB.getWritableDatabase();
        String cmd="insert into lugares(longitud,latitud,tipo,fecha) values("+lugar.getPosicion().getLongitud()+","+lugar.getPosicion().getLatitud()+","+lugar.getTipo().ordinal()+","+lugar.getFecha()+")";
        db.execSQL(cmd);
        Cursor c=db.rawQuery("select _id from lugares where fecha="+lugar.getFecha(),null);
        if(c.moveToNext())
        {
            id=c.getInt(0);
        }
        c.close();
        db.close();
        return id;
    }

    static void borrar(int id)
    {
        SQLiteDatabase db=lugaresDB.getWritableDatabase();
        db.execSQL("delete from lugares where _id="+id);
        db.close();
    }

    public static int size()
    {
        return 0;
    }

    public static List<Lugar> ejemploLugares()
    {
        ArrayList<Lugar> lugares=new ArrayList<Lugar>();
        lugares.add(new Lugar("Escuela politecnica superior de Gandia","C/ Paranimf, 1 46730 Gandia (Spain)",-0.166093,38.995656,TipoLugar.EDUCACION,0,"http://www.epgs.upv.es","uno de los mejores lugares para formarse",3));
        lugares.add(new Lugar("Al de siempre","C/ Paranimf, 1 46730 Gandia (Spain)",-0.166093,38.995656,TipoLugar.BAR,0,"http://www.epgs.upv.es","uno de los mejores lugares para formarse",3));
        lugares.add(new Lugar("Android curso.com","C/ Paranimf, 1 46730 Gandia (Spain)",-0.166093,38.995656,TipoLugar.COMPRAS,962849300,"http://www.epgs.upv.es","uno de los mejores lugares para formarse",3));
        lugares.add(new Lugar("Barranco del infierno","C/ Paranimf, 1 46730 Gandia (Spain)",-0.166093,38.995656,TipoLugar.DEPORTE,962849300,"http://www.epgs.upv.es","uno de los mejores lugares para formarse",3));
        lugares.add(new Lugar("La vital","C/ Paranimf, 1 46730 Gandia (Spain)",-0.166093,38.995656,TipoLugar.HOTEL,962849300,"http://www.epgs.upv.es","uno de los mejores lugares para formarse",3));
        return lugares;
    }

    public static  void inicializaDB(Context context)
    {
        lugaresDB=new LugaresDB(context);
    }

    public static Cursor listado()
    {
        SQLiteDatabase db=lugaresDB.getReadableDatabase();
        return db.rawQuery("select * from lugares", null);
    }
    public static void actualizaLugar(int id,Lugar lugar)
    {
        String comando="";
        comando+="update lugares set ";
        comando+=" nombre='"+lugar.getNombre()+"'";
        comando+=",direccion='"+lugar.getDireccion()+"'";
        comando+=",longitud="+lugar.getPosicion().getLatitud();
        comando+=",latitud="+lugar.getPosicion().getLongitud();
        comando+=",tipo="+lugar.getTipo().ordinal();
        comando+=",telefono='"+lugar.getTelefono()+"'";
        comando+=",url='"+lugar.getUrl()+"'";
        comando+=",comentario='"+lugar.getComentario()+"'";
        comando+=",valoracion="+lugar.getValoracion();
        comando+=",foto='"+lugar.getFoto()+"'";
        comando+=" where _id="+id;
        SQLiteDatabase db=lugaresDB.getWritableDatabase();
        db.execSQL(comando);
        db.close();
    }
    public static int buscarNombre(String nombre)
    {
        int id=-1;
        SQLiteDatabase db=lugaresDB.getReadableDatabase();
        Cursor c=db.rawQuery("select _if from lugares quere nombre ='"+nombre+"'",null);
        if(c.moveToNext())
        {
            id=c.getInt(c.getColumnIndex("_id"));
        }
        c.close();
        db.close();
        return  id;
    }
}
