package Reservas;

import Clientes.ClientesClass;
import Clientes.ClientesControllerNuevo;
import Inicio.InicioMain;
import Utilidades.HabitacionInfo;
import Utilidades.MensajeError;
import Utilidades.ReservaInfo;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Button;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import utilidades.BDUtilidades;
import utilidades.ClienteInfo;
import utilidades.UsuarioInfo;

public class ReservasController {

    @FXML
    private ComboBox<String> numHabitacionesComboBox;
    
    @FXML
    private TextField capacidadField;

    @FXML
    private DatePicker datePickerEntrada;

    @FXML
    private DatePicker datePickerSalida;

    @FXML
    private Button btnBuscarHabitaciones;

    @FXML
    private Button btnReservar;

    @FXML
    private TableView<HabitacionInfo> tablaHabitaciones;

    @FXML
    private TextField numeroTarjetaField;

    private int nivelAcceso;
    private UsuarioInfo usuarioInfo;
    private ClientesClass clienteSeleccionado;

    public void initialize() {
        // Configuración inicial del controlador al cargar la vista
        
        // Llenar el ComboBox con opciones
        numHabitacionesComboBox.getItems().addAll("1", "2", "3", "4");

        // Configurar DatePicker para evitar selección de fechas pasadas
        datePickerEntrada.setDayCellFactory(picker -> createDayCell());
        datePickerSalida.setDayCellFactory(picker -> createDayCell());
        
        // Configurar eventos para los botones
        btnBuscarHabitaciones.setOnAction(event -> buscarHabitaciones(event));
        btnReservar.setOnAction(event -> reservarHabitaciones());
    }

    private DateCell createDayCell() {
        // Configurar celdas de DatePicker para evitar selección de fechas pasadas
        return new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        };
    }

    public void setNivelAcceso(int nivelAcceso) {
        // Setter para nivel de acceso del usuario
        this.nivelAcceso = nivelAcceso;
    }

    public void setUsuarioInfo(UsuarioInfo usuarioInfo) {
        // Setter para la información del usuario actual
        this.usuarioInfo = usuarioInfo;
    }

    @FXML
    public void reservarHabitaciones() {
        // Acción al hacer clic en el botón "Reservar"
        
        String numHabitaciones = numHabitacionesComboBox.getValue();
        LocalDate entrada = datePickerEntrada.getValue();
        LocalDate salida = datePickerSalida.getValue();

        // Obtener valores y realizar la reserva
        if (clienteSeleccionado != null) {
            String dniCliente = clienteSeleccionado.getDni();
            int idHabitacion = tablaHabitaciones.getSelectionModel().getSelectedItem().getIdHabitacion();
            int idUsuario = usuarioInfo.getIdUsuario();
            int numPersonas = Integer.parseInt(numPersonasField.getText());

            double precioHabitacionDouble = tablaHabitaciones.getSelectionModel().getSelectedItem().getPrecio();
            BigDecimal precioHabitacion = BigDecimal.valueOf(precioHabitacionDouble);
            BigDecimal precioPersona = precioHabitacion.divide(BigDecimal.valueOf(numPersonas), 2, RoundingMode.HALF_UP);
            BigDecimal totalEstancia = precioHabitacion.multiply(BigDecimal.valueOf(numPersonas));

            String numeroTarjetaCredito = numeroTarjetaField.getText();
            Date fechaEntrada = fechaEntradaPicker.getValue();
            Date fechaSalida = fechaSalidaPicker.getValue();

            ReservaInfo reserva = new ReservaInfo(idReserva, idCliente, idHabitacion, idUsuario, numPersonas,
                precioPersona, totalEstancia, , numeroTarjetaCredito,
                fechaEntrada, fechaSalida);

            BDUtilidades.insertarReservaEnBaseDeDatos(reserva);
        } else {
            MensajeError.mostrarMensaje("Por favor, selecciona un cliente.", AlertType.WARNING);
        }
    }
    @FXML
    private void confirmarReserva(ActionEvent event) {
        try {
            // Cargar el FXML de la ventana de confirmación de reserva
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ConfirmarReservaFXML.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la ventana emergente
            ConfirmarReservaController confirmarReservaController = loader.getController();

            // Configurar los campos de la reserva en el controlador de la ventana emergente
            confirmarReservaController.setInfoReserva(numHabitacionesComboBox.getValue(), 
                                                      datePickerEntrada.getValue(),
                                                      datePickerSalida.getValue());

            // Crear una nueva escena y mostrar la ventana emergente
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


public class ConfirmarReservaController {
    @FXML
    private Label habitacionesLabel;
    
    @FXML
    private Label entradaLabel;
    
    @FXML
    private Label salidaLabel;
    
   

    public void setInfoReserva(String numHabitaciones, LocalDate entrada, LocalDate salida) {
        habitacionesLabel.setText(numHabitaciones);
        entradaLabel.setText(entrada.toString());
        salidaLabel.setText(salida.toString());
    }

    @FXML
    private void confirmar(ActionEvent event) {
        // Realizar acciones para confirmar la reserva (guardar en base de datos, etc.)
        // Cerrar la ventana emergente
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    private void cancelar(ActionEvent event) {
        // Cerrar la ventana emergente sin realizar la reserva
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }
}


    @FXML
    private void buscarHabitaciones(ActionEvent event) {
        // Acción al hacer clic en el botón "Buscar Habitaciones"
        
        int capacidad = Integer.parseInt(capacidadField.getText());
        LocalDate entrada = datePickerEntrada.getValue();
        LocalDate salida = datePickerSalida.getValue();

        List<HabitacionInfo> habitaciones = BDUtilidades.obtenerHabitacionesDesdeDB(entrada, salida, capacidad);
        ObservableList<HabitacionInfo> habitacionesList = FXCollections.observableArrayList(habitaciones);
        tablaHabitaciones.setItems(habitacionesList);
    }

    @FXML
    private void buscarClientes(ActionEvent event) throws SQLException {
        // Acción al hacer clic en el botón "Buscar Clientes"
        
        ClientesControllerNuevo clientesController = new ClientesControllerNuevo();
        clientesController.buscarClientes();
    }

    @FXML
    private void cerrarMenu(ActionEvent event) {
        // Acción al hacer clic en el botón "Cerrar"
        
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            InicioMain inicioMain = new InicioMain();
            inicioMain.start(new Stage());

        } catch (Exception e) {
            MensajeError.mostrarMensaje("Error al cerrar la ventana: " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
    }
}

