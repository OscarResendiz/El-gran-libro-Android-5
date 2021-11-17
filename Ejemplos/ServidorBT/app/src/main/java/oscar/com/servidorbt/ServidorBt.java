package oscar.com.servidorbt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ServidorBt extends AppCompatActivity {
    private static final String TAG="ServidorBt";
    //declaramos una constante para lanzar los intents de activacion de bluetooth
    private static final int REQUEST_ENABLE_BT=1;
    private static final String ALERTA="alerta";
    //declaramos una variable privada para cada control de la actividad
    private Button bntEnviar;
    private Button bntBluetooth;
    private Button bntSalir;
    private EditText txtMensaje;
    private TextView tvMensaje;
    private TextView tvConexion;
    private BluetoothAdapter bAdapter;
    private SerServidorBt servicio;
    private BluetoothDevice ultimoDispositivo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servidor_bt);
        configurarControles();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_servidor_bt,menu);
        return  true;
    }
    private void configurarControles() {
        //referenciamos los controles
         bntEnviar=(Button) findViewById(R.id.bntEnviar);
         bntBluetooth=(Button)findViewById(R.id.bntBluetooth);
         bntSalir=(Button)findViewById(R.id.bntSalir);
         txtMensaje=(EditText)findViewById(R.id.txtMensaje);
         tvMensaje=(TextView)findViewById(R.id.tvMensaje);
         tvConexion=(TextView)findViewById(R.id.tvConexion);
         bntEnviar.setEnabled(false);
         configurarAdaptadorBluetooth();
        registraEventosBlueTooth();
    }
    private void  configurarAdaptadorBluetooth()
    {
        //obtenemos el adaptador bluetooht. Si es null significara que el dispositivo no posee bluetooth
        // por lo que deshabilitamos el boton encargado de activar/ desactivar esta caracteristica.
        bAdapter=BluetoothAdapter.getDefaultAdapter();
        if(bAdapter==null) {
            bntBluetooth.setEnabled(false);
            return;
        }
        // comprobabos si el boton esta activo y cabiamos el texto de los botones dependiendo del estado.
        // Tambien activamos o desactivamos los botones asociados ala conexion
        if(bAdapter.isEnabled())
        {
            bntBluetooth.setText(R.string.desactivarBluetooth);
        }
        else
        {
            bntBluetooth.setText(R.string.activarBluetooth);
        }
    }
    private final BroadcastReceiver bReceiver=new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action=intent.getAction();
            //BluetoothAdapter.ACTION_STATE_CHANGED
            //codigo qwue se ejecuta cuando el bluetooth cambie de estado
            //manejatremos los siguientes estados
            // STATE_OFF es bluettoth se desactiva
            // STATE_ON el bluetooth se activa
            if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action))
            {
                final int estado=intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,BluetoothAdapter.ERROR);
                switch (estado)
                {
                    case BluetoothAdapter.STATE_OFF: //apagado
                        Toast.makeText(context,"Apagando",Toast.LENGTH_LONG);
                        ((Button)findViewById(R.id.bntBluetooth)).setText(R.string.activarBluetooth);
                        break;
                    case BluetoothAdapter.STATE_ON: //apagado
                        Toast.makeText(context,"Encendiendo",Toast.LENGTH_LONG);
                        ((Button)findViewById(R.id.bntBluetooth)).setText(R.string.desactivarBluetooth);
                        Intent discoberableIntent=new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                        discoberableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,120);
                        break;
                }
            }
        }
    };
    private void registraEventosBlueTooth()
    {
        //registramos el broadcast reseiver que instanacioamos previamente para detectar los distintos eventos que queremos recibir
        IntentFilter filtro=new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        this.registerReceiver(bReceiver,filtro);
    }
    public void enviarClick(View view)
    {
        if(servicio==null)
            return;
        servicio.enviar(txtMensaje.getText().toString().getBytes());
        txtMensaje.setText("");
    }
    public void bluetoothClick(View view)
    {
        if(bAdapter.isEnabled())
        {
            if(servicio!=null)
                servicio.finalizarServicio();
            bAdapter.disable();
        }
        else
        {
            Intent activarbluettot=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(activarbluettot,REQUEST_ENABLE_BT);
        }
    }
    public void salirClick(View view)
    {
        if(servicio!=null)
            servicio.finalizarServicio();
        finish();
        System.exit(0);
    }
    protected void onActivityResult(int requesCode,int resultCode,Intent data)
    {
        switch (requesCode)
        {
            case REQUEST_ENABLE_BT:
                Log.v(TAG,"onActivityResult: REQUEST_ENABLE_BT");
                if(resultCode==RESULT_OK)
                {
                    bntBluetooth.setText(R.string.desactivarBluetooth);
                    if(servicio!=null)
                    {
                        servicio.finalizarServicio();
                        servicio.iniciarServicio();
                    }
                    else
                    {
                        servicio=new SerServidorBt(this,handler,bAdapter);
                    }
                }
                break;
        }
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        this.unregisterReceiver(bReceiver);
        if(servicio!=null)
        {
            servicio.finalizarServicio();
        }
    }
    @Override
    public synchronized void onResume()
    {
        super.onResume();
        if(servicio!=null)
        {
            if(servicio.getEstado()==SerServidorBt.ESTADO_NINGUNO)
            {
                servicio.iniciarServicio();
            }
        }
    }
    @Override
    public synchronized void onPause()
    {
        super.onPause();
    }
    private final Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            byte[] buffer=null;
            String mensaje=null;
            //atendemos al tipo de mensaje
            switch (msg.what)
            {
                //mesaje de lectura. se mostrara en el TextView
                case SerServidorBt.MSG_LEER:
                {
                    buffer=(byte[])msg.obj;
                    mensaje=new String(buffer,0,msg.arg1);
                    tvMensaje.setText(mensaje);
                    break;
                }
                //mensaje de escritura
                case SerServidorBt.MSG_ESCRIBIR:
                {
                    buffer=(byte[])msg.obj;
                    mensaje=new String(buffer);
                    mensaje=getString(R.string.enviandoMensaje)+": "+mensaje;
                    Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();
                    break;
                }
                //mensaje de cambio de estado
                case SerServidorBt.MSG_CAMBIO_ESTADO:
                {
                    switch (msg.arg1)
                    {
                        case SerServidorBt.ESTADO_ATENDIENDO_PETICIONES:
                            break;
                            // conectado: se muestra e dispositivo al que se ha conectado y se activa el boton de enviar
                        case SerServidorBt.ESTADO_CONECTADO:
                        {
                            mensaje=getString(R.string.conexionActual)+" "+servicio.getNombreDispositivo();
                            Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();
                            tvConexion.setText(mensaje);
                            bntEnviar.setEnabled(true);
                            break;
                        }
                        // ninguno: mensaje por defecto. desactivacion del boton enviar
                        case SerServidorBt.ESTADO_NINGUNO:
                        {
                            mensaje=getString(R.string.sinConexion);
                            Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();
                            tvConexion.setText(mensaje);
                            bntEnviar.setEnabled(false);
                            break;
                        }
                    }
                    break;
                }
                // mensaje de alerta: se mostrara en el Toast
                case SerServidorBt.MSG_ALERTA:
                {
                    mensaje=msg.getData().getString(ALERTA);
                    Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();
                    break;
                }
            }
        }
    };
}
