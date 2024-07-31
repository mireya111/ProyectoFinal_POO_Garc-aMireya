import com.itextpdf.text.Paragraph;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.DocumentException;
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
        /*Conformación de la tabla carrito*/
        modeloDos.addColumn("Codigo Producto");
        modeloDos.addColumn("Nombre del producto");
        modeloDos.addColumn("Cantidad ha comprar");
        modeloDos.addColumn("Precio del producto");
        carritoProducto.setModel(modeloDos);

        agregarCarrito.addActionListener(new ActionListener() {
            double contadorPrecio = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                /*Validacion de el campo que almacena la cantidad solicitante, no tiene que ser vacio*/
                if (cantidadCliente.getText().isEmpty()) {
                    confirmacionErrores.setText("El campo de cantidad no puede estar vacío.");
                    return;
                }

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

                /*Validacion para que se seleccione un producto*/
                if (catalogosProductos.getSelectedRow() == -1) {
                    erroresCatalogo.setText("Se debe seleccionar un producto");
                    return;
                }

                /*Obtencion de la información del producto*/
                int codigoDelProducto = Integer.parseInt(modelo.getValueAt(catalogosProductos.getSelectedRow(), 0).toString());
                String nombreDelProducto = modelo.getValueAt(catalogosProductos.getSelectedRow(), 1).toString();
                int cantidadDelProducto = Integer.parseInt(modelo.getValueAt(catalogosProductos.getSelectedRow(), 2).toString());
                Double precioDelProducto = Double.parseDouble(modelo.getValueAt(catalogosProductos.getSelectedRow(), 3).toString());

                /*Validacion para que no se exceda el valor del estock*/
                if (cantidadSolicitada > cantidadDelProducto) {
                    confirmacionErrores.setText("La cantidad solicitada excede la cantidad disponible.");
                    return;
                }

                /*Calculo del precio total por cada producto, creacion y seteo de objetos*/
                Compra compraNueva = new Compra();
                compraNueva.setCantidadProducto(cantidadSolicitada);
                Double precioPorCantidad = compraNueva.getCantidadProducto() * precioDelProducto;

                /*Calculo para la disminución de la cantidad del producto*/
                int cantidadDisminuye = cantidadDelProducto - cantidadSolicitada;

                /*Linea de conexion*/
                try (MongoClient mongoClient = MongoClients.create("mongodb+srv://mireya:Nena1112004@cluster0.z9ytrsk.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")) {
                    MongoDatabase db = mongoClient.getDatabase("Proyectofinalpoo");
                    MongoCollection<Document> collection = db.getCollection("cadaProducto");

                    /*A qué registro*/
                    Document filtro = new Document("Codigo", codigoDelProducto);

                    /*Qué parte del registro se cambiará*/
                    Document actualizacion = new Document("$set", new Document("Cantidad_disponible", cantidadDisminuye));
                    UpdateResult resultado = collection.updateOne(filtro, actualizacion);

                    /*Verificar si el producto se modificó, no se agrega en la interfaz gráfica solo en el intelligent*/
                    System.out.println("Documentos modificados: " + resultado.getModifiedCount());

                    /*Visualizacion de la tabla*/
                    modelo.setValueAt(cantidadDisminuye, catalogosProductos.getSelectedRow(), 2);
                    confirmacionErrores.setText("Se ha agregado al carrito el producto " + nombreDelProducto);

                    /*Seteo de valores*/
                    compraNueva.setCantidadProducto(cantidadSolicitada);
                    compraNueva.setPrecioTotal(precioPorCantidad);
                    contadorPrecio += precioPorCantidad;
                    totalPagar.setText(String.valueOf(contadorPrecio));

                    /*Colocación de valores en la tabla carrito*/
                    modeloDos.addRow(new Object[]{codigoDelProducto, nombreDelProducto, cantidadSolicitada, compraNueva.getPrecioTotal()});
                } catch (Exception ex) {
                    confirmacionErrores.setText("Error al añadir producto al carrito: " + ex.getMessage());
                }

            }
        });
        editarProducto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(carritoProducto.getSelectedRow() == -1){
                    confirmacionErrores.setText("No se encuentra selecccionado ningun producto, por favor seleccione uno");
                }else {
                    cantidadCliente.setText(modeloDos.getValueAt(carritoProducto.getSelectedRow(), 2).toString());
                }
            }
        });
        actualizarPedido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*Creacion de objetos y setearlos*/
                Compra compraNueva = new Compra();
                compraNueva.setCantidadProducto(Integer.parseInt(cantidadCliente.getText()));

                /*Validacion de la seleccion de un producto*/
                if (carritoProducto.getSelectedRow() == -1) {
                    confirmacionErrores.setText("No se encuentra seleccionado ningún producto, por favor seleccione uno.");
                } else {
                    /*Validacion del campo que almacena la cantidad solicitada, no debe estar vacío*/
                    if (cantidadCliente.getText().isEmpty()) {
                        confirmacionErrores.setText("El campo de cantidad no puede estar vacío.");
                        return;
                    }

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

                    /*Obtener la información del producto seleccionado en la tabla carritoProducto*/
                    int codigoDelProductoCarrito = Integer.parseInt(modeloDos.getValueAt(carritoProducto.getSelectedRow(), 0).toString());
                    String nombreDelProducto = modeloDos.getValueAt(carritoProducto.getSelectedRow(), 1).toString();
                    int cantidadAnteriorCarrito = Integer.parseInt(modeloDos.getValueAt(carritoProducto.getSelectedRow(), 2).toString());

                    /*Linea de conexion*/
                    try (MongoClient mongoClient = MongoClients.create("mongodb+srv://mireya:Nena1112004@cluster0.z9ytrsk.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")) {
                        MongoDatabase database = mongoClient.getDatabase("Proyectofinalpoo");
                        MongoCollection<Document> collection = database.getCollection("cadaProducto");

                        /* Realiza las consultas */
                        FindIterable<Document> documento1 = collection.find();

                        /*Verificacion de que si existe un registro en la base de datos con datos iguales a los que se pretende actualizar en la tabla carritoProducto*/
                        boolean productoEncontrado = false;
                        for (Document documento : documento1) {
                            Integer codigo = documento.getInteger("Codigo");
                            String nombre = documento.getString("Nombre_producto");

                            if (codigo != null && nombre != null && codigo.equals(codigoDelProductoCarrito) && nombre.equals(nombreDelProducto)) {
                                productoEncontrado = true;
                                int cantidadAnteriorCatalogo = documento.getInteger("Cantidad_disponible");
                                int cantidadAntetior = cantidadAnteriorCatalogo + cantidadAnteriorCarrito;
                                int cantidadNuevaProducto = cantidadAntetior - compraNueva.getCantidadProducto();

                                /*Nuevo precio del producto*/
                                Double precioPorProducto = documento.getDouble("Precio");
                                Double precioNuevoPorProducto = compraNueva.getCantidadProducto() * precioPorProducto;

                                System.out.println("Precio nuevo del producto: "+ precioNuevoPorProducto);

                                /*Actualizacion en la base de datos*/
                                Document filtro = new Document("Codigo", codigoDelProductoCarrito);
                                Document actualizacion = new Document("$set", new Document("Cantidad_disponible", cantidadNuevaProducto));
                                UpdateResult resultado = collection.updateOne(filtro, actualizacion);

                                /*Verificar si el producto se modifico*/
                                System.out.println("Documentos modificados: " + resultado.getModifiedCount());

                                /*Insercion de los nuevos datos en las tablas*/
                                modelo.setValueAt(cantidadNuevaProducto, catalogosProductos.getSelectedRow(), 2);
                                modeloDos.setValueAt(compraNueva.getCantidadProducto(), carritoProducto.getSelectedRow(), 2);
                                modeloDos.setValueAt(precioNuevoPorProducto, carritoProducto.getSelectedRow(), 3);
                                confirmacionErrores.setText("Se ha modificado el carrito en el producto " + nombreDelProducto);
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
                if(carritoProducto.getSelectedRow()==-1){
                    errorCarrito.setText("Para eliminar necesita seleccionar un producto del carrito");
                }else{
                    /*Eliminar la fila de la tabla*/
                    modeloDos.removeRow(carritoProducto.getSelectedRow());
                    errorCarrito.setText("Se ha eliminado correctamente");
                }
            }
        });
        generarOrden.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*Para la generación del pdf*/
                com.itextpdf.text.Document pdfDocumento = new com.itextpdf.text.Document();
                /*Crear un objeto y setear los valores*/
                Compra detalleCompra = new Compra();
                /*El pedido debe tener un numero que lo identifique, este es */
                int numeroLimite = 1;
                detalleCompra.setNumero_pedido(numeroLimite);
                try (MongoClient mongoClient = MongoClients.create("mongodb+srv://mireya:Nena1112004@cluster0.z9ytrsk.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")){
                    MongoDatabase database = mongoClient.getDatabase("Proyectofinalpoo");
                    MongoCollection<Document> collection = database.getCollection("detalleCompra");
                    /*Insertar el numero del pedido y la fecha del pedido*/
                    Document detalleDocumento = new Document("Numero_pedido", detalleCompra.getNumero_pedido())
                            .append("Fecha_pedido", detalleCompra.getFecha());

                    /*Se almacenara una lista en la base de datos para el detalle de los productos, por lo tanto, se crea la misma*/
                    List<Document> productos = new ArrayList<>();

                    /*Agregar lo escogido por el cliente a la lista productos*/
                    for (int i = 0; i < carritoProducto.getRowCount(); i++) {

                        /*Crear un documento para cada producto*/
                        Document producto = new Document("Nombre del producto", modeloDos.getValueAt(i, 1))
                                .append("Cantidad a comprar", modeloDos.getValueAt(i, 2))
                                .append("Precio del producto", modeloDos.getValueAt(i, 3));
                        /*Agregar el documento que contiene el detalle de los productos a la lista*/
                        productos.add(producto);
                    }

                    /*Generar un campo "Productos" que tendra la lista dentro de la base de datos*/
                    detalleDocumento.append("Productos", productos);

                    /*Insertar el documento completo en la colección*/
                    collection.insertOne(detalleDocumento);

                    /*Confirmación de la insercion en la base de datos*/
                    System.out.println("Documento insertado: " + detalleDocumento);

                    /*Obtener la ruta del escritorio*/
                    String ruta = System.getProperty("user.home");
                    String directorio = ruta + File.separator + "Escritorio";

                    /*Crear el archivo PDF*/
                    File archivoPDF = new File(directorio + File.separator + "Factura_0"+detalleCompra.getNumero_pedido()+".pdf");
                    PdfWriter.getInstance(pdfDocumento, new FileOutputStream(archivoPDF));

                    pdfDocumento.open();
                    Paragraph textoInformacion = new Paragraph();
                    textoInformacion.add("Zapatos Lombardy");
                    textoInformacion.add("Cedula: "+ Login.ClientesDatos.getCedulaCliente());
                    textoInformacion.add("Nombre: "+ Login.ClientesDatos.getNombreCliente());
                    textoInformacion.add("Apellido: "+ Login.ClientesDatos.getApellidoCliente());
                    textoInformacion.add("Correo: "+ Login.ClientesDatos.getEmailCliente());
                    textoInformacion.add("Numero de la compra: "+detalleCompra.getNumero_pedido());
                    PdfPTable table = new PdfPTable(3);
                    table.addCell("Nombre del producto");
                    table.addCell("Cantidad del producto");
                    table.addCell("Precio del producto");

                    for (int i = 0; i < carritoProducto.getRowCount(); i++) {
                        table.addCell(modeloDos.getValueAt(i, 1).toString());
                        table.addCell(modeloDos.getValueAt(i, 2).toString());
                        table.addCell(modeloDos.getValueAt(i, 3).toString());
                    }
                    String totalAPagar = totalPagar.getText();
                    textoInformacion.add("Total: " + totalAPagar);
                    pdfDocumento.add(table);
                    pdfDocumento.add(textoInformacion);
                    pdfDocumento.close();
                    errorCarrito.setText("Se ha generado adecuadamente el pdf de la compra");
                    System.out.println("PDF creado en la ruta: " + archivoPDF.getAbsolutePath());

                }catch(FileNotFoundException | DocumentException exception){
                    errorCarrito.setText("Error al generar el PDF" + exception.getMessage());
                }catch(MongoException exception){
                    errorCarrito.setText("Error al guardar datos en la base de datos");
                }
            }
        });
        atras.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame();
                frame.setContentPane(new Login(frame).panelLogin);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(800, 600);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame1.setVisible(false);
            }
        });
    }
}
