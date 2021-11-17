package oscar.com.miniventa;
//clase que representa un articulo de la venta de la base de datos
public class DetalleVenta {
    private int idVenta;
    private int idArticulo;

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(int idArticulo) {
        this.idArticulo = idArticulo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    private double precio;
    private int cantidad;
    public double getImporte()
    {
        return  cantidad*precio;
    }
}
