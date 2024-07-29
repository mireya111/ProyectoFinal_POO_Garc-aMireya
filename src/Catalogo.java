import com.mongodb.client.*;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Catalogo {
    public JPanel catalogoProductos;
    private JTable catalogosProductos;
    private JTextField cantidadCliente;
    private JButton agregarCarrito;
    private JButton generarOrden;
    private JButton eliminarProducto;
    private JButton actualizarPedido;
    private JButton atrás;
    private JLabel erroresCatalogo;
    private JLabel confirmacionErrores;
    private JTable carritoProducto;
    private JLabel errorCarrito;
    private JLabel totalPagar;
    private JButton editarProducto;
    private JTable carrito;
    private double contadorPrecio = 0;

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

    public Catalogo() {
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
                com.itextpdf.text.Document pdfDocumento = new com.itextpdf.text.Document();
                try{
                    String ruta = System.getProperty("user.home");
                    PdfWriter.getInstance(pdfDocumento, new FileOutputStream(ruta + "/Desktop/Compra_.pdf"));
                    pdfDocumento.open();
                    PdfPTable table = new PdfPTable(4);


                } catch(){

                }
            }
        });
    }
}
