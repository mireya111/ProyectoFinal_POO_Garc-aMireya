import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SubirProductos {
    public JPanel subirProductos;
    private JTextField stock;
    private JTextField nombreProducto;
    private JTextField precioProducto;
    private JButton explorarImagenes;
    private JButton subir;
    private JPanel foto;
    private JTable resultados;
    private JLabel foto2;
    private JTextField ruta;
    private JLabel confirmaciones;
    public File selectFile;
    /*Para los nombres de cada columna*/
    DefaultTableModel modelo = new DefaultTableModel();

    public SubirProductos() {
        /*Para acceder a los archivos y que se muestre antes de subir el producto*/
        explorarImagenes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /**/
                JFileChooser imagenesEmpleado = new JFileChooser();
                imagenesEmpleado.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imagenes", "jpg", "png", "jpeg", "gif"));
                int resultado = imagenesEmpleado.showOpenDialog(foto);
                /*Si se selecciona un archivo ¿Qué sucede?*/
                if (resultado == JFileChooser.APPROVE_OPTION) {
                    selectFile = imagenesEmpleado.getSelectedFile();
                    /*Se obtiene la ruta completa de la imagen seleccionada*/
                    ruta.setText(selectFile.getAbsolutePath());
                    try {
                        Image img = ImageIO.read(selectFile);
                        ImageIcon imagenSeleccionada = new ImageIcon(img);
                        int ancho = foto2.getWidth();
                        int alto = foto2.getHeight();
                        /*Escalar la imagen*/
                        Image imagenEscalada = imagenSeleccionada.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                        foto2.setIcon(new ImageIcon(imagenEscalada));
                    } catch (Exception ex) {
                        foto2.setText("No se pudo cargar la imagen");
                    }
                }
            }
        });
        subir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*Creacion de un nuevo objeto*/
                Productos productoNuevo = new Productos();
                /*Seteo de valores con los digitados por el usuario "empleado"*/
                productoNuevo.setNombre(nombreProducto.getText());
                productoNuevo.setCantidadDisponible(Integer.parseInt(stock.getText()));
                productoNuevo.setPrecio(Double.parseDouble(precioProducto.getText()));
                /*Asignación a una variable la ruta generada cuando el usuario selecciona una imagen*/
                String rutaImagen = ruta.getText();
                /*No se lo utiliza, pero es parte de la clase "Productos", se setea la imagen seleccionada por el empleado*/
                productoNuevo.setImagen(selectFile);
                /*Linea de conexión, mensaje de error cuando el usuario digite algo que no sea un numero número*/
                try {
                    productoNuevo.setCantidadDisponible(Integer.parseInt(stock.getText()));
                } catch (NumberFormatException ex) {
                    confirmaciones.setText("La cantidad disponible debe ser un número entero.");
                    return;
                }
                /*Linea de conexión, mensaje de error cuando el usuario digite algo que no sea un numero double*/
                try {
                    productoNuevo.setPrecio(Double.parseDouble(precioProducto.getText()));
                } catch (NumberFormatException ex) {
                    confirmaciones.setText("El precio debe ser un número válido.");
                    return;
                }
                /*Almacenamiento de todos los datos del producto*/
                try (MongoClient mongoClient = MongoClients.create("mongodb+srv://mireya:Nena1112004@cluster0.z9ytrsk.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")) {
                    MongoDatabase db = mongoClient.getDatabase("Productos");
                    MongoCollection<Document> collection = db.getCollection("cadaProducto");
                    Document productosNuevos = new Document("Nombre_producto", productoNuevo.getNombre())
                            .append("Cantidad_disponible", productoNuevo.getCantidadDisponible())
                            .append("Precio", productoNuevo.getPrecio())
                            .append("Imagen", rutaImagen);
                    collection.insertOne(productosNuevos);
                    confirmaciones.setText("Se subió satisfactoriamente el producto");
                } catch (MongoException exception) {
                    confirmaciones.setText("No se pudo subir el producto");
                }
            }
        });
    }
}

