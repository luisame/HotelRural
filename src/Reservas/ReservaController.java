package Reservas;

import Inicio.InicioController;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import utilidades.DataSourceManager;
import utilidades.UsuarioInfo;
import java.io.IOException;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Window;
/**
 * El controlador para gestionar las reservas de los huéspedes.
 * Esta clase se encarga de manejar la lógica y la interacción de la interfaz de usuario 
 * para gestionar la información de las reservas.
 */
public class ReservaController {

    @FXML private TextField dniField;
    @FXML private TextField nombreField;
    @FXML private TextField apellidoField;
    @FXML private TextField direccionField;
    @FXML private TextField ciudadField;
    @FXML private TextField codPostalField;
    @FXML private TextField telefonoField;
    @FXML private TextField emailField;
    @FXML private TextField otraInfoField;
    @FXML private DatePicker fechaEntradaField;
    @FXML private DatePicker fechaSalidaField;
    @FXML private TextField numPersonasField;

    @FXML private TableView<Habitacion> tablaHabitacionesReservadas;
    @FXML private TableColumn<Habitacion, String> columnaDescripcion;
    @FXML private TableColumn<Habitacion, Integer> columnaCapacidad;
    @FXML private TableColumn<Habitacion, BigDecimal> columnaPrecio;

    @FXML private Label precioBaseLabel;
    @FXML private Label numPersonasLabel;
    @FXML private Label numNinosLabel;
    @FXML private Label numBebesLabel;
    @FXML private Label suplementoLabel;
    @FXML private Label precioTotalLabel;

    @FXML private TextField descripcionField;
    @FXML private TextField capacidadField;
    @FXML private TextField estadoField;

    private int idHabitacion;
    private UsuarioInfo usuarioInfo;
    private Habitacion habitacionSeleccionada;
@FXML
    private ProgressIndicator ReservaProgress;
    public void setUsuarioInfo(UsuarioInfo usuarioInfo) {
        this.usuarioInfo = usuarioInfo;
    }

    public void setHabitacionId(int idHabitacion) {
        this.idHabitacion = idHabitacion;
    }

    public void setFechaEntrada(LocalDate fecha) {
        fechaEntradaField.setValue(fecha);
    }
/**
 * 
 * @param fecha 
 */
    public void setFechaSalida(LocalDate fecha) {
        fechaSalidaField.setValue(fecha);
    }

    public void setNumPersonas(int numPersonas) {
        numPersonasField.setText(String.valueOf(numPersonas));
        numPersonasLabel.setText(String.valueOf(numPersonas));
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        precioTotalLabel.setText("€" + precioTotal.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
    }

    public void setPrecioBase(BigDecimal precioBase) {
        precioBaseLabel.setText("€" + precioBase.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
    }

    public void setNumNinos(int numNinos) {
        numNinosLabel.setText(String.valueOf(numNinos));
    }

    public void setNumBebes(int numBebes) {
        numBebesLabel.setText(String.valueOf(numBebes));
    }

    public void setSuplemento(BigDecimal suplemento) {
        suplementoLabel.setText("€" + suplemento.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
    }
/**
 * Método que inicializa los parámetros de la tabla 
 */
    @FXML
public void initialize() {
    columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
    columnaCapacidad.setCellValueFactory(new PropertyValueFactory<>("capacidad"));
    columnaPrecio.setCellValueFactory(new PropertyValueFactory<>("precioPorNoche"));
        System.out.println("ReservaController: "+ usuarioInfo);
    // Listener para actualizar los datos mostrados en los campos cuando se selecciona una fila
    tablaHabitacionesReservadas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
        if (newSelection != null) {
            mostrarDatosHabitacionSeleccionada(newSelection);
        }
    });

   // Cargar datos del cliente automáticamente al perder el foco del campo DNI
    dniField.focusedProperty().addListener((observable, oldValue, newValue) -> {
        if (!newValue) { // Si el campo DNI pierde el foco
            String dni = dniField.getText();
            if (dni != null && !dni.isEmpty()) {
                if (dni.matches("\\d{8}[A-Za-z]")) {
                    buscarClientePorDNI(dni);
                } else {
                    mostrarAlerta("DNI Inválido", "Por favor, ingrese un DNI válido de 8 dígitos seguidos de una letra.");
                }
            }
        }
    });
}

 /**
     * Busca un cliente por DNI.
     * 
     * @param dni El DNI del cliente.
     */
    @FXML
    public void buscarClientePorDNI(String dni) {
        String sql = "SELECT * FROM clientes WHERE dni = ?";

        try (Connection conn = DataSourceManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nombreField.setText(rs.getString("nombre_cliente"));
                apellidoField.setText(rs.getString("apellido_cliente"));
                direccionField.setText(rs.getString("direccion"));
                ciudadField.setText(rs.getString("ciudad"));
                codPostalField.setText(rs.getString("codPostal"));
                telefonoField.setText(rs.getString("telefono"));
                emailField.setText(rs.getString("email"));
                otraInfoField.setText(rs.getString("otra_informacion"));
            } else {
                limpiarCamposFormulario();
                mostrarAlerta("Cliente no encontrado", "No se encontró un cliente con el DNI proporcionado.");
            }
            rs.close();
        } catch (SQLException e) {
            String errorMessage = "Hubo un problema al buscar el cliente. Por favor, inténtelo de nuevo.";
            System.err.println(errorMessage);
            mostrarAlerta("Error", errorMessage + "\nDetalles: " + e.getMessage());
        }
    }

