package utilidades;

/**
 * Clase que representa la información básica de un cliente.
 */
public class ClienteInfo {
    // Atributos de la clase
    private String dni;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;

    // Constructor que recibe los valores iniciales para los atributos
    public ClienteInfo(String dni, String nombre, String apellidos, String email, String telefono) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.telefono = telefono;
    }

    // Métodos getter y setter para cada atributo

    /**
     * Obtiene el DNI del cliente.
     */
    public String getDni() {
        return dni;
    }

    /**
     * Establece el DNI del cliente.
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Obtiene el nombre del cliente.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del cliente.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene los apellidos del cliente.
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Establece los apellidos del cliente.
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Obtiene el correo electrónico del cliente.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del cliente.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene el número de teléfono del cliente.
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el número de teléfono del cliente.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
