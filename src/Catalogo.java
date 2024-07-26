import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Catalogo {
    public JPanel catalogoProductos;
    private JTable productosDisponibles;
    private JTextField cantidadCliente;
    private JButton agregarCarrito;
    private JButton generarOrden;
    private JButton realizarPedido;
    private JButton actualizarPedido;
    private JButton eliminarPedido;
    private JButton atrás;
    private JLabel erroresCatalogo;
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
        catalogoProduct.catalogo(modelo, productosDisponibles, erroresCatalogo);
    }
}
