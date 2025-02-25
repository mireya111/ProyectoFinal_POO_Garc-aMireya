import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.*;
import java.util.List;

import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


public class Catalogo extends JFrame {
    public JPanel catalogoProductos;
    private JTable catalogosProductos;
    private JTextField cantidadCliente;
    private JButton agregarCarrito;
    private JButton generarOrden;
    private JButton eliminarProducto;
    private JButton actualizarPedido;
    private JButton atras;
    private JLabel erroresCatalogo;
    private JLabel confirmacionErrores;
    private JTable carritoProducto;
    private JLabel errorCarrito;
    private JLabel totalPagar;
    private JButton editarProducto;
    private JTable carrito;
    private double contadorPrecio = 0;
    private JFrame frame1;
    private int contadorPdfs = 0;
    private int contadorNumero = 0;

    DefaultTableModel modelo = new DefaultTableModel() {
        @Override
        public Class<?> getColumnClass(int column) {
            if (column == 0) {
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

    DefaultTableModel modeloDos = new DefaultTableModel() {
        @Override
        public Class<?> getColumnClass(int column) {
            if (column == 0) {
                return Integer.class;
            } else if (column == 1) {
                return String.class;
            } else if (column == 2) {
                return Integer.class;
            } else if (column == 3) {
                return Double.class;
            }
            return Object.class;
        }
    };

    public Catalogo(JFrame frame){
        frame1=frame;
        Productos catalogoProduct = new Productos();
        catalogoProduct.catalogo(modelo, catalogosProductos, erroresCatalogo);
        /**
         * @return catalogoProduct El catalogo completo de los productos publicados por el cliente.
         */
        /*Conformación de la tabla carrito*/
        modeloDos.addColumn("Codigo Producto");
        modeloDos.addColumn("Nombre del producto");
        modeloDos.addColumn("Cantidad ha comprar");
        modeloDos.addColumn("Precio del producto");
        carritoProducto.setModel(modeloDos);
        /**
         * @param modeloDos Modelo de la tabla carrito que se visualizará cuando el cliente agregue productos al carrito.
         * @param carritoProducto La tabla que tendrá los productos que comprará el cliente, servirá para la generación de la factura.
         */
        /*Para que la fila se agrande*/
        carritoProducto.setRowHeight(30);
        agregarCarrito.addActionListener(new ActionListener() {
            double contadorPrecio = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                /*Validacion de el campo que almacena la cantidad solicitante, no tiene que ser vacio*/
                if (cantidadCliente.getText().isEmpty()) {
                    confirmacionErrores.setText("El campo de cantidad no puede estar vacío.");
                    return;
                }
                /**
                 * @param cantidadCliente Numero de unidades que se desea el clietne por producto.
                 * @param confirmacionErrores Presenta al usuario los errores, si el campo esta vacio se depleja.
                 */

                /*Validacion para que el campo que almacena la cantidad solicitante no sea un numero negativo*/
                /*La validacion para que el campo solo reciba caracteres numericos*/
                int cantidadSolicitada;
                try {
                    cantidadSolicitada = Integer.parseInt(cantidadCliente.getText());
                    if (cantidadSolicitada <= 0) {
                        confirmacionErrores.setText("La cantidad debe ser un número positivo.");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    confirmacionErrores.setText("Ingrese un número válido para la cantidad.");
                    return;
                }
                /**
                 * @param cantidadSolicitada Almacena el valor entero que representa la cantidad de unidades solicitadas por el cliente.
                 * @param confirmacionErrores Presenta un mensaje de error en un JLabel.
                 */

                /*Validacion para que se seleccione un producto*/
                if (catalogosProductos.getSelectedRow() == -1) {
                    erroresCatalogo.setText("Se debe seleccionar un producto");
                    return;
                }
                /**
                 * @param catalogosProductos Tabla que posee los productos publicados por los empleados de la empresa de zaapatos.
                 */

                /*Obtencion de la información del producto*/
                int codigoDelProducto = Integer.parseInt(modelo.getValueAt(catalogosProductos.getSelectedRow(), 0).toString());
                String nombreDelProducto = modelo.getValueAt(catalogosProductos.getSelectedRow(), 1).toString();
                int cantidadDelProducto = Integer.parseInt(modelo.getValueAt(catalogosProductos.getSelectedRow(), 2).toString());
                Double precioDelProducto = Double.parseDouble(modelo.getValueAt(catalogosProductos.getSelectedRow(), 3).toString());
                /**
                 * @param catalogosProductos La tabla de productos desde la cual se trae la información del producto seleccionado.
                 * @param modelo El modelo de la tabla que contiene los datos de los productos.
                 * @return codigoDelProducto El código del producto extraído de la primera columna de la fila seleccionada.
                 * @return nombreDelProducto El nombre del producto extraído de la segunda columna de la fila seleccionada.
                 * @return cantidadDelProducto La cantidad del producto disponible extraída de la tercera columna de la fila seleccionada.
                 * @return precioDelProducto El precio del producto extraído de la cuarta columna de la fila seleccionada.
                 */

                /*Validacion para que no se exceda el valor del estock*/
                if (cantidadSolicitada > cantidadDelProducto) {
                    confirmacionErrores.setText("La cantidad solicitada excede la cantidad disponible.");
                    return;
                }

                /*Calculo del precio total por cada producto, creacion y seteo de objetos*/
                Compra compraNueva = new Compra();
                compraNueva.setCantidadProducto(cantidadSolicitada);
                Double precioPorCantidad = compraNueva.getCantidadProducto() * precioDelProducto;
                /**
                 * @param precioPorCantidad La cantidad a pagar por cada producto, se multipla la cantidad que desea el cliente por el precio del producto.
                 * @param compraNueva Objeto al que se le setea la cantidad solicitada por el cliente.
                 */

                /*Calculo para la disminución de la cantidad del producto*/
                int cantidadDisminuye = cantidadDelProducto - cantidadSolicitada;
                /**
                 * @param cantidadDisminuye Es la cantidad disponible que se mostrara al público cuando se generen compras, es decir,
                 *                          cantidad disponible actual menos la cantidad que desea el cliente.
                 */
                /*Linea de conexion*/
                try (MongoClient mongoClient = MongoClients.create("mongodb+srv://mireya:Nena1112004@cluster0.z9ytrsk.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")) {
                    MongoDatabase db = mongoClient.getDatabase("Proyectofinalpoo");
                    MongoCollection<Document> collection = db.getCollection("cadaProducto");

                    /*A qué registro*/
                    Document filtro = new Document("Codigo", codigoDelProducto);
                    /**
                     * @param filtro Documento que se utiliza para buscar el registro del producto que se actualizará.
                     *               Filtra el registro basado en el codigo del producto.
                     */

                    /*Qué parte del registro se cambiará*/
                    Document actualizacion = new Document("$set", new Document("Cantidad_disponible", cantidadDisminuye));
                    /**
                     * @param actualizacion Documento que define la actualización del campo 'Cantidad_disponible' en el registro.
                     *                      Actualiza la cantidad disponible del producto en la base de datos.
                     */
                    UpdateResult resultado = collection.updateOne(filtro, actualizacion);
                    /**
                     * @param resultado Resultado de la operación de actualización.
                     */
                    /*Verificar si el producto se modificó, no se agrega en la interfaz gráfica solo en el intelligent*/
                    System.out.println("Documentos modificados: " + resultado.getModifiedCount());

                    /*Visualizacion de la tabla*/
                    modelo.setValueAt(cantidadDisminuye, catalogosProductos.getSelectedRow(), 2);
                    confirmacionErrores.setText("Se ha agregado al carrito el producto " + nombreDelProducto);
                    /**
                     * @param modelo Actualiza la cantidad disponible del producto en la fila seleccionada de la tabla.
                     */
                    /*Seteo de valores*/
                    compraNueva.setCantidadProducto(cantidadSolicitada);
                    compraNueva.setPrecioTotal(precioPorCantidad);
                    contadorPrecio += precioPorCantidad;
                    totalPagar.setText(String.format("%.2f", contadorPrecio));
                    /**
                     * @param modeloDos Anade una nueva fila con los detalles del producto al carrito en la interfaz gráfica.
                     */
                    /*Colocación de valores en la tabla carrito*/
                    modeloDos.addRow(new Object[]{codigoDelProducto, nombreDelProducto, cantidadSolicitada, compraNueva.getPrecioTotal()});
                } catch (Exception ex) {
                    confirmacionErrores.setText("Error al añadir producto al carrito: " + ex.getMessage());
                    /**
                     * @param confirmacionErrores Presenta al cliente un error al agregar un produto al carrito
                     */
                }

            }
        });

        editarProducto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(carritoProducto.getSelectedRow() == -1){
                    confirmacionErrores.setText("No se encuentra selecccionado ningun producto, por favor seleccione uno.");
                }else {
                    cantidadCliente.setText(modeloDos.getValueAt(carritoProducto.getSelectedRow(), 2).toString());
                }
            }
            /**
             * @param  editarProducto El bóton hace que la cantidad solicitada por el cliente en la tabla carrito se ponga a disponibilidad para ser modificada
             *                        En el lugar donde el usuario ingresa la cantidad que desea se presentan los valores de la fila seleccionada propensa ha ser modificada.
             */
        });

        actualizarPedido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*Creacion de objetos y setearlos*/
                Compra compraNueva = new Compra();
                compraNueva.setCantidadProducto(Integer.parseInt(cantidadCliente.getText()));
                /**
                 * @param compraNueva objeto que guarda la cantidad del producto actual.
                 */

                /*Validacion de la seleccion de un producto*/
                if (carritoProducto.getSelectedRow() == -1) {
                    confirmacionErrores.setText("No se encuentra seleccionado ningún producto, por favor seleccione uno.");
                } else {
                    /*Obtener informacion del catalogo para validarla*/
                    int cantidadDelProducto = Integer.parseInt(modelo.getValueAt(catalogosProductos.getSelectedRow(), 2).toString());

                    /*Validacion del campo que almacena la cantidad solicitada, no debe estar vacío*/
                    if (cantidadCliente.getText().isEmpty()) {
                        confirmacionErrores.setText("El campo de cantidad no puede estar vacío.");
                        return;
                    }
                    /**
                     * @param confirmacionErrores si se encuentra que el campo donde se ingresa la cantidad se encuentra vacio se coloca en un JLabel el error.
                     */

                    /*Validacion para que el campo que almacena la cantidad solicitada no sea un número negativo*/
                    /*Validacion para que el campo solo reciba caracteres numéricos*/
                    int cantidadSolicitada;
                    try {
                        cantidadSolicitada = Integer.parseInt(cantidadCliente.getText());
                        if (cantidadSolicitada <= 0) {
                            confirmacionErrores.setText("La cantidad debe ser un número positivo.");
                            return;
                        }
                    } catch (NumberFormatException ex) {
                        confirmacionErrores.setText("Ingrese un número válido, este campo no acepta caracteres.");
                        return;
                    }
                    /**
                     * @param  cantidadSolicitada Almacena el valor entero que representa la cantidad de unidades solicitadas por el cliente.
                     * @param confirmacionErrores Almacena el error si lo ingresado por el usuario es un caracter no numerico
                     */

                    /*Validacion para que no se exceda el valor del estock*/
                    if (cantidadSolicitada > cantidadDelProducto) {
                        confirmacionErrores.setText("La cantidad solicitada excede la cantidad disponible.");
                        return;
                    }

                    /*Obtener la información del producto seleccionado en la tabla carritoProducto*/
                    int codigoDelProductoCarrito = Integer.parseInt(modeloDos.getValueAt(carritoProducto.getSelectedRow(), 0).toString());
                    String nombreDelProducto = modeloDos.getValueAt(carritoProducto.getSelectedRow(), 1).toString();
                    int cantidadAnteriorCarrito = Integer.parseInt(modeloDos.getValueAt(carritoProducto.getSelectedRow(), 2).toString());
                    /**
                     * @return codigoDelProductoCarrito Codigo del producto escogido por el cliente y visualizado en la tabla "carritoProducto (en esta tabla se selecciona el producto)"
                     * @return nombreDelProducto Nombre del producto seleccionado por el cliente y visualizado en la tabla "carritoProducto".
                     * @return cantidadAnteriorCarrito Cantidad del producto que estaba previamente en el carrito del cliente.
                     */

                    /*Linea de conexion*/
                    try (MongoClient mongoClient = MongoClients.create("mongodb+srv://mireya:Nena1112004@cluster0.z9ytrsk.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")) {
                        MongoDatabase database = mongoClient.getDatabase("Proyectofinalpoo");
                        MongoCollection<Document> collection = database.getCollection("cadaProducto");

                        /* Realiza las consultas */
                        FindIterable<Document> documento1 = collection.find();
                        /**
                         * @param documento1 Coleccion "cadaProducto", aquí se encuentran los productos publicados por el empleado.
                         */
                        /*Verificacion de que si existe un registro en la base de datos con datos iguales a los que se pretende actualizar en la tabla carritoProducto*/
                        boolean productoEncontrado = false;
                        for (Document documento : documento1) {
                            Integer codigo = documento.getInteger("Codigo");
                            String nombre = documento.getString("Nombre_producto");
                            /**
                             * @param codigo Codigo del producto publicado por el empleado.
                             * @param nombre Nombre del producto publicado por el empleado.
                             */
                            if (codigo != null && nombre != null && codigo.equals(codigoDelProductoCarrito) && nombre.equals(nombreDelProducto)) {
                                productoEncontrado = true;
                                int cantidadAnteriorCatalogo = documento.getInteger("Cantidad_disponible");
                                int cantidadAntetior = cantidadAnteriorCatalogo + cantidadAnteriorCarrito;
                                int cantidadNuevaProducto = cantidadAntetior - compraNueva.getCantidadProducto();
                                /**
                                 * @param productoEncontrado Variable que demuestra la presencia del producto en la base de datos.
                                 * @param cantidadAnteriorCatalogo Cantidad disponible actualmente del producto, cantidad que se disminuyo cuando se agrego el producto al carrito.
                                 * @param cantidadAnterior Suma de la cantidad disminuida y la cantidad solicitada, dando comol resultado la cantidad disponible anterior a la agregación del carrito.
                                 * @param cantidadNuevaProducto Resta de la cantidad disponible anterior a la agregación del carrito y la cantidad nueva solicitada por el cliente.
                                 */
                                /*Nuevo precio del producto*/
                                Double precioPorProducto = documento.getDouble("Precio");
                                Double precioNuevoPorProducto = compraNueva.getCantidadProducto() * precioPorProducto;
                                /**
                                 * @param precioPorProducto Precio encontrado en la base de datos.
                                 * @param precioNuevoPorProducto Precio total deacuerdo a la cantidad deseada por producto seleccionado.
                                 */

                                System.out.println("Precio nuevo del producto: " + precioNuevoPorProducto);

                                /*Actualizacion en la base de datos*/
                                Document filtro = new Document("Codigo", codigoDelProductoCarrito);
                                Document actualizacion = new Document("$set", new Document("Cantidad_disponible", cantidadNuevaProducto));
                                UpdateResult resultado = collection.updateOne(filtro, actualizacion);
                                /**
                                 * @param filtro Identifica el documento a eliminar.
                                 * @param actualizacion Que parametros del docuemento se actualizará.
                                 * @return resultado El resultado de la operación de la actualización.
                                 */

                                /*Verificar si el producto se modifico*/
                                System.out.println("Documentos modificados: " + resultado.getModifiedCount());

                                /*Insercion de los nuevos datos en las tablas*/
                                modelo.setValueAt(cantidadNuevaProducto, catalogosProductos.getSelectedRow(), 2);
                                modeloDos.setValueAt(compraNueva.getCantidadProducto(), carritoProducto.getSelectedRow(), 2);
                                modeloDos.setValueAt(precioNuevoPorProducto, carritoProducto.getSelectedRow(), 3);
                                confirmacionErrores.setText("Se ha modificado el carrito en el producto " + nombreDelProducto);

                                /*Recalcular el total a pagar*/
                                double totalPagarValor = 0.0;
                                for (int i = 0; i < modeloDos.getRowCount(); i++) {
                                    totalPagarValor += Double.parseDouble (modeloDos.getValueAt(i, 3).toString());
                                }

                                /*Actualizar el JLabel totalPagar*/
                                String totalPagarFormateado = String.format("%.2f", totalPagarValor).replace(".", ",");
                                totalPagar.setText(totalPagarFormateado);

                                /**
                                 * @return La tabla con las modificaciones realizadas, tanto en la tabla del catalogo como la del carrito.
                                 */
                                break;
                            }
                        }
                        if (!productoEncontrado) {
                            confirmacionErrores.setText("El producto seleccionado en el carrito no se encuentra en la base de datos.");
                        }
                    } catch (Exception ex) {
                        confirmacionErrores.setText("La actualización del producto seleccionado no fue correcta: " + ex.getMessage());
                    }
                }
            }
        });

        eliminarProducto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (carritoProducto.getSelectedRow() == -1) {
                    errorCarrito.setText("Para eliminar necesita seleccionar un producto del carrito.");
                } else {
                    /*Obtener la información del producto seleccionado en la tabla carritoProducto*/
                    int codigoDelProductoCarrito = Integer.parseInt(modeloDos.getValueAt(carritoProducto.getSelectedRow(), 0).toString());
                    String nombreDelProducto = modeloDos.getValueAt(carritoProducto.getSelectedRow(), 1).toString();
                    int cantidadAnteriorCarrito = Integer.parseInt(modeloDos.getValueAt(carritoProducto.getSelectedRow(), 2).toString());
                    double precioEliminar = Double.parseDouble(modeloDos.getValueAt(carritoProducto.getSelectedRow(), 3).toString().replace(",", "."));

                    /*Línea de conexión*/
                    try (MongoClient mongoClient = MongoClients.create("mongodb+srv://mireya:Nena1112004@cluster0.z9ytrsk.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")) {
                        MongoDatabase database = mongoClient.getDatabase("Proyectofinalpoo");
                        MongoCollection<Document> collection = database.getCollection("cadaProducto");

                        /*Realiza las consultas*/
                        Document filtro = new Document("Codigo", codigoDelProductoCarrito);
                        Document documento = collection.find(filtro).first();

                        if (documento != null) {
                            String nombre = documento.getString("Nombre_producto");

                            if (nombre != null && nombre.equals(nombreDelProducto)) {
                                /*Cantidad anterior del producto*/
                                int cantidadAnteriorCatalogo = documento.getInteger("Cantidad_disponible");
                                int cantidadAnterior = cantidadAnteriorCatalogo + cantidadAnteriorCarrito;

                                /*Actualización en la base de datos*/
                                Document actualizacion = new Document("$set", new Document("Cantidad_disponible", cantidadAnterior));
                                UpdateResult resultado = collection.updateOne(filtro, actualizacion);
                                // Verificar si el producto se modificó
                                System.out.println("Documentos modificados: " + resultado.getModifiedCount());

                                /*Establecer precios*/
                                double precioAnterior = Double.parseDouble(totalPagar.getText().replace(",", "."));
                                double precioTotal = precioAnterior - precioEliminar;
                                /*Formatear el precio total con dos decimales*/
                                String precioFormateado = String.format("%.2f", precioTotal).replace(".", ",");
                                totalPagar.setText(precioFormateado);

                                /*Eliminar la fila de la tabla*/
                                modelo.setValueAt(cantidadAnterior, catalogosProductos.getSelectedRow(), 2);
                                modeloDos.removeRow(carritoProducto.getSelectedRow());
                                errorCarrito.setText("Se ha eliminado correctamente");
                            } else {
                                confirmacionErrores.setText("El nombre del producto no coincide.");
                            }
                        } else {
                            confirmacionErrores.setText("El producto no se encontró en la base de datos.");
                        }
                    } catch (MongoException ex) {
                        confirmacionErrores.setText("Error con la base de datos: " + ex.getMessage());
                    } catch (Exception ex) {
                        confirmacionErrores.setText("La actualización del producto seleccionado no fue correcta: " + ex.getMessage());
                    }
                }
            }
        });
        /*Solo se coloque la fecha, no la hora ni la zona horaria*/
        Date fecha = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        int dia = cal.get(Calendar.DAY_OF_MONTH);
        int mes = cal.get(Calendar.MONTH) + 1;
        int anio = cal.get(Calendar.YEAR);
        String solofecha = dia + "/" + mes + "/" + anio;
        /*Contador para diferentes pdfs*/
        contadorPdfs =contadorPdfs + 1;
        /**
         * @param contadorPdfs Para que se generen varios pdfs, pdf_1, pdf_2, pdf_3 y así sucesivamente.
         */
        generarOrden.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*Para la generación del pdf*/
                com.itextpdf.text.Document pdfDocumento = new com.itextpdf.text.Document();

                /*Paera el numero del pedido*/
                contadorNumero = contadorNumero + 1;

                /*Crear un objeto y setear los valores*/
                Compra detalleCompra = new Compra();
                detalleCompra.setFecha(solofecha);
                detalleCompra.setNumero_pedido(contadorNumero);

                detalleCompra.setNumero_pedido(contadorPdfs);
                try (MongoClient mongoClient = MongoClients.create("mongodb+srv://mireya:Nena1112004@cluster0.z9ytrsk.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")){
                    MongoDatabase database = mongoClient.getDatabase("Proyectofinalpoo");
                    MongoCollection<Document> collection = database.getCollection("detalleCompra");
                    /*Insertar el numero del pedido y la fecha del pedido*/
                    Document detalleDocumento = new Document("Numero_pedido", detalleCompra.getNumero_pedido())
                            .append("Fecha_pedido", detalleCompra.getFecha());
                    /**
                     * @param detalleDocumento Información de cada compra realizada por el cliente.
                     *                         Contiene los siguientes campos:
                     *                         - "Numero_pedido": Número de identificación del pedido.
                     *                         - "Fecha_pedido": Fecha en que se realizó el pedido.
                     *                         - "Productos": Todos los productos seleccionados por el cliente, con el nombre, la cantidad y el precio por producto.
                     */

                    /*Se almacenara una lista en la base de datos para el detalle de los productos, por lo tanto, se crea la misma*/
                    List<Document> productos = new ArrayList<>();
                    /**
                     * @param productos Arreglo donde se insertaran todos los productos seleccionados por el cliente
                     */

                    /*Agregar lo escogido por el cliente a la lista productos*/
                    for (int i = 0; i < carritoProducto.getRowCount(); i++) {

                        /*Crear un documento para cada producto*/
                        Document producto = new Document("Nombre del producto", modeloDos.getValueAt(i, 1))
                                .append("Cantidad a comprar", modeloDos.getValueAt(i, 2))
                                .append("Precio del producto", modeloDos.getValueAt(i, 3));
                        /*Agregar el documento que contiene el detalle de los productos a la lista*/
                        productos.add(producto);
                        /**
                         * @action Agrega el documento con los detalles del producto a la lista de productos para su procesamiento posterior.
                         * @return for Cada iteración del bucle corresponde a una fila en la tabla del carrito,
                         * representando un producto distinto seleccionado por el cliente.
                         */
                    }

                    /*Generar un campo "Productos" que tendra la lista dentro de la base de datos*/
                    detalleDocumento.append("Productos", productos);

                    /*Insertar el documento completo en la colección*/
                    collection.insertOne(detalleDocumento);
                    /**
                     * @param insertOne Se inserta el docuemento completo dentro de la coleccion "detalleCompra".
                     */

                    /*Confirmación de la insercion en la base de datos*/
                    System.out.println("Documento insertado: " + detalleDocumento);

                    /*Obtener la ruta del escritorio*/
                    String ruta = System.getProperty("user.home");
                    String directorio = ruta + File.separator + "Escritorio";
                    /**
                     * @param ruta Obtiene la ruta al directorio de inicio del usuario..
                     * @param directorio Se concatena File.separator + "Escritorio" para formar la ruta al escritorio del usuario.
                     */

                    /*Crear el archivo PDF*/
                    File archivoPDF = new File(directorio + File.separator + "Factura_"+detalleCompra.getNumero_pedido()+".pdf");
                    PdfWriter.getInstance(pdfDocumento, new FileOutputStream(archivoPDF));
                    /**
                     * @param archivoPDF Nuevo documento en formato pdf y con el nombre "Factura_" y el numero que arroge el contadorpdfs
                     *                   PdfWriter.getInstance(pdfDocumento, new FileOutputStream(archivoPDF)) Esta línea permite que se escriba dentro del docuemento.
                     */
                    /*Cargar una fuente en negrilla*/

                    // Crear una fuente en negrilla
                    com.itextpdf.text.Font font1 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
                    /**
                     * @param font1 Fuente, tamaño y la oportunidad de que el texto se encuentre en negrilla
                     */
                    /*Se abre el documento*/
                    pdfDocumento.open();

                    Paragraph textoInformacion = new Paragraph();
                    /**
                     * @param textoInformacion Es una especie de parrafo que se va llenando con textos que tendrá la factura
                     */
                    Paragraph textoInformacion2 = new Paragraph();
                    /**
                     * @param textoInformacion2 Es una especie de parrafo que se va llenando con textos que tendrá la factura
                     */

                    textoInformacion2.add(new Paragraph("Zapato Mart", font1));
                    textoInformacion2.add(new Paragraph("Fecha: ", font1));
                    textoInformacion2.add(detalleCompra.getFecha() + '\n');
                    textoInformacion2.add(new Paragraph("Nombre: ", font1));
                    textoInformacion2.add(Login.ClientesDatos.getNombreCliente() + '\n');
                    textoInformacion2.add(new Paragraph("Apellido: ", font1));
                    textoInformacion2.add(Login.ClientesDatos.getApellidoCliente() + '\n');
                    textoInformacion2.add(new Paragraph("Correo: ", font1));
                    textoInformacion2.add(Login.ClientesDatos.getEmailCliente() + '\n');
                    textoInformacion2.add(new Paragraph("Numero de la compra: ", font1));
                    textoInformacion2.add(String.valueOf(detalleCompra.getNumero_pedido() + '\n'));
                    pdfDocumento.add(textoInformacion2);

                    PdfPTable table = new PdfPTable(3);
                    table.addCell("Nombre del producto");
                    table.addCell("Cantidad del producto");
                    table.addCell("Precio del producto");
                    /**
                     * @param table Tabla que tendrá cada producto que selecciono el cliente con nombre del producto, la cantidad del producto
                     *              y su precio.
                     */

                    for (int i = 0; i < carritoProducto.getRowCount(); i++) {
                        table.addCell(modeloDos.getValueAt(i, 1).toString());
                        table.addCell(modeloDos.getValueAt(i, 2).toString());
                        table.addCell(modeloDos.getValueAt(i, 3).toString());
                    }
                    String totalAPagar = totalPagar.getText();
                    /**
                     * @param totalAPagar Total a pagar por todos los productos.
                     */
                    textoInformacion.add(new Paragraph("Total: ", font1));
                    textoInformacion.add(totalAPagar);
                    pdfDocumento.add(table);
                    pdfDocumento.add(textoInformacion);
                    /**
                     * @param pdfDocumento Al documento se le añade la tabla y los textos creados,solo se le puede agregar elementos no variables.
                     */
                    pdfDocumento.close();
                    errorCarrito.setText("Se ha generado adecuadamente el pdf de la compra");
                    System.out.println("PDF creado en la ruta: " + archivoPDF.getAbsolutePath());

                }catch(FileNotFoundException | DocumentException exception){
                    errorCarrito.setText("Error al generar el PDF" + exception.getMessage());
                    /**
                     * @param errorCarrito Si el documento no se crea o presenta algun error se presenta el mismo en el JLabel.
                     */
                }catch(MongoException exception){
                    errorCarrito.setText("Error al guardar datos en la base de datos");
                    /**
                     * @param errorCarrito Si el documento no genera adecuadamente la conexión.
                     */
                }
            }
        });

        atras.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame();
                frame.setContentPane(new Login(frame).panelLogin);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(700, 400);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame1.setVisible(false);
            }
        });
    }
}
