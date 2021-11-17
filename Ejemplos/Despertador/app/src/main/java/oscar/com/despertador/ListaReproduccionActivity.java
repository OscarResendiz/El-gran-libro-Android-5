package oscar.com.despertador;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.List;

public class ListaReproduccionActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    public AdaptadorListaReproduccion adaptador;
    private DataBase db=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_reproduccion);

        adaptador=new AdaptadorListaReproduccion(this);
        ListView listView=(ListView )findViewById(R.id.listView);
        listView.setAdapter(adaptador);
        listView.setOnItemClickListener(this);
        db=new DataBase(this);
    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_lista_reproduccion,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        int id=item.getItemId();
        switch (id)
        {
            case R.id.agregar:
                menuAgregar();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListaReproduccion lista=(ListaReproduccion)adaptador.getItem(position);
        if(lista==null)
            return;
        int idLista=lista.getIdLista();
        Intent i=new Intent(this,PropiedadesListaReproduccionActivity.class);
        i.putExtra("idLista",idLista);
        startActivityForResult(i,456);
    }
    private void menuAgregar()
    {
        Intent i=new Intent(this,PropiedadesListaReproduccionActivity.class);
        i.putExtra("idLista",-1);
        startActivityForResult(i,456);
    }
    @Override
    protected void onActivityResult(int codigo,int resultado, Intent datos)
    {
        if(resultado== RESULT_OK) {
            adaptador.RecargaListas();
            adaptador.notifyDataSetChanged();
        }
    }
    public void eliminarLista(View view)
    {
        ImageButton    bEliminar=(ImageButton)view;
        int idLista=(int)bEliminar.getTag();
        //primero boroo todas las cnaciones
        List<Cancion> canciones=db.getCancionesLista(idLista);
        for(Cancion c: canciones)
        {
            db.deleteCancionLista(idLista,c.getIdCancion());
        }
        //ahora borro la lista
        db.deleteLista(idLista);
        adaptador.RecargaListas();
        adaptador.notifyDataSetChanged();
    }
    public void editarLista(View view)
    {
        ImageButton    bEditar=(ImageButton)view;
        int idLista=(int)bEditar.getTag();
        Intent i=new Intent(this,PropiedadesListaReproduccionActivity.class);
        i.putExtra("idLista",idLista);
        startActivityForResult(i,456);

    }
}
