public class Clientes {
    int cedula;
    String nombre;
    String apellido;
    String correo;
    String contrasenia;
    /*Constructores*/
    public Clientes() {}
    /**
     * Se almacena la información colocada por el cliente en la ventana "RegistroClientes".
     *
     * @param cedula El número de cédula del cliente.
     * @param nombre El nombre del cliente.
     * @param apellido El apellido del cliente.
     * @param correo El correo electrónico del cliente.
     * @param contrasenia La contraseña del cliente.
     */
    public Clientes(int cedula, String nombre, String apellido, String correo, String contrasenia) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasenia = contrasenia;
    }

    public int getCedula() {
        return cedula;
    }

    public void setCedula(int cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
}
