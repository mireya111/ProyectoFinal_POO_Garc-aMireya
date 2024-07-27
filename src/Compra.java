import java.io.File;

public class Compra extends Productos {
    int cantidadProducto;
    double precioTotal;
    /*Constructores*/
    public Compra() {}
    public Compra(int cantidadProducto, double precioTotal) {
        this.cantidadProducto = cantidadProducto;
        this.precioTotal = precioTotal;
    }

    public Compra(int codigo, String nombre, int cantidadDisponible, double precio, File imagen, int cantidadProducto, double precioTotal) {
        super(codigo, nombre, cantidadDisponible, precio, imagen);
        this.cantidadProducto = cantidadProducto;
        this.precioTotal = precioTotal;
    }
    /*Setters getters*/
    public int getCantidadProducto() {
        return cantidadProducto;
    }

    public void setCantidadProducto(int cantidadProducto) {
        this.cantidadProducto = cantidadProducto;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }
}
