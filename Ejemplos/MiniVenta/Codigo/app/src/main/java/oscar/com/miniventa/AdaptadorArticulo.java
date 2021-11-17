package oscar.com.miniventa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AdaptadorArticulo extends AdaptadorBase {
    List<Articulo> lista;
    public AdaptadorArticulo(Context context)
    {
        super(context);
        loadData();
    }
    @Override
    public void loadData()
    {
        lista=dataBase.getArticulos("");
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
        Articulo articulo=lista.get(position);
        if (convertView == null) {
            convertView = inflador.inflate(R.layout.elemento_articulo, null);
        }
        TextView nombre=(TextView) convertView.findViewById(R.id.nombre);
        nombre.setText(articulo.getNombre());
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
        Intent i=new Intent(Contexto,PropiedadesArticulo.class);
        PantallaCatalogo main=(PantallaCatalogo)Contexto;
        i.putExtra("ID",-1);
        main.startActivityForResult(i,main.EDITAR);

    }
    @Override
    public void eliminar(int id) throws Exception
    {
        Articulo articulo=lista.get(id);
        dataBase.deleteArticulo(articulo.getIdArticulo());
        idSeleccionado=-1;
    }
    @Override
    public void editar(int id)
    {
        Articulo articulo= lista.get(id);
        Intent i=new Intent(Contexto,PropiedadesArticulo.class);
        i.putExtra("ID",articulo.getIdArticulo());
        PantallaCatalogo main=(PantallaCatalogo)Contexto;
        main.startActivityForResult(i,main.EDITAR);
    }
    @Override
    public String getSelectedName()
    {
        if(idSeleccionado==-1)
            return "";
        Articulo articulo= lista.get(idSeleccionado);
        if(articulo==null)
            return  "";
        return articulo.getNombre();
    }
}
