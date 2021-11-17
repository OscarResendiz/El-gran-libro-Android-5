package oscar.com.despertador;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class Principal extends AppCompatActivity implements AdapterView.OnItemClickListener, IAlarmaEventListener{

    public AdaptadorAlarma adaptador;
    private DataBase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=new DataBase(this);
        setContentView(R.layout.activity_principal);


        adaptador=new AdaptadorAlarma(this);
        ListView listView=(ListView )findViewById(R.id.listView);
        listView.setAdapter(adaptador);
        listView.setOnItemClickListener(this);
        HiloAlarma.InciaAlarma(this);
        HiloAlarma.getControlador().addAlarmaVenetListener(this);

    }
    /////// Para el manejo de menus//////////////////////
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_principal,menu);
        return true;
    }
    //------------------------MENU-----------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        switch (id)
        {
            case R.id.agregar:
                menuAgregarAlarma();
                return true;
            case R.id.listas:
                menuListaReproduccion();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    ///////////////////////////////////////////////////
    private void mensaje(String msg)
    {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i=new Intent(this,VistaAlarma.class);
        Alarma alarma=(Alarma)adaptador.getItem(position);
        i.putExtra("idAlarma",alarma.getIdAlarma());
        startActivityForResult(i,789);

    }
    private void menuListaReproduccion()
    {
        Intent intent=new Intent(this,ListaReproduccionActivity.class);
        startActivity(intent);
    }
    private void menuAgregarAlarma()
    {
        Intent i=new Intent(this,VistaAlarma.class);
        i.putExtra("idAlarma",-1);
        startActivityForResult(i,789);
    }
    protected void onActivityResult(int codigo,int resultado, Intent datos)
    {
        if(resultado== RESULT_OK) {
            adaptador.cargaAlarmas();
            adaptador.notifyDataSetChanged();
        }
    }
    @Override
    protected void onRestart()
    {
        super.onRestart();
        adaptador.cargaAlarmas();
        adaptador.notifyDataSetChanged();
    }

    @Override
    public void OnAlarma(AlarmaEventObject arg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adaptador.cargaAlarmas();
                adaptador.notifyDataSetChanged();
            }
        });

    }
}
