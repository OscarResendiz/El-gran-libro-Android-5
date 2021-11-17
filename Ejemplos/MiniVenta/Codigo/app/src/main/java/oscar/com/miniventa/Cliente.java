package oscar.com.miniventa;

import java.util.Date;

//clase que representa un objeto cliente de la base de datos
public class Cliente {
    private int idCliente;
    private String nombre;

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getFechaNaciiento() {
        return fechaNaciiento;
    }

    public void setFechaNaciiento(Date fechaNaciiento) {
        this.fechaNaciiento = fechaNaciiento;
    }

    public boolean esHombre()
    {
        return (sexo.equals("M"));
    }
    public boolean esMujer()
    {
        return (sexo.equals("F"));
    }
    public void setSexoHombre(boolean valor)
    {
        if(valor==true)
            sexo="M";
        else
            sexo="F";
    }
    public void setSexoMujer(boolean valor)
    {
        if(valor==true)
            sexo="F";
        else
            sexo="M";

    }
    private String apellido;
    private  String sexo;
    private String direccion;
    private String telefono;
    private String email;
    private Date fechaNaciiento;
}
