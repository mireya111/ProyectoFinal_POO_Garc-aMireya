import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SubirProductos {
    public JPanel subirProductos;
    private JTextField stock;
    private JPasswordField nombreProducto;
    private JTextField precioProducto;
    private JButton publicarBoton;
    private JButton buscarBoton;
    private JTable table1Mostrar;
    private JLabel imagen;
    /*Para los nombres de cada columna*/
    DefaultTableModel modelo = new DefaultTableModel();
    public SubirProductos() {
        String[] columnaNombres = {"Nombre del producto", "Cantidad disponible", "Precio del producto", "Imagen"};
        modelo.setColumnIdentifiers(columnaNombres);
        table1Mostrar.setModel(modelo);
    }
    /*Para la agregación de datos*/
}
