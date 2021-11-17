package com.example.mislugares;

/**
 * Created by oscar on 21/02/2018.
 */

public class Principal {
    public  static void main(String[] main)
    {
     //   Lugar lugar=new Lugar("Mi casa","Cerro del encinal manzana 1 lote 29",-0.166093,38.995656,TipoLugar.EDUCACION,962849300,"www.epsg.uvp.es","uno de os mejores lugares para estar",3);
       // System.out.println("Lugar"+lugar.toString());
        //System.out.println("Hola oscar");
        for(Lugar l : Lugares.vectorLugares)
        {
            System.out.println(l.toString());
        }
    }
}
