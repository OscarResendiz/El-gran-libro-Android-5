package oscar.com.miniventa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AdaptadorVenta extends AdaptadorBase {
    List<DetalleVenta> lista;
    int idVenta;
    public AdaptadorVenta(Context context, int idventa)
    {
        super(context);
        idVenta=idventa;
        loadData();
    }
    @Override
    public void loadData()
    {
        lista=dataBase.getDetalleVenta(idVenta);
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
        DetalleVenta detalleVenta=lista.get(position);
        if (convertView == null) {
            convertView = inflador.inflate(R.layout.elemento_venta, null);
        }
        //me traigo el nombre del producto para mostrarlo
        TextView TArticulo=(TextView) convertView.findViewById(R.id.TArticulo);
        Articulo articulo= dataBase.getArticulo(detalleVenta.getIdArticulo());
        TArticulo.setText(articulo.getNombre());
        //me traigo la cantidad para mostrarla
        TextView TCantidad=(TextView) convertView.findViewById(R.id.TCantidad);
        TCantidad.setText(detalleVenta.getCantidad());

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
      //  Intent i=new Intent(Contexto,PropiedadesCliente.class);
        //PantallaCatalogo main=(PantallaCatalogo)Contexto;
        //i.putExtra("ID",-1);
        //main.startActivityForResult(i,main.EDITAR);

    }

    @Override
    public void eliminar(int id) throws Exception
    {
        //Cliente cliente=        lista.get(id);
        //dataBase.deleteCliente(cliente.getIdCliente());
        //idSeleccionado=-1;
    }
    @Override
    public void editar(int id)
    {
        //Cliente cliente= lista.get(id);
        //Intent i=new Intent(Contexto,PropiedadesCliente.class);
        //i.putExtra("ID",cliente.getIdCliente());
        //PantallaCatalogo main=(PantallaCatalogo)Contexto;
        //main.startActivityForResult(i,main.EDITAR);
    }
    @Override
    public String getSelectedName()
    {
        if(idSeleccionado==-1)
            return "";
        DetalleVenta detalleVenta= lista.get(idSeleccionado);
        if(detalleVenta==null)
            return  "";
        Articulo articulo=dataBase.getArticulo( detalleVenta.getIdArticulo());
        return articulo.getNombre();
    }

}
