package oscar.com.miniventa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class CerrarVentaActivity extends AppCompatActivity {
    TextView TNombreCliente;
    TextView TTotalArticulos;
    TextView TTotalImporte;
    private Venta venta;
    private DataBase dataBase;
    private int totalArticulos=0;
    private double totalImporte=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cerrar_venta);
        //me traigo los componentes
         TNombreCliente=(TextView) findViewById(R.id.TNombreCliente);
        TTotalArticulos=(TextView) findViewById(R.id.TTotalArticulos);
        TTotalImporte=(TextView) findViewById(R.id.TTotalImporte);
        dataBase = new DataBase(this);
        cargaVenta();
    }
    public void cancelar(View view)
    {
        finish();
    }
    public void  imprimir(View view)
    {

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
        Cliente cliente=dataBase.getCliente(venta.getIdCliente());
        TNombreCliente.setText(cliente.getNombre());
        List<DetalleVenta> lista=        dataBase.getDetalleVenta(venta.getIdVenta());
        for (DetalleVenta detalleVenta: lista)
        {
            totalArticulos+=detalleVenta.getCantidad();
            totalImporte+=detalleVenta.getImporte();
        }
        TTotalArticulos.setText(String.valueOf(totalArticulos));
        TTotalImporte.setText(String.valueOf(totalImporte));
    }
    public void aceptar(View view)
    {
        dataBase.updateVenta(venta.getIdVenta(),venta.getIdCliente(),venta.getFecha(),totalImporte,0);
        Intent i=getIntent();
        setResult(RESULT_OK,i);
        finish();
    }
}
