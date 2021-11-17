package oscar.com.despertador;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
//import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.support.v4.app.ActivityCompat.requestPermissions;
import static android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale;

public class AdaptadorCancion extends BaseAdapter {
    private LayoutInflater inflador; //crea layouts a partir del xml
    CheckBox checkBox;
    private  List<Cancion> canciones;
    DataBase db;
    Context Contexto;
    ImageButton BPlay;
    public AdaptadorCancion(Context contexto)
    {
        Contexto=contexto;
        inflador=(LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        CargaCanciones("");
        db=new DataBase(contexto);
    }
    @Override
    public int getCount() {
        int n=canciones.size();
        return n;
    }

    @Override
    public Object getItem(int position) {
        return canciones.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Cancion cancion=canciones.get(position);
        if (convertView == null) {
            convertView = inflador.inflate(R.layout.elemento_cancion, null);
        }
        //carlo los componetes visuales
        checkBox=(CheckBox) convertView.findViewById(R.id.checkBox);
        BPlay=(ImageButton) convertView.findViewById(R.id.BPlay);
        //asigno datos
        checkBox.setText(cancion.getNombre());
        checkBox.setTag(cancion.getPath());
        if(db.ExisteCancionSistema(cancion.getNombre()))
            checkBox.setChecked(true);
        else
            checkBox.setChecked(false);
        BPlay.setTag(cancion);
        return  convertView;
    }
    public void Filtra(String filtro)
    {
        CargaCanciones(filtro);
    }
    public void MarcaTodos()
    {
        int n=canciones.size();
        for(int i=0;i<n;i++)
        {
            Cancion c=canciones.get(i);
            db.insertCancionSistema(c.getNombre(),c.getPath());
        }
        notifyDataSetChanged();
    }
    public void CargaCanciones( String filtro) {
        if (ActivityCompat.checkSelfPermission(Contexto, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // verificamos la version de ANdroid que sea al menos la M para mostrar
                // el dialog de la solicitud de la camara
                if (shouldShowRequestPermissionRationale((Activity)Contexto,Manifest.permission.READ_EXTERNAL_STORAGE))
                    ;
                requestPermissions((Activity)Contexto,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            return;
        } else {
            try {
                canciones = new ArrayList<Cancion>();
                ContentResolver cr = Contexto.getContentResolver();
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
                String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
                Cursor cur = cr.query(uri, null, selection, null, sortOrder);
                int count = 0;
                int i = 1;
                if (cur != null) {
                    count = cur.getCount();
                    if (count > 0) {
                        while (cur.moveToNext()) {
                            String nombre = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                            String Path = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DATA));
                            if (nombre.contains(filtro)) {
                                if (tieneCaracterRaro(nombre) == false && tieneCaracterRaro(Path) == false)//si tiene caracteres raros no los proceso
                                {
                                    Cancion cancion = new Cancion();
                                    cancion.setNombre(nombre);
                                    cancion.setPath(Path);
                                    cancion.setIdCancion(i);
                                    i++;
                                    canciones.add(cancion);
                                }
                            }
                        }
                    }
                }
                cur.close();
            } catch (Exception ie) {
                Log.e("ERROR", ie.getMessage());
            }
        }
    }
    private boolean tieneCaracterRaro(String cadena)
    {
        //aqui se agregan los caracteres que puedan hacer tronar la aplicacion
        if(cadena.contains("\'"))
            return  true;
        return false;
    }
}
