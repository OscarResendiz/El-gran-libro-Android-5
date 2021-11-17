package oscar.com.despertador;

import java.util.ArrayList;
import java.util.List;

public class ListaReproduccion {
    private List<Cancion> canciones;
    private  int idLista;
    private String nombre;
    private int posicion;

    public List<Cancion> getCanciones() {
        return canciones;
    }

    public void setCanciones(List<Cancion> canciones) {
        this.canciones = canciones;
    }

    public int getIdLista() {
        return idLista;
    }

    public void setIdLista(int idLista) {
        this.idLista = idLista;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ListaReproduccion()
    {
        canciones=new ArrayList<Cancion>();
        posicion=-1;
    }
    public int size()
    {
        return canciones.size();
    }
    public void add(Cancion obj)
    {
        canciones.add(obj);
    }
    public Cancion next()
    {
        //regresa la siguiente cancion a reproducir
        if(size()==0)
            return  null;
        if((posicion+1)>size())
            posicion=0; //se vuleve a reproducir desde la primer cancion
        posicion++;
        return canciones.get(posicion);
    }
    public void remove(int idCancion)
    {
        //elimina la cancion de la lista
        for (Cancion obj: canciones)
        {
            if(obj.getIdCancion()==idCancion)
            {
                canciones.remove(obj);
                return;
            }
        }
    }
    public void  clear()
    {
        //limpoa todas las canciones
        canciones.clear();
    }
}
