package com.example.oscar.mislugares;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LugaresDB extends SQLiteOpenHelper {
    public  LugaresDB(Context context)
    {
        super(context,"lugares",null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase bd) {
        //creacion de la estructura de la base de datos
        bd.execSQL("Create table lugares(_id integer primary key autoincrement, nombre text,direccion text, longitud real, latitud real, tipo integer, foto text, telefono integer, url text, comentario text, fecha long, valoracion real)");

        agregaLugar(bd,"Escuela politecnica superior de Gandia" , "C/ Paranimf, 1 46730 Gandia (Spain)" ,-0.166093 , 38.995656 , TipoLugar.EDUCACION.ordinal() , "" ,"5456789123" ,"http://www.epgs.upv.es" ,"uno de los mejores lugares para formarse" , System.currentTimeMillis() ,1.0 );
        agregaLugar(bd,"Al de siempre" , "C/ Paranimf, 1 46730 Gandia (Spain)" ,-0.166093 , 38.995656 , TipoLugar.BAR.ordinal() , "" ,"5123789456" ,"http://www.epgs.upv.es" ,"cocinan sabroso" , System.currentTimeMillis() ,3.5 );
        agregaLugar(bd,"Android curso.com", "C/ Paranimf, 1 46730 Gandia (Spain)" ,-0.166093 , 38.995656 , TipoLugar.COMPRAS.ordinal() , "" ,"1478963210" ,"http://www.epgs.upv.es" ,"Dicen que hay buena documentacion" , System.currentTimeMillis() ,2.0 );
        agregaLugar(bd,"Barranco del infierno" , "C/ Paranimf, 1 46730 Gandia (Spain)" ,-0.166093 , 38.995656 , TipoLugar.DEPORTE.ordinal() , "" ,"5209637410" ,"http://www.epgs.upv.es" ,"huy que miedo" , System.currentTimeMillis() ,3.0 );
        agregaLugar(bd,"La vital" , "C/ Paranimf, 1 46730 Gandia (Spain)" ,-0.166093 , 38.995656 , TipoLugar.HOTEL.ordinal() , "" ,"5554013673" ,"http://www.epgs.upv.es" ,"no se que es" , System.currentTimeMillis() ,5.0 );
    }
    private void agregaLugar(SQLiteDatabase bd,String nombre , String direccion ,double longitud , double latitud , int tipo , String foto ,String telefono ,String url ,String comentario , long fecha ,double valoracion )
    {
        String cmd="insert into lugares (nombre ,direccion , longitud , latitud , tipo , foto , telefono , url , comentario , fecha , valoracion ) values('"+nombre+"' ,'"+direccion+"' , "+longitud+" , "+latitud+" , "+tipo+" , '"+foto+"' , '"+telefono+"' , '"+url+"' , '"+comentario+"' , "+fecha+" , "+valoracion+")" ;
        bd.execSQL(cmd);

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
