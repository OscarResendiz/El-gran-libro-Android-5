package org.example.localizacion;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.provider.CallLog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener{
    private static final int SOLICITUD_PERMISO_ACCESS_FINE_LOCATION = 0;
//    private static final int SOLICITUD_ULTIMA_LOCALIZACION = 1;
//    private static final int SOLICITUD_LISTAR_PROVEEDORES = 2;

    private static final long TIEMPO_MIN=10*1000;// 10 segundos
    private static final long DISTANCI_MIN=5; //5 metros
    private static final String[] A={"n/d","preciso","impreciso"};
    private static final String[] P={"n/d","bajo","medio","alto"};
    private static final String[] E={"fuera de servicio","temporalmente no disponible","disponible"};
    private LocationManager manejador;
    private String proveedor;
    private TextView salida;
    private boolean tengoPermiso=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        salida=(TextView) findViewById(R.id.salida);
        manejador=(LocationManager)getSystemService(LOCATION_SERVICE);
        log("proveedores de localizacion");
        ValidaPermiso();

    }
    private void Inicializa()
    {
        //se llama si se tiene el permiso o si el usuario lo otorga
        muestraProveedores();
        Criteria criterio= new Criteria();
        criterio.setCostAllowed(false); //no se requiere bajo costo
        criterio.setAltitudeRequired(false); //no se requiere la altitud
        criterio.setAccuracy(Criteria.ACCURACY_FINE);
        proveedor=manejador.getBestProvider(criterio,true);
        log("mejor proveedor: "+proveedor+"\n");
        log("Comenzamos con la ultima localizacion conocida");
        ObteerUltimaLocalizacion();
    }
    private void ValidaPermiso()
    {
        //verifica si tengo el permiso para ver la localizacion y si no la tengo lo solivcita
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            tengoPermiso=true;
            Inicializa();
        }
        else {
            solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION, "Sin el permiso no pse puede obtener la localizacion del equipo",SOLICITUD_PERMISO_ACCESS_FINE_LOCATION, this);
        }
    }
    //metodos del siclo de vida de la actividad
    @Override
    protected void onResume()
    {
        super.onResume();
        ObteerLocalizacion();
    }
    private void ObteerUltimaLocalizacion()
    {
        Location localizacion=null;
        if (tengoPermiso)
        {
            if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                localizacion = manejador.getLastKnownLocation(proveedor);
                muestraLocalizacion(localizacion);
            }
        }
    }
    private void ObteerLocalizacion()
    {
        if (tengoPermiso)
        {
            if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                manejador.requestLocationUpdates(proveedor, TIEMPO_MIN, DISTANCI_MIN, this);
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
    @Override public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults)
    {
        if (requestCode == SOLICITUD_PERMISO_ACCESS_FINE_LOCATION)
        {
            if (grantResults.length== 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                tengoPermiso=true;
                Inicializa();
            }
            else {
                Toast.makeText(this, "Sin el permiso, no puedo realizar la acción", Toast.LENGTH_SHORT).show();
                log(":( no tengo permisos");

            }
        }
    }

    @Override
    protected void  onPause()
    {
        super.onPause();
        manejador.removeUpdates(this);
    }
    //metodos para mostrar informacion
    private void log(String cadena)
    {
        salida.append(cadena+"\n");
    }
    private void muestraProveedores()
    {
        //verifico si existe el permiso
        if (tengoPermiso)
        {
            log("proveedores de localizacion: \n");
            List<String> proveedores=manejador.getAllProviders();
            for(String proveedor: proveedores)
                muestraProveedor(proveedor);
        }

    }
    private void muestraProveedor(String proveedor)
    {
        LocationProvider info=manejador.getProvider(proveedor);
        log("LocationProvider [\ngetName="+info.getName()+"\n"
        +", isProviderEnabled="+manejador.isProviderEnabled(proveedor)+"\n"
        +", getAccuracy="+A[Math.max(0,info.getAccuracy())]+"\n"
        +", getPowerRequirement="+P[Math.max(0,info.getPowerRequirement())]+"\n"
        +", hasMonetaryCost="+ info.hasMonetaryCost()+"\n"
        +", requiresCell="+info.requiresCell()+"\n"
        +", requiresNetwork="+info.requiresNetwork()+"\n"
                        +", requiresSatellite="+info.requiresSatellite()+"\n"
                        +", supportsAltitude="+info.supportsAltitude()+"\n"
                        +", supportsBearing="+info.supportsBearing()+"\n"
                        +", supportsSpeed="+info.supportsSpeed()+"]\n");
    }
    private void muestraLocalizacion(Location localizacion)
    {
        if(localizacion==null)
            log("localizacion desconocida\n");
        else
            log(localizacion.toString()+"\n");
    }
    // metodos de la interface LocationListener
    @Override
    public void onLocationChanged(Location location)
    {
        log("nueva localizacion");
        muestraLocalizacion(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        log("Cambia estado proveedor: "+proveedor+", estado="+E[Math.max(0,status)]+", extras= "+extras+"\n");
    }

    @Override
    public void onProviderEnabled(String provider) {
        log("proveedor habilitado: "+proveedor+"\n");
    }

    @Override
    public void onProviderDisabled(String provider) {
        log("proveedor deshabilitado: "+proveedor+"\n");
    }
}