    /**
     * Limpia los campos del formulario.
     */
    private void limpiarCamposFormulario() {
        nombreField.setText("");
        apellidoField.setText("");
        direccionField.setText("");
        ciudadField.setText("");
        codPostalField.setText("");
        telefonoField.setText("");
        emailField.setText("");
        otraInfoField.setText("");
    }

    

/**
 * 
 * @param habitacionSeleccionada
 * @param fechaEntrada
 * @param fechaSalida
 * @param numPersonas 
 */
   public void iniciarConDatosDeReserva(Habitacion habitacionSeleccionada, LocalDate fechaEntrada, LocalDate fechaSalida, int numPersonas) {
    this.habitacionSeleccionada = habitacionSeleccionada;
    fechaEntradaField.setValue(fechaEntrada);
    fechaSalidaField.setValue(fechaSalida);
    numPersonasField.setText(String.valueOf(numPersonas));
    ObservableList<Habitacion> data = FXCollections.observableArrayList(habitacionSeleccionada);
    tablaHabitacionesReservadas.setItems(data);
    
}


/**
 * 
 * @param habitacionSeleccionada 
 */
    public void mostrarDatosHabitacionSeleccionada(Habitacion habitacionSeleccionada) {
        descripcionField.setText(habitacionSeleccionada.getDescripcion());
        capacidadField.setText(String.valueOf(habitacionSeleccionada.getCapacidad()));
        estadoField.setText(habitacionSeleccionada.getEstado());
    }
    
    // Método para volver a la pantalla de disponibilidad
    @FXML
    
/**
 * 
 */
public void volverADisponibilidad(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservas/DisponibilidadFXML.fxml"));
        Parent root = loader.load();

        // Obtener el controlador de la pantalla de disponibilidad
        DisponibilidadController disponibilidadController = loader.getController();
        disponibilidadController.setUsuarioInfo(this.usuarioInfo); // Pasar usuarioInfo de vuelta
        // Pasar los datos necesarios
        disponibilidadController.setFechaInicio(fechaEntradaField.getValue());
        disponibilidadController.setFechaFin(fechaSalidaField.getValue());
        disponibilidadController.setNumeroPersonas(Integer.parseInt(numPersonasField.getText()));

        // Obtener la escena actual y cambiarla
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();

        // Cerrar la ventana actual
        stage.close();
    } catch (IOException e) {
        
        mostrarAlerta("Error", "No se pudo cargar la pantalla de disponibilidad.");
    }
}

 

/**
 * Clase que representa el precio total y la temporada correspondiente.
 */
public class PrecioTemporada {
    private BigDecimal precioTotal;
    private String temporada;

    /**
     * Constructor de la clase PrecioTemporada.
     * 
     * @param precioTotal El precio total calculado para la temporada.
     * @param temporada El nombre de la temporada.
     */
    public PrecioTemporada(BigDecimal precioTotal, String temporada) {
        this.precioTotal = precioTotal;
        this.temporada = temporada;
    }

    /**
     * Obtiene el precio total.
     * 
     * @return El precio total.
     */
    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    /**
     * Obtiene el nombre de la temporada.
     * 
     * @return El nombre de la temporada.
     */
    public String getTemporada() {
        return temporada;
    }
}

/**
 * 
 * @param inicio
 * @param fin
 * @param idHabitacion
 * @return 
 */
