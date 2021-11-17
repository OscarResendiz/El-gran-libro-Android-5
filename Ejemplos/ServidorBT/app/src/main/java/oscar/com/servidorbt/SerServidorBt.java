package oscar.com.servidorbt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.security.PublicKey;
import java.util.UUID;
import android.os.Handler;
import android.os.Debug;

public class SerServidorBt {
    private static final String TAG="SerServidorBt";
    private static final boolean DEBUG_MODE=true;
    private final Handler handler;
    private final Context context;
    private final BluetoothAdapter bAdapter;
    public static final  String NOMBRE_SEGURO="SerServidorBtSecure";
    public static UUID UUID_SEGURO=UUID.fromString("12345678-4321-4111-ADDA-345127542950");
    public static final int ESTADO_NINGUNO=0;
    public static final int ESTADO_CONECTADO=1;
    public static final int ESTADO_REALIZANDO_CONEXION=2;
    public static final int ESTADO_ATENDIENDO_PETICIONES=3;
    public static final int MSG_CAMBIO_ESTADO=10;
    public static final int MSG_LEER=11;
    public static final int MSG_ESCRIBIR=12;
    public static final int MSG_ATENDER_PETICIONES=13;
    public static final int MSG_ALERTA=14;
    private int estado;
    private HiloServidor hiloServidor=null;
    private  HiloConexion hiloConexion=null;
    public SerServidorBt(Context context, Handler handler, BluetoothAdapter adapter)
    {

        debug("SerServidorBt()","iniciando metodo");
        this.context=context;
        this.handler=handler;
        this.bAdapter=adapter;
        this.estado=ESTADO_NINGUNO;
    }
    private synchronized void setEstado(int stado)
    {
        this.estado=estado;
        handler.obtainMessage(MSG_CAMBIO_ESTADO,estado,-1).sendToTarget();
    }
    public synchronized int getEstado()
    {
        return  estado;
    }
    public synchronized String getNombreDispositivo()
    {
        //regresa el nombre del dispoitivo
        if(hiloConexion!=null)
        {
            hiloConexion.getName();
        }
        return "";
    }
    private class HiloConexion extends  Thread
    {
        private final BluetoothSocket socket;
        private final InputStream inputStream; //flujo de entrada
        private final OutputStream outputStream; //flujo de salida
        public HiloConexion(BluetoothSocket socket)
        {
            this.socket=socket;
            setName(socket.getRemoteDevice().getName()+"["+ socket.getRemoteDevice().getAddress()+"]");
            //se usan variables temporales debido a que los atributos se declararan como final y no seria
            //posible asignarles valor porteriormnte si falla la llamada
            InputStream inputStream=null; //flujo de entrada
            OutputStream outputStream=null; //flujo de salida
            //obtenemos los flujos de entrada y salida del socket
            try
            {
                inputStream=socket.getInputStream();
                outputStream=socket.getOutputStream();
            }
            catch (IOException e)
            {
                Log.e(TAG,"HiloConexion(): Error al obtener los flujos de E/S",e);
            }
            this.inputStream=inputStream;
            this.outputStream=outputStream;
        }
        public void run()
        {
            debug("HiloConexion.run()","iniciando metodo");
            byte[] buffer=new byte[1924];
            int bytes;
            setEstado(ESTADO_CONECTADO);
            //mientras se mantenga la conexion el hilo se mantiene en espera
            //ocupado leyendo el flujo de entrada
            while (true)
            {
                try
                {
                    //leemos el flujo de entrada del socket
                    bytes=inputStream.read(buffer);
                    //enviamos la informacion a la actividada a travez del handler
                    //el metodo handlermensaje se encarga de recibir el  mensaje y mostrar los datos en textview
                    handler.obtainMessage(MSG_LEER,bytes,-1,buffer).sendToTarget();
                    sleep(500);
                }
                catch (IOException e)
                {
                    Log.e(TAG,"HiloConexion.run(): error al realizar la lectura",e);
                }
                 catch (InterruptedException e)
                 {
                    e.printStackTrace();
                }
            }
        }
        public void escribir(byte[] buffer)
        {
            try {
                //escribimos en el flujo de salida del socket
                outputStream.write(buffer);
                //enviamos la informacion a la actividad a travez del handler.
                // el metodo handlerMessage sera el encargado de recibir el mensaje y mostrar los datos enviados en el Toast
                handler.obtainMessage(MSG_ESCRIBIR,-1,-1,buffer).sendToTarget();
            }
            catch (IOException e)
            {
                Log.e(TAG,"HiloConexion.escribir(): error al realizar la escritura",e);
            }
        }
        public void cancelarConexion()
        {
            debug("HiloConexion.cancelarConexion()","iniciando metodo");
            try

            {
                //forzamos el cierre del socket
                socket.close();
                //cambiamos el estado del servicio
                setEstado(ESTADO_NINGUNO);
            } catch (IOException e) {
                Log.e(TAG,"HiloConexion.cancelarConexion(): error al cerrar la conexion",e);
            }
        }
    }
    private class HiloServidor extends Thread
    {
        private final BluetoothServerSocket serverSocket;
        public  HiloServidor()
        {
            BluetoothServerSocket tmpServerSocket=null;
            //creamos un socket para escuchar la peticiones de conexion
            try
            {
                tmpServerSocket=bAdapter.listenUsingRfcommWithServiceRecord(NOMBRE_SEGURO,UUID_SEGURO);
            } catch (IOException e) {
                Log.e(TAG,"HiloServidor(): Error al abrir el socket servidor",e);
            }
            serverSocket=tmpServerSocket;
        }
        public void run()
        {
            debug("HiloServidor.run()","Iniciando metodo");
            BluetoothSocket socket=null;
            setName("HiloServidor");
            setEstado(ESTADO_ATENDIENDO_PETICIONES);
            //el hilo se mantendra en estado de espera ocuapdo aceptando conecciones entrantes
            // sirmpee y cuando no exista una conexionactiva en el momento que entre una nueva conexion
            while (estado!=ESTADO_CONECTADO)
            {
                try
                {
                    //cuando un cliente solicite la conexion se abrira el socket
                    socket=serverSocket.accept();
                }
                catch (IOException e)
                {
                    Log.e(TAG,"HiloServidor.run(): Error al aceptar conexiones entrantes");
                    break;
                }
            }
            //si el socket tiene valor sera porque un cliente ha solicitado la conexion
            if(socket!=null)
            {
                //realizamos el lock del objeto
                synchronized (SerServidorBt.this)
                {
                    switch (estado)
                    {
                        case ESTADO_ATENDIENDO_PETICIONES:
                        case ESTADO_REALIZANDO_CONEXION:
                        {
                            //estado esperando, se crea el hilo de conexion que recibira y enviara los mensajes
                            hiloConexion=new HiloConexion(socket);
                            hiloConexion.start();
                            break;
                        }
                        case ESTADO_NINGUNO:
                        case ESTADO_CONECTADO:
                        {
                            // no preparado o conexion ya realizada. Se cierra el nuevo socket
                            try
                            {
                                socket.close();
                            }
                            catch (IOException e)
                            {
                                Log.e(TAG,"HiloServidor.run(): Error al cerrar el socket",e);
                            }
                            break;
                        }
                    }
                }
            }
        }
        public void cancelaConexion()
        {
            try
            {
                serverSocket.close();
            } catch (IOException e) {
                Log.e(TAG,"HiloServidor.CancelaConexion(): Error al cerrar el socket",e);
            }
        }
    }
    //inicial el servicio creando un HiloServidor que se dedicara a atender las peticiones de conexion
    public synchronized void iniciarServicio()
    {
        debug("iniciarServicio()","iniciando metodo");
        //si existe una conexion previa se cancela
        if(hiloConexion!=null)
        {
            hiloConexion.cancelarConexion();
            hiloConexion=null;
        }
        //arranca el hilo servidor para que emiece a recibir preticiones de conexion
        if(hiloServidor==null)
        {
            hiloServidor=new HiloServidor();
            hiloServidor.start();
        }
        debug("iniciarServicio()","FINALIZADO metodo");
    }
    public void finalizarServicio()
    {
        debug("finalizarServicio()","iniciado metodo");
        if(hiloConexion!=null)
            hiloConexion.cancelarConexion();
        if(hiloServidor!=null)
            hiloServidor.cancelaConexion();
        hiloServidor=null;
        hiloConexion=null;
        setEstado(ESTADO_NINGUNO);
    }
    public synchronized void realizaConexion(BluetoothSocket socket, BluetoothDevice dispositivo)
    {
        debug("realizaConexion()","iniciado metodo");
        hiloConexion=new HiloConexion(socket);
        hiloConexion.start();
    }
    //sincroniza el objeto con el hilo HiloConexion e invoca a su metodo escribir para enviar el mensaje
    //como flujo de salida del socket
    public int enviar(byte[] buffer)
    {
        debug("enviar()","iniciado metodo");
        HiloConexion tmpConexion;
        synchronized (this)
        {
            if(estado!=ESTADO_CONECTADO)
                return -1;
            tmpConexion=hiloConexion;
        }
        tmpConexion.escribir(buffer);
        return buffer.length;
    }
    public void debug(String metodo,String msg)
    {
        if(DEBUG_MODE)
            Log.d(TAG,metodo+": "+msg);
    }
}
