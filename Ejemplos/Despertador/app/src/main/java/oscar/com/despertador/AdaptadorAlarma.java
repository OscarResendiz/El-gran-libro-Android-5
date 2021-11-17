package oscar.com.despertador;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

public class AdaptadorAlarma extends BaseAdapter {
    //variables
    private LayoutInflater inflador; //crea layouts a partir del xml
    TextView nombre;
    TextView informacion;
    ImageView fotostatus;
    ImageView imagenplay;
    DataBase db;
    Context Contexto;
    List<Alarma> alarmas;
    public AdaptadorAlarma(Context contexto)
    {
        Contexto=contexto;
        db=new DataBase(Contexto);
        inflador=(LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        cargaAlarmas();
    }
    @Override
    public int getCount() {
        return alarmas.size();
    }

    @Override
    public Object getItem(int position) {
        return alarmas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View vistaReciclada, ViewGroup parent) {
        Alarma alarma = alarmas.get(position);
        if (vistaReciclada == null) {
            vistaReciclada = inflador.inflate(R.layout.elemento_lista, null);
        }
        //carlo los componetes visuales
        nombre = (TextView) vistaReciclada.findViewById(R.id.nombre);
        informacion = (TextView) vistaReciclada.findViewById(R.id.informacion);
        fotostatus = (ImageView) vistaReciclada.findViewById(R.id.fotostatus);
        imagenplay= (ImageView) vistaReciclada.findViewById(R.id.imagenplay);
        //asigno datos
        nombre.setText(alarma.getNombre());
        informacion.setText(alarma.toString());

//        int id=R.drawable.reloggris64;
  //      if(alarma.isEncendida())
    //        id=R.drawable.relog64;
        imagenplay.setVisibility(View.INVISIBLE);
        //fotostatus.setImageResource(id);
        fotostatus.setScaleType(ImageView.ScaleType.FIT_END);
        //checo si se esta ejecutando
        HiloAlarma controlador=HiloAlarma.getControlador();
        if(controlador.AlarmaEjecutando(alarma.getIdAlarma()))
        {
          //  imagenplay.setVisibility(View.VISIBLE);
            fotostatus.setImageResource(R.drawable.relog64);
            //vistaReciclada.setBackgroundColor(Color.GREEN);
        }
        else {
            fotostatus.setImageResource(R.drawable.reloggris64);
            imagenplay.setVisibility(View.INVISIBLE);
            //vistaReciclada.setBackgroundColor(Color);

        }
        return vistaReciclada;

    }
    public void cargaAlarmas()
    {
        alarmas=db.getAlarmas();
    }
}
