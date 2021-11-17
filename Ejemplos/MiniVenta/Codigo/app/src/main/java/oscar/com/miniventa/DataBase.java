package oscar.com.miniventa;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.Time;

import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class DataBase extends SQLiteOpenHelper {
    //-----------------------------------CONSTANTES------------------------------
    //definicion de nombres de tablas
    private static final String TABLA_EMPRESA = "Empresa";
    private static final String TABLA_GRUPO = "Grupo";
    private static final String TABLA_CLIENTE = "Cliente";
    private static final String TABLA_VENTA = "Venta";
    private static final String TABLA_DETALLE_VENTA = "DetalleVenta";
    private static final String TABLA_ARTICULO = "Articulo";
    private static final String TABLA_MOVIMIENTO_STOCK = "Movimiento";
    private static final String TABLA_CONFIGURACION = "Configuracion";
    //nobre de Campos
    private static final String ID_CLIENTE = "ID_Cliente";
    private static final String ID_VENTA = "ID_Venta";
    private static final String ID_EMPRESA = "ID_Empresa";
    private static final String ID_ARTICULO = "ID_Articulo";
    private static final String ID_GRUPO = "ID_Grupo";
    private static final String ID_CLAVE = "Clave";
    private static final String ID_MOVIMIENTO = "ID_Movimiento";

    private static final String CAMPO_NOMBRE = "Nombre";
    private static final String CAMPO_APELLIDO = "Apellido";
    private static final String CAMPO_SEXO = "Sexo";
    private static final String CAMPO_DIRECCION = "Direccion";
    private static final String CAMPO_TELEFONO = "Telefono";
    private static final String CAMPO_EMAIL = "Email";
    private static final String CAMPO_FECHA = "Fecha";
    private static final String CAMPO_IMPORTE = "Importe";
    private static final String CAMPO_PRECIO = "Precio";
    private static final String CAMPO_CANTIDAD = "Cantidad";
    private static final String CAMPO_USUARIO = "Usuario";
    private static final String CAMPO_PASSWORD = "Password";
    private static final String CAMPO_MOTIVO = "Motivo";
    private static final String CAMPO_BARCODE = "BarCode";
    private static final String CAMPO_DESCRIPCION = "Descripcion";
    private static final String CAMPO_EXISTENCIA = "Existencia";
    private static final String CAMPO_VALOR = "Valor";
    private static final String CAMPO_STATUS = "Status";

    //-----------------------------------VARIABLES----------------------------------------------
    private Cursor cursor;
    private SQLiteDatabase db;

    //--------------------------------FUNCIONES ESTANDAR----------------------------------------
    //Constructor
    public DataBase(Context context) {
        super(context, "MiniVenta", null, 1);
    }

    //ejecuta una consulta y regresa el cursor asociado
    private Cursor consulta(String comando) {
        db = getReadableDatabase();
        cursor = db.rawQuery(comando, null);
        return cursor;
    }

    // cierra la consulta. esta asociada a la funcion consulta
    private void cierraConsulta() {
        cursor.close();
        db.close();
    }

    // ejecuta un comando
    private void executa(String comando) {
        db = getWritableDatabase();
        db.execSQL(comando);
        db.close();
    }

    //crea las tablas de la base de datos
    @Override
    public void onCreate(SQLiteDatabase db) {
        //creo la estructura de la base de datos
        db.execSQL("create table " + TABLA_EMPRESA + "(" + CAMPO_NOMBRE + " text, " + CAMPO_USUARIO + " text, " + CAMPO_PASSWORD + " text)");
        db.execSQL("create table " + TABLA_CLIENTE + "(" + ID_CLIENTE + " integer primary key autoincrement, " + CAMPO_NOMBRE + " text, " + CAMPO_APELLIDO + " text, " + CAMPO_SEXO + " text, " + CAMPO_DIRECCION + " text, " + CAMPO_TELEFONO + " text, " + CAMPO_EMAIL + " text, " + CAMPO_FECHA + " long)");
        db.execSQL("create table " + TABLA_GRUPO + "(" + ID_GRUPO + " integer primary key autoincrement, " + CAMPO_NOMBRE + " text)");
        db.execSQL("create table " + TABLA_ARTICULO + "(" + ID_ARTICULO + " integer primary key autoincrement, " + CAMPO_NOMBRE + " text, " + CAMPO_BARCODE + " text, " + CAMPO_DESCRIPCION + " text, " + CAMPO_PRECIO + " double, " + CAMPO_EXISTENCIA + " INTEGER, " + ID_GRUPO + " INTEGER, foreign key (" + ID_GRUPO + ") references " + TABLA_GRUPO + "(" + ID_GRUPO + ") )");
        db.execSQL("create table " + TABLA_VENTA + "(" + ID_VENTA + " integer primary key autoincrement, " + ID_CLIENTE + " integer, " + CAMPO_FECHA + " long, " + CAMPO_IMPORTE + " double, "+CAMPO_STATUS+" integer, foreign key (" + ID_CLIENTE + ") references " + TABLA_CLIENTE + "(" + ID_CLIENTE + "))");
        db.execSQL("create table " + TABLA_DETALLE_VENTA + "(" + ID_VENTA + " integer, " + ID_ARTICULO + " integer, " + CAMPO_PRECIO + " DOUBLE, " + CAMPO_CANTIDAD + " integer, primary key (" + ID_VENTA + "," + ID_ARTICULO + "), foreign key (" + ID_VENTA + ") references " + TABLA_VENTA + " (" + ID_VENTA + "), foreign key (" + ID_ARTICULO + ") references " + TABLA_ARTICULO + " (" + ID_ARTICULO + ") )");
        db.execSQL("create table " + TABLA_MOVIMIENTO_STOCK + "(" + ID_MOVIMIENTO + " integer primary key autoincrement, " + ID_ARTICULO + " integer, " + CAMPO_CANTIDAD + " integer, " + CAMPO_MOTIVO + " text, " + CAMPO_FECHA + " long, foreign key (" + ID_ARTICULO + ") references " + TABLA_ARTICULO + " (" + ID_ARTICULO + "))");
        db.execSQL("create table " + TABLA_CONFIGURACION + "( " + ID_CLAVE + " TEXT primary key, " + CAMPO_VALOR + " text)");
        //creo un grupo generico
        db.execSQL("insert into "+TABLA_GRUPO+" ("+CAMPO_NOMBRE+") values('General')");
        //agrego cliente generico
        db.execSQL("insert into "+TABLA_CLIENTE+" ("+CAMPO_NOMBRE+","+CAMPO_APELLIDO+","+CAMPO_SEXO+","+CAMPO_DIRECCION+","+CAMPO_TELEFONO+","+CAMPO_EMAIL+","+CAMPO_FECHA+") values('General',' ','M',' ',' ',' ',date('now'))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //-------------------------------manejo de la tabla empresa------------------------------------
    public Empresa getDatosEmpresa() {
        //regresa los datos de la empresa
        Empresa empresa = null;
        Cursor cursor = consulta("select * from " + TABLA_EMPRESA);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                empresa = new Empresa();
                empresa.setNombre(cursor.getString(cursor.getColumnIndex(CAMPO_NOMBRE)));
                empresa.setUsuario(cursor.getString(cursor.getColumnIndex(CAMPO_USUARIO)));
                empresa.setPassword(cursor.getString(cursor.getColumnIndex(CAMPO_PASSWORD)));
                break;
            }
        }
        return empresa;
    }

    public void setDatosEmpresa(String nombre, String usuario, String password) {
        if (getDatosEmpresa() == null) {
            //hay que insertar
            executa("insert into " + TABLA_EMPRESA + "(" + CAMPO_NOMBRE + "," + CAMPO_USUARIO + "," + CAMPO_PASSWORD + ") values('" + nombre + "','" + usuario + "','" + password + "')");
        } else {
            //hay que actualizar
            executa("update " + TABLA_EMPRESA + " set " + CAMPO_NOMBRE + "='" + nombre + "', " + CAMPO_USUARIO + "='" + usuario + "', " + CAMPO_PASSWORD + "='" + password + "'");
        }
    }

    //-----------------------------------------------manejo de configuraciones---------------------------------------
    public Configuracion getConfiguracion(String clave) {
        Configuracion configuracion = null;
        Cursor cursor = consulta("select * from " + TABLA_CONFIGURACION + " where " + ID_CLAVE + "='" + clave + "'");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                configuracion=new Configuracion();
                configuracion.setClave(cursor.getString(cursor.getColumnIndex(ID_CLAVE)));
                configuracion.setValor(cursor.getString(cursor.getColumnIndex(CAMPO_VALOR)));
                break;
            }
        }
        return configuracion;
    }
    public void setConfiguracion(String clave, String valor)
    {
        //veo si ya existe
        if(getConfiguracion(clave)==null)
        {
            //hay que agregar
            executa("insert into "+TABLA_CONFIGURACION+" ("+ID_CLAVE+","+CAMPO_VALOR+") values('"+clave+"','"+valor+"')");
        }
        else
        {
            //hay que actualizar
            executa("update "+TABLA_CONFIGURACION+" set "+CAMPO_VALOR+"='"+valor+"' where "+ID_CLAVE+"='"+valor+"'");
        }
    }
    public void deleteConfiguracion(String clave)
    {
        executa("delete from "+TABLA_CONFIGURACION+" where "+ID_CLAVE+"='"+clave+"'");
    }
    //--------------------------Manejo del catalogo de grupos-------------------------------------------------
    public List<Grupo> getGrupos() {
        //regresa el listado completo de grupos del sistema
        List<Grupo> lista=new ArrayList<Grupo>();
        Cursor cursor = consulta("select * from "+TABLA_GRUPO+"");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Grupo grupo=new Grupo();
                grupo.setIdGrupo(cursor.getInt(cursor.getColumnIndex(ID_GRUPO)));
                grupo.setNombre(cursor.getString(cursor.getColumnIndex(CAMPO_NOMBRE)));
                lista.add(grupo);
            }
            cierraConsulta();
        }
        return lista;
    }
    public Grupo getGrupo(int idGrupo) {
        //regresa un grupo en especifico
        Grupo grupo = null;
        Cursor cursor = consulta("select * from " + TABLA_GRUPO + " where " + ID_GRUPO + "=" + idGrupo + "");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                grupo=new Grupo();
                grupo.setIdGrupo(cursor.getInt(cursor.getColumnIndex(ID_GRUPO)));
                grupo.setNombre(cursor.getString(cursor.getColumnIndex(CAMPO_NOMBRE)));
                break;
            }
            cierraConsulta();
        }
        return grupo;
    }
    public Grupo getGrupo(String nombre) {
        //regresa un grupo por medio del nombre
        Grupo grupo = null;
        Cursor cursor = consulta("select * from " + TABLA_GRUPO + " where " + CAMPO_NOMBRE + "='" + nombre + "'");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                grupo=new Grupo();
                grupo.setIdGrupo(cursor.getInt(cursor.getColumnIndex(ID_GRUPO)));
                grupo.setNombre(cursor.getString(cursor.getColumnIndex(CAMPO_NOMBRE)));
                break;
            }
            cierraConsulta();
        }
        return grupo;
    }
    public List<Grupo> getGrupos(String nombre) {
        //regresa la lista de gruos que conicidan con el nombre
        List<Grupo> lista=new ArrayList<Grupo>();
        Cursor cursor = consulta("select * from " + TABLA_GRUPO + " where " + CAMPO_NOMBRE + "like '%" + nombre + "%'");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Grupo grupo=new Grupo();
                grupo.setIdGrupo(cursor.getInt(cursor.getColumnIndex(ID_GRUPO)));
                grupo.setNombre(cursor.getString(cursor.getColumnIndex(CAMPO_NOMBRE)));
                lista.add(grupo);
            }
            cierraConsulta();
        }
        return lista;
    }

    public int insertGrupo(String nombre) throws Exception {
        int id=-1;
        if(getGrupo(nombre)!=null)
        {
            //ya existe, por lo que no se puede agregar
            throw new Exception("El grupo ya existe");
        }
        executa("insert into "+TABLA_GRUPO+" ("+CAMPO_NOMBRE+") values('"+nombre+"')");
        //me traigo el ID del nuevo registro
        Cursor cursor=consulta("select max("+ID_GRUPO+") as "+ID_GRUPO+"  from "+TABLA_GRUPO+"");
        if(cursor!=null)
        {
            if(cursor.moveToNext())
            {
                id=cursor.getInt(cursor.getColumnIndex(ID_GRUPO));
            }
            cierraConsulta();
        }
        return id;
    }
    public void updateGrupo(int idGrupo,String nombre)throws Exception
    {
        //actualiza el nombre del grupo
        if(getGrupo(idGrupo)==null)
        {
            //ya existe, por lo que no se puede agregar
            throw new Exception("El grupo no existe");
        }
        executa("update "+TABLA_GRUPO+" set "+CAMPO_NOMBRE+"='"+nombre+"' where "+ID_GRUPO+"="+idGrupo);
    }
    public void deleteGrupo(int idGrupo)
    {
        executa("delete from "+TABLA_GRUPO+" where "+ID_GRUPO+"="+idGrupo+"");
    }
    //-------------------------Catalogo de clientes-------------------------------------------------------
    public Cliente getCliente(int idCliente)
    {
        Cliente cliente=null;
        Cursor cursor=consulta("select * from "+TABLA_CLIENTE+" where "+ID_CLIENTE+"="+idCliente+"");
        if(cursor!=null)
        {
            if(cursor.moveToNext())
            {
                cliente=new Cliente();
                cliente.setApellido(cursor.getString(cursor.getColumnIndex(CAMPO_APELLIDO)));
                cliente.setDireccion(cursor.getString(cursor.getColumnIndex(CAMPO_DIRECCION)));
                cliente.setEmail(cursor.getString(cursor.getColumnIndex(CAMPO_EMAIL)));
                cliente.setFechaNaciiento(new Date(cursor.getLong(cursor.getColumnIndex(CAMPO_FECHA))));
                cliente.setIdCliente(cursor.getInt(cursor.getColumnIndex(ID_CLIENTE)));
                cliente.setNombre(cursor.getString(cursor.getColumnIndex(CAMPO_NOMBRE)));
                cliente.setSexo(cursor.getString(cursor.getColumnIndex(CAMPO_SEXO)));
                cliente.setTelefono(cursor.getString(cursor.getColumnIndex(CAMPO_TELEFONO)));
            }
            cierraConsulta();
        }
        return  cliente;
    }
    public Cliente getCliente(String nombre)
    {
        Cliente cliente=null;
        Cursor cursor=consulta("select * from "+TABLA_CLIENTE+" where "+CAMPO_NOMBRE+"='"+nombre+"'");
        if(cursor!=null)
        {
            if(cursor.moveToNext())
            {
                cliente=new Cliente();
                cliente.setApellido(cursor.getString(cursor.getColumnIndex(CAMPO_APELLIDO)));
                cliente.setDireccion(cursor.getString(cursor.getColumnIndex(CAMPO_DIRECCION)));
                cliente.setEmail(cursor.getString(cursor.getColumnIndex(CAMPO_EMAIL)));
                cliente.setFechaNaciiento(new Date(cursor.getLong(cursor.getColumnIndex(CAMPO_FECHA))));
                cliente.setIdCliente(cursor.getInt(cursor.getColumnIndex(ID_CLIENTE)));
                cliente.setNombre(cursor.getString(cursor.getColumnIndex(CAMPO_NOMBRE)));
                cliente.setSexo(cursor.getString(cursor.getColumnIndex(CAMPO_SEXO)));
                cliente.setTelefono(cursor.getString(cursor.getColumnIndex(CAMPO_TELEFONO)));
            }
            cierraConsulta();
        }
        return  cliente;
    }
    public List< Cliente> getClientes()
    {
        List< Cliente> lista=new ArrayList<Cliente>();
        Cursor cursor=consulta("select * from "+TABLA_CLIENTE);
        if(cursor!=null)
        {
            while (cursor.moveToNext())
            {
                Cliente  cliente=new Cliente();
                cliente.setApellido(cursor.getString(cursor.getColumnIndex(CAMPO_APELLIDO)));
                cliente.setDireccion(cursor.getString(cursor.getColumnIndex(CAMPO_DIRECCION)));
                cliente.setEmail(cursor.getString(cursor.getColumnIndex(CAMPO_EMAIL)));
                cliente.setFechaNaciiento(new Date(cursor.getLong(cursor.getColumnIndex(CAMPO_FECHA))));
                cliente.setIdCliente(cursor.getInt(cursor.getColumnIndex(ID_CLIENTE)));
                cliente.setNombre(cursor.getString(cursor.getColumnIndex(CAMPO_NOMBRE)));
                cliente.setSexo(cursor.getString(cursor.getColumnIndex(CAMPO_SEXO)));
                cliente.setTelefono(cursor.getString(cursor.getColumnIndex(CAMPO_TELEFONO)));
                lista.add(cliente);
            }
            cierraConsulta();
        }
        return  lista;
    }
    public int insertCliente(String nombre,String apellido,String sexo,String direccion,String telefono,String email,Date fechaNaciiento)
    {
        int id=-1;
        executa("insert into "+TABLA_CLIENTE+" ("+CAMPO_NOMBRE+","+CAMPO_APELLIDO+","+CAMPO_SEXO+","+CAMPO_DIRECCION+","+CAMPO_TELEFONO+","+CAMPO_EMAIL+","+CAMPO_FECHA+") values('"+nombre+"','"+apellido+"','"+sexo+"','"+direccion+"','"+telefono+"','"+email+"',"+fechaNaciiento.getTime()+")");
        Cursor cursor=consulta("select max( "+ID_CLIENTE+") as "+ID_CLIENTE+" from "+TABLA_CLIENTE);
        if(cursor!=null)
        {
            if(cursor.moveToNext())
            {
                id=cursor.getInt(cursor.getColumnIndex(ID_CLIENTE));
            }
            cierraConsulta();
        }
        return id;
    }
    public void updateCliente(int idCliente, String nombre,String apellido,String sexo,String direccion,String telefono,String email,Date fechaNaciiento) throws Exception
    {
        if(getCliente(idCliente)==null)
        {
            throw new Exception("No se reconoce el cliente");
        }
        executa("update "+TABLA_CLIENTE+" set "+CAMPO_NOMBRE+"='"+nombre+"', "+CAMPO_APELLIDO+"='"+apellido+"', "+CAMPO_SEXO+"='"+sexo+"', "+CAMPO_DIRECCION+"='"+direccion+"', "+CAMPO_TELEFONO+"='"+telefono+"', "+CAMPO_EMAIL+"='"+email+"', "+CAMPO_FECHA+"="+fechaNaciiento.getTime()+" where "+ID_CLIENTE+"="+idCliente+"");
    }
    public void deleteCliente(int idCliente)
    {
        executa("delete from "+TABLA_CLIENTE+" where "+ID_CLIENTE+"="+idCliente+"");
    }
    //-------------------------------------------Catalogo de articulos--------------------------------------------------
    public Articulo getArticulo(int idArticulo)
    {
        Articulo articulo=null;
        Cursor cursor=consulta("select * from "+TABLA_ARTICULO+" where "+ID_ARTICULO+"="+idArticulo+"");
        if(cursor!=null)
        {
            if(cursor.moveToNext())
            {
                articulo=new Articulo();
                articulo.setBarCode(cursor.getString(cursor.getColumnIndex(CAMPO_BARCODE)));
                articulo.setDescripcion(cursor.getString(cursor.getColumnIndex(CAMPO_DESCRIPCION)));
                articulo.setExistencia(cursor.getInt(cursor.getColumnIndex(CAMPO_EXISTENCIA)));
                articulo.setIdArticulo(cursor.getInt(cursor.getColumnIndex(ID_ARTICULO)));
                articulo.setIdGrupo(cursor.getInt(cursor.getColumnIndex(ID_GRUPO)));
                articulo.setNombre(cursor.getString(cursor.getColumnIndex(CAMPO_NOMBRE)));
                articulo.setPrecio(cursor.getDouble(cursor.getColumnIndex(CAMPO_PRECIO)));
            }
            cierraConsulta();
        }
        return articulo;
    }
    public  Articulo FindArticulo(String barCode)
    {
        Articulo articulo=null;
        Cursor cursor=consulta("select * from "+TABLA_ARTICULO+" where "+CAMPO_BARCODE+"='"+barCode+"'");
        if(cursor!=null)
        {
            if(cursor.moveToNext())
            {
                articulo=new Articulo();
                articulo.setBarCode(cursor.getString(cursor.getColumnIndex(CAMPO_BARCODE)));
                articulo.setDescripcion(cursor.getString(cursor.getColumnIndex(CAMPO_DESCRIPCION)));
                articulo.setExistencia(cursor.getInt(cursor.getColumnIndex(CAMPO_EXISTENCIA)));
                articulo.setIdArticulo(cursor.getInt(cursor.getColumnIndex(ID_ARTICULO)));
                articulo.setIdGrupo(cursor.getInt(cursor.getColumnIndex(ID_GRUPO)));
                articulo.setNombre(cursor.getString(cursor.getColumnIndex(CAMPO_NOMBRE)));
                articulo.setPrecio(cursor.getDouble(cursor.getColumnIndex(CAMPO_PRECIO)));
            }
            cierraConsulta();
        }
        return articulo;
    }
    public List<Articulo> getArticulos(String nombre)
    {
        //regresa la lista de articulos que coincidan con el nombre
        //si se quieren traer todos, el nombre debe venir vacio
        List<Articulo> lista=new ArrayList<Articulo>();
        Cursor cursor=consulta("select * from "+TABLA_ARTICULO+" where "+CAMPO_NOMBRE+" like '%"+nombre+"%'");
        if(cursor!=null)
        {
            while (cursor.moveToNext())
            {
                Articulo articulo=new Articulo();
                articulo.setBarCode(cursor.getString(cursor.getColumnIndex(CAMPO_BARCODE)));
                articulo.setDescripcion(cursor.getString(cursor.getColumnIndex(CAMPO_DESCRIPCION)));
                articulo.setExistencia(cursor.getInt(cursor.getColumnIndex(CAMPO_EXISTENCIA)));
                articulo.setIdArticulo(cursor.getInt(cursor.getColumnIndex(ID_ARTICULO)));
                articulo.setIdGrupo(cursor.getInt(cursor.getColumnIndex(ID_GRUPO)));
                articulo.setNombre(cursor.getString(cursor.getColumnIndex(CAMPO_NOMBRE)));
                articulo.setPrecio(cursor.getDouble(cursor.getColumnIndex(CAMPO_PRECIO)));
                lista.add(articulo);
            }
            cierraConsulta();
        }
        return lista;
    }
    public List<Articulo> getArticulosGrupo(int idGrupo)
    {
        //regresa ls lista de articulos que pertenecen al grupo
        List<Articulo> lista=new ArrayList<Articulo>();
        Cursor cursor=consulta("select * from "+TABLA_ARTICULO+" where "+ID_GRUPO+"="+idGrupo+"");
        if(cursor!=null)
        {
            if(cursor.moveToNext())
            {
                Articulo articulo=new Articulo();
                articulo.setBarCode(cursor.getString(cursor.getColumnIndex(CAMPO_BARCODE)));
                articulo.setDescripcion(cursor.getString(cursor.getColumnIndex(CAMPO_DESCRIPCION)));
                articulo.setExistencia(cursor.getInt(cursor.getColumnIndex(CAMPO_EXISTENCIA)));
                articulo.setIdArticulo(cursor.getInt(cursor.getColumnIndex(ID_ARTICULO)));
                articulo.setIdGrupo(cursor.getInt(cursor.getColumnIndex(ID_GRUPO)));
                articulo.setNombre(cursor.getString(cursor.getColumnIndex(CAMPO_NOMBRE)));
                articulo.setPrecio(cursor.getDouble(cursor.getColumnIndex(CAMPO_PRECIO)));
                lista.add(articulo);
            }
            cierraConsulta();
        }
        return lista;
    }
    public List<Articulo> getArticulosGrupo(String nombreGrupo)
    {
        //regresa la lista de articulos de los grupos que coincidan con el nombre
        List<Articulo> lista=new ArrayList<Articulo>();
        Cursor cursor=consulta("Select a.* from "+TABLA_ARTICULO+" a, "+TABLA_GRUPO+" g where a."+ID_GRUPO+"=g."+ID_GRUPO+" and g."+CAMPO_NOMBRE+" like '%"+nombreGrupo+"%'");
        if(cursor!=null)
        {
            if(cursor.moveToNext())
            {
                Articulo articulo=new Articulo();
                articulo.setBarCode(cursor.getString(cursor.getColumnIndex(CAMPO_BARCODE)));
                articulo.setDescripcion(cursor.getString(cursor.getColumnIndex(CAMPO_DESCRIPCION)));
                articulo.setExistencia(cursor.getInt(cursor.getColumnIndex(CAMPO_EXISTENCIA)));
                articulo.setIdArticulo(cursor.getInt(cursor.getColumnIndex(ID_ARTICULO)));
                articulo.setIdGrupo(cursor.getInt(cursor.getColumnIndex(ID_GRUPO)));
                articulo.setNombre(cursor.getString(cursor.getColumnIndex(CAMPO_NOMBRE)));
                articulo.setPrecio(cursor.getDouble(cursor.getColumnIndex(CAMPO_PRECIO)));
                lista.add(articulo);
            }
            cierraConsulta();
        }
        return lista;
    }
    public List<Articulo> buscaArticulos(String nombre)
    {
        //regresa la lista de articulos que coinciden con el nombre y que ademas pertenecen al grupo quecoicide con el nombre
        List<Articulo> lista1=getArticulos(nombre);
        List<Articulo> lista2=getArticulosGrupo(nombre);
        for (Articulo articulo : lista2)
        {
            //lo busco en la lista1
            boolean encontrado=false;
            for (Articulo articulo2 : lista1)
            {
                if(articulo.getIdArticulo()==articulo2.getIdArticulo())
                {
                    encontrado=true;
                    break;
                }
            }
            if(encontrado==false)
            {
                //lo agrego
                lista1.add(articulo);
            }

        }
        return lista1;
    }
    public int insertArticulo(String nombre, String barCode, String descripcion, double precio, int existencia, int idGrupo)
    {
        //agrega un articulo a la base
        int idArticulo=-1;
        executa("insert into "+TABLA_ARTICULO+" ("+CAMPO_NOMBRE+","+CAMPO_BARCODE+","+CAMPO_DESCRIPCION+","+CAMPO_PRECIO+","+CAMPO_EXISTENCIA+","+ID_GRUPO+") values ('"+nombre+"','"+barCode+"','"+descripcion+"',"+precio+","+existencia+","+idGrupo+")");
        Cursor cursor=consulta("select max( "+ID_ARTICULO+") as "+ID_ARTICULO+" from "+TABLA_ARTICULO+"");
        if(cursor!=null)
        {
            if(cursor.moveToNext())
            {
                idArticulo=cursor.getInt(cursor.getColumnIndex(ID_ARTICULO));
            }
            cierraConsulta();
        }
        return idArticulo;
    }
    public void updateArticulo(int idArticulo,String nombre, String barCode, String descripcion, double precio, int existencia, int idGrupo)
    {
        executa("update "+TABLA_ARTICULO+" set "+CAMPO_NOMBRE+"='"+nombre+"', "+CAMPO_BARCODE+"='"+barCode+"', "+CAMPO_DESCRIPCION+"='"+descripcion+"', "+CAMPO_PRECIO+"="+precio+", "+CAMPO_EXISTENCIA+"="+existencia+", "+ID_GRUPO+"="+idGrupo+" where "+ID_ARTICULO+"="+idArticulo+"");
    }
    public void deleteArticulo(int idArticulo) throws Exception
    {
        //elimina el articulo siempre y cuando no tenga ventas no existencias
        if(getDetalleVentaArticulo(idArticulo).size()>0)
        {
            throw new Exception("No se puede eliminar porque el articulo tiene ventas");
        }
        //veo si tiene movimientos
        if(getMoviientosStockArticulo(idArticulo).size()>0)
        {
            throw new Exception("No se puede eliminar porque el articulo tiene movimientos registrados");
        }
        //ahora si lo puedo eliminar
        executa("delete from "+TABLA_ARTICULO+" where "+ID_ARTICULO+"="+idArticulo);
    }
    //-------------------------Movimientos de Stock----------------------------------------------------------
    public List<MovimientosStock> getMoviientosStockArticulo(int idArticulo)
    {
        List<MovimientosStock> lista=new ArrayList<MovimientosStock>();
        Cursor cursor=consulta("select * from "+TABLA_MOVIMIENTO_STOCK+" where "+ID_ARTICULO+"="+idArticulo+"");
        if(cursor!=null)
        {
            while (cursor.moveToNext())
            {
                MovimientosStock movimientosStock=new MovimientosStock();
                movimientosStock.setCantidad(cursor.getInt(cursor.getColumnIndex(CAMPO_CANTIDAD)));
                movimientosStock.setFecha(new Date(cursor.getLong(cursor.getColumnIndex(CAMPO_FECHA))));
                movimientosStock.setIdArticulo(cursor.getInt(cursor.getColumnIndex(ID_ARTICULO)));
                movimientosStock.setIdMovimiento(cursor.getInt(cursor.getColumnIndex(ID_MOVIMIENTO)));
                movimientosStock.setMotivo(cursor.getString(cursor.getColumnIndex(CAMPO_MOTIVO)));
                lista.add(movimientosStock);
            }
            cierraConsulta();
        }
        return  lista;
    }
    private void addMovimientStock(int idArticulo, int cantidad, String motivo)
    {
        Time hoy=new Time(Time.getCurrentTimezone());
        hoy.setToNow();
        executa("insert into "+TABLA_MOVIMIENTO_STOCK+" ("+ID_ARTICULO+", "+CAMPO_CANTIDAD+", "+CAMPO_MOTIVO+", "+CAMPO_FECHA+") values("+idArticulo+", "+cantidad+", '"+motivo+"', date('now'))");
        Articulo articulo=getArticulo(idArticulo);
        //actualizo la existencia
        articulo.setExistencia(articulo.getExistencia()+cantidad);
        updateArticulo(idArticulo,articulo.getNombre(),articulo.getBarCode(),articulo.getDescripcion(),articulo.getPrecio(),articulo.getExistencia(),articulo.getIdGrupo());
    }
    private void deleteMovmientosArticulo(int idArticulo)
    {
        //limpia los movimientos del articulo y pone su existencia a cero
        executa("delete from "+TABLA_MOVIMIENTO_STOCK+" where "+ID_ARTICULO+"="+idArticulo);
    }
    //--------------------------VENTA---------------------------------------------------------------------

    public Venta getVentaActiva()
    {
        Venta venta=null;
        Cursor cursor=consulta("select * from "+TABLA_VENTA+" where "+CAMPO_STATUS+"=1");
        if(cursor!=null)
        {
            if (cursor.moveToNext())
            {
                venta=new Venta();
                venta.setFecha(new Date(cursor.getLong(cursor.getColumnIndex(CAMPO_FECHA))));
                venta.setIdCliente(cursor.getInt(cursor.getColumnIndex(ID_CLIENTE)));
                venta.setIdVenta(cursor.getInt(cursor.getColumnIndex(ID_VENTA)));
                venta.setImporte(cursor.getDouble(cursor.getColumnIndex(CAMPO_IMPORTE)));
            }
            cierraConsulta();
        }
        return venta;
    }
    public Venta getVenta(int idVenta)
    {
        Venta venta=null;
        Cursor cursor=consulta("select * from "+TABLA_VENTA+" where "+ID_VENTA+"="+idVenta);
        if(cursor!=null)
        {
            if (cursor.moveToNext())
            {
                venta=new Venta();
                venta.setFecha(new Date(cursor.getLong(cursor.getColumnIndex(CAMPO_FECHA))));
                venta.setIdCliente(cursor.getInt(cursor.getColumnIndex(ID_CLIENTE)));
                venta.setIdVenta(cursor.getInt(cursor.getColumnIndex(ID_VENTA)));
                venta.setImporte(cursor.getDouble(cursor.getColumnIndex(CAMPO_IMPORTE)));
            }
            cierraConsulta();
        }
        return venta;
    }
    public List<Venta> getVentas()
    {
        //regresa todas las ventas
        List<Venta> lista=new ArrayList<Venta>();
        Cursor cursor=consulta("select * from "+TABLA_VENTA+" order by "+CAMPO_FECHA+" desc");
        if(cursor!=null)
        {
            if (cursor.moveToNext())
            {
                Venta venta=new Venta();
                venta.setFecha(new Date(cursor.getLong(cursor.getColumnIndex(CAMPO_FECHA))));
                venta.setIdCliente(cursor.getInt(cursor.getColumnIndex(ID_CLIENTE)));
                venta.setIdVenta(cursor.getInt(cursor.getColumnIndex(ID_VENTA)));
                venta.setImporte(cursor.getDouble(cursor.getColumnIndex(CAMPO_IMPORTE)));
                lista.add(venta);
            }
            cierraConsulta();
        }
        return lista;
    }
    public List<Venta> getVentasCliente(int idCliente)
    {
        //regresa todas las ventas
        List<Venta> lista=new ArrayList<Venta>();
        Cursor cursor=consulta("select * from "+TABLA_VENTA+" where "+ID_CLIENTE+"="+idCliente+" order by "+CAMPO_FECHA+" desc");
        if(cursor!=null)
        {
            if (cursor.moveToNext())
            {
                Venta venta=new Venta();
                venta.setFecha(new Date(cursor.getLong(cursor.getColumnIndex(CAMPO_FECHA))));
                venta.setIdCliente(cursor.getInt(cursor.getColumnIndex(ID_CLIENTE)));
                venta.setIdVenta(cursor.getInt(cursor.getColumnIndex(ID_VENTA)));
                venta.setImporte(cursor.getDouble(cursor.getColumnIndex(CAMPO_IMPORTE)));
                lista.add(venta);
            }
            cierraConsulta();
        }
        return lista;
    }
    public void deleteVenta(int idVenta)
    {
        //elimina la venta y todo su detalle
        deleteDetalleVenta(idVenta);
        executa("delete from "+TABLA_VENTA+" where "+ID_VENTA+"="+idVenta);
    }
    public void updateVenta(int idVenta,int idCliente,Date feha,double importe,int status)
    {
        executa("update "+TABLA_VENTA+" set "+ID_CLIENTE+"="+idCliente+", "+CAMPO_FECHA+"="+feha.getTime()+", "+CAMPO_IMPORTE+"="+importe+" ,"+CAMPO_STATUS+"="+status+" where "+ID_VENTA+"="+idVenta);
    }
    public int insertVenta(int idCliente)
    {
        executa("insert into "+TABLA_VENTA+" ("+ID_CLIENTE+","+CAMPO_FECHA+","+CAMPO_IMPORTE+","+CAMPO_STATUS+") values ("+idCliente+", date('now'),0,1)");
        int idVenta=-1;
        Cursor cursor=consulta("select max( "+ID_VENTA+") as "+ID_VENTA+" from "+TABLA_VENTA);
        if(cursor!=null)
        {
            if(cursor.moveToNext())
            {
                idVenta=cursor.getInt(cursor.getColumnIndex(ID_VENTA));
            }
            cierraConsulta();
        }
        return  idVenta;
    }
    //--------------------------Detalle ventas---------------------------------------------------------------
    public void deleteDetalleVenta(int idVenta)
    {
        //elimina todo el detalle de la venta
        executa("delete from "+TABLA_DETALLE_VENTA+" where "+ID_VENTA+"="+idVenta);
    }
    public List<DetalleVenta> getDetalleVentaArticulo(int idArticulo)
    {
        //regresa el detalle de las ventas del articulo
        List<DetalleVenta> lista=new ArrayList<DetalleVenta>();
        Cursor cursor=consulta("select * from "+TABLA_DETALLE_VENTA+" where "+ID_ARTICULO+"="+idArticulo+"");
        if(cursor!=null)
        {
            while (cursor.moveToNext())
            {
                DetalleVenta detalleVenta=new DetalleVenta();
                detalleVenta.setIdArticulo(cursor.getInt(cursor.getColumnIndex(ID_ARTICULO)));
                detalleVenta.setIdVenta(cursor.getInt(cursor.getColumnIndex(ID_VENTA)));
                detalleVenta.setPrecio(cursor.getInt(cursor.getColumnIndex(CAMPO_PRECIO)));
                detalleVenta.setCantidad(cursor.getInt(cursor.getColumnIndex(CAMPO_CANTIDAD)));
                lista.add(detalleVenta);
            }
            cierraConsulta();
        }
        return lista;
    }
    public List<DetalleVenta> getDetalleVenta(int idVenta)
    {
        //regresa el detalle de las ventas del articulo
        List<DetalleVenta> lista=new ArrayList<DetalleVenta>();
        Cursor cursor=consulta("select * from "+TABLA_DETALLE_VENTA+" where "+ID_VENTA+"="+idVenta+"");
        if(cursor!=null)
        {
            while (cursor.moveToNext())
            {
                DetalleVenta detalleVenta=new DetalleVenta();
                detalleVenta.setIdArticulo(cursor.getInt(cursor.getColumnIndex(ID_ARTICULO)));
                detalleVenta.setIdVenta(cursor.getInt(cursor.getColumnIndex(ID_VENTA)));
                detalleVenta.setPrecio(cursor.getInt(cursor.getColumnIndex(CAMPO_PRECIO)));
                detalleVenta.setCantidad(cursor.getInt(cursor.getColumnIndex(CAMPO_CANTIDAD)));
                lista.add(detalleVenta);
            }
            cierraConsulta();
        }
        return lista;
    }
    public double getTotalImporteVenta(int idVenta)
    {
        //regresa el total de la venta
        double total=0;
        List<DetalleVenta> lista=getDetalleVenta(idVenta);
        for (DetalleVenta detalle : lista)
        {
            total+=detalle.getImporte();
        }
        return total;
    }
    public void deleteDetalleVenta(int idVenta, int idArticulo)
    {
        //elimina el articulo de la venta
        executa("delete from "+TABLA_DETALLE_VENTA+" where "+ID_VENTA+"="+idVenta+" and "+ID_ARTICULO+"="+idArticulo);
    }
    public DetalleVenta getDetalleVenta(int idVenta,int idArticulo )
    {
        //regresa el detalle del articulo
        DetalleVenta detalleVenta=null;
        Cursor cursor=consulta("select * from "+TABLA_DETALLE_VENTA+" where "+ID_VENTA+"="+idVenta+" and "+ID_ARTICULO+"="+idArticulo);
        if(cursor!=null)
        {
            if(cursor.moveToNext())
            {
                detalleVenta=new DetalleVenta();
                detalleVenta.setIdArticulo(cursor.getInt(cursor.getColumnIndex(ID_ARTICULO)));
                detalleVenta.setIdVenta(cursor.getInt(cursor.getColumnIndex(ID_VENTA)));
                detalleVenta.setPrecio(cursor.getInt(cursor.getColumnIndex(CAMPO_PRECIO)));
                detalleVenta.setCantidad(cursor.getInt(cursor.getColumnIndex(CAMPO_CANTIDAD)));
            }
            cierraConsulta();
        }
        return detalleVenta;
    }
    public void insertDetalleVenta(int idVenta,int idArticulo,double precio, int cantidad) throws Exception
    {
        if(getDetalleVenta(idVenta,idArticulo)!=null)
        {
            throw new Exception("El articulo ya se encuentra en la venta");
        }
        executa("insert into "+TABLA_DETALLE_VENTA+" ( "+ID_VENTA+", "+ID_ARTICULO+", "+CAMPO_PRECIO+", "+CAMPO_CANTIDAD+") values("+idVenta+","+idArticulo+","+precio+","+cantidad+")");
    }
    public void updateDetalleVenta(int idVenta,int idArticulo,double precio, int cantidad) throws Exception
    {
        executa("update "+TABLA_DETALLE_VENTA+" set "+CAMPO_PRECIO+"="+precio+", "+CAMPO_CANTIDAD+"="+cantidad+" where "+ID_VENTA+"="+idVenta+" and "+ID_ARTICULO+"="+idArticulo+"");
    }

}
