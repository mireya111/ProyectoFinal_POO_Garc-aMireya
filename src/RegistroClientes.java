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
    private JTextField contra;
    private JButton registroCliente;
    private JLabel confirmacion;

    public RegistroClientes() {
        registroCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Clientes clienteNuevo = new Clientes();
                clienteNuevo.setCedula(Integer.parseInt(cedula.getText()));
                clienteNuevo.setNombre(nombre.getText());
                clienteNuevo.setApellido(apellido.getText());
                clienteNuevo.setCorreo(correo.getText());
                clienteNuevo.setContrasenia(contra.getText());
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
            }
        });
    }
}

