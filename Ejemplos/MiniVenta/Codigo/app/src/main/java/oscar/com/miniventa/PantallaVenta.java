package oscar.com.miniventa;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class PantallaVenta extends AppCompatActivity implements AdapterView.OnItemClickListener{
    protected DataBase dataBase;
    private int idVenta=-1;
    private TextView TTituloVenta;
    private TextView TTotal;
    private Venta venta;
    public AdaptadorDetalleVenta adaptador;
    public final int ELIMINAR=2;
    public final int CERRAR_VENTA=5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_venta);
        dataBase = new DataBase(this);

        adaptador=new AdaptadorDetalleVenta(this);
        ListView listView=(ListView )findViewById(R.id.listView);
        listView.setAdapter(adaptador);
        listView.setOnItemClickListener(this);
        //cargo componentes
        TTituloVenta = (TextView) findViewById(R.id.TTituloVenta);
        TTotal=(TextView) findViewById(R.id.TTotal);
        ///---------------manejo de la camara par el escaner
        escaner = (SurfaceView) findViewById(R.id.Scanner);
        initQR();
        //-------------------------------------
        IniciaSonido();
        cargaVenta();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_venta, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_guardar:
               // guardar();
                return true;
            case R.id.MenuAgregar:
                AgregaArticulo();
                break;
            case R.id.MenuCancelar:
                cancelarVenta();
                break;
            case R.id.MenuTerminar:
                muestraCerrarVenta();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void cargaVenta()
    {
        venta=dataBase.getVentaActiva();
        if(venta==null)
        {
            //no hay venta abierta por lo que hay que pedir primero a que cliente le vamos a vender
            Intent i=new Intent(this,SeleccionarClienteActivity.class);
            startActivityForResult(i,147);
            return;
        }
        idVenta=venta.getIdVenta();
        Cliente cliente=dataBase.getCliente(venta.getIdCliente());
        TTituloVenta.setText(cliente.getNombre());
        MuestraArticulos();
    }
    protected void onActivityResult(int codigo,int resultado, Intent datos)
    {
        if(codigo==147) {
            if (resultado == RESULT_OK) {
                Bundle extras = datos.getExtras();
                int idCliente = extras.getInt("idCliente");
                Cliente cliente = dataBase.getCliente(idCliente);
                TTituloVenta.setText(cliente.getNombre());
                idVenta = dataBase.insertVenta(idCliente);
                venta = dataBase.getVenta(idVenta);
            } else {
                //no se selecciono ningun cliente
                finish();
            }
        }
        if(codigo==152)
        {
            cargaVenta();
        }
        if(codigo==ELIMINAR && resultado==RESULT_OK) {
            try {
                //primero quito todos los articulos de la venta
                dataBase.deleteVenta(idVenta);
                finish();
            } catch (Exception ex) {
                Toast.makeText(this, ex.getMessage().toString(), Toast.LENGTH_LONG).show();
                return;
            }
        }
        if(codigo==CERRAR_VENTA && resultado==RESULT_OK) {
            finish();
        }


    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/*        Intent i=new Intent();
        Grupo grupo=(Grupo) adaptador.getItem(position);
        i.putExtra("idGrupo",grupo.getIdGrupo());
        setResult(RESULT_OK,i);
        finish();
        */
    }
    private void MuestraArticulos()
    {
        adaptador.setIdVenta(idVenta);
        adaptador.refresh();
        TTotal.setText(String.valueOf(dataBase.getTotalImporteVenta(idVenta)));
    }
    private void AgregaArticulo()
    {
        Intent intent=new Intent(this,ArticuloVenta.class);
        intent.putExtra("BarCode","");// sin codigo de barras
        startActivityForResult(intent,152);
    }
    ///-------------------MANEJO de la CAmara------------------------------
    private CameraSource cameraSource;
    private SurfaceView escaner;
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private String token = "";
    private String tokenanterior = "";
    public void initQR() {

        // creo el detector qr
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ALL_FORMATS).build();

        // creo la camara
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1600, 1024)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        // listener de ciclo de vida de la camara
        escaner.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                // verifico shouldShowRequestPermissionRationalesi el usuario dio los permisos para la camara
                if (ActivityCompat.checkSelfPermission(PantallaVenta.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                        // verificamos la version de ANdroid que sea al menos la M para mostrar
                        // el dialog de la solicitud de la camara
                        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) ;
                        requestPermissions(new String[]{Manifest.permission.CAMERA},MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                    return;
                }
                else
                {
                    try {
                        cameraSource.start(escaner.getHolder());
                    } catch (IOException ie) {
                        Log.e("CAMERA SOURCE", ie.getMessage());
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        // preparo el detector de QR
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }


            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() > 0)
                {

                    // obtenemos el token
                    token = barcodes.valueAt(0).displayValue.toString();
                    // verificamos que el token anterior no se igual al actual
                    // esto es util para evitar multiples llamadas empleando el mismo token
                    if (!token.equals(tokenanterior))
                    {
                        //----------------OSCAR------------
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                OnScanner(token);
                            }
                        });

                        //----------------FIN OSCAR----------
                        // guardamos el ultimo token proceado
                        tokenanterior = token;
                        Log.i("token", token);
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    synchronized (this) {
                                        wait(5000);
                                        // limpiamos el token
                                        tokenanterior = "";
                                    }
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    Log.e("Error", "Waiting didnt work!!");
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    }
                }
            }
        });

    }
    private void OnScanner(String barcode)
    {
        if(barcode.trim().equals(""))
            return;
        Articulo a=dataBase.FindArticulo(barcode);
        if(a==null)
            return;
        soundPool.play(beepSound,1,1,1,0,1);
        Intent intent=new Intent(this,ArticuloVenta.class);
        intent.putExtra("BarCode",barcode);// sin codigo de barras
        startActivityForResult(intent,152);
    }
    //----------------sonido------------------------------
    SoundPool soundPool;
    int beepSound;
    private void  IniciaSonido()
    {
        soundPool=new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        beepSound=soundPool.load(this,R.raw.beep,0);

    }
    //------------------------------------------------------------------
    private void  cancelarVenta()
    {
        Intent i=new Intent(this,MessageBox.class);
        String mensaje="¿Cancelar la venta? ";
        i.putExtra("MENSAJE",mensaje);
        startActivityForResult(i,ELIMINAR);

    }
    private void  muestraCerrarVenta()
    {
        Intent i=new Intent(this,CerrarVentaActivity.class);
        i.putExtra("idVenta",idVenta);
        startActivityForResult(i,CERRAR_VENTA);
    }
}
