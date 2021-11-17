package com.example.oscar.mislugares;

/**
 * Created by oscar on 21/02/2018.
 */

public enum TipoLugar {
    OTROS("otros",R.drawable.otros),
    RESTAURANTE("restaurante",R.drawable.restaurante),
    BAR("bar",R.drawable.bar),
    COPAS("copas",R.drawable.copas),
    ESPECTACULO("espectaculo",R.drawable.espectaculos),
    HOTEL("hotel",R.drawable.hotel),
    COMPRAS("compras",R.drawable.compras),
    EDUCACION("educacion",R.drawable.educacion),
        DEPORTE("deporte",R.drawable.deporte),
            NATURALEZA("naturaleza",R.drawable.naturaleza),
    GASOLINERA("gasolinera",R.drawable.gasolinera);

    public String getTexto() {
        return texto;
    }

    public int getRecurso() {
        return recurso;
    }

    private final String texto;
    private final int recurso;
    TipoLugar(String texto, int recurso)
    {
        this.recurso=recurso;
        this.texto=texto;
    }
    public static String[]getNombres()
    {
        String[] resultado=new String[TipoLugar.values().length];
        for (TipoLugar tipo: TipoLugar.values())
        {
            resultado[tipo.ordinal()]=tipo.texto;
        }
        return resultado;
    }

}
