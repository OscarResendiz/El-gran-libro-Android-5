package oscar.com.miniventa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ActivitySeleccionarArticulo extends AppCompatActivity implements AdapterView.OnItemClickListener{
    public AdaptadorArticulo adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_articulo);
        adaptador=new AdaptadorArticulo(this);
        ListView listView=(ListView )findViewById(R.id.listView);
        listView.setAdapter(adaptador);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i=new Intent();
        Articulo articulo=(Articulo) adaptador.getItem(position);
        i.putExtra("IdArticulo",articulo.getIdArticulo());
        setResult(RESULT_OK,i);
        finish();
    }
}
