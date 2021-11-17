package com.example.mislugares;

/**
 * Created by oscar on 21/02/2018.
 */

public enum TipoLugar {
    OTROS("otros",5),
    RESTAURANTE("restaurante",2),
    BAR("bar",6),
    COPAS("copas",0),
    ESPECTACULO("espectaculo",0),
    HOTEL("hotel",0),
    COMPRAS("compras",0),
    EDUCACION("educacion",0),
        DEPORTE("deporte",0),
            NATURALEZA("naturaleza",0),
    GASOLINERA("gasolinera",0);

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


}
