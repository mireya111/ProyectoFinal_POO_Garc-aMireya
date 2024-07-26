import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
    private JLabel errorPrecio;
    private JLabel camposVacios;
    private JLabel errorCantidad;
    private JButton modificarProductoButton;
    private JButton eliminarProductoButton;
    private JTextField codigoProducto;
    private JLabel errorCodigo;
    private JButton borrarInformaciónDelFormularioButton;
    private JLabel errorTabla;
    private JButton editarProductoButton;
    public File selectFile;
    /*Definir el tipo de dato que almacenara cada columna de la tabla*/
    DefaultTableModel modelo = new DefaultTableModel() {
        @Override
        public Class<?> getColumnClass(int column) {
            if (column == 0){
                return Integer.class;
            } else if (column == 1) {
                return String.class;
            } else if (column == 2) {
                return Integer.class;
            } else if (column == 3) {
                return Double.class;
            } else if (column == 4) {
                return ImageIcon.class;
            }
            return null;
        }
    };
    public SubirProductos() {
        /*Para acceder a los archivos y que se muestre antes de subir el producto*/
        explorarImagenes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*Se crea una nueva imagen*/
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
                productoNuevo.setCodigo(Integer.parseInt(codigoProducto.getText()));
                productoNuevo.setNombre(nombreProducto.getText());
                productoNuevo.setCantidadDisponible(Integer.parseInt(stock.getText()));
                productoNuevo.setPrecio(Double.parseDouble(precioProducto.getText()));
                /*Asignación a una variable la ruta generada cuando el usuario selecciona una imagen*/
                String rutaImagen = ruta.getText();
                /*No se lo utiliza, pero es parte de la clase "Productos", se setea la imagen seleccionada por el empleado*/
                productoNuevo.setImagen(selectFile);
                if (codigoProducto.getText().isEmpty() || nombreProducto.getText().isEmpty() || stock.getText().isEmpty() || precioProducto.getText().isEmpty()){
                    errorTabla.setText("Se detecto campos vacíos, llene los campos porfavor");
                }
                try{
                    productoNuevo.setCodigo(Integer.parseInt(codigoProducto.getText()));
                }catch (NumberFormatException ex){
                    errorCodigo.setText("El codigo solo deben ser numeros enteros");
                }
                /*Linea de conexión, mensaje de error cuando el usuario digite algo que no sea un numero número*/
                try {
                    productoNuevo.setCantidadDisponible(Integer.parseInt(stock.getText()));
                } catch (NumberFormatException ex) {
                    errorCantidad.setText("La cantidad disponible debe ser un número entero.");
                    return;
                }
                /*Linea de conexión, mensaje de error cuando el usuario digite algo que no sea un numero double*/
                try {
                    productoNuevo.setPrecio(Double.parseDouble(precioProducto.getText()));
                } catch (NumberFormatException ex) {
                    errorPrecio.setText("El precio debe ser un número válido.");
                    return;
                }
                /*Almacenamiento de todos los datos del producto*/
                try (MongoClient mongoClient = MongoClients.create("mongodb+srv://mireya:Nena1112004@cluster0.z9ytrsk.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")) {
                    MongoDatabase db = mongoClient.getDatabase("Proyectofinalpoo");
                    MongoCollection<Document> collection = db.getCollection("cadaProducto");
                    Document productosNuevos = new Document("Codigo",productoNuevo.getCodigo())
                            .append("Nombre_producto", productoNuevo.getNombre())
                            .append("Cantidad_disponible", productoNuevo.getCantidadDisponible())
                            .append("Precio", productoNuevo.getPrecio())
                            .append("Imagen", rutaImagen);
                    collection.insertOne(productosNuevos);
                    camposVacios.setText("Se subió satisfactoriamente el producto");
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
                    camposVacios.setText("No se pudo subir el producto");
                }
            }
        });

        /*Eliminación de productos*/
        eliminarProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (resultados.getSelectedRow() == -1) {
                    errorTabla.setText("No se ha seleccionado ningún producto");
                } else {
                    try (MongoClient mongoClient = MongoClients.create("mongodb+srv://mireya:Nena1112004@cluster0.z9ytrsk.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")) {
                        MongoDatabase db = mongoClient.getDatabase("Proyectofinalpoo");
                        MongoCollection<Document> collection = db.getCollection("cadaProducto");

                        /*Obtener el valor de la celda antes de eliminar la fila*/
                        int codigo = Integer.parseInt(modelo.getValueAt(resultados.getSelectedRow(), 0).toString());
                        Document filtro = new Document("Codigo", codigo);

                        /* Eliminar el documento de MongoDB*/
                        DeleteResult resultado = collection.deleteOne(filtro);
                        /*Confirmación de cuantos documentos se han eliminado*/
                        System.out.println("Documentos borrados: " + resultado.getDeletedCount());

                        /*Eliminar la fila del modelo de la tabla*/
                        modelo.removeRow(resultados.getSelectedRow());
                        errorTabla.setText("Se ha eliminado correctamente");
                    } catch (Exception ex) {
                        errorTabla.setText("Error al eliminar el producto: " + ex.getMessage());
                    }
                }
            }
        });

        /*Mover la información de la tabla a el formulario para su modificacion*/
        editarProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (resultados.getSelectedRow() == -1) {
                    errorTabla.setText("No se ha seleccionado ningun producto");
                } else {
                    codigoProducto.setText(modelo.getValueAt(resultados.getSelectedRow(),0).toString());
                    nombreProducto.setText(modelo.getValueAt(resultados.getSelectedRow(),1).toString());
                    stock.setText(modelo.getValueAt(resultados.getSelectedRow(),2).toString());
                    precioProducto.setText(modelo.getValueAt(resultados.getSelectedRow(),3).toString());
                    ruta.setText(modelo.getValueAt(resultados.getSelectedRow(),4).toString());
                    ImageIcon imagen = (ImageIcon) modelo.getValueAt(resultados.getSelectedRow(), 4);
                    foto2.setIcon(imagen);
                }
            }
        });

        /*Actualizar los productos*/
        modificarProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (resultados.getSelectedRow() == -1){
                    errorTabla.setText("No se ha seleccionado ningun producto");
                } else {
                    try (MongoClient mongoClient = MongoClients.create("mongodb+srv://mireya:Nena1112004@cluster0.z9ytrsk.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")) {
                        MongoDatabase database = mongoClient.getDatabase("Proyectofinalpoo");
                        MongoCollection<Document> collection = database.getCollection("cadaProducto");
                        /*Obtener el valor de la celda antes de modificar la fila*/
                        int codigo = Integer.parseInt(codigoProducto.getText().trim());
                        String nombre = nombreProducto.getText().trim();
                        int cantidad = Integer.parseInt(stock.getText().trim());
                        Double precio = Double.parseDouble(precioProducto.getText().trim());
                        String rutaImagen = ruta.getText().trim();
                        /*En dodne se actualizará*/
                        Document filtro = new Document("Codigo", codigo);
                        /*Que se actualizará*/
                        Document actualizacion = new Document("$set", new Document("Nombre_producto", nombre)
                                .append("Cantidad_disponible", cantidad)
                                .append("Precio", precio)
                                .append("Imagen", rutaImagen));
                        UpdateResult resultado = collection.updateOne(filtro, actualizacion);
                        /*Verificar si el producto se modifico*/
                        System.out.println("Documentos modificados: " + resultado.getModifiedCount());
                        /*Visualización de la tabla*/
                        modelo.setValueAt(codigo, resultados.getSelectedRow(), 0);
                        modelo.setValueAt(nombre, resultados.getSelectedRow(), 1);
                        modelo.setValueAt(cantidad, resultados.getSelectedRow(), 2);
                        modelo.setValueAt(precio, resultados.getSelectedRow(), 3);

                        /*Actualiza la imagen en la tabla*/
                        try {
                            Image img = ImageIO.read(new File(rutaImagen));
                            if (img != null) {
                                /*Se ajusta la imagen*/
                                ImageIcon imageIcon = new ImageIcon(img.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                                modelo.setValueAt(imageIcon, resultados.getSelectedRow(), 4);
                            } else {
                                modelo.setValueAt(null, resultados.getSelectedRow(), 4);
                            }
                        } catch (IOException ex) {
                            modelo.setValueAt(null, resultados.getSelectedRow(), 4);
                        }
                    }
                }
            }
        });

        /*Borrar la información para colocar un nuevo producto*/
        borrarInformaciónDelFormularioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                codigoProducto.setText("");
                nombreProducto.setText("");
                stock.setText("");
                precioProducto.setText("");
                ruta.setText("");
                foto2.setIcon(null);
            }
        });
    }
}

