package utilidades;

public class UsuarioInfo {
    public static UsuarioInfo instance;

    private int idUsuario;
    private int nivelAcceso;
    private String nombreEmpleado;
    private String apellidoEmpleado;
    private final boolean initialized = false; // Nuevo atributo

    public  UsuarioInfo(int idUsuario, String nombre, String apellido, int nivelAcceso) {
        this.idUsuario = idUsuario;
        this.nombreEmpleado = nombre;
        this.apellidoEmpleado = apellido;
        this.nivelAcceso = nivelAcceso;
    }

    public static UsuarioInfo getInstance() {
        if (instance == null) {
            throw new IllegalStateException("UsuarioInfo no inicializado");
        }
        return instance;
    }

    public static void initializeInstance(int idUsuario, String nombre, String apellido, int nivelAcceso) {
        if (instance != null) {
            throw new IllegalStateException("UsuarioInfo ya inicializado");
        }
        instance = new UsuarioInfo(idUsuario, nombre, apellido, nivelAcceso);
    }
    public boolean isInitialized() {
        return initialized;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getNivelAcceso() {
        return nivelAcceso;
    }

    public void setNivelAcceso(int nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public String getApellidoEmpleado() {
        return apellidoEmpleado;
    }

    public void setApellidoEmpleado(String apellidoEmpleado) {
        this.apellidoEmpleado = apellidoEmpleado;
    }
    @Override
    public String toString() {
        return "UsuarioInfo{" +
                "idUsuario=" + idUsuario +
                ", nivelAcceso=" + nivelAcceso +
                ", nombreEmpleado='" + nombreEmpleado + '\'' +
                ", apellidoEmpleado='" + apellidoEmpleado + '\'' +
                '}';
    }
}
