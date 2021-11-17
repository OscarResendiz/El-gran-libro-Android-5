package com.example.oscar.asteroides;

import java.util.Vector;

public class AlmacenPuntuacionesArray implements AlmacenPuntuaciones {
    private Vector<String> puntuaciones;
    public  AlmacenPuntuacionesArray()
    {
        puntuaciones=new Vector<String>();
        puntuaciones.add("123000 Pepito Domingez");
        puntuaciones.add("111000 Pdero Martinez");
        puntuaciones.add("011000 Paco Perez");
    }
    @Override
    public void guardarPuntuacion(int puntos, String nombre, long fecha) {
        puntuaciones.add(0,puntos+" "+nombre);
    }

    @Override
    public Vector<String> listaPuntuaciones(int cantidad) {
        return puntuaciones;
    }
}
