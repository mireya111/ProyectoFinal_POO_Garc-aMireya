
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
    /*public static int cedulaCliente;*/
    public static String nombreCliente;
    public static String apellidoCliente;
    public static String email;

    /**
     * Constructor de la clase Login.
     * Configura el botón de inicio de sesión y gestiona las acciones realizadas al presionar el botón.
     *
     * @param frame1 El JFrame que contiene el panel de login.
     */
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
                                /*String cedula = documento.getString("cedula");*/
                                String nombre = documento.getString("nombre");
                                String apellido = documento.getString("apellido");
                                String email = documento.getString("correo");
                                ClientesDatos.setCliente(nombre, apellido, email);
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
    public static class ClientesDatos {
        /*private static String cedulaCliente;*/
        private static String nombreCliente;
        private static String apellidoCliente;
        private static String email;
        /**
         * Establece los datos del cliente.
         *
         * @param nombre   Almacena el nombre traído de la base de datos del cliente para la factura.
         * @param apellido Almacena el apellido traído de la base de datos del cliente para la factura.
         * @param correo   Almacena el correo traído de la base de datos del cliente para la factura.
         */

        public static void setCliente( String nombre, String apellido, String correo) {
            nombreCliente = nombre;
            apellidoCliente = apellido;
            email = correo;
        }

        /**
         * Obtiene el nombre del cliente.
         *
         * @return El nombre del cliente.
         */
        public static String getNombreCliente() {
            return nombreCliente;
        }

        /**
         * Obtiene el apellido del cliente.
         *
         * @return El apellido del cliente.
         */
        public static String getApellidoCliente() {
            return apellidoCliente;
        }

        /**
         * Obtiene el correo electrónico del cliente.
         *
         * @return El correo electrónico del cliente.
         */
        public static String getEmailCliente() {
            return email;
        }
    }

}

