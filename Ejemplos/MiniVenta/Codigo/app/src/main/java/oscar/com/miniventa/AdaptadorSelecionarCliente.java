package oscar.com.miniventa;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AdaptadorSelecionarCliente extends AdaptadorBase {
    List<Cliente> lista;
    public AdaptadorSelecionarCliente(Context context)
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
            convertView = inflador.inflate(R.layout.elemento_seleccionar_cliente, null);
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
}
