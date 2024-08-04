import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
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
    private JButton regresar;
    public File selectFile;
    private JFrame frame1;
    public SubirProductos(JFrame frame) {
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
                return Object.class;
            }
        };
        /*Se añade los nombres de las columnas*/
        modelo.addColumn("Codigo");
        modelo.addColumn("Nombre_producto");
        modelo.addColumn("Cantidad_disponible");
        modelo.addColumn("Precio");
        modelo.addColumn("Imagen");
        resultados.setModel(modelo);
        /*Asegúrate de que la columna 4 (índice 4) tenga un renderer de ImageIcon*/
        /*resultados.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof ImageIcon) {
                    return new JLabel((ImageIcon) value);
                } else {
                    return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                }
            }
        });*/
        frame1=frame;
        /*Para acceder a los archivos y que se muestre antes de subir el producto*/
        explorarImagenes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*Se crea una nueva imagen*/
                JFileChooser imagenesEmpleado = new JFileChooser();
                imagenesEmpleado.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imagenes", "jpg", "png", "jpeg", "gif"));
                int resultado = imagenesEmpleado.showOpenDialog(foto);
                /**
                 * @param JFileChooser Se crea un objeto de esta clase, un objeto que será una imagen
                 * Con setFileFilter se determinan las extenciones de imagenes que se aceptarán
                 * @param resultado Se abre el cuadro de diálogo de selección de archivos y se almacena el resultado de la acción del usuario.
                 * El usuario puede seleccionar una imagen y aceptar.
                 * El usuario puede seleccionar una imagen y cancelar.
                 * La seleccion de la imagen es inválida.
                 * */
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
                /**
                 * Si se lecciona una imagen y se acepta su exportación.
                 * Se obtendra la direccion de de la imagen y visualización de la misma en una JLabel llamado "foto2".
                 * @param imagenEscalada se adapta a las dimenciones del JLabel "foto2".
                 * */
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
                /**
                 * @param rutaImagen Se almacena la ruta de la imagen
                 */
                /*No se lo utiliza, pero es parte de la clase "Productos", se setea la imagen seleccionada por el empleado*/
                productoNuevo.setImagen(selectFile);
                /*Limpieza de errores*/
                errorTabla.setText("");
                camposVacios.setText("");
                if (codigoProducto.getText().isEmpty() || nombreProducto.getText().isEmpty() || stock.getText().isEmpty() || precioProducto.getText().isEmpty()){
                    errorTabla.setText("Se detecto campos vacíos, llene los campos porfavor");
                    /**
                     *"isEmpty()" ayuda a la comprobación de los campos vacíos.
                     * Si se encuentra que el campo esta vacio se coloca en el JLabel el error.
                     */
                }
                try{
                    productoNuevo.setCodigo(Integer.parseInt(codigoProducto.getText()));
                }catch (NumberFormatException ex){
                    errorCodigo.setText("El codigo solo deben ser numeros enteros");
                }
                /**
                 * @param try utilizado para verificar si lo colocado en la ventana es un numero
                 * @param catch utilizado para indicarle al usuario el error si se esta insertando son caracteres no permitidos
                 */
                /*Linea de conexión, mensaje de error cuando el usuario digite algo que no sea un numero número*/
                try {
                    productoNuevo.setCantidadDisponible(Integer.parseInt(stock.getText()));
                } catch (NumberFormatException ex) {
                    errorCantidad.setText("La cantidad disponible debe ser un número entero.");
                    return;
                }
                /**
                 * @param try utilizado para verificar si lo colocado en la ventana es un numero
                 * @param catch utilizado para indicarle al usuario el error si se esta insertando son caracteres no permitidos
                 */
                /*Linea de conexión, mensaje de error cuando el usuario digite algo que no sea un numero double*/
                try {
                    productoNuevo.setPrecio(Double.parseDouble(precioProducto.getText()));
                } catch (NumberFormatException ex) {
                    errorPrecio.setText("El precio debe ser un número válido.");
                    return;
                }
                /**
                 * @param try utilizado para verificar si lo colocado en la ventana es un numero decimal
                 * @param catch utilizado para indicarle al usuario el error si se esta insertando son caracteres no permitidos
                 */
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
                    /*Para que la fila se agrande*/
                    resultados.setRowHeight(50);

                    /*Seteo del nuevo modelo a la tabla vacía*/
                    resultados.setModel(modelo);
                    /*Limpia el modelo de la tabla antes de llenar los datos*/
                    modelo.setRowCount(0);

                    /**
                     * Con el "modelo" podemos insertar las diferentes columnas
                     * @param modelo Define cómo se estructuran y manejan los datos que se muestran en la tabla.
                     * Para la inserción de diferentes datos a las columnas de la tabla, se tiene que especificar el tipo de datos que tendra cada una
                     * columna 1 = 0, se recepta numeros enteros
                     * columna 2 = 1, se recepta cadenas de texto
                     * columna 3 = 2, se recepta numeros enteros
                     * columna 4 = 3, se receptan decimales
                     * columna 5 = 4, se receptan imagenes
                     * Si no se encuentran los indices acteriormente expuesto retornara un valor null.
                     */
                    /*Busqueda para la muestra de los detalles de los productos publicados*/
                    FindIterable<Document> documentos = collection.find();
                    /**
                     * @param FindIterable Busca una determinada coleccion, para los datos arrogados insertarlos en la tabla"
                     */
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
                        /**
                         * @param img Se lee la ruta almacenada cuando se exporto la imagen y se la hace visible en la columna final.
                         * Si ocurre un error, en la tabla se colocara una igen en blaco, un campo vacío.
                         * @param BufferedImage Indica que la imagen se podrá modificar.
                         */
                        /*Se añade los valores de las celdas de una fila, arreglo de objetos*/
                        modelo.addRow(new Object[]{codigo, nombre, cantidad, precio, imagenIcono});
                        /**
                         * Con "addRow" se añade una nueva fila al modelo de la tabla con los datos del producto especificados en el arreglo de objetos.
                         */
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
                    /**
                     * Si getSelectedTow es igual a -1 quiere decir que ninguna fila ha sido seleccionada, si sucede  se presenta en un JLabel un error
                     */
                } else {
                    errorTabla.setText("");
                    camposVacios.setText("");
                    try (MongoClient mongoClient = MongoClients.create("mongodb+srv://mireya:Nena1112004@cluster0.z9ytrsk.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")) {
                        MongoDatabase db = mongoClient.getDatabase("Proyectofinalpoo");
                        MongoCollection<Document> collection = db.getCollection("cadaProducto");
                        /**
                         * Obtener el valor de la celda antes de eliminar la fila.
                         *
                         * @param modelo El modelo de la tabla del cual se obtiene el valor.
                         * @param resultados La tabla de la cual se selecciona la fila.
                         * @return El código del producto que se va a eliminar.
                         */
                        /*Obtener el valor de la celda antes de eliminar la fila*/
                        int codigo = Integer.parseInt(modelo.getValueAt(resultados.getSelectedRow(), 0).toString());
                        Document filtro = new Document("Codigo", codigo);
                        /**
                         * Eliminar el documento de MongoDB.
                         *
                         * @param collection La colección de MongoDB de la cual se elimina el documento.
                         * @param filtro El filtro para identificar el documento a eliminar.
                         * @return El resultado de la operación de eliminación.
                         */
                        /* Eliminar el documento de MongoDB*/
                        DeleteResult resultado = collection.deleteOne(filtro);
                        /*Confirmación de cuantos documentos se han eliminado*/
                        System.out.println("Documentos borrados: " + resultado.getDeletedCount());

                        /*Eliminar la fila del modelo de la tabla*/
                        modelo.removeRow(resultados.getSelectedRow());
                        errorTabla.setText("Se ha eliminado correctamente");
                        /**
                         * Con getValueAt(resultados.getSelectedRow(), 0) se traen los datos de una columna determinada de una fila seleccionada
                         * El dato obtenido de la fila seleccionada se elimina de la tabla con "removeRow" y en la base de datos con "deleteOne".
                         */
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
                /*Limpieza de errores*/
                errorTabla.setText("");
                camposVacios.setText("");
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
                /**
                 * @param  editarProductoButton El bóton hace que la información publicada de la fila seleccionada en la tabla de productos se ponga a disponibilidad para ser modificada
                 * En los lugares donde el usuario ingresa la información se presentan los valores de la fila seleccionada propensa ha ser modificada.
                 */
            }
        });

        /*Actualizar los productos*/
        modificarProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /* Limpieza de errores */
                errorTabla.setText("");
                camposVacios.setText("");
                if (resultados.getSelectedRow() == -1) {
                    errorTabla.setText("No se ha seleccionado ningún producto");
                } else {
                    try (MongoClient mongoClient = MongoClients.create("mongodb+srv://mireya:Nena1112004@cluster0.z9ytrsk.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")) {
                        MongoDatabase database = mongoClient.getDatabase("Proyectofinalpoo");
                        MongoCollection<Document> collection = database.getCollection("cadaProducto");

                        /* Obtener el valor de la celda antes de modificar la fila */
                        int codigo = Integer.parseInt(codigoProducto.getText().trim());
                        String nombre = nombreProducto.getText().trim();
                        int cantidad = Integer.parseInt(stock.getText().trim());
                        Double precio = Double.parseDouble(precioProducto.getText().trim());

                        /* Buscar el producto en la base de datos */
                        Document filtro = new Document("Codigo", codigo);
                        Document producto = collection.find(filtro).first();

                        if (producto != null) {
                            /* Obtener la ruta de la imagen existente si no se proporciona una nueva */
                            String rutaImagen = producto.getString("Imagen");


                            /* Crear documento de actualización */
                            Document actualizacion = new Document("$set", new Document("Nombre_producto", nombre)
                                    .append("Cantidad_disponible", cantidad)
                                    .append("Precio", precio)
                                    .append("Imagen", rutaImagen));

                            UpdateResult resultado = collection.updateOne(filtro, actualizacion);

                            /* Verificar si el producto se modificó */
                            System.out.println("Documentos modificados: " + resultado.getModifiedCount());

                            /* Actualizar la visualización en la tabla */
                            modelo.setValueAt(codigo, resultados.getSelectedRow(), 0);
                            modelo.setValueAt(nombre, resultados.getSelectedRow(), 1);
                            modelo.setValueAt(cantidad, resultados.getSelectedRow(), 2);
                            modelo.setValueAt(precio, resultados.getSelectedRow(), 3);

                            /* Actualiza la imagen en la tabla */
                            try {
                                Image img = ImageIO.read(new File(rutaImagen));
                                if (img != null) {
                                    ImageIcon imageIcon = new ImageIcon(img.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                                    modelo.setValueAt(imageIcon, resultados.getSelectedRow(), 4);
                                } else {
                                    modelo.setValueAt(null, resultados.getSelectedRow(), 4);
                                }
                            } catch (IOException ex) {
                                modelo.setValueAt(null, resultados.getSelectedRow(), 4);
                            }
                        } else {
                            errorTabla.setText("El producto no se encontró en la base de datos");
                        }
                    } catch (Exception ex) {
                        errorTabla.setText("Error al actualizar el producto: " + ex.getMessage());
                    }
                }
            }
        });


        /*Borrar la información para colocar un nuevo producto*/
        borrarInformaciónDelFormularioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                errorTabla.setText("");
                camposVacios.setText("");
                codigoProducto.setText("");
                nombreProducto.setText("");
                stock.setText("");
                precioProducto.setText("");
                ruta.setText("");
                foto2.setIcon(null);
            }
        });
        regresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame();
                frame.pack();
                frame.setContentPane(new Login(frame).panelLogin);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(800, 600);
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
                frame1.setVisible(false);
            }
        });
    }
}

