package com.example.oscar.mislugares;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.util.Date;

public class VistaLugar extends AppCompatActivity {
    private long id;
    private Lugar lugar;
    private ImageView imageView;
    final static int RESULTADO_EDITAR=1;
    final static int RESULTADO_GALERIA=2;
    final static int RESULTADO_FOTO=3;
    private Uri urifoto;
    @Override
    protected void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.vista_lugar);
        Bundle extras=getIntent().getExtras();
        id=extras.getLong("id",1);
        imageView=(ImageView)findViewById(R.id.foto);
        actualizarVista();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.vista_lugar,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.accion_compartir:

                Intent intent=  new Intent(Intent.ACTION_SEND).setType("text/plain").putExtra(Intent.EXTRA_TEXT,lugar.getNombre()+" - "+lugar.getUrl());
                startActivity(intent);
                return true;
            case R.id.accion_llegar:
                verMapa(null);
                return  true;
            case R.id.accion_editar:
                Editar();
                return true;
            case R.id.accion_borrar:
                Borrar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void Borrar()
    {
        new android.support.v7.app.AlertDialog.Builder(this)
                .setTitle("Borrado de lugar")
                .setMessage("¿Estas seguro que quieres eliminar este lugar?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Lugares.borrar((int)id);
                        finish();
                    }
                }).setNegativeButton("Cancelar",null).show();
    }
    public void Editar()
    {
        Intent vl=new Intent(this,EdicionLugar.class);
        vl.putExtra("id",id);
        startActivityForResult(vl,RESULTADO_EDITAR);
    }
    @Override
    protected void onActivityResult(int codigo, int resultado,Intent data)
    {
        if(codigo==RESULTADO_EDITAR)
        {
            actualizarVista();
            findViewById(R.id.scrollView1).invalidate();
        }
        else if(codigo==RESULTADO_GALERIA && resultado== Activity.RESULT_OK)
        {
            lugar.setFoto(data.getDataString());
            ponerFoto(imageView,lugar.getFoto());
        }
        else if(codigo==RESULTADO_FOTO && resultado== Activity.RESULT_OK && lugar!=null && urifoto!=null)
        {
            lugar.setFoto(urifoto.toString());
            ponerFoto(imageView,lugar.getFoto());
        }
    }
    private void actualizarVista()
    {
        lugar=Lugares.elemento((int)id);
        TextView nombre=(TextView) findViewById(R.id.nombre);
        nombre.setText(lugar.getNombre());

        ImageView logo_tipo=(ImageView)findViewById(R.id.Logo_tipo);
        logo_tipo.setImageResource(lugar.getTipo().getRecurso());

        TextView tipo=(TextView) findViewById(R.id.tipo);
        tipo.setText(lugar.getTipo().getTexto());

        TextView direccion=(TextView) findViewById(R.id.direccion);
        direccion.setText(lugar.getDireccion());
        if(lugar.getTelefono()==0) {
            findViewById(R.id.telefono).setVisibility(View.GONE);
        }
        else
        {
            findViewById(R.id.telefono).setVisibility(View.VISIBLE);
            TextView telefono = (TextView) findViewById(R.id.telefono);
            telefono.setText(Integer.toString(lugar.getTelefono()));
        }
        TextView url=(TextView) findViewById(R.id.url);
        url.setText(lugar.getUrl());

        TextView comentario=(TextView) findViewById(R.id.comentario);
        comentario.setText(lugar.getComentario());

        TextView fecha=(TextView) findViewById(R.id.fecha);
        fecha.setText(DateFormat.getDateInstance().format( new Date( lugar.getFecha())));

        TextView hora=(TextView) findViewById(R.id.hora);
        hora.setText(DateFormat.getDateInstance().format( new Date( lugar.getFecha())));

        RatingBar valoracion=(RatingBar) findViewById(R.id.valoracion);
        valoracion.setRating(lugar.getValoracion());
        valoracion.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                lugar.setValoracion(v);
            }
        });
        ponerFoto(imageView,lugar.getFoto());
    }
    public void verMapa(View view)
    {
        Uri uri;
        double lat=lugar.getPosicion().getLatitud();
        double lon=lugar.getPosicion().getLongitud();
        if(lat!=0 || lon!=0)
        {
            uri=Uri.parse("geo:"+lat+","+lon);
        }
        else
        {
            uri=Uri.parse("geo:0,0?q="+lugar.getDireccion());
        }
        Intent intent=new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }
    public void llamadaTelefonica(View view)
    {
        startActivity(new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+lugar.getTelefono())));
    }
    public void pgWeb(View view)
    {
        startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(lugar.getUrl())));
    }
    public void galeria(View view)
    {
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/");
        startActivityForResult(intent,RESULTADO_GALERIA);
    }
    protected void ponerFoto(ImageView imageView, String uri)
    {
        if(uri!=null)
        {
            imageView.setImageBitmap(reduceBitMap(this,uri,1024,1024));
        }
        else
        {
            imageView.setImageBitmap(null);
        }
    }
    public void tomarFoto(View view)
    {
        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
        urifoto=Uri.fromFile(new File(Environment.getExternalStorageDirectory()+File.separator+"omg_"+(System.currentTimeMillis()/1000)+".png"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT,urifoto);
        startActivityForResult(intent,RESULTADO_FOTO);
    }
    public void eliminarFoto(View view)
    {
        lugar.setFoto(null);
        ponerFoto(imageView,null);
    }
    public static Bitmap reduceBitMap(Context contexto, String uri, int maxAncho, int maxAlto)
    {
        try
        {
            final BitmapFactory.Options options=new BitmapFactory.Options();
            options.inJustDecodeBounds=true;
            BitmapFactory.decodeStream(contexto.getContentResolver().openInputStream(Uri.parse(uri)),null,options);
            options.inSampleSize=(int)Math.max(Math.ceil(options.outWidth/maxAncho),Math.ceil(options.outHeight/maxAlto));
            options.inJustDecodeBounds=false;
            return BitmapFactory.decodeStream(contexto.getContentResolver().openInputStream(Uri.parse(uri)),null,options);
        }
        catch (FileNotFoundException e)
        {
            Toast.makeText(contexto,"Fichero/ recurso no encontrado",Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return null;
        }
    }
}
