package oscar.com.miniventa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class SeleccionarClienteActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    public AdaptadorSelecionarCliente adaptador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_cliente);
        adaptador=new AdaptadorSelecionarCliente(this);
        ListView listView=(ListView )findViewById(R.id.listView);
        listView.setAdapter(adaptador);
        listView.setOnItemClickListener(this);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i=new Intent();
        Cliente cliente=(Cliente) adaptador.getItem(position);
        i.putExtra("idCliente",cliente.getIdCliente());
        setResult(RESULT_OK,i);
        finish();
    }

}
