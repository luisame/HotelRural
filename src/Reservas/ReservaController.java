package Reservas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utilidades.DataSourceManager;

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
    @FXML private TextField idHabitacionField;
    @FXML private TextField descripcionField;
    @FXML private TextField capacidadField;
    @FXML private TextField estadoField;
    @FXML private TextField numPersonasField;
    @FXML private TableView<Habitacion> tablaHabitacionesDisponibles;
    // Este método se llamaría para cargar los datos en la tabla

     @FXML
    public void initialize() {
        inicializarTablaHabitaciones();
        configurarListenersDatosCliente();
    }

    // Método para inicializar la tabla habitaciones
    private void inicializarTablaHabitaciones() {
        TableColumn<Habitacion, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Habitacion, String> descripcionColumn = new TableColumn<>("Descripción");
        descripcionColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        TableColumn<Habitacion, Integer> capacidadColumn = new TableColumn<>("Capacidad");
        capacidadColumn.setCellValueFactory(new PropertyValueFactory<>("capacidad"));

        TableColumn<Habitacion, String> estadoColumn = new TableColumn<>("Estado");
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));

        tablaHabitacionesDisponibles.getColumns().addAll(idColumn, descripcionColumn, capacidadColumn, estadoColumn);

        tablaHabitacionesDisponibles.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                mostrarDatosHabitacionSeleccionada(newSelection);
            }
        });
    }

    // Método para cargar los datos en la tabla
    public void cargarDatosEnTabla(List<Habitacion> listaHabitaciones) {
        ObservableList<Habitacion> habitaciones = FXCollections.observableArrayList(listaHabitaciones);
        tablaHabitacionesDisponibles.setItems(habitaciones);
    }

    public void mostrarDatosHabitacionSeleccionada(Habitacion habitacionSeleccionada) {
        idHabitacionField.setText(String.valueOf(habitacionSeleccionada.getId()));
        descripcionField.setText(habitacionSeleccionada.getDescripcion());
        capacidadField.setText(String.valueOf(habitacionSeleccionada.getCapacidad()));
        estadoField.setText(habitacionSeleccionada.getEstado());
    }

    private void configurarListenersDatosCliente() {
        dniField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                buscarYMostrarDatosCliente();
            }
        });
    }
    public void buscarYMostrarDatosCliente() {
        String dni = dniField.getText().trim();
        if (dni.isEmpty()) {
            mostrarAlerta("Error", "Por favor, ingrese el DNI del cliente.");
            return;
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DataSourceManager.getConnection();
            String query = "SELECT * FROM clientes WHERE dni = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, dni);
            rs = stmt.executeQuery();

            if (rs.next()) {
                // Cliente encontrado, mostrar datos en los campos correspondientes
                nombreField.setText(rs.getString("nombre_cliente"));
                apellidoField.setText(rs.getString("apellido_cliente"));
                direccionField.setText(rs.getString("direccion"));
                ciudadField.setText(rs.getString("ciudad"));
                codPostalField.setText(rs.getString("codPostal"));
                telefonoField.setText(rs.getString("telefono"));
                emailField.setText(rs.getString("email"));
                otraInfoField.setText(rs.getString("otra_informacion"));
            } else {
                // Cliente no encontrado, puedes optar por limpiar los campos o mostrar un mensaje
                limpiarCamposCliente();
                mostrarAlerta("Información", "No se encontraron datos del cliente. Por favor, complete la información.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error de Base de Datos", "Error al consultar los datos del cliente: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void limpiarCamposCliente() {
        nombreField.setText("");
        apellidoField.setText("");
        direccionField.setText("");
        ciudadField.setText("");
        codPostalField.setText("");
        telefonoField.setText("");
        emailField.setText("");
        otraInfoField.setText("");
    }


    public void prepararDatosDeHabitacion(Habitacion habitacionSeleccionada) {
        idHabitacionField.setText(String.valueOf(habitacionSeleccionada.getId()));
        descripcionField.setText(habitacionSeleccionada.getDescripcion());
        capacidadField.setText(String.valueOf(habitacionSeleccionada.getCapacidad()));
        estadoField.setText(habitacionSeleccionada.getEstado());
    }

    public void prepararDatosDeReserva(LocalDate fechaEntrada, LocalDate fechaSalida, int numPersonas) {
        fechaEntradaField.setValue(fechaEntrada);
        fechaSalidaField.setValue(fechaSalida);
        numPersonasField.setText(String.valueOf(numPersonas));
    }

    // Continuación desde la parte de initData...
public void initData(LocalDate fechaInicioVal, LocalDate fechaFinVal, int numPersonas) {
    fechaEntradaField.setValue(fechaInicioVal);
    fechaSalidaField.setValue(fechaFinVal);
    numPersonasField.setText(String.valueOf(numPersonas));
}

public void crearReserva(ActionEvent event) {
    int idCliente = obtenerIdCliente();
    if (idCliente == -1) {
        // Si el cliente no existe, crear un nuevo registro en la tabla de clientes
        // y mostrar un mensaje indicando que el cliente no está registrado.
        mostrarAlerta("Error", "Cliente no registrado. Por favor, verifique el DNI o registre al cliente.");
        return;
    }
if (idHabitacionField.getText().isEmpty() || numPersonasField.getText().isEmpty()) {
        mostrarAlerta("Error", "Por favor, complete todos los campos requeridos.");
        return;
    }
//  obtener los valores numéricos
    int idHabitacion, numPersonas;
    try {
        idHabitacion = Integer.parseInt(idHabitacionField.getText());
        numPersonas = Integer.parseInt(numPersonasField.getText());
    } catch (NumberFormatException e) {
        mostrarAlerta("Error de Formato", "Por favor, ingrese valores numéricos válidos.");
        return;
    }

    // Prosigue con la obtención del ID del cliente
     idCliente = obtenerIdCliente();
    if (idCliente == -1) {
        mostrarAlerta("Error", "Cliente no registrado. Por favor, verifique el DNI o registre al cliente.");
        return;
    }

     idHabitacion = Integer.parseInt(idHabitacionField.getText());
    LocalDate fechaEntrada = fechaEntradaField.getValue();
    LocalDate fechaSalida = fechaSalidaField.getValue();
     numPersonas = Integer.parseInt(numPersonasField.getText());

    // la lógica para grabar la reserva en la  base de datos
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
        conn = DataSourceManager.getConnection();
        String insertQuery = "INSERT INTO reservas (id_cliente, id_habitacion, FechaEntrada, FechaSalida, num_personas) VALUES (?, ?, ?, ?, ?)";
        pstmt = conn.prepareStatement(insertQuery);
        pstmt.setInt(1, idCliente);
        pstmt.setInt(2, idHabitacion);
        pstmt.setDate(3, java.sql.Date.valueOf(fechaEntrada));
        pstmt.setDate(4, java.sql.Date.valueOf(fechaSalida));
        pstmt.setInt(5, numPersonas);
        
        int affectedRows = pstmt.executeUpdate();
        if (affectedRows > 0) {
            mostrarAlerta("Reserva Exitosa", "La reserva ha sido creada con éxito.");
        } else {
            mostrarAlerta("Error al crear reserva", "No se pudo crear la reserva.");
        }
    } catch (SQLException e) {
        mostrarAlerta("Error de Base de Datos", "No se pudo completar la operación: " + e.getMessage());
    } finally {
        try {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
public void inicializarConDatosHabitacion(Habitacion habitacion) {
    if (habitacion != null) {
        // Asegúrate de que los nombres de los campos coincidan con los fx:id en tu archivo FXML
        idHabitacionField.setText(String.valueOf(habitacion.getId())); // Establece el ID de la habitación en el campo de texto correspondiente
        descripcionField.setText(habitacion.getDescripcion()); // Establece la descripción de la habitación en el campo de texto correspondiente
        capacidadField.setText(String.valueOf(habitacion.getCapacidad())); // Establece la capacidad de la habitación en el campo de texto correspondiente
        estadoField.setText(habitacion.getEstado()); // Establece el estado de la habitación en el campo de texto correspondiente
        // Puedes agregar más líneas aquí para establecer otros campos si es necesario
    }
}

public void mostrarFXMLReservas(Habitacion habitacionSeleccionada)   {
    // Carga el archivo FXML de la ventana de reserva
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservas/ReservaFXML.fxml"));
    Parent root = null;
        try {
            root = loader.load();
        } catch (java.io.IOException ex) {
            Logger.getLogger(ReservaController.class.getName()).log(Level.SEVERE, null, ex);
        }

    // Obtiene el controlador de la ventana de reserva y pasa los datos de la habitación
    ReservaController reservaController = loader.getController();
    reservaController.inicializarConDatosHabitacion(habitacionSeleccionada);

    // Crea una nueva ventana de reserva
    Stage stage = new Stage();
    stage.setTitle("Detalles de la Reserva");
    stage.setScene(new Scene(root));
    stage.show();
}



// Método para buscar el ID del cliente por DNI
private int obtenerIdCliente() {
    String dni = dniField.getText();
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    int idCliente = -1;
    try {
        conn = DataSourceManager.getConnection();
        String query = "SELECT id_cliente FROM clientes WHERE dni = ?";
        pstmt = conn.prepareStatement(query);
        pstmt.setString(1, dni);
        rs = pstmt.executeQuery();
        if (rs.next()) {
            idCliente = rs.getInt("id_cliente");
        } else {
            mostrarAlerta("Cliente No Encontrado", "No se encontró un cliente con el DNI proporcionado.");
        }
    } catch (SQLException e) {
        mostrarAlerta("Error de Base de Datos", "No se pudo consultar el cliente: " + e.getMessage());
    } finally {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return idCliente;
}

// Método para mostrar alertas
private void mostrarAlerta(String titulo, String mensaje) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(titulo);
    alert.setHeaderText(null);
    alert.setContentText(mensaje);
    alert.showAndWait();
}
}