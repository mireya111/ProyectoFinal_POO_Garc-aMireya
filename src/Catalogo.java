import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
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
    private JButton eliminarPedido;
    private JButton atrás;
    private JLabel erroresCatalogo;
    private JLabel confirmacionErrores;
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
    public Catalogo() {
        Productos catalogoProduct = new Productos();
        catalogoProduct.catalogo(modelo, catalogosProductos, erroresCatalogo);
        agregarCarrito.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*Setteo y creacion de un objeto*/
                Compra compraNueva = new Compra();
                compraNueva.setCantidadProducto(Integer.parseInt(cantidadCliente.getText()));
                int cantidadSolicitada = compraNueva.getCantidadProducto();
                // Validación de la entrada del usuario
                String cantidadTexto = cantidadCliente.getText();
                if (cantidadTexto == null || cantidadTexto.trim().isEmpty()) {
                    confirmacionErrores.setText("El campo de cantidad no puede estar vacío.");
                }
                try {
                    cantidadSolicitada = Integer.parseInt(cantidadTexto);
                } catch (NumberFormatException ex) {
                    confirmacionErrores.setText("Por favor, ingrese un número válido");
                    return;
                }
                /*Obtencion de la información de los productos*/
                int codigoDelProducto = Integer.parseInt(modelo.getValueAt(catalogosProductos.getSelectedRow(), 0).toString());
                String nombreDelProducto = catalogosProductos.getValueAt(catalogosProductos.getSelectedRow(), 1).toString();
                int cantidadDelProducto = Integer.parseInt(catalogosProductos.getValueAt(catalogosProductos.getSelectedRow(), 2).toString());
                Double precioDelProducto = Double.parseDouble(catalogosProductos.getValueAt(catalogosProductos.getSelectedRow(), 3).toString());
                /*Calculo del precio total por cada producto*/
                Double precioPorCantidad = compraNueva.getCantidadProducto() * precioDelProducto;
                compraNueva.setPrecioTotal(precioPorCantidad);
                /*Calculo para la disminución de la cantidad del producto*/
                int cantidadDisminuye = cantidadDelProducto - cantidadSolicitada;
                /*Linea de conexion*/
                try (MongoClient mongoClient = MongoClients.create("mongodb+srv://mireya:Nena1112004@cluster0.z9ytrsk.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")) {
                    MongoDatabase db = mongoClient.getDatabase("Proyectofinalpoo");
                    MongoCollection<Document> collection = db.getCollection("cadaProducto");
                    /*A que registro*/
                    Document filtro = new Document("Codigo", codigoDelProducto);
                    Document actualizacion = new Document("$set", new Document("Cantidad_disponible", cantidadDisminuye));
                    UpdateResult resultado = collection.updateOne(filtro, actualizacion);
                    /*Verificar si el producto se modifico*/
                    System.out.println("Documentos modificados: " + resultado.getModifiedCount());
                    /*Visualización de la tabla*/
                    modelo.setValueAt(cantidadDelProducto, catalogosProductos.getSelectedRow(), 2);
                    confirmacionErrores.setText("Se ha agregado al carrito el producto ");
                } catch (Exception ex) {
                    confirmacionErrores.setText("Error al añadir producto al carrito: " + ex.getMessage());
                }
            }
        });
    }
}