private PrecioTemporada calcularPrecioPorTemporada(LocalDate inicio, LocalDate fin, int idHabitacion) {
    BigDecimal precioTotal = BigDecimal.ZERO;
    String temporada = "";
    String sqlTemporadas = "SELECT rango_fechas_desde, rango_fechas_hasta, precio_temporada, temporada " +
                           "FROM temporadas " +
                           "WHERE rango_fechas_desde <= ? AND rango_fechas_hasta >= ?";

    String sqlHabitacion = "SELECT precio FROM habitaciones WHERE id_habitacion = ?";

    try (Connection conn = DataSourceManager.getConnection();
         PreparedStatement stmtTemporadas = conn.prepareStatement(sqlTemporadas);
         PreparedStatement stmtHabitacion = conn.prepareStatement(sqlHabitacion)) {

        // Obtener el precio base de la habitación
        stmtHabitacion.setInt(1, idHabitacion);
        ResultSet rsHabitacion = stmtHabitacion.executeQuery();
        BigDecimal precioBaseHabitacion = BigDecimal.ZERO;
        if (rsHabitacion.next()) {
            precioBaseHabitacion = rsHabitacion.getBigDecimal("precio");
        }
        rsHabitacion.close();

        // Obtener los suplementos de temporada
        stmtTemporadas.setDate(1, java.sql.Date.valueOf(fin));
        stmtTemporadas.setDate(2, java.sql.Date.valueOf(inicio));
        ResultSet rsTemporadas = stmtTemporadas.executeQuery();

        while (rsTemporadas.next()) {
            LocalDate fechaDesde = rsTemporadas.getDate("rango_fechas_desde").toLocalDate();
            LocalDate fechaHasta = rsTemporadas.getDate("rango_fechas_hasta").toLocalDate();
            BigDecimal suplementoPorDia = rsTemporadas.getBigDecimal("precio_temporada");
            temporada = rsTemporadas.getString("temporada");

            System.out.println("Fecha desde: " + fechaDesde + ", Fecha hasta: " + fechaHasta + ", Suplemento por día: " + suplementoPorDia + ", Temporada: " + temporada);

            // Calcula el rango de fechas que se solapan con las fechas de la reserva y la temporada
            LocalDate solapamientoInicio = inicio.isAfter(fechaDesde) ? inicio : fechaDesde;
            LocalDate solapamientoFin = fin.isBefore(fechaHasta) ? fin : fechaHasta;

            // Cálculo de días de solapamiento
            long diasSolapamiento = ChronoUnit.DAYS.between(solapamientoInicio, solapamientoFin.plusDays(1));

            System.out.println("Solapamiento inicio: " + solapamientoInicio + ", Solapamiento fin: " + solapamientoFin + ", Días de solapamiento: " + diasSolapamiento);

            // Sumar el suplemento total para el rango de fechas solapadas
            BigDecimal suplementoParaRango = suplementoPorDia.multiply(BigDecimal.valueOf(diasSolapamiento));
            precioTotal = precioTotal.add(precioBaseHabitacion.multiply(BigDecimal.valueOf(diasSolapamiento)).add(suplementoParaRango));

            System.out.println("Suplemento para el rango: " + suplementoParaRango + ", Precio total acumulado: " + precioTotal);
        }

        rsTemporadas.close();
    } catch (SQLException e) {
        e.printStackTrace();
        // Manejar adecuadamente la excepción
    }
    System.out.println("Precio Total: " + precioTotal + " Temporada: " + temporada);
    return new PrecioTemporada(precioTotal, temporada);
}
/**
 * 
 * @param inicio
 * @param fin
 * @param numPersonas
 * @param idHabitacion
 * @return 
 */
private BigDecimal calcularPrecioPorPersona(LocalDate inicio, LocalDate fin, int numPersonas, int idHabitacion) {
    PrecioTemporada precioTemporada = calcularPrecioPorTemporada(inicio, fin, idHabitacion);
    BigDecimal precioTotal = precioTemporada.getPrecioTotal();
    if (numPersonas > 0) {
        BigDecimal precioPorPersona = precioTotal.divide(BigDecimal.valueOf(numPersonas), BigDecimal.ROUND_HALF_UP);
        System.out.println("Precio por Persona calculado: " + precioPorPersona);
        return precioPorPersona;
    } else {
        System.out.println("Número de personas es 0 o negativo: " + numPersonas);
        return BigDecimal.ZERO;
    }
}

