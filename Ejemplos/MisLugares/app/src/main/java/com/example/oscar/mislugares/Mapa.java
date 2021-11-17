package com.example.oscar.mislugares;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Mapa extends FragmentActivity implements GoogleMap.OnInfoWindowClickListener {
    private GoogleMap mapa;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa);
        mapa=((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapa)).getMap();
//        mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapa.setMyLocationEnabled(true);
        mapa.getUiSettings().setZoomControlsEnabled(true);
        mapa.getUiSettings().setCompassEnabled(true);
        mapa.setOnInfoWindowClickListener(this);

        boolean primero=true;
        Cursor c=Lugares.listado();
        while (c.moveToNext())
        {
            GeoPunto p= new GeoPunto(c.getDouble(c.getColumnIndex("longitud")),c.getDouble(c.getColumnIndex("latitud")));
            if(p!=null && p.getLatitud()!=0)
            {
                if(primero)
                {
                    mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(p.getLatitud(),p.getLongitud()),12));
                    primero=false;
                }
                BitmapDrawable iconocDrawable=(BitmapDrawable)getResources().getDrawable(TipoLugar.values()[c.getInt(c.getColumnIndex("tipo"))].getRecurso());
                Bitmap iGrande=iconocDrawable.getBitmap();
                Bitmap icono=Bitmap.createScaledBitmap(iGrande,iGrande.getWidth()/7,iGrande.getHeight()/7,false);
                mapa.addMarker(new MarkerOptions().position(new LatLng(p.getLatitud(),p.getLongitud())).title(c.getString(c.getColumnIndex("nombre"))).snippet(c.getString(c.getColumnIndex("direccion"))).icon(BitmapDescriptorFactory.fromBitmap(icono)));
            }

        }
    }
    @Override
    public void onInfoWindowClick(Marker marker)
    {
        int id=Lugares.buscarNombre(marker.getTitle());
        if(id!=-1)
        {
            Intent intent=new Intent(this,VistaLugar.class);
            intent.putExtra("id",(long)id);
            startActivity(intent);

        }
    }

}
