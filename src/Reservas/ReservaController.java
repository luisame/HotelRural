package Reservas;

import Inicio.InicioController;
import Inicio.InicioMain;
import static Reservas.DisponibilidadController.disponibilidadController;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import utilidades.DataSourceManager;
import utilidades.UsuarioInfo;

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
     @FXML
    private TableView<Habitacion> tablaHabitacionesReservadas;
    @FXML
    private TableColumn<Habitacion, String> columnaDescripcion;
    @FXML
    private TableColumn<Habitacion, Integer> columnaCapacidad;
    @FXML
    private TableColumn<Habitacion, String> columnaEstado;
     // Variables de control
    private UsuarioInfo usuarioInfo;
    // Este método se llamaría para cargar los datos en la tabla
    private int idHabitacion;
    public Label precioBaseLabel;
    public Label numPersonasLabel;
    public Label numNinosLabel;
    public Label numBebesLabel;
    public Label suplementoLabel;
    public Label precioTotalLabel;

    public void setHabitacionId(int idHabitacion) {
        this.idHabitacion = idHabitacion;
        
    }
    // Métodos setter
    public void setPrecioBase(BigDecimal precio) {
        precioBaseLabel.setText(String.format("€%.2f", precio));
    }
    
    public void setNumPersonas(int numPersonas) {
        numPersonasLabel.setText(String.valueOf(numPersonas));
    }
    
    public void setNumNinos(int numNinos) {
        numNinosLabel.setText(String.valueOf(numNinos));
    }
    
    public void setNumBebes(int numBebes) {
        numBebesLabel.setText(String.valueOf(numBebes));
    }
    
   public void setSuplemento(BigDecimal suplemento) {
    if (suplementoLabel != null) {
        suplementoLabel.setText(String.format("€%.2f", suplemento));
    } else {
        System.err.println("suplementoLabel no está inicializado.");
    }
}

     @FXML
    public void initialize() {
    configurarListenersDatosCliente();
    
    // Configurar el CellValueFactory para cada columna
    columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
    columnaCapacidad.setCellValueFactory(new PropertyValueFactory<>("capacidad"));
    columnaEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
    
    // Crear las columnas y configurar el CellValueFactory
    TableColumn<Habitacion, String> descripcionColumn = new TableColumn<>("Descripción");
    descripcionColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

    TableColumn<Habitacion, Integer> capacidadColumn = new TableColumn<>("Capacidad");
    capacidadColumn.setCellValueFactory(new PropertyValueFactory<>("capacidad"));

    TableColumn<Habitacion, String> estadoColumn = new TableColumn<>("Estado");
    estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));

    // Agregar las columnas a la tabla
    tablaHabitacionesReservadas.getColumns().addAll(descripcionColumn, capacidadColumn, estadoColumn);

    tablaHabitacionesReservadas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
        if (newSelection != null) {
            mostrarDatosHabitacionSeleccionada(newSelection);
        }
        if (tablaHabitacionesReservadas == null) {
        System.out.println("tablaHabitacionesReservadas es nulo.");
       
        // Puedes agregar más mensajes o acciones aquí según sea necesario
        return;
    }
        if (columnaDescripcion == null || columnaCapacidad == null || columnaEstado == null) {
        System.out.println("Al menos una de las columnas es nula.");
        // Puedes agregar más mensajes o acciones aquí según sea necesario
        return;
        }
    });
}


    // Método para cargar los datos en la tabla
    public void cargarDatosHabitacion(Habitacion habitacion) {
    ObservableList<Habitacion> habitacionesReservadas = FXCollections.observableArrayList();
    habitacionesReservadas.add(habitacion); // Añadir la habitación seleccionada a la lista
    tablaHabitacionesReservadas.setItems(habitacionesReservadas); // Establecer los datos en la tabla

    }


    public void mostrarDatosHabitacionSeleccionada(Habitacion habitacionSeleccionada) {
        descripcionField.setText(habitacionSeleccionada.getDescripcion());
        capacidadField.setText(String.valueOf(habitacionSeleccionada.getCapacidad()));
        estadoField.setText(habitacionSeleccionada.getEstado());
        System.out.println("Habitacion ID:  "+" " + idHabitacion);
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


    // Método para inicializar la vista con los datos de la habitación seleccionada
   public void iniciarConDatosDeReserva(Habitacion habitacionSeleccionada, LocalDate fechaEntrada, LocalDate fechaSalida, int numPersonas) {
    if (habitacionSeleccionada != null) {
      
    // Actualiza los campos de la interfaz con la información de la reserva
    fechaEntradaField.setValue(fechaEntrada);
    fechaSalidaField.setValue(fechaSalida);
    numPersonasField.setText(String.valueOf(numPersonas));
// Suponiendo que Habitacion es una clase que tiene las propiedades descripcion, capacidad, y estado.
    ObservableList<Habitacion> data = FXCollections.observableArrayList(habitacionSeleccionada);
    tablaHabitacionesReservadas.setItems(data);
    }
   }
    


    // Continuación desde la parte de initData...
public void initData(LocalDate fechaInicioVal, LocalDate fechaFinVal, int numPersonas) {
    fechaEntradaField.setValue(fechaInicioVal);
    fechaSalidaField.setValue(fechaFinVal);
    numPersonasField.setText(String.valueOf(numPersonas));
}

public void crearReserva(ActionEvent event) {
    // Obtener el ID del cliente
    int idCliente = obtenerIdCliente();
    
    // Verificar si el ID del cliente es válido
    if (idCliente == -1) {
        mostrarAlerta("Error", "Cliente no registrado. Por favor, verifique el DNI o registre al cliente.");
        return;
    }
// Obtener la habitación seleccionada de la tabla
    Habitacion habitacionSeleccionada = tablaHabitacionesReservadas.getSelectionModel().getSelectedItem();
    // Verificar si se han completado todos los campos requeridos
    if (idHabitacionField.getText().isEmpty() || numPersonasField.getText().isEmpty()) {
        mostrarAlerta("Error", "Por favor, complete todos los campos requeridos.");
        return;
    }
    
    // Obtener los valores numéricos
    int  numPersonas;
    try {
        idHabitacion = Integer.parseInt(idHabitacionField.getText());
        numPersonas = Integer.parseInt(numPersonasField.getText());
    } catch (NumberFormatException e) {
        mostrarAlerta("Error de Formato", "Por favor, ingrese valores numéricos válidos.");
        return;
    }

    // Obtener las fechas de entrada y salida
    LocalDate fechaEntrada = fechaEntradaField.getValue();
    LocalDate fechaSalida = fechaSalidaField.getValue();

    // Conectar a la base de datos y ejecutar la consulta de inserción
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
        
        // Ejecutar la consulta de inserción
        int affectedRows = pstmt.executeUpdate();
        if (affectedRows > 0) {
            mostrarAlerta("Reserva Exitosa", "La reserva ha sido creada con éxito.");
        } else {
            mostrarAlerta("Error al crear reserva", "No se pudo crear la reserva.");
        }
    } catch (SQLException e) {
        mostrarAlerta("Error de Base de Datos", "No se pudo completar la operación: " + e.getMessage());
    } finally {
        // Cerrar la conexión y liberar los recursos
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

// Método para obtener la información del usuario actual
private void volverAlInicio(ActionEvent event) {
        try {
            // Load the Inicio FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Disponibilidad/DisponibilidadFXML.fxml"));
            Parent root = loader.load();

            // Get the controller for the Inicio FXML
            InicioController inicioController = loader.getController();

            // Here you can pass the UsuarioInfo to
            // the InicioController
            inicioController.setUsuarioInfo(this.usuarioInfo);

            // Close the current window
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

            // Open the Inicio window
            Stage inicioStage = new Stage();
            inicioStage.setTitle("Inicio");
            inicioStage.setScene(new Scene(root));
            inicioStage.show();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            // Handle exceptions here, maybe show an alert dialog
        }
    }



// Método para mostrar alertas
private void mostrarAlerta(String titulo, String mensaje) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(titulo);
    alert.setHeaderText(null);
    alert.setContentText(mensaje);
    alert.showAndWait();
}

    public void setPrecioTotal(BigDecimal precioTotal) {
    // Asegúrate de que el fx:id de precioTotalLabel está correctamente vinculado en tu archivo FXML.
    if (precioTotalLabel != null) {
        // Aquí convertimos el BigDecimal a String, formateando para tener dos decimales.
        precioTotalLabel.setText(String.format("€%.2f", precioTotal));
    } else {
        // Esto es solo para debug. Si ves este mensaje significa que precioTotalLabel no está vinculado.
        System.err.println("precioTotalLabel no está inicializado.");
    }
    }
    
}