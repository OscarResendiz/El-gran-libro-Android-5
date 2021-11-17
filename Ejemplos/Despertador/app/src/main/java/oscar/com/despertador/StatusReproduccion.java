package oscar.com.despertador;

public class StatusReproduccion {
    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
        actualiza();
    }

    private int tipo;
    private int identificador;
    private int posicion;
    private int maxinmo;
    private DataBase dataBase;
    public void decrenetaPosicion()
    {
        posicion--;
        if(posicion<0)
            posicion=maxinmo-1;
        actualiza();
    }
    public void incrementaPosicion()
    {
        posicion++;
        if(posicion>=maxinmo)
            posicion=0;
        actualiza();
    }
    public void  setDataBase(DataBase db)
    {
        dataBase=db;
    }
    public void setMaxinmo(int maxinmo) {
        this.maxinmo = maxinmo;
    }
    private void actualiza()
    {
        if(dataBase!=null)
        {
            if(identificador!=-1) {
                dataBase.setStatusReproduccion(tipo, identificador, posicion);
            }
        }
    }
}
