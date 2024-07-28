import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Catalogo {
    public JPanel catalogoProductos;
    private JTable catalogosProductos;
    private JTextField cantidadCliente;
    private JButton agregarCarrito;
    private JButton generarOrden;
    private JButton realizarPedido;
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
                /*Validacion de la selecccion de un producto*/
                if(carritoProducto.getSelectedRow() == -1){
                    confirmacionErrores.setText("No se encuentra selecccionado ningun producto, por favor seleccione uno");
                }else{
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

                    /*Obtener el valor de la celda antes de modificar la fila de la tabla "modelo"*/
                    int codigoDelProducto = Integer.parseInt(modelo.getValueAt(catalogosProductos.getSelectedRow(), 0).toString());
                    String nombreDelProducto = modelo.getValueAt(catalogosProductos.getSelectedRow(), 1).toString();
                    int cantidadDelProducto = Integer.parseInt(modelo.getValueAt(catalogosProductos.getSelectedRow(), 2).toString());

                    /*Obtener el valor de la celda antes de modificar la fila de la tabla "modeloDos"*/
                    int codigoDelProducto2 = Integer.parseInt(modeloDos.getValueAt(catalogosProductos.getSelectedRow(), 0).toString());
                    String nombreDelProducto2 = modeloDos.getValueAt(catalogosProductos.getSelectedRow(), 1).toString();
                    int cantidadDelProducto2 = Integer.parseInt(modeloDos.getValueAt(catalogosProductos.getSelectedRow(), 2).toString());
                    double precioDelProducto2 = Double.parseDouble(modelo.getValueAt(catalogosProductos.getSelectedRow(), 3).toString());

                    /*Cantidad del producto anterior*/
                    int cantidadAnterior = cantidadDelProducto + cantidadDelProducto2;

                    /*Linea de conexion*/
                    try (MongoClient mongoClient = MongoClients.create("mongodb+srv://mireya:Nena1112004@cluster0.z9ytrsk.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")) {
                        MongoDatabase database = mongoClient.getDatabase("Proyectofinalpoo");
                        MongoCollection<Document> collection = database.getCollection("cadaProducto");

                        /*Determinacion de la nueva cantidad disponible*/
                        int cantidadDisNueva = cantidadAnterior - cantidadSolicitada;
                        /*En dodne se actualizará*/
                        Document filtro = new Document("Codigo", codigoDelProducto);
                        /*Que se actualizará*/
                        Document actualizacion = new Document("$set", new Document("Cantidad_disponible", cantidadDisNueva ));
                        UpdateResult resultado = collection.updateOne(filtro, actualizacion);
                        /*Verificar si el producto se modifico*/
                        System.out.println("Documentos modificados: " + resultado.getModifiedCount());

                        /*Visualizacion de la tabla*/
                        modelo.setValueAt(cantidadDisNueva, catalogosProductos.getSelectedRow(), 2);
                        modeloDos.setValueAt(cantidadDisNueva, catalogosProductos.getSelectedRow(), 2);
                        confirmacionErrores.setText("Se ha agregado al carrito el producto " + nombreDelProducto);
                    }
                }

            }
        });

    }
}
