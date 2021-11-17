package oscar.com.miniventa;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class AdaptadorSelecionarGrupo extends AdaptadorBase {
    List<Grupo> lista;
    public AdaptadorSelecionarGrupo(Context context)
    {
        super(context);
        loadData();
    }
    @Override
    public void loadData()
    {
        lista=dataBase.getGrupos();
    }
    @Override
    public int getCount()
    {
        return lista.size();
    }
    @Override
    public Object getItem(int position)
    {
        return lista.get(position);
    }
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Grupo grupo=lista.get(position);
        if (convertView == null) {
            convertView = inflador.inflate(R.layout.elemento_seleccionar_grupo, null);
        }
        TextView nombre=(TextView) convertView.findViewById(R.id.nombre);
        nombre.setText(grupo.getNombre());
        if(position==idSeleccionado)
        {
            convertView.setBackgroundColor(Color.DKGRAY);
        }
        else
        {
            convertView.setBackgroundColor(Color.TRANSPARENT);

        }
        return convertView;
    }

}
