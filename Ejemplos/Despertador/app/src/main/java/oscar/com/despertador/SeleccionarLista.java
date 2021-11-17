package oscar.com.despertador;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class SeleccionarLista extends AppCompatActivity implements AdapterView.OnItemClickListener{
    public AdaptadorSeleccionarLista adaptador;
    private DataBase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=new DataBase(this);
        setContentView(R.layout.activity_seleccionar_lista);
        adaptador=new AdaptadorSeleccionarLista(this);
        ListView listView=(ListView )findViewById(R.id.listView);
        listView.setAdapter(adaptador);
        listView.setOnItemClickListener(this);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i=new Intent();
        ListaReproduccion lista=(ListaReproduccion)adaptador.getItem(position);
        i.putExtra("idLista",lista.getIdLista());
        setResult(RESULT_OK,i);
        finish();

    }
}
