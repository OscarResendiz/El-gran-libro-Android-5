package oscar.com.despertador;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DataBase extends SQLiteOpenHelper {
    public static final String ID_CANCION="idCancion";
    public static final String PATH="path";
    public static final String NOMBRE="nombre";
    public static final String ID_LISTA="idLista";
    public static final String ID_ALARMA="idAlarma";
    public static final String HORA="hora";
    public static final String MINUTO="minuto";
    public static final String AM="am";
    public static final String ACTIVA="activa";
    public static final String LUNES="lunes";
    public static final String MARTES="martes";
    public static final String MIERCOLES="miercoles";
    public static final String JUEVES="jueves";
    public static final String VIERNES="viernes";
    public static final String SABADO="sabado";
    public static final String DOMINGO="domingo";
    public static final String TABLA_CANCION="cancion";
    public static final String TABLA_LISTA_CANCION="listaCancion";
    public static final String TABLA_LISTA_REPRODUCCION="listaReproduccion";
    public static final String TABLA_ALARMA="alarma";
    public static final String TABLA_CANCION_SISTEMA="cancionSistema";
    public static final String ENCENDIDA="alarma";
    public static final String TABLA_REPRODUCCION="reproduccion";
    public static final String TIPO="tipo";
    public static final String POSICION="posicion";
    public static final String IDENTIFICADOR="identificador";
    private Cursor cursor;
    private SQLiteDatabase db;
    public DataBase(Context context)
    {
        super(context,"despertador",null,1);
    }
    //--------------------------------------------------------------------Manejo de cancioes---------------------
    public  Cancion getCancion(int idCancion)
    {
        //regresa una cancion especifica
        Cancion cancion=null;
        Cursor c=consulta(" select * from "+TABLA_CANCION+" where "+ID_CANCION+"="+idCancion);
        if(c!=null)
        {
            if(c.moveToNext())
            {
                cancion.setIdCancion(c.getInt(c.getColumnIndex(ID_CANCION)));
                cancion.setNombre(c.getString(c.getColumnIndex(NOMBRE)));
                cancion.setPath(c.getString(c.getColumnIndex(PATH)));
            }
            cierraConsulta();
        }
        return cancion;
    }
    public  Cancion getCancionByName(String nombre)
    {
        //regresa una cancion especifica
        Cancion cancion=null;
        Cursor c=consulta(" select * from "+TABLA_CANCION+" where "+NOMBRE+"='"+nombre+"'");
        if(c!=null)
        {
            if(c.moveToNext())
            {
                cancion=new Cancion();
                cancion.setIdCancion(c.getInt(c.getColumnIndex(ID_CANCION)));
                cancion.setNombre(c.getString(c.getColumnIndex(NOMBRE)));
                cancion.setPath(c.getString(c.getColumnIndex(PATH)));
            }
            cierraConsulta();
        }
        return cancion;
    }
    public List<Cancion> getCancionesLista(int idLista)
    {
        //regresa las canciones que pertenecen a la lista
        List<Cancion> lista=new ArrayList<Cancion>();
        Cursor c=consulta("select c.* from "+TABLA_CANCION+" c, "+TABLA_LISTA_CANCION+" l where c."+ID_CANCION+"=l."+ID_CANCION+" and l."+ID_LISTA+"="+idLista);
        if(c!=null)
        {
            while (c.moveToNext())
            {
                Cancion cancion=new Cancion();
                cancion.setIdCancion(c.getInt(c.getColumnIndex(ID_CANCION)));
                cancion.setNombre(c.getString(c.getColumnIndex(NOMBRE)));
                cancion.setPath(c.getString(c.getColumnIndex(PATH)));
                lista.add(cancion);
            }
        }
        cierraConsulta();
        return lista;
    }
    public List<Cancion> getCanciones()
    {
        //regresa todas las canciones almacenadas
        List<Cancion> lista=new ArrayList<Cancion>();
        Cursor c=consulta("select * from "+TABLA_CANCION+"");
        if(c!=null)
        {
            while (c.moveToNext())
            {
                Cancion cancion=new Cancion();
                cancion.setIdCancion(c.getInt(c.getColumnIndex(ID_CANCION)));
                cancion.setNombre(c.getString(c.getColumnIndex(NOMBRE)));
                cancion.setPath(c.getString(c.getColumnIndex(PATH)));
                lista.add(cancion);
            }
        }
        cierraConsulta();
        return lista;
    }
    public boolean ExisteCancion(String nombre)
    {
        boolean resultado=false;
        Cursor c=consulta("select * from "+TABLA_CANCION+" where "+ NOMBRE+" = '"+nombre+"'");
        if(c!=null) {
            while (c.moveToNext()) {
                resultado=true;
                break;
            }
        }
        cierraConsulta();
        return resultado;

    }
    public void updateCancion(int idCancion, String nombre, String path)
    {
        //actualiza los datos de la cancion
        executa("update "+TABLA_CANCION+" set "+NOMBRE+"='"+nombre+"' where "+ID_CANCION+"="+idCancion);
    }
    public int insertCancion(String nombre, String path)
    {
        //agrega la cancion a la base y regresa su id
        int id=-1;
        executa("insert into "+TABLA_CANCION+"("+NOMBRE+","+PATH+") values ('"+nombre+"','"+path+"')");
        Cursor c=consulta("select max("+ID_CANCION+") as id from "+TABLA_CANCION+"");
        if(c!=null)
        {
            if(c.moveToNext())
            {
                id=c.getInt(c.getColumnIndex("id"));
            }
            cierraConsulta();
        }
        return id;
    }
    public void insertCancionLista(int idLista,int idCancion)
    {
        //asocia una cancion a una lista
        executa("insert into listaCancion(idCancion,idLista) values("+idCancion+","+idLista+")");
    }
    public boolean ExisteCancionLista(int idLista, int idCancion)
    {
        boolean resultado=false;
        Cursor c=consulta("select * from "+TABLA_LISTA_CANCION+" where "+ID_CANCION+"="+idCancion+" and "+ID_LISTA+"="+idLista);
        if(c!=null)
        {
            while (c.moveToNext())
            {
                resultado=true;
                break;
            }
        }
        cierraConsulta();
        return resultado;

    }
    public void deleteCancion(int idCancion)
    {
        //elimina una cancion de la base y de todas las listas a las que este asociada
        executa("delete from "+TABLA_CANCION+" where "+ID_CANCION+"="+idCancion);
    }
    public void deleteCanciones()
    {
        //elimina una cancion de la base y de todas las listas a las que este asociada
        executa("delete from cancion ");
    }
    public void  deleteCancionLista(int idLista, int idCancion)
    {
        //quita la cancion de la lista
        executa("delete from "+TABLA_LISTA_CANCION+" where "+ID_LISTA+"="+idLista+" and "+ID_CANCION+"="+idCancion);
    }

    //----------------------------------------------------- manejo de canciones del sistema----------------------
    public  Cancion getCancionSistema(int idCancion)
    {
        //regresa una cancion especifica
        Cancion cancion=null;
        Cursor c=consulta(" select * from "+TABLA_CANCION_SISTEMA+" where "+ID_CANCION+"="+idCancion);
        if(c!=null)
        {
            if(c.moveToNext())
            {
                cancion.setIdCancion(c.getInt(c.getColumnIndex(ID_CANCION)));
                cancion.setNombre(c.getString(c.getColumnIndex(NOMBRE)));
                cancion.setPath(c.getString(c.getColumnIndex(PATH)));
            }
            cierraConsulta();
        }
        return cancion;
    }
    public  Cancion getCancionSistemaByName(String nombre)
    {
        //regresa una cancion especifica
        Cancion cancion=null;
        Cursor c=consulta(" select * from "+TABLA_CANCION_SISTEMA+" where "+NOMBRE+"='"+nombre+"'");
        if(c!=null)
        {
            if(c.moveToNext())
            {
                cancion=new Cancion();
                cancion.setIdCancion(c.getInt(c.getColumnIndex(ID_CANCION)));
                cancion.setNombre(c.getString(c.getColumnIndex(NOMBRE)));
                cancion.setPath(c.getString(c.getColumnIndex(PATH)));
            }
            cierraConsulta();
        }
        return cancion;
    }
    public List<Cancion> getCancionesSistema()
    {
        //regresa todas las canciones almacenadas
        List<Cancion> lista=new ArrayList<Cancion>();
        Cursor c=consulta("select * from "+TABLA_CANCION_SISTEMA+"");
        if(c!=null)
        {
            while (c.moveToNext())
            {
                Cancion cancion=new Cancion();
                cancion.setIdCancion(c.getInt(c.getColumnIndex(ID_CANCION)));
                cancion.setNombre(c.getString(c.getColumnIndex(NOMBRE)));
                cancion.setPath(c.getString(c.getColumnIndex(PATH)));
                lista.add(cancion);
            }
        }
        cierraConsulta();
        return lista;
    }
    public boolean ExisteCancionSistema(String nombre)
    {
        boolean resultado=false;
        Cursor c=consulta("select * from "+TABLA_CANCION_SISTEMA+" where "+ NOMBRE+" = '"+nombre+"'");
        if(c!=null) {
            while (c.moveToNext()) {
                resultado=true;
                break;
            }
        }
        cierraConsulta();
        return resultado;

    }
    public int insertCancionSistema(String nombre, String path)
    {
        //agrega la cancion a la base y regresa su id
        int id=-1;
        executa("insert into "+TABLA_CANCION_SISTEMA+"("+NOMBRE+","+PATH+") values ('"+nombre+"','"+path+"')");
        Cursor c=consulta("select max("+ID_CANCION+") as id from "+TABLA_CANCION_SISTEMA+"");
        if(c!=null)
        {
            if(c.moveToNext())
            {
                id=c.getInt(c.getColumnIndex("id"));
            }
            cierraConsulta();
        }
        return id;
    }
    public void deleteCancionSistema(int idCancion)
    {
        //elimina una cancion de la base y de todas las listas a las que este asociada
        executa("delete from "+TABLA_CANCION_SISTEMA+" where "+ID_CANCION+"="+idCancion);
    }
    public void deleteCancionesSistema()
    {
        //elimina una cancion de la base y de todas las listas a las que este asociada
        executa("delete from "+TABLA_CANCION_SISTEMA);
    }
    //----------------------------------------------------- manejo de lsitas de reoproduccion----------------------
    public ListaReproduccion getLista(int idLista)
    {
        //regresa una lista en especifico
        ListaReproduccion obj=null;
        Cursor c=consulta("select * from "+TABLA_LISTA_REPRODUCCION+" where "+ID_LISTA+"="+idLista);
        if(c!=null)
        {
            if(c.moveToNext())
            {
                obj =new ListaReproduccion();
                //obj.setCanciones(getCancionesLista(idLista));
                obj.setIdLista(idLista);
                obj.setNombre(c.getString(c.getColumnIndex(NOMBRE)));
            }
            cierraConsulta();
        }
        return obj;
    }
    public List<ListaReproduccion> getListas()
    {
        //regresa todas las listas de reproduccion
        List<ListaReproduccion> lista=new ArrayList<ListaReproduccion>();
        Cursor c=consulta("select * from "+TABLA_LISTA_REPRODUCCION+"");
        if(c!=null)
        {
            while (c.moveToNext())
            {
                ListaReproduccion obj =new ListaReproduccion();
                //obj.setCanciones(getCancionesLista(idLista));
                obj.setIdLista(c.getInt(c.getColumnIndex(ID_LISTA)));
                obj.setNombre(c.getString(c.getColumnIndex(NOMBRE)));
                lista.add(obj);
            }
            cierraConsulta();
        }
        return lista;

    }
    public void updateLista(int idLista, String nombre)
    {
        //actualiza la lista de reproduccion
        executa("update "+TABLA_LISTA_REPRODUCCION+" set "+NOMBRE+"='"+nombre+"' where "+ID_LISTA+"="+idLista);
    }
    public int insertLista(String nombre)
    {
        //agrega una lista de reproduccion y regresa su id
        int id=-1;
        executa("insert into "+TABLA_LISTA_REPRODUCCION+"("+NOMBRE+") values('"+nombre+"')");
        Cursor c=consulta("select max("+ID_LISTA+") as id from "+TABLA_LISTA_REPRODUCCION+"");
        if(c!=null)
        {
            if(c.moveToNext())
            {
                id=c.getInt(c.getColumnIndex("id"));
            }
            cierraConsulta();
        }
        return id;
    }
    public void deleteLista(int idLista)
    {
        //elimina la lista y todas sus canciones
        executa("delete from "+TABLA_LISTA_REPRODUCCION+" where "+ID_LISTA+"="+idLista);
    }
    private boolean getBoolean(Cursor cursor,String campo)
    {
        if(cursor.getInt(cursor.getColumnIndex(campo))==0)
            return  false;
        return true;
    }
    //-------------------------------------------------------Manejo de alarmas--------------------------------------
    public Alarma getAlarma(int idAlarma)
    {
        //regresa una alarma especifica
        Alarma obj=null;
        Cursor c=consulta("select * from "+TABLA_ALARMA+" where "+ID_ALARMA+"="+idAlarma);
        if(c!=null)
        {
            if(c.moveToNext())
            {
                obj=new Alarma();
                obj.setIdAlarma(c.getInt(c.getColumnIndex(ID_ALARMA)));
                obj.setNombre(c.getString(c.getColumnIndex(NOMBRE)));
                obj.setHora(c.getInt(c.getColumnIndex(HORA)));
                obj.setMinuto(c.getInt(c.getColumnIndex(MINUTO)));
//                obj.setAm(getBoolean(c,AM));
                obj.setActiva(getBoolean(c,ACTIVA));
                obj.setLunes(getBoolean(c,LUNES));
                obj.setMartes(getBoolean(c,MARTES));
                obj.setMiercoles(getBoolean(c,MIERCOLES));
                obj.setJueves(getBoolean(c,JUEVES));
                obj.setViernes(getBoolean(c,VIERNES));
                obj.setSabado(getBoolean(c,SABADO));
                obj.setDomingo(getBoolean(c,DOMINGO));
                obj.setIdLista(c.getInt(c.getColumnIndex(ID_LISTA)));
            }
            cierraConsulta();
        }
        return  obj;
    }
    public List<Alarma> getAlarmas()
    {
        //regresa todas las alarmas
        List<Alarma> lista=new ArrayList<Alarma>();
        Cursor c=consulta("select * from "+TABLA_ALARMA);
        if(c!=null)
        {
            while (c.moveToNext())
            {
                Alarma obj=new Alarma();
                obj.setIdAlarma(c.getInt(c.getColumnIndex(ID_ALARMA)));
                obj.setNombre(c.getString(c.getColumnIndex(NOMBRE)));
                obj.setHora(c.getInt(c.getColumnIndex(HORA)));
                obj.setMinuto(c.getInt(c.getColumnIndex(MINUTO)));
//                obj.setAm(getBoolean(c,AM));
                obj.setActiva(getBoolean(c,ACTIVA));
                obj.setLunes(getBoolean(c,LUNES));
                obj.setMartes(getBoolean(c,MARTES));
                obj.setMiercoles(getBoolean(c,MIERCOLES));
                obj.setJueves(getBoolean(c,JUEVES));
                obj.setViernes(getBoolean(c,VIERNES));
                obj.setSabado(getBoolean(c,SABADO));
                obj.setDomingo(getBoolean(c,DOMINGO));
                obj.setIdLista(c.getInt(c.getColumnIndex(ID_LISTA)));
                lista.add(obj);
            }
            cierraConsulta();
        }
        return  lista;
    }
    public List<Alarma> getAlarmasActivas()
    {
        //regresa todas las alarmas activas
        List<Alarma> lista=new ArrayList<Alarma>();
        Cursor c=consulta("select * from "+TABLA_ALARMA+" where "+ACTIVA+"=1");
        if(c!=null)
        {
            while (c.moveToNext())
            {
                Alarma obj=new Alarma();
                obj.setIdAlarma(c.getInt(c.getColumnIndex(ID_ALARMA)));
                obj.setNombre(c.getString(c.getColumnIndex(NOMBRE)));
                obj.setHora(c.getInt(c.getColumnIndex(HORA)));
                obj.setMinuto(c.getInt(c.getColumnIndex(MINUTO)));
//                obj.setAm(getBoolean(c,AM));
                obj.setActiva(getBoolean(c,ACTIVA));
                obj.setLunes(getBoolean(c,LUNES));
                obj.setMartes(getBoolean(c,MARTES));
                obj.setMiercoles(getBoolean(c,MIERCOLES));
                obj.setJueves(getBoolean(c,JUEVES));
                obj.setViernes(getBoolean(c,VIERNES));
                obj.setSabado(getBoolean(c,SABADO));
                obj.setDomingo(getBoolean(c,DOMINGO));
                obj.setIdLista(c.getInt(c.getColumnIndex(ID_LISTA)));
                lista.add(obj);
            }
            cierraConsulta();
        }
        return  lista;
    }
    private int BoolToInt(boolean valor)
    {
        if(valor==true)
            return 1;
        return 0;
    }
    public int insertAlarma(String nombre,int hora,int minuto,boolean am,boolean lunes,boolean martes,boolean miercoles,boolean jueves,boolean viernes,boolean sabado,boolean domingo,boolean activa)
    {
        //agrega una alarma y regresa su id
        executa("insert into "+TABLA_ALARMA+"("+NOMBRE+","+HORA+","+MINUTO+","+AM+","+LUNES+","+MARTES+","+MIERCOLES+","+JUEVES+","+VIERNES+","+SABADO+","+DOMINGO+","+ACTIVA+") values('"+nombre+"',"+hora+","+minuto+","+BoolToInt(am)+","+BoolToInt(lunes)+","+BoolToInt(martes)+","+BoolToInt(miercoles)+","+BoolToInt(jueves)+","+BoolToInt(viernes)+","+BoolToInt(sabado)+","+BoolToInt(domingo)+","+BoolToInt(activa)+")");
        Cursor c=consulta("select max("+ID_ALARMA+") as id from "+TABLA_ALARMA+"");
        int id=-1;
        if(c!=null)
        {
            if(c.moveToNext())
            {
                id=c.getInt(c.getColumnIndex("id"));
            }
            cierraConsulta();
        }
        return id;
    }
    public void  updateAlarma(int idAlarma,String nombre,int hora,int minuto,boolean am,boolean lunes,boolean martes,boolean miercoles,boolean jueves,boolean viernes,boolean sabado,boolean domingo,boolean activa)
    {
        //actualiza los datos de una alarma
        executa("update "+TABLA_ALARMA+" set "+NOMBRE+"='"+nombre+"',"+HORA+"="+hora+","+MINUTO+"="+minuto+","+AM+"="+BoolToInt(am)+","+LUNES+"="+BoolToInt(lunes)+","+MARTES+"="+BoolToInt(martes)+","+MIERCOLES+"="+BoolToInt(miercoles)+","+JUEVES+"="+BoolToInt(jueves)+","+VIERNES+"="+BoolToInt(viernes)+","+SABADO+"="+BoolToInt(sabado)+","+DOMINGO+"="+BoolToInt(domingo)+","+ACTIVA+"="+BoolToInt(activa)+" where "+ID_ALARMA+"="+Integer.toHexString(idAlarma));
    }
    public void deleteAlarma(int idAlarma)
    {
        //elimina la alarma
        executa("delete from "+TABLA_ALARMA+" where "+ID_ALARMA+"="+idAlarma);
    }
    public void AsignaListaAlarma(int idAlarma, int idLista)
    {
        if(idLista!=-1)
            executa("update "+TABLA_ALARMA+" set "+ID_LISTA+"="+idLista+" where "+ID_ALARMA+"="+Integer.toString( idAlarma));
        else
            executa("update "+TABLA_ALARMA+" set "+ID_LISTA+"=null where "+ID_ALARMA+"="+Integer.toString(idAlarma));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    private Cursor consulta(String comando)
    {
        db=getReadableDatabase();
        cursor=db.rawQuery(comando,null);
        return cursor;
    }
    private void cierraConsulta()
    {
        cursor.close();
        db.close();
    }
    private void executa(String comando)
    {
        db=getWritableDatabase();
        db.execSQL(comando);
        db.close();

    }
    //-----------MANEJO DE LA POSICION DE LA REPRODUCCION-----------------------------------
    public StatusReproduccion getPosicionReproduccion(int tipo, int identificador)
    {
        StatusReproduccion obj=new StatusReproduccion();
        obj.setTipo(tipo);
        obj.setIdentificador(identificador);
        Cursor c=consulta("select * from "+TABLA_REPRODUCCION+" where "+TIPO+"="+tipo+" and "+IDENTIFICADOR+"="+identificador);
        if(c!=null) {
            while (c.moveToNext()) {
                obj.setPosicion(c.getInt(c.getColumnIndex(POSICION)));
            }
        }
        cierraConsulta();
        return  obj;
    }

    public void setStatusReproduccion(int tipo, int identificador, int posicion)
    {
        //primero veo si ya existe
        Cursor c=consulta("select * from "+TABLA_REPRODUCCION+" where "+TIPO+"="+tipo+" and "+IDENTIFICADOR+"="+identificador);
        if(c!=null) {
            //si existe
            while (c.moveToNext()) {
                cierraConsulta();
                executa("update " + TABLA_REPRODUCCION + " set " + POSICION + "=" + posicion + " where " + TIPO + "=" + tipo + " and " + IDENTIFICADOR + "=" + identificador);
                return;
            }
        }
        //no existe
        executa("insert into "+TABLA_REPRODUCCION+"("+TIPO+","+IDENTIFICADOR+","+POSICION+") values("+tipo+","+identificador+","+posicion+")");
    }

    //--------------------creacion de la estructura de la base de datos---------------------
    @Override
    public void onCreate(SQLiteDatabase db) {
        String comando="";
        db.execSQL("create table "+TABLA_CANCION+"("+ID_CANCION+" integer primary key autoincrement, "+PATH+" text,"+NOMBRE+" text)");
        db.execSQL("create table "+TABLA_LISTA_REPRODUCCION+"("+ID_LISTA+" integer primary key autoincrement, "+NOMBRE+" text)");
        comando="create table "+TABLA_ALARMA+"("+ID_ALARMA+" integer primary key autoincrement, "+NOMBRE+" text, "+HORA+" integer, "+MINUTO+" integer, "+AM+" integer, "+ACTIVA+" integer, "+LUNES+" integer, "+MARTES+" integer, "+MIERCOLES+" integer, "+JUEVES+" integer, "+VIERNES+" integer, "+SABADO+" integer, "+DOMINGO+" integer, "+ID_LISTA+" integer,foreign key ("+ID_LISTA+") references "+TABLA_LISTA_REPRODUCCION+"("+ID_LISTA+"))";
        db.execSQL(comando);
        comando="create table "+TABLA_LISTA_CANCION+"("+ID_CANCION+" integer, "+ID_LISTA+" integer, foreign key ("+ID_CANCION+") references "+TABLA_CANCION+"("+ID_CANCION+"),foreign key ("+ID_LISTA+") references "+TABLA_LISTA_REPRODUCCION+"("+ID_LISTA+"))";
        db.execSQL(comando);
        db.execSQL("create table "+TABLA_CANCION_SISTEMA+"("+ID_CANCION+" integer primary key autoincrement, "+PATH+" text,"+NOMBRE+" text)");
        db.execSQL("create table "+TABLA_REPRODUCCION+"("+TIPO+" integer, "+IDENTIFICADOR+" integer, "+POSICION+" integer)");
    }
}
