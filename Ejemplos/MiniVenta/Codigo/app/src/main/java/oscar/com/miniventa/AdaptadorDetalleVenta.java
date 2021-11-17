package oscar.com.miniventa;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AdaptadorDetalleVenta extends AdaptadorBase {
    List<DetalleVenta> lista;
    private int IdVenta=-1;

    public void setIdVenta(int idVenta) {
        IdVenta = idVenta;
    }

    public AdaptadorDetalleVenta(Context context)
    {
        super(context);
        loadData();
    }
    @Override
    public void loadData()
    {
        lista=dataBase.getDetalleVenta(IdVenta);
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
        DetalleVenta detalle=lista.get(position);
        if (convertView == null) {
            convertView = inflador.inflate(R.layout.elemento_venta, null);
        }
        Articulo articulo=dataBase.getArticulo(detalle.getIdArticulo());

        TextView TArticulo=(TextView) convertView.findViewById(R.id.TArticulo);
        TArticulo.setText(articulo.getNombre());

        TextView TCantidad=(TextView) convertView.findViewById(R.id.TCantidad);
        String scantidad=String.valueOf(detalle.getCantidad());
        TCantidad.setText(scantidad);

        TextView TPrecio=(TextView) convertView.findViewById(R.id.TPrecio);
        String precio=String.valueOf(detalle.getPrecio());
        TPrecio.setText(precio);

        TextView Timporte=(TextView) convertView.findViewById(R.id.TImporte);
        String importe=String.valueOf(detalle.getImporte());
        Timporte.setText(importe);

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
