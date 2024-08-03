import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistroDuenio {
    private JTextField codigo;
    public JPanel registrarempleado;
    private JTextField nombre;
    private JTextField apellido;
    private JTextField correo;
    private JButton registrarEmpleadoButton;
    private JPasswordField contrase;
    private JLabel confirmacion;
    private JButton regresar;
    private JFrame frame1;
    /**
     *  @param frame frame JFrame principal de la aplicación.
     * */
    public RegistroDuenio(JFrame frame) {
        frame1=frame;
        registrarEmpleadoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(codigo.getText().isEmpty() || nombre.getText().isEmpty()
                        || apellido.getText().isEmpty() || correo.getText().isEmpty() || contrase.getText().isEmpty()){
                    confirmacion.setText("Llene los campos vacíos porfavor");
                    return;
                    /**
                     *"isEmpty()" ayuda a la comprobación de los campos vacíos.
                     * Si se encuentra que el campo esta vacio se coloca en el JLabel el error.
                     * */
                } else {
                    /*Creacion de un objeto y seteo del mismo*/
                    Empleados empleadoNuevo = new Empleados();
                    empleadoNuevo.setCodigo(codigo.getText());
                    empleadoNuevo.setNombre(nombre.getText());
                    empleadoNuevo.setApellido(apellido.getText());
                    empleadoNuevo.setCorreo(correo.getText());
                    empleadoNuevo.setContrasenia(contrase.getText());
                    /*Linea de conexión*/
                    try (MongoClient mongoClient = MongoClients.create("mongodb+srv://mireya:Nena1112004@cluster0.z9ytrsk.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")) {
                        MongoDatabase db = mongoClient.getDatabase("Proyectofinalpoo");
                        MongoCollection<Document> collection = db.getCollection("empleado");
                        Document registroClientes = new Document("codigo_trabajador", empleadoNuevo.getCodigo())
                                .append("nombres", empleadoNuevo.getNombre())
                                .append("apellidos", empleadoNuevo.getApellido())
                                .append("correo", empleadoNuevo.getCorreo())
                                .append("contraseña", empleadoNuevo.getContrasenia());
                        collection.insertOne(registroClientes);
                        confirmacion.setText("El cliente se ha registraqdo correctamente");
                    } catch (MongoException exception) {
                        confirmacion.setText("El cliente no se registro correctamente");
                    }
                    /**
                     * @param try Intenta la conexión con la base de datos.
                     * Se crea una colleccion "empleado" en la base de datos "Proyectofinalpoo".
                     * Se crea un documento que tendra dentro los datos del empleado que servirá para la gestion de login y la visualizacion de diferentes ventanas.
                     * @param catch Si no se genera la conexión adecuadamente se presenta en el JLabel "confirmacion" un mensaje de insatisfacción con la conexión de la base de datos.
                     * */
                }
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
