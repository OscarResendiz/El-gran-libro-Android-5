package oscar.com.miniventa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class SeleccionarGrupoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    public AdaptadorSelecionarGrupo adaptador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_grupo);
        adaptador=new AdaptadorSelecionarGrupo(this);
        ListView listView=(ListView )findViewById(R.id.listView);
        listView.setAdapter(adaptador);
        listView.setOnItemClickListener(this);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i=new Intent();
        Grupo grupo=(Grupo) adaptador.getItem(position);
        i.putExtra("idGrupo",grupo.getIdGrupo());
        setResult(RESULT_OK,i);
        finish();
    }

}
