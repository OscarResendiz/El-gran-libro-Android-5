package oscar.com.miniventa;
// clase que representa a un objeto configuracion de la base de datos
public class Configuracion {
    private String clave;

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    private String valor;
}
