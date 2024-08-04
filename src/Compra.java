import java.io.File;
import java.util.Date;

public class Compra extends Productos {
    int numero_pedido;
    int cantidadProducto;
    double precioTotal;
    String fecha;
    /*Constructores*/
    public Compra() {}
    /**
     * Se almacena datos de de cada compra realizada por el cliente".
     *
     * @param numero_pedido Identificador de cada pedido.
     * @param cantidadProducto Cantidad del producto que desea el cliente.
     * @param precioTotal Precio total de todos los productos escogidos por el cliente.
     * @param fecha La fecha en la que el cliente realizo la compra.
     */
    public Compra(int numero_pedido, int cantidadProducto, double precioTotal, String fecha) {
        this.numero_pedido = numero_pedido;
        this.cantidadProducto = cantidadProducto;
        this.precioTotal = precioTotal;
        this.fecha = fecha;
    }

    public Compra(int codigo, String nombre, int cantidadDisponible, double precio, File imagen, int numero_pedido, int cantidadProducto, double precioTotal, Date fecha) {
        super(codigo, nombre, cantidadDisponible, precio, imagen);
        this.numero_pedido = numero_pedido;
        this.cantidadProducto = cantidadProducto;
        this.precioTotal = precioTotal;
        this.fecha = String.valueOf(fecha);
    }
    /*Setters y getters*/

    public int getNumero_pedido() {
        return numero_pedido;
    }

    public void setNumero_pedido(int numero_pedido) {
        this.numero_pedido = numero_pedido;
    }

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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
