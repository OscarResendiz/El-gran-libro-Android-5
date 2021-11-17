package oscar.com.despertador;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class PropiedadesListaReproduccionActivity extends AppCompatActivity implements IReproductor {
    private DataBase db;
    private int idLista = -1;
    private AdaptadorCancionesLista adaptador;
    private TextView tNombre;
    MenuItem BPlay;
    private List<Cancion> canciones=null;
    HiloReproductor hiloReproductor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propiedades_lista_reproduccion);
        db = new DataBase(this);
        if (savedInstanceState == null)
            db.deleteCancionesSistema();
        Bundle extras = getIntent().getExtras();
        idLista = extras.getInt("idLista", -1);
        //creo el adaptador
        adaptador = new AdaptadorCancionesLista(this, idLista);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adaptador);
        tNombre = (TextView) findViewById(R.id.nombre);
        if (idLista != -1) {
            ListaReproduccion l = db.getLista(idLista);
            tNombre.setText(l.getNombre());
        }
        BPlay=(MenuItem)findViewById(R.id.BPlay);
        canciones=db.getCancionesLista(idLista);
        hiloReproductor=HiloReproductor.getReproductor();
        hiloReproductor.setVista(this);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_propiedades_lista,menu);
        BPlay=menu.getItem(3);
        setPlayIcon(hiloReproductor.getIconoplayActual());

        //BPlay=(MenuItem)findViewById(R.id.BPlay);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        switch (id)
        {
            case R.id.agregar:
                menuAgregarCanciones();
                return true;
            case R.id.guardar:
                menuGuardar();
                return true;
            case R.id.BAnterior:
                anterior();
                return true;
            case R.id.BPlay:
                BPlay=item;
                playpausa();
                return true;
            case R.id.BSiguiente:
                siguiente();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void siguiente()
    {
        hiloReproductor.siguiente();
    }
    private void anterior()
    {
        hiloReproductor.anterior();
    }
    private void playpausa()
    {
        if (hiloReproductor.isPlaying()) {
            hiloReproductor.stop();

        } else {
            hiloReproductor.play();
        }
    }
    private void menuAgregarCanciones()
    {
        Intent i=new Intent(this,AgregarCancionesActivity.class);
        startActivityForResult(i,123);

    }
    @Override
    protected void onActivityResult(int codigo,int resultado, Intent datos)
    {
        if(resultado== RESULT_OK) {
            adaptador.CargaCanciones();
            adaptador.notifyDataSetChanged();
        }
    }
    private void menuGuardar()
    {
        List<Cancion> canciones;
        if(tNombre.getText().toString().trim().equals(""))
        {
            Toast.makeText(this,"Se requiere el nombre",Toast.LENGTH_LONG).show();
            return;
        }
        if(idLista==-1)
        {
            //hay que agregarlo
            idLista=db.insertLista(tNombre.getText().toString());
        }
        else
        {
            db.updateLista(idLista,tNombre.getText().toString());
        }
        //agrego las canciones
        canciones=db.getCancionesSistema();
        for(Cancion c : canciones)
        {
            int idcancion;
            if(db.ExisteCancion(c.getNombre())==false) {
                idcancion=db.insertCancion(c.getNombre(),c.getPath());
            }
            else
            {
                //me traigo la cancion
                idcancion=db.getCancionByName(c.getNombre()).getIdCancion();
            }
            //veo si ya existe
            if(db.ExisteCancionLista(idLista,idcancion)==false) {
                db.insertCancionLista(idLista, idcancion);
            }
        }
        Intent i=new Intent();
        setResult(RESULT_OK,i);
        finish();

    }
    public void Quitar(View view)
    {
        ImageButton boton=(ImageButton)view;
        int idCancion=(int)boton.getTag();
        if(idLista==-1)
        {
            db.deleteCancionSistema(idCancion);
        }
        else
        {
            db.deleteCancionLista(idLista,idCancion);
        }
        adaptador.CargaCanciones();
        adaptador.notifyDataSetChanged();
    }

    @Override
    public void setPlayIcon(int recurso) {
        if(BPlay!=null)
            BPlay.setIcon(recurso);
    }
    @Override
    public int getId()
    {
        return  idLista;
    }
    @Override
    public List<Cancion> getCacnciones()
    {
        if(canciones==null)
            canciones=db.getCancionesLista(idLista);
        return  canciones;
    }
    @Override
    public int getTipo()
    {
        return 0; //indica que es Lista de reproduccion
    }
    @Override
    public Context getContexto()
    {
        return this;
    }
}