public BigDecimal calcularPrecioTotal(LocalDate inicio, LocalDate fin, int numPersonas, int idHabitacion) {
    PrecioTemporada precioTemporada = calcularPrecioPorTemporada(inicio, fin, idHabitacion);
    BigDecimal precioBase = precioTemporada.getPrecioTotal(); // Obtener el precio total
    BigDecimal precioTotal = precioBase.multiply(BigDecimal.valueOf(numPersonas));
    System.out.println("Precio Total con " + numPersonas + " personas: " + precioTotal);
    return precioTotal;
}
/**
 * 
 * @param event 
 */
@FXML
public void crearReserva(ActionEvent event) {
    UsuarioInfo usuarioInfo;
    try {
        usuarioInfo = UsuarioInfo.getInstance();
    } catch (IllegalStateException e) {
        mostrarAlerta("Error", "Usuario no autenticado. Por favor, inicie sesión de nuevo.");
        return;
    }

    if (usuarioInfo.getNivelAcceso() != 1) {
        mostrarAlerta("Acceso Denegado", "No tiene permiso para crear una reserva.");
        return;
    }

    String dni = dniField.getText();
    if (dni == null || dni.isEmpty()) {
        mostrarAlerta("DNI Requerido", "Por favor, ingrese el DNI del cliente.");
        return;
    }

    Habitacion habitacionSeleccionada = tablaHabitacionesReservadas.getSelectionModel().getSelectedItem();
    if (habitacionSeleccionada == null) {
        mostrarAlerta("Error", "Seleccione una habitación para reservar.");
        return;
    }

    LocalDate fechaEntrada = fechaEntradaField.getValue();
    LocalDate fechaSalida = fechaSalidaField.getValue();
    int numPersonas;
    try {
        numPersonas = Integer.parseInt(numPersonasField.getText());
    } catch (NumberFormatException e) {
        mostrarAlerta("Error de Formato", "Ingrese valores numéricos válidos para el número de personas.");
        return;
    }

    PrecioTemporada precioTemporada = calcularPrecioPorTemporada(fechaEntrada, fechaSalida, habitacionSeleccionada.getId());
    BigDecimal precioPorPersona = calcularPrecioPorPersona(fechaEntrada, fechaSalida, numPersonas, habitacionSeleccionada.getId());
    String temporada = precioTemporada.getTemporada();
    System.out.println("Precio por Persona después de calcular: " + precioPorPersona + " Temporada: " + temporada);

    try (Connection conn = DataSourceManager.getConnection()) {
        // Verificar si el cliente ya existe
        String querySelect = "SELECT id_cliente FROM clientes WHERE dni = ?";
        int idCliente = 0;
        boolean clienteExiste = false;
        try (PreparedStatement pstmtSelect = conn.prepareStatement(querySelect)) {
            pstmtSelect.setString(1, dni);
            try (ResultSet rs = pstmtSelect.executeQuery()) {
                if (rs.next()) {
                    idCliente = rs.getInt("id_cliente");
                    clienteExiste = true;
                }
            }
        }

        // Cliente no existe, insertarlo
        if (!clienteExiste) {
            String queryInsert = "INSERT INTO clientes (dni, nombre_cliente, apellido_cliente, direccion, ciudad, codPostal, telefono, email, otra_informacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmtInsert = conn.prepareStatement(queryInsert, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmtInsert.setString(1, dni);
                pstmtInsert.setString(2, nombreField.getText());
                pstmtInsert.setString(3, apellidoField.getText());
                pstmtInsert.setString(4, direccionField.getText());
                pstmtInsert.setString(5, ciudadField.getText());
                pstmtInsert.setString(6, codPostalField.getText());
                pstmtInsert.setString(7, telefonoField.getText());
                pstmtInsert.setString(8, emailField.getText());
                pstmtInsert.setString(9, otraInfoField.getText());
                pstmtInsert.executeUpdate();

                try (ResultSet generatedKeys = pstmtInsert.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        idCliente = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Error al obtener el ID del nuevo cliente.");
                    }
                }
            }
        }

        // Crear la reserva
        String insertQuery = "INSERT INTO reservas (id_cliente, id_habitacion, fecha_entrada, fecha_salida, nuPersonas, id_usuario, precio_persona, temporada) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmtReserva = conn.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmtReserva.setInt(1, idCliente);
            pstmtReserva.setInt(2, habitacionSeleccionada.getId());
            pstmtReserva.setDate(3, java.sql.Date.valueOf(fechaEntrada));
            pstmtReserva.setDate(4, java.sql.Date.valueOf(fechaSalida));
            pstmtReserva.setInt(5, numPersonas);
            pstmtReserva.setInt(6, usuarioInfo.getIdUsuario()); // Guarda la identificación del usuario
            pstmtReserva.setBigDecimal(7, precioPorPersona); // Guarda el precio por persona
            pstmtReserva.setString(8, temporada); // Guarda la temporada

            System.out.println("Datos antes de insertar la reserva:");
            System.out.println("id_cliente: " + idCliente);
            System.out.println("id_habitacion: " + habitacionSeleccionada.getId());
            System.out.println("fecha_entrada: " + java.sql.Date.valueOf(fechaEntrada));
            System.out.println("fecha_salida: " + java.sql.Date.valueOf(fechaSalida));
            System.out.println("nuPersonas: " + numPersonas);
            System.out.println("id_usuario: " + usuarioInfo.getIdUsuario());
            System.out.println("precio_persona: " + precioPorPersona);
            System.out.println("temporada: " + temporada);

            int affectedRows = pstmtReserva.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmtReserva.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idReserva = rs.getInt(1);
                        actualizarEstadoHabitacion(habitacionSeleccionada.getId(), "R");
                        mostrarAlerta("Reserva Exitosa", "La reserva ha sido creada con éxito.");
                        cerrarVentanaYVolverInicio(event);
                    }
                }
            } else {
                mostrarAlerta("Error al crear reserva", "No se pudo crear la reserva.");
            }
        }
    } catch (SQLException e) {
        mostrarAlerta("Error de Base de Datos", "No se pudo completar la operación: " + e.getMessage());
    }
}

