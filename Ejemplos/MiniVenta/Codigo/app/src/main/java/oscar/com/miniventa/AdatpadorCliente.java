package oscar.com.miniventa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AdatpadorCliente extends AdaptadorBase {
    List<Cliente> lista;
    public AdatpadorCliente(Context context)
    {
        super(context);
        loadData();
    }
    @Override
    public void loadData()
    {
        lista=dataBase.getClientes();
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
        Cliente cliente=lista.get(position);
        if (convertView == null) {
            convertView = inflador.inflate(R.layout.elemento_cliente, null);
        }
        TextView nombre=(TextView) convertView.findViewById(R.id.nombre);
        nombre.setText(cliente.getNombre());
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
        Intent i=new Intent(Contexto,PropiedadesCliente.class);
        PantallaCatalogo main=(PantallaCatalogo)Contexto;
        i.putExtra("ID",-1);
        main.startActivityForResult(i,main.EDITAR);

    }
    @Override
    public void eliminar(int id) throws Exception
    {
        Cliente cliente=        lista.get(id);
        dataBase.deleteCliente(cliente.getIdCliente());
        idSeleccionado=-1;
    }
    @Override
    public void editar(int id)
    {
        Cliente cliente= lista.get(id);
        Intent i=new Intent(Contexto,PropiedadesCliente.class);
        i.putExtra("ID",cliente.getIdCliente());
        PantallaCatalogo main=(PantallaCatalogo)Contexto;
        main.startActivityForResult(i,main.EDITAR);
    }
    @Override
    public String getSelectedName()
    {
        if(idSeleccionado==-1)
            return "";
        Cliente cliente= lista.get(idSeleccionado);
        if(cliente==null)
            return  "";
        return cliente.getNombre();
    }

}
