package oscar.com.miniventa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ArticuloVenta extends AppCompatActivity {
    TextView TArticulo;
    EditText TCantidad;
    TextView TImporte;
    Articulo articulo;
    Venta venta;
    String barCode="";
    //private int idVenta;
    protected DataBase dataBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_articulo_venta);
         TArticulo=(TextView)findViewById(R.id.TArticulo);
        TCantidad=(EditText)findViewById(R.id.TCantidad);
        TCantidad.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (articulo != null) {
                    String scantidad=TCantidad.getText().toString();
                    if (!scantidad.equals("")) {
                        int cantidad = Integer.valueOf(scantidad);
                        double importe = articulo.getPrecio() * cantidad;
                        TImporte.setText(String.valueOf(importe));
                    }
                }
                return false;
            }
        });
        TImporte=(TextView)findViewById(R.id.TImporte);
        dataBase = new DataBase(this);
        TImporte.setText("0");
       CargaInfo();
    }
    private void CargaInfo() {
        barCode = getIntent().getExtras().getString("BarCode", "");
        if (!barCode.equals("")) {
            //busco el articulo por medio de su codigo de barras
            articulo = dataBase.FindArticulo(barCode);
            if (articulo != null) {
                //muestro la informacion del articulo
                TArticulo.setText(articulo.getNombre());
                if (!barCode.equals("")) {
                    TCantidad.setText("1");
                    TImporte.setText(String.valueOf(articulo.getPrecio()));
                }
            }
        }

        //veo si tiene venta asignada
        venta=dataBase.getVentaActiva();
        if(venta!=null && articulo!=null)
        {
            //busco si ya existe el articulo en la venta

            DetalleVenta detalleVenta=dataBase.getDetalleVenta(venta.getIdVenta(),articulo.getIdArticulo());
            if(detalleVenta!=null) {
                int cantidad = detalleVenta.getCantidad();
                if (!barCode.equals("")) {
                    cantidad++;
                }
                TCantidad.setText(String.valueOf(cantidad));
                TImporte.setText(String.valueOf(detalleVenta.getImporte()));
            }
        }
    }
    public void Buscar(View view)
    {
        Intent i=new Intent(this,ActivitySeleccionarArticulo.class);
        startActivityForResult(i,150);
        return;

    }
    public void Cancelar(View view)
    {
        finish();
    }
    protected void onActivityResult(int codigo,int resultado, Intent datos)
    {
        if(resultado== RESULT_OK) {
            Bundle extras=            datos.getExtras();
            int idArticulo=extras.getInt("IdArticulo");
            articulo = dataBase.getArticulo(idArticulo);
            TArticulo.setText(articulo.getNombre());
            if(venta!=null && articulo!=null)
            {
                //busco si ya existe el articulo en la venta

                DetalleVenta detalleVenta=dataBase.getDetalleVenta(venta.getIdVenta(),articulo.getIdArticulo());
                if(detalleVenta!=null)
                {
                    TCantidad.setText(String.valueOf(detalleVenta.getCantidad()));
                    TImporte.setText(String.valueOf( detalleVenta.getImporte()));
                }
            }
        }
    }
    public void Aceptar(View view)
    {
        try {
            //agre el articulo a la venta
            if (articulo != null && venta != null) {
                String scantidad = TCantidad.getText().toString();
                if (!scantidad.equals("")) {
                    int cantidad = Integer.valueOf(scantidad);
                    //busco si ya existe el articulo en la venta
                    DetalleVenta detalleVenta = dataBase.getDetalleVenta(venta.getIdVenta(), articulo.getIdArticulo());
                    if (detalleVenta != null) {
                        dataBase.updateDetalleVenta(venta.getIdVenta(), articulo.getIdArticulo(), articulo.getPrecio(), cantidad);
                    } else {
                        dataBase.insertDetalleVenta(venta.getIdVenta(),articulo.getIdArticulo(),articulo.getPrecio(),cantidad);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG);
            return;
        }
        finish();
    }

}
