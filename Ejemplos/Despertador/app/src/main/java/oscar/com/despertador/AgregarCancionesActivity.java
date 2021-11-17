package oscar.com.despertador;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AgregarCancionesActivity extends AppCompatActivity {
    private AdaptadorCancion adaptador;
    DataBase db;
    private TextView tbuscar;
    private MediaPlayer reproductor;
    ImageButton botonActual;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_canciones);
        reproductor = new MediaPlayer();
        adaptador=new AdaptadorCancion(this);
        ListView listView=(ListView )findViewById(R.id.listView);
        listView.setAdapter(adaptador);
        tbuscar=(TextView)findViewById(R.id.TBuscar);
        db=new DataBase(this);
        if(savedInstanceState==null)
            db.deleteCancionesSistema();
        botonActual=null;
        reproductor.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                                public void onCompletion(MediaPlayer mp) {
                                                    if(botonActual!=null) {
                                                        botonActual.setImageResource(android.R.drawable.ic_media_play);
                                                        Cancion cancion=(Cancion)botonActual.getTag();
                                                        cancion.setStausPlay(false);
                                                        reproductor.stop();
                                                        reproductor.reset();

                                                    }
                                                }
                                            }
        );
    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_canciones,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        switch (id)
        {
            case R.id.todos:
                agregarTodos();
                return true;
            case R.id.guardar:
                menuGuardar();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void agregarTodos()
    {
        adaptador.MarcaTodos();
        Toast.makeText(this,"Agregar todos",Toast.LENGTH_LONG).show();

    }
    private void menuGuardar()
    {
        Intent i=new Intent();
    //    i.putExtra("resultado",123);
        setResult(RESULT_OK,i);
        finish();
    }
    public void Buscar(View view)
    {
        String filtro=tbuscar.getText().toString();
        adaptador.Filtra(filtro);
        adaptador.notifyDataSetChanged();
    }
    public void ClickCancion(View view)
    {
        CheckBox checkBox=(CheckBox) view;
        String nombre=checkBox.getText().toString();
        String path=(String)checkBox.getTag();
        //busco la cancion
        if(checkBox.isChecked())
        {
            db.insertCancionSistema(nombre,path);
        }
        else
        {
            Cancion c=db.getCancionSistemaByName(nombre);
            if(c!=null)
                db.deleteCancionSistema(c.getIdCancion());
        }
    }
    public void play(View view)
    {
        boolean cambio=false;
        ImageButton boton=(ImageButton )view;
        if(botonActual!=null && botonActual!=boton)
        {
            //no son el mismo objeto
            botonActual.setImageResource(android.R.drawable.ic_media_play);
            Cancion cancion=(Cancion)botonActual.getTag();
            cancion.setStausPlay(false);
            cambio=true;
        }
        botonActual=boton;
        Cancion cancion=(Cancion)boton.getTag();
        if(cancion.isStausPlay())
        {
            boton.setImageResource(android.R.drawable.ic_media_play);
            cancion.setStausPlay(false);
            reproductor.stop();
            reproductor.reset();

        }
        else {
            if(cambio)
            {
                reproductor.stop();
                reproductor.reset();
            }
            try {
                boton.setImageResource(android.R.drawable.ic_media_pause);
                cancion.setStausPlay(true);
                reproductor.setDataSource(cancion.getPath());
                reproductor.prepare();
                reproductor.start();
            }
            catch (Exception e)
            {
                boton.setImageResource(android.R.drawable.ic_media_play);
                cancion.setStausPlay(false);
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
}
