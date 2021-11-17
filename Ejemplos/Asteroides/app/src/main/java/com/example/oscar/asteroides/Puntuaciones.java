package com.example.oscar.asteroides;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Puntuaciones extends ListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puntuaciones);
        setListAdapter(new MiAdaptador(this,MainActivity.almacen.listaPuntuaciones(10)));
    }
    @Override
    protected void onListItemClick(ListView listView, View view,int posicion, long id)
    {
        super.onListItemClick(listView,view,posicion, id);
        Object o=getListAdapter().getItem(posicion);
        Toast.makeText(this,"seleccion: "+ Integer.toString(posicion)+" - "+o.toString(),Toast.LENGTH_LONG).show();
    }
}
