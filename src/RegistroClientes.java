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
                } else {
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
                    /*Linea de conexión*/
                    try (MongoClient mongoClient = MongoClients.create("mongodb+srv://mireya:Nena1112004@cluster0.z9ytrsk.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")) {
                        MongoDatabase db = mongoClient.getDatabase("Proyectofinalpoo");
                        MongoCollection<Document> collection = db.getCollection("clientes");
                        /*String contraseniaEncriptada = hashPassword(clienteNuevo.getContrasenia());*/
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

    /*private String hashPassword(String contrasenia) {

    }*/

}


