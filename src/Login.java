
import com.mongodb.MongoException;
import com.mongodb.client.*;
import org.bson.Document;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class Login {
    public JPanel panelLogin;
    private JTextField correo;
    private JPasswordField contrasenia;
    private JButton entrarButton;
    private JButton registroClienteButton;
    private JButton registroEmpleadosButton;
    private JLabel errores;
    private static String cedulaCliente;
    private static String nombreCliente;
    private static String apellidoCliente;
    private static String email;
    public Login(JFrame frame1) {
        entrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*Objetos*/
                Clientes clienteValidacion = new Clientes();
                Empleados empleadoValidacion = new Empleados();
                /*Setear objeto*/
                clienteValidacion.setCorreo(correo.getText());
                clienteValidacion.setContrasenia(contrasenia.getText());
                empleadoValidacion.setCorreo(correo.getText());
                empleadoValidacion.setContrasenia(contrasenia.getText());
                if (Objects.equals(clienteValidacion.getCorreo(), "") || Objects.equals(clienteValidacion.getContrasenia(), "")){
                    errores.setText("Se encuentran campos vacíos");
                }else {
                    /*Linea de conexion*/
                    try (MongoClient mongoClient = MongoClients.create("mongodb+srv://mireya:Nena1112004@cluster0.z9ytrsk.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")) {
                        MongoDatabase database = mongoClient.getDatabase("Proyectofinalpoo");
                        MongoCollection<Document> collection1 = database.getCollection("clientes");
                        MongoCollection<Document> collection2 = database.getCollection("empleado");

                        System.out.println("Conectado a la base de datos");

                        /* Realiza las consultas */
                        FindIterable<Document> documento1 = collection1.find();
                        FindIterable<Document> documento2 = collection2.find();

                        boolean clienteEncontrado = false;
                        boolean empleadoEncontrado = false;

                        for (Document documento : documento1) {
                            if (documento.getString("correo").equals(clienteValidacion.getCorreo()) &&
                                    documento.getString("contraseña").equals(clienteValidacion.getContrasenia())) {
                                String nombre = documento.getString("nombre");
                                String apellido = documento.getString("apellido");
                                String email = documento.getString("email");
                                clienteEncontrado = true;
                                break;
                            }
                        }

                        if (!clienteEncontrado) {
                            for (Document documento : documento2) {
                                System.out.println("Documento empleado: " + documento.toJson());
                                if (documento.getString("correo").equals(empleadoValidacion.getCorreo()) &&
                                        documento.getString("contraseña").equals(empleadoValidacion.getContrasenia())) {
                                    empleadoEncontrado = true;
                                    break;
                                }
                            }
                        }

                        if (clienteEncontrado) {
                            System.out.println("Login de cliente exitoso");
                            errores.setText("Ingresando al catálogo del producto ...");
                            JFrame frame = new JFrame();
                            frame.setContentPane(new Catalogo(frame).catalogoProductos);
                            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            frame.setSize(800, 600);
                            frame.setLocationRelativeTo(null);
                            frame.setVisible(true);
                            frame1.setVisible(false);
                        } else if (empleadoEncontrado) {
                            System.out.println("Login de empleado exitoso");
                            errores.setText("Ingresando a la sección empleados ...");
                            JFrame frame = new JFrame();
                            frame.setContentPane(new SubirProductos(frame).subirProductos);
                            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            frame.setSize(800, 600);
                            frame.setLocationRelativeTo(null);
                            frame.setVisible(true);
                            frame1.setVisible(false);
                        } else {
                            errores.setText("ERROR! NO COINCIDEN LAS CREDENCIALES");
                        }

                    } catch (MongoException ex) {
                        errores.setText("Error de conexión: " + ex.getMessage());
                    }
                }
            }
        });
        registroClienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame();
                frame.setContentPane(new RegistroClientes(frame).registroclientes);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(800, 600);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame1.setVisible(false);
            }
        });
        registroEmpleadosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame();
                frame.setContentPane(new RegistroDuenio(frame).registrarempleado);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(800, 600);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame1.setVisible(false);
            }
        });
    }
    public class ClientesDatos {
        private static String cedulaCliente;
        private static String nombreCliente;
        private static String apellidoCliente;
        private static String email;

        public static void setCliente(String cedula, String nombre, String apellido, String correo) {
            cedulaCliente = cedula;
            nombreCliente = nombre;
            apellidoCliente = apellido;
            email = correo;
        }

        public static String getCedulaCliente() {
            return cedulaCliente;
        }

        public static String getNombreCliente() {
            return nombreCliente;
        }

        public static String getApellidoCliente() {
            return apellidoCliente;
        }

        public static String getEmailCliente() {
            return email;
        }
    }

}

