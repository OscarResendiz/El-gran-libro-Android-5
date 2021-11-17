package oscar.com.despertador;

public class Cancion {
    private int idCancion; //clave de la cancion

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private String nombre; //nombre dela cancion

    public int getIdCancion() {
        return idCancion;
    }

    public void setIdCancion(int idCancion) {
        this.idCancion = idCancion;
    }

    private  String path; //ruta completa del archivo de la cancion
    public Cancion()
    {
        stausPlay=false;
    }
    public Cancion(int idCancion, String path, String nombre)
    {
        this.idCancion=idCancion;
        this.path=path;
        this.nombre=nombre;
    }

    public boolean isStausPlay() {
        return stausPlay;
    }

    public void setStausPlay(boolean stausPlay) {
        this.stausPlay = stausPlay;
    }

    private boolean stausPlay;
}
