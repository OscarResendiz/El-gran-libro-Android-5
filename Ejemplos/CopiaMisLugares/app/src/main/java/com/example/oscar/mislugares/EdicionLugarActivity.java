package com.example.oscar.mislugares;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

//Esta es la pantalla principal (MainActivity)
public class EdicionLugarActivity extends AppCompatActivity implements AdapterView.OnItemClickListener ,LocationListener {
    //para localizacion
    private static final int SOLICITUD_PERMISO_ACCESS_FINE_LOCATION = 0;
    private LocationManager manejador;
    private Location mejorLocaliz;
    private boolean tengoPermiso=false;
    private static final long DOS_MINUTOS=2*60*1000;

    public AdaptadorLugares adaptador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.edicion_lugar);
        setContentView(R.layout.activity_main);
        adaptador=new AdaptadorLugares(this);
        ListView listView=(ListView )findViewById(R.id.listView);
        listView.setAdapter(adaptador);
        listView.setOnItemClickListener(this);
        // mnejo de localizacion
        ValidaPermiso();
    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        if(id==R.id.acercaDe)
        {
            lanzarAcercaDe(null);
            return true;
        }
        if(id==R.id.menu_buscar)
        {
            lanzaVistaLugar(null);
            return  true;
        }
        if(id==R.id.menu_mapa)
        {
            Intent intent=new Intent(this,Mapa.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    public void lanzarAcercaDe(View view)
    {
        Intent i=new Intent(this,AcercaDe.class);
        startActivity(i);
    }
    public void  lanzaVistaLugar(View view)
    {
        final EditText entrada=new EditText(this); //cuadro de dialogo
        entrada.setText("0");
        new AlertDialog.Builder(this)
                .setTitle("selecc ion de lugar")
                .setMessage("Indica su ID")
                .setView(entrada)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        long id=Long.parseLong(entrada.getText().toString());
                        Intent vl=new Intent(EdicionLugarActivity.this,VistaLugar.class);
                        vl.putExtra("id",id);
                        startActivity(vl);
                    }
                }).setNegativeButton("Cancelar",null).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View vista, int posicion, long id)
    {
        Intent i=new Intent(this,VistaLugar.class);
        i.putExtra("id",id);
        startActivity(i);
    }
    private void ValidaPermiso()
    {
        //verifica si tengo el permiso para ver la localizacion y si no la tengo lo solivcita
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            tengoPermiso=true;
            Inicializa();
        }
        else {
            solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION, "Sin el permiso no pse puede obtener la localizacion del equipo",SOLICITUD_PERMISO_ACCESS_FINE_LOCATION, this);
        }
    }
    private void Inicializa()
    {
        // mnejo de localizacion
        manejador=(LocationManager)getSystemService(LOCATION_SERVICE);
        //checa si el GPS esta habilitado
        if(manejador.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            if (tengoPermiso)
            {
                if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
                    {

                        actualizaMejorLocaliz(manejador.getLastKnownLocation(LocationManager.GPS_PROVIDER));
                    }
            }
        }
        //verifica si esta activada la localizacion por red
        if(manejador.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            if (tengoPermiso) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    actualizaMejorLocaliz(manejador.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
                }
            }
        }

    }
    public static void solicitarPermiso(final String permiso, String justificacion, final int requestCode, final Activity actividad)
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(actividad,permiso))
        {
            new AlertDialog.Builder(actividad)
                    .setTitle("Solicitud de permiso")
                    .setMessage(justificacion)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ActivityCompat.requestPermissions(actividad,
                                    new String[]{permiso}, requestCode);
                        }})
                    .show();
        } else {
            ActivityCompat.requestPermissions(actividad,new String[]{permiso}, requestCode);
        }
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        activarProveedores();
    }
    private void activarProveedores() {
        if (tengoPermiso) {
            if (manejador.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    manejador.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20 * 1000, 5, this);
                }
            }
            if (manejador.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    manejador.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 20 * 1000, 10, this);
                }
            }
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        manejador.removeUpdates(this);
    }
    @Override
    public void onLocationChanged(Location location)
    {
        Log.d(Lugares.TAG, "Nueva localizacion: " + location);
        actualizaMejorLocaliz(location);
    }
    @Override
    public void onProviderEnabled(String provider) {
        Log.d(Lugares.TAG,"se habilita: "+provider);
        activarProveedores();
    }
    @Override
    public void onProviderDisabled(String provider) {
        Log.d(Lugares.TAG,"se deshabilita: "+provider);
        activarProveedores();
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(Lugares.TAG,"Cambia estado: "+provider);
        activarProveedores();
    }
    private void actualizaMejorLocaliz(Location localiz)
    {
        if(localiz!=null&&(mejorLocaliz==null||localiz.getAccuracy()<2*mejorLocaliz.getAccuracy()||localiz.getTime()-mejorLocaliz.getTime()>DOS_MINUTOS))
        {
            Log.d(Lugares.TAG,"Nueva mejor localizacion");
            mejorLocaliz=localiz;
            Lugares.posicionActual.setLatitud(localiz.getLatitude());
            Lugares.posicionActual.setLongitud(localiz.getLongitude());
        }
    }
}
