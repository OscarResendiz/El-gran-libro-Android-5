package oscar.com.miniventa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class PantallaCatalogo extends AppCompatActivity implements AdapterView.OnItemClickListener{

    AdaptadorBase adaptador;
    public final int EDITAR=1;
    public final int ELIMINAR=2;
    int seleccionado=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_catalogo);
        //me traigo el tipo de catalogo que hay que generar
        Bundle extras = getIntent().getExtras();
        TipoCatalogo tipoCatalogo=(TipoCatalogo) extras.get("catalogo");
        switch (tipoCatalogo)
        {
            case ARTICULOS:
                adaptador=new AdaptadorArticulo(this);
                break;
            case GRUPOS:
                adaptador=new AdaptadorGrupo(this);
                break;
            case CLIENTES:
                adaptador=new AdatpadorCliente(this);
                break;
            case MOVIMIENTOS:
                adaptador=new AdaptadorMovimiento(this);
                break;
        }
        adaptador.setPantallaCatalogo(this);
        ListView listView=(ListView )findViewById(R.id.listView);
        listView.setAdapter(adaptador);
        listView.setOnItemClickListener(this);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        switch (id)
        {
            case R.id.MenuAgregar:
                nuevo();
                return true;
            case R.id.MenuEditar:
                editar();
                return true;
            case R.id.MenuEliminar:
                elimina();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        seleccionado=position;
        adaptador.seleccionado(position);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalogo, menu);
        return true;
    }
    private void nuevo()
    {
      adaptador.nuevo();
    }
    private void elimina()
    {
        if(seleccionado==-1)
        {
            Toast.makeText(this,"Debe seleccionar el elemento a eliminar",Toast.LENGTH_LONG).show();
            return;
        }
        Intent i=new Intent(this,MessageBox.class);
        String mensaje="¿Elimnar el elemento: "+adaptador.getSelectedName()+"?";
        i.putExtra("MENSAJE",mensaje);
        startActivityForResult(i,ELIMINAR);

    }
    private void editar( )
    {
        if(seleccionado==-1)
        {
            Toast.makeText(this,"Debe seleccionar el elemento a modificar",Toast.LENGTH_LONG).show();
            return;
        }
        adaptador.editar(seleccionado);
    }
    protected void onActivityResult(int codigo,int resultado, Intent datos)
    {
        if(codigo==ELIMINAR && resultado==RESULT_OK) {
            try {
                adaptador.eliminar(seleccionado);
                adaptador.refresh();
            } catch (Exception ex) {
                Toast.makeText(this, ex.getMessage().toString(), Toast.LENGTH_LONG).show();
                return;
            }
        }
        if(codigo==EDITAR) {
            adaptador.refresh();
        }
    }
}