/**
 * Actualiza el estado de una habitación en la base de datos.
 * 
 * @param idHabitacion El ID de la habitación cuyo estado se va a actualizar.
 * @param nuevoEstado El nuevo estado que se va a asignar a la habitación.
 */
private void actualizarEstadoHabitacion(int idHabitacion, String nuevoEstado) {
    // Consulta SQL para actualizar el estado de la habitación
    String updateQuery = "UPDATE habitaciones SET estado = ? WHERE id_habitacion = ?";
    
    // Intentar conectar a la base de datos y ejecutar la consulta
    try (Connection conn = DataSourceManager.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
        
        // Configurar los parámetros de la consulta
        pstmt.setString(1, nuevoEstado);
        pstmt.setInt(2, idHabitacion);
        
        // Ejecutar la actualización
        pstmt.executeUpdate();
    } catch (SQLException e) {
        // Mostrar alerta en caso de error de base de datos
        mostrarAlerta("Error de Base de Datos", "No se pudo actualizar el estado de la habitación: " + e.getMessage());
    }
}

/**
 * Cierra todas las ventanas abiertas y abre la ventana de inicio.
 * 
 * @param event El evento que desencadena esta acción.
 */
private void cerrarVentanaYVolverInicio(ActionEvent event) {
    try {
        // Cargar la nueva ventana de inicio
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Inicio/InicioFXML.fxml"));
        Parent root = loader.load();

        // Obtener el controlador de la pantalla de inicio
        InicioController inicioController = loader.getController();
        inicioController.setUsuarioInfo(this.usuarioInfo); // Pasar usuarioInfo de vuelta

        // Crear una nueva escena y una nueva ventana para la pantalla de inicio
        Stage nuevoStage = new Stage();
        nuevoStage.setScene(new Scene(root));
        nuevoStage.show();

        // Cerrar todas las ventanas abiertas excepto la nueva ventana de inicio
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        for (Window window : Window.getWindows()) {
            if (window instanceof Stage && window != nuevoStage) {
                ((Stage) window).close();
            }
        }

        // Cerrar la ventana de "Reservas"
        currentStage.close();
    } catch (IOException e) {
        // Mostrar alerta en caso de error al cargar la ventana de inicio
        mostrarAlerta("Error", "No se pudo cargar la pantalla de inicio: " + e.getMessage());
    }
}

/**
 * Muestra una alerta con un título y un mensaje específicos.
 * 
 * @param titulo El título de la alerta.
 * @param mensaje El mensaje de la alerta.
 */
private void mostrarAlerta(String titulo, String mensaje) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(titulo);
    alert.setHeaderText(null);
    alert.setContentText(mensaje);
    alert.showAndWait();
}
}