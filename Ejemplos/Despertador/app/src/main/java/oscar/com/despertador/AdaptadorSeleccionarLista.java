package oscar.com.despertador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class AdaptadorSeleccionarLista extends BaseAdapter {
    //variables
    private DataBase db=null;
    private LayoutInflater inflador; //crea layouts a partir del xml
    List<ListaReproduccion> listasReproduccion;
    TextView nombre;
    public AdaptadorSeleccionarLista(Context contexto)
    {
        inflador=(LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        db=new DataBase(contexto);


    }
    @Override
    public int getCount() {
        if(listasReproduccion==null)
            listasReproduccion=db.getListas();
        return listasReproduccion.size();
    }

    @Override
    public Object getItem(int position) {
        if(listasReproduccion==null)
            listasReproduccion=db.getListas();
        return listasReproduccion.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View vistaReciclada, ViewGroup parent) {
        if(listasReproduccion==null)
            listasReproduccion=db.getListas();
        ListaReproduccion obj = listasReproduccion.get(position);
        if (vistaReciclada == null) {
            vistaReciclada = inflador.inflate(R.layout.elemento_seleccionar_lista, null);
        }
        //carlo los componetes visuales
        nombre = (TextView) vistaReciclada.findViewById(R.id.nombre);
        //asigno datos
        nombre.setText(obj.getNombre());
        return vistaReciclada;

    }
    public void RecargaListas()
    {
        listasReproduccion=db.getListas();
    }
}
