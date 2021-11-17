package oscar.com.miniventa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AdaptadorGrupo extends AdaptadorBase {
    List<Grupo> lista;
    public AdaptadorGrupo(Context contexto)
    {
        super(contexto);
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
            convertView = inflador.inflate(R.layout.elemento_grupo, null);
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
    @Override
    public void nuevo()
    {
        Intent i=new Intent(Contexto,PropiedadesGrupo.class);
        PantallaCatalogo main=(PantallaCatalogo)Contexto;
        i.putExtra("ID",-1);
        main.startActivityForResult(i,main.EDITAR);

    }
    @Override
    public void eliminar(int id) throws Exception
    {
        Grupo grupo=        lista.get(id);
        dataBase.deleteGrupo(grupo.getIdGrupo());
        idSeleccionado=-1;
    }
    @Override
    public void editar(int id)
    {
        Grupo grupo= lista.get(id);
        Intent i=new Intent(Contexto,PropiedadesGrupo.class);
        i.putExtra("ID",grupo.getIdGrupo());
        PantallaCatalogo main=(PantallaCatalogo)Contexto;
        main.startActivityForResult(i,0);
    }
    @Override
    public String getSelectedName()
    {
        if(idSeleccionado==-1)
            return "";
        Grupo grupo= lista.get(idSeleccionado);
        if(grupo==null)
            return  "";
        return grupo.getNombre();
    }

}
