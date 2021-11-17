package oscar.com.miniventa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Principal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
    }
    public void grupos(View view)
    {
        Intent i=new Intent(this,PantallaCatalogo.class);
        i.putExtra("catalogo",TipoCatalogo.GRUPOS);
        startActivity(i);
    }
    public void articulos(View view)
    {
        Intent i=new Intent(this,PantallaCatalogo.class);
        i.putExtra("catalogo",TipoCatalogo.ARTICULOS);
        startActivity(i);
    }
    public void clientes(View view)
    {
        Intent i=new Intent(this,PantallaCatalogo.class);
        i.putExtra("catalogo",TipoCatalogo.CLIENTES);
        startActivity(i);
    }
    public void ventas(View view)
    {
        Intent i=new Intent(this,PantallaVenta.class);
        startActivity(i);
    }
    public void movimientos(View view)
    {
        Intent i=new Intent(this,PantallaCatalogo.class);
        i.putExtra("catalogo",TipoCatalogo.MOVIMIENTOS);
        startActivity(i);
    }
    public void salir(View view)
    {
        finish();
    }
}
