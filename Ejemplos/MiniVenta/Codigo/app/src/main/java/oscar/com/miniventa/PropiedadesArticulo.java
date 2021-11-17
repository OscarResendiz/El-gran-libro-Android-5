package oscar.com.miniventa;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class PropiedadesArticulo extends AppCompatActivity {
    private TextView TNombre;
    private TextView TBarCode;
    private TextView TPrecio;
    private TextView TDescripcion;
    private TextView TGrupo;
    private int idArticulo;
    private int idGrupo;
    private int existencia;
    protected DataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propiedades_articulo);
        //me traigo los componentes que componen la pantalla
        TNombre = (TextView) findViewById(R.id.TNombre);
        TBarCode = (TextView) findViewById(R.id.TBarCode);
        TPrecio = (TextView) findViewById(R.id.TPrecio);
        TDescripcion = (TextView) findViewById(R.id.TDescripcion);
        TGrupo = (TextView) findViewById(R.id.TGrupo);
        dataBase = new DataBase(this);
        idGrupo=-1;
        existencia=0;
        //me traigo los extras
        idArticulo = getIntent().getExtras().getInt("ID");
        if (idArticulo != -1) {
            //me traigo la informacion
            Articulo articulo = dataBase.getArticulo(idArticulo);
            TNombre.setText(articulo.getNombre());
            TBarCode.setText(articulo.getBarCode());
            double precio = articulo.getPrecio();
            TPrecio.setText(String.valueOf(precio));
            TDescripcion.setText(articulo.getDescripcion());
            idGrupo=articulo.getIdGrupo();
            Grupo grupo = dataBase.getGrupo(idGrupo);
            TGrupo.setText(grupo.getNombre());
            existencia=articulo.getExistencia();
        }
        else
        {
            //por default pongo el primer grupo
            List<Grupo> l=dataBase.getGrupos();
            if(l.size()>0) {
                Grupo grupo = l.get(0);
                idGrupo=grupo .getIdGrupo();
                TGrupo.setText(grupo.getNombre());
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_articulo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_guardar:
                guardar();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void muestraMensaje(String msg)
    {
        Toast.makeText(this,msg,Toast.LENGTH_LONG);
    }
    private void guardar() {
        //me traigo los valores
        String nombre=TNombre.getText().toString();
        String barCode=TBarCode.getText().toString();
        String sprecio=TPrecio.getText().toString();
        String descripcion=TDescripcion.getText().toString();
        double precio=0;
        if(nombre.trim().equals(""))
        {
            muestraMensaje("Falta el nomnre");
            return;
        }
        if(sprecio.trim().equals(""))
        {
            muestraMensaje("Falta el precio");
            return;
        }
        try
        {
            precio=Double.parseDouble(sprecio);
        }
        catch (Exception ex)
        {
            muestraMensaje("Precio no valido");
        }
        if(idGrupo==-1)
        {
            muestraMensaje("Falta el grupo");
            return;
        }
        try {
            if (idArticulo == -1) {
                //es nuevo
                dataBase.insertArticulo(nombre, barCode, descripcion, precio, 0, idGrupo);
            } else {
                //ya existe
                dataBase.updateArticulo(idArticulo, nombre, barCode, descripcion, precio, existencia, idGrupo);
            }
        }
        catch (Exception ex)
        {
            muestraMensaje(ex.getMessage());
        }
        finish();
    }
    public void seleccionaGrupro(View view)
    {
        Intent i=new Intent(this,SeleccionarGrupoActivity.class);
        startActivityForResult(i,147);
    }
    protected void onActivityResult(int codigo,int resultado, Intent datos)
    {
        if(resultado== RESULT_OK) {
            Bundle extras=            datos.getExtras();
            idGrupo=extras.getInt("idGrupo");
            Grupo grupo = dataBase.getGrupo(idGrupo);
            TGrupo.setText(grupo.getNombre());
        }
    }
}
