// IServicioMusica.aidl
package servicioremoto.example.org.servicioremoto;

// Declare any non-default types here with import statements

interface IServicioMusica {
    String reproduce(in String mensaje);
    void setPosicion(int ms);
    int getPosicion();
}
