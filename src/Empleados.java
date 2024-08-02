public class Empleados {
    String codigo;
    String nombre;
    String apellido;
    String correo;
    String contrasenia;
    /*Contructores*/
    public Empleados() {}
    /**
     * Se almacena la información colocada por el cliente en la ventana "RegistroDuenio".
     *
     * @param codigo El codigo entregado al empleado.
     * @param nombre El nombre del empleado.
     * @param apellido El apellido del empleado.
     * @param correo El correo electrónico del empleado.
     * @param contrasenia La contraseña del empleado.
     */
    public Empleados(String codigo, String nombre, String apellido, String correo, String contrasenia) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasenia = contrasenia;
    }
    /*Getters y setters*/

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
