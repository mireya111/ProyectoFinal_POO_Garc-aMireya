import java.awt.image.BufferedImage;
import java.io.File;

public class Productos {
    String nombre;
    int cantidadDisponible;
    double precio;
    File imagen;
    /*Constructores*/
    public Productos(){}
    public Productos(String nombre, int cantidadDisponible, double precio, File imagen) {
        this.nombre = nombre;
        this.cantidadDisponible = cantidadDisponible;
        this.precio = precio;
        this.imagen = imagen;
    }
    /*Setters y getters*/
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public File getImagen() {
        return imagen;
    }

    public void setImagen(File imagen) {
        this.imagen = imagen;
    }
}
