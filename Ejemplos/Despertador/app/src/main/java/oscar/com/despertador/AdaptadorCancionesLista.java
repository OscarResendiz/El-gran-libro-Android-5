package oscar.com.despertador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorCancionesLista extends BaseAdapter {
    private LayoutInflater inflador; //crea layouts a partir del xml
    TextView tNombre;
    private static List<Cancion> canciones;
    DataBase db;
    private int idLista;
    Context Contexto;
    ImageButton boton;
    public void setIdLista(int id)
    {
        idLista=id;
    }

    @Override
    public int getCount() {
        return canciones.size();
    }

    @Override
    public Object getItem(int position) {
        return canciones.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public  AdaptadorCancionesLista(Context contexto, int idlista)
    {
        idLista=idlista;
        Contexto=contexto;
        inflador=(LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        db=new DataBase(contexto);
        Contexto=contexto;
        CargaCanciones();
    }
    public void CargaCanciones()
    {
        canciones=new ArrayList<Cancion>();
        if(idLista!=-1)
        {
            //ha que mostrar las canciones que hay e la lista
            canciones=db.getCancionesLista(idLista);
        }
        //me traigo las canciones elegidas por el usuario
        List<Cancion> listtmp=db.getCancionesSistema();
        //agrego las canciones a la lista
        for(Cancion c: listtmp)
        {
            boolean esta=false;
            for (Cancion c2: canciones)
            {
                if(c2.getPath().contains(c.getPath()))
                {
                    esta=true;
                    break;
                }
            }
            if(!esta)
            {
                //no esta, por lo que lo agregp
                canciones.add(c);
            }
        }


    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Cancion cancion=canciones.get(position);
        if (convertView == null) {
            convertView = inflador.inflate(R.layout.elemento_lista_cancion, null);
        }
        //carlo los componetes visuales
        tNombre=(TextView) convertView.findViewById(R.id.TNombre);
        boton=(ImageButton) convertView.findViewById(R.id.BQuitar);
        //asigno datos
        tNombre.setText(cancion.getNombre());
        boton.setTag(cancion.getIdCancion());
        return  convertView;
    }
}
