package oscar.com.miniventa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class AdaptadorBase extends BaseAdapter {
    protected LayoutInflater inflador; //crea layouts a partir del xml
    private PantallaCatalogo pantallaCatalogo;
    protected Context Contexto;
    protected DataBase dataBase;
    protected int idSeleccionado=-1;
    public AdaptadorBase(Context contexto)
    {
        inflador=(LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Contexto=contexto;
        dataBase=new DataBase(contexto);
    }
    //clase que se toma como base para los catalogos
    @Override
    public int getCount() {
        return 0;
    }

    public void setPantallaCatalogo(PantallaCatalogo pantallaCatalogo) {
        this.pantallaCatalogo = pantallaCatalogo;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
    public void nuevo()
    {

    }
    public void eliminar(int id) throws Exception
    {

    }
    public void editar(int id)
    {

    }
    public void seleccionado(int pos)
    {
        idSeleccionado=pos;
        notifyDataSetChanged();
    }
    public void refresh()
    {
        loadData();
        notifyDataSetChanged();
    }
    public void loadData()
    {

    }
    public String getSelectedName()
    {
        return "";
    }
}
