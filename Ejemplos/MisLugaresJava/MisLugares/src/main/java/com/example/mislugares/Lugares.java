package com.example.mislugares;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscar on 21/02/2018.
 */

public class Lugares {
    protected static List<Lugar> vectorLugares=ejemploLugares();
    public Lugares()
    {
        vectorLugares=ejemploLugares();
    }
    static Lugar elemento(int id)
    {
        return vectorLugares.get(id);
    }
    static void anyade(Lugar lugar)
    {
        vectorLugares.add(lugar);
    }
    static int nuevo()
    {
        Lugar lugar=new Lugar();
        vectorLugares.add(lugar);
        return vectorLugares.size()-1;
    }
    static void borrar(int id)
    {
        vectorLugares.remove(id);
    }
    public static int size()
    {
        return vectorLugares.size();
    }
    public static List<Lugar> ejemploLugares()
    {
        ArrayList<Lugar> lugares=new ArrayList<Lugar>();
        lugares.add(new Lugar("Escuela politecnica superior de Gandia","C/ Paranimf, 1 46730 Gandia (Spain)",-0.166093,38.995656,TipoLugar.EDUCACION,962849300,"http://www.epgs.upv.es","uno de los mejores lugares para formarse",3));
        lugares.add(new Lugar("Al de siempre","C/ Paranimf, 1 46730 Gandia (Spain)",-0.166093,38.995656,TipoLugar.BAR,0,"http://www.epgs.upv.es","uno de los mejores lugares para formarse",3));
        lugares.add(new Lugar("Android curso.com","C/ Paranimf, 1 46730 Gandia (Spain)",-0.166093,38.995656,TipoLugar.COMPRAS,962849300,"http://www.epgs.upv.es","uno de los mejores lugares para formarse",3));
        lugares.add(new Lugar("Barranco del infierno","C/ Paranimf, 1 46730 Gandia (Spain)",-0.166093,38.995656,TipoLugar.DEPORTE,962849300,"http://www.epgs.upv.es","uno de los mejores lugares para formarse",3));
        lugares.add(new Lugar("La vital","C/ Paranimf, 1 46730 Gandia (Spain)",-0.166093,38.995656,TipoLugar.HOTEL,962849300,"http://www.epgs.upv.es","uno de los mejores lugares para formarse",3));
        return lugares;
    }
}
