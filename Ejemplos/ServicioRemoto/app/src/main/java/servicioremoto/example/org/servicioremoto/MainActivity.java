package servicioremoto.example.org.servicioremoto;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    private IServicioMusica servicio;
    private ServiceConnection conexion=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            servicio=IServicioMusica.Stub.asInterface(service);
            Toast.makeText(MainActivity.this,"Servicio conectado",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(MainActivity.this,"Se perdio la coneccion con el servicio",Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button botonConectar=(Button)findViewById(R.id.boton_arrancar);
        botonConectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.bindService(new Intent(MainActivity.this,ServicioRemoto.class),conexion, Context.BIND_AUTO_CREATE);
            }
          }
        );

        Button botonReproducir=(Button)findViewById(R.id.boton_reproducir);
        botonReproducir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    servicio.reproduce("Titulo");
                }
                catch (Exception e)
                {
                    Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button botonAvanzar=(Button)findViewById(R.id.boton_avanzar);
        botonAvanzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    servicio.setPosicion(servicio.getPosicion()+1000);
                }
                catch (Exception e)
                {
                    Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button botonDetener=(Button)findViewById(R.id.boton_detener);
        botonDetener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    MainActivity.this.unbindService(conexion);
                }
                catch (Exception e)
                {
                    Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                }
                servicio=null;
            }
        });
    }
}
