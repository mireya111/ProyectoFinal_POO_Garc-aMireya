import com.mongodb.MongoException;
import com.mongodb.client.*;
import org.bson.Document;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Productos {
    int codigo;
    String nombre;
    int cantidadDisponible;
    double precio;
    File imagen;
    /*Constructores*/
    public Productos(){}
    public Productos(int codigo, String nombre, int cantidadDisponible, double precio, File imagen) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.cantidadDisponible = cantidadDisponible;
        this.precio = precio;
        this.imagen = imagen;
    }
    /*Setters y getters*/

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

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
    /*Metodo para visualizar los productos*/
    public void catalogo (DefaultTableModel modelo, JTable resultados, JLabel camposVacios ){
        try (MongoClient mongoClient = MongoClients.create("mongodb+srv://mireya:Nena1112004@cluster0.z9ytrsk.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")) {
            MongoDatabase db = mongoClient.getDatabase("Proyectofinalpoo");
            MongoCollection<Document> collection = db.getCollection("cadaProducto");
            /*Se añade los nombres de las columnas*/
            modelo.addColumn("Codigo");
            modelo.addColumn("Nombre_producto");
            modelo.addColumn("Cantidad_disponible");
            modelo.addColumn("Precio");
            modelo.addColumn("Imagen");
            /*Seteo del nuevo modelo a la tabla vacía*/
            resultados.setModel(modelo);
            /*Busqueda para la muestra de los detalles de los productos publicados*/
            FindIterable<Document> documentos = collection.find();
            for (Document documento : documentos) {
                int codigo = documento.getInteger("Codigo", 0);
                String nombre = documento.getString("Nombre_producto");
                int cantidad = documento.getInteger("Cantidad_disponible", 0);
                double precio = documento.getDouble("Precio");
                String imagenRuta = documento.getString("Imagen");
                ImageIcon imagenIcono = null;
                try {
                    /*Lectura de la imagen*/
                    Image img = ImageIO.read(new File(imagenRuta));
                    /*Ajustar la imagen*/
                    imagenIcono = new ImageIcon(img.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                } catch (Exception ex) {
                    /*Forma una imagen blanca si ocurre algun error*/
                    imagenIcono = new ImageIcon(new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB));
                }
                /*Se añade los valores de las celdas de una fila, arreglo de objetos*/
                modelo.addRow(new Object[]{codigo, nombre, cantidad, precio, imagenIcono});
            }
            /*Darle a la tabla vacía un modelo con lo antes colocado*/
            resultados.setModel(modelo);
        } catch (MongoException exception) {
            camposVacios.setText("No se conecto correctamente a la base de datos");
        }
    }
}
