import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoClients;
import org.bson.Document;


public class RegistroClientes {
    private JTextField cedula;
    public JPanel registroclientes;
    private JTextField nombre;
    private JTextField apellido;
    private JTextField correo;
    private JPasswordField contra ;
    private JButton registroCliente;
    private JLabel confirmacion;
    private JButton regresar;
    private JFrame frame1;

    public RegistroClientes(JFrame frame) {
        frame1=frame;
        registroCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*Validación de campos vacios*/
                if(cedula.getText().isEmpty() || nombre.getText().isEmpty()
                        || apellido.getText().isEmpty() || correo.getText().isEmpty() || contra.getText().isEmpty()){
                    confirmacion.setText("Llene los campos vacíos porfavor");
                    return;
                    /**
                     *"isEmpty()" ayuda a la comprobación de los campos vacíos.
                     * Si se encuentra que el campo esta vacio se coloca en el JLabel el error.
                     * */
                } else {
                    /*Creacion de un objeto y seteo del mismo*/
                    Clientes clienteNuevo = new Clientes();
                    clienteNuevo.setNombre(nombre.getText());
                    clienteNuevo.setApellido(apellido.getText());
                    clienteNuevo.setCorreo(correo.getText());
                    clienteNuevo.setContrasenia(contra.getText());
                    /*Validación para que el usuario ingrese el solo numeros en el campo cedula*/
                    try{
                        clienteNuevo.setCedula(Integer.parseInt(cedula.getText()));
                    } catch (NumberFormatException exception){
                       confirmacion.setText("En el campo cedula, solo se aceptan numeros");
                    }
                    /**
                     * @param try utilizado para verificar si lo colocado en la ventana es un numero
                     * @param catch utilizado para indicarle al usuario el error si l que esta insertando son caracteres no permitidos
                     * */
                    /*Linea de conexión*/
                    try (MongoClient mongoClient = MongoClients.create("mongodb+srv://mireya:Nena1112004@cluster0.z9ytrsk.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")) {
                        MongoDatabase db = mongoClient.getDatabase("Proyectofinalpoo");
                        MongoCollection<Document> collection = db.getCollection("clientes");
                        Document registroClientes = new Document("cedula", clienteNuevo.getCedula())
                                .append("nombre", clienteNuevo.getNombre())
                                .append("apellido", clienteNuevo.getApellido())
                                .append("correo", clienteNuevo.getCorreo())
                                .append("contraseña", clienteNuevo.getContrasenia());
                        collection.insertOne(registroClientes);
                        confirmacion.setText("El cliente se ha registraqdo correctamente");
                    } catch (MongoException exception) {
                        confirmacion.setText("El cliente no se registro correctamente");
                    }
                    /**
                     * @param try Intenta la conexión con la base de datos.
                     * Se crea una colleccion "clientes" en la base de datos "Proyectofinalpoo".
                     * Se crea un documento que tendra dentro los datos del cliente que servirá tanto para la factura como para la gestion de login
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
                frame.setSize(1000, 800);
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
                frame1.setVisible(false);
            }
        });
    }
}


