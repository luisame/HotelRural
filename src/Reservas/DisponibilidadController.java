package Reservas;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utilidades.DataSourceManager;
import utilidades.UsuarioInfo;

public class DisponibilidadController {
    @FXML
    private DatePicker fechaInicio;
    @FXML
    private DatePicker fechaFin;
    @FXML
    private TextField numeroPersonas;
    @FXML
    private TableView<Habitacion> tablaHabitacionesDisponibles;
    private UsuarioInfo usuarioInfo;
    @FXML
    private Label descripcionHabitacionLabel;

    public void setDescripcionHabitacion(String descripcion) {
        descripcionHabitacionLabel.setText(descripcion);
    }

    @FXML
    public void initialize() {
        // Configurar el DatePicker de fecha de inicio
        fechaInicio.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
                if (date.equals(LocalDate.now())) {
                    // Si el día seleccionado es hoy, mostrarlo en verde
                    setStyle("-fx-background-color: lightgreen;");
                }
            }
        });

        // Añadir listener a la selección de la tabla de habitaciones disponibles
        tablaHabitacionesDisponibles.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                LocalDate fechaInicioValue = fechaInicio.getValue();
                LocalDate fechaFinValue = fechaFin.getValue();
                int numPersonasValue = Integer.parseInt(numeroPersonas.getText());
                mostrarFXMLReservas(newSelection, fechaInicioValue, fechaFinValue, numPersonasValue);
            }
        });
    }

private ReservaController reservaController;

public void setReservaController(ReservaController reservaController) {
    this.reservaController = reservaController;
}

@FXML
private void handleTablaHabitacionesDisponiblesMouseClicked(MouseEvent event) {
    if (event.getClickCount() == 1) {
        Habitacion habitacionSeleccionada = tablaHabitacionesDisponibles.getSelectionModel().getSelectedItem();
        if (habitacionSeleccionada != null && reservaController != null) {
            reservaController.mostrarDatosHabitacionSeleccionada(habitacionSeleccionada);
        }
    }
}

    private void mostrarFXMLReservas(Habitacion habitacionSeleccionada, LocalDate fechaEntrada, LocalDate fechaSalida,
            int numPersonas) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservas/ReservaFXML.fxml"));
            Parent root = loader.load();

            ReservaController controller = loader.getController();
            controller.prepararDatosDeReserva(fechaEntrada, fechaSalida, numPersonas);

            Stage stage = new Stage();
            stage.setTitle("Detalles de la Reserva");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void buscarDisponibilidad() {
        LocalDate inicio = fechaInicio.getValue();
        LocalDate fin = fechaFin.getValue();
        int numPersonas = 0;

        try {
            numPersonas = Integer.parseInt(numeroPersonas.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Por favor, ingrese un número válido para el número de personas.");
            return;
        }

        if (inicio == null || fin == null) {
            mostrarAlerta("Error", "Por favor, seleccione las fechas de inicio y fin.");
            return;
        }

        if (inicio.isAfter(fin)) {
            mostrarAlerta("Error", "La fecha de inicio no puede ser posterior a la fecha de fin.");
            return;
        }

        // Utilizar el método para buscar habitaciones disponibles
        ObservableList<Habitacion> habitacionesDisponibles = buscarHabitacionesDisponibles(inicio, fin, numPersonas);

        // Establecer los resultados en la tabla
        tablaHabitacionesDisponibles.setItems(habitacionesDisponibles);
    }

    private ObservableList<Habitacion> buscarHabitacionesDisponibles(LocalDate inicio, LocalDate fin,
            int numPersonas) {
        ObservableList<Habitacion> habitacionesDisponibles = FXCollections.observableArrayList();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Obtener conexión desde el DataSourceManager
            conn = DataSourceManager.getConnection();

            // Consulta SQL para obtener habitaciones disponibles
            String query = "SELECT * FROM habitaciones WHERE estado = 'Libre' AND capacidad >= ? "
                    + "AND id_habitacion NOT IN (SELECT id_habitacion FROM reservas "
                    + "WHERE ? BETWEEN FechaEntrada AND FechaSalida OR ? BETWEEN FechaEntrada AND FechaSalida)";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, numPersonas);
            stmt.setDate(2, java.sql.Date.valueOf(inicio));
            stmt.setDate(3, java.sql.Date.valueOf(fin));

            rs = stmt.executeQuery();

            // Procesar los resultados y crear objetos Habitacion
            while (rs.next()) {
                int id = rs.getInt("id_habitacion");
                String descripcion = rs.getString("descripcion");
                int capacidad = rs.getInt("capacidad");
                String estado = rs.getString("estado");
                Habitacion habitacion = new Habitacion(id, descripcion, capacidad, estado);
                habitacionesDisponibles.add(habitacion);


                habitacionesDisponibles.add(habitacion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Cerrar recursos
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return habitacionesDisponibles;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public void setUsuarioInfo(UsuarioInfo usuarioInfo) {
        this.usuarioInfo = usuarioInfo;
    }

    @FXML
    private void seleccionarHabitacion() {
        Habitacion habitacionSeleccionada = tablaHabitacionesDisponibles.getSelectionModel().getSelectedItem();

        if (habitacionSeleccionada != null) {
            LocalDate fechaInicioValue = fechaInicio.getValue();
            LocalDate fechaFinValue = fechaFin.getValue();
            int numPersonasValue = Integer.parseInt(numeroPersonas.getText());
            mostrarFXMLReservas(habitacionSeleccionada, fechaInicioValue, fechaFinValue, numPersonasValue);
        }
    }
    
    @FXML
private void crearReserva(ActionEvent event) throws IOException {
    LocalDate fechaInicioVal = fechaInicio.getValue();
    LocalDate fechaFinVal = fechaFin.getValue();
    int numPersonasVal;
    try {
        numPersonasVal = Integer.parseInt(numeroPersonas.getText()); // Convierte el texto a entero
    } catch (NumberFormatException e) {
        mostrarAlerta("Error de Entrada", "El número de personas debe ser un entero válido.");
        return; // Salir del método si la conversión falla
    }

    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservas/ReservasFXML.fxml"));
    Parent root = loader.load();
    ReservaController reservaController = loader.getController();
    // Ahora se pasan los valores extraídos y convertidos
    reservaController.initData(fechaInicioVal, fechaFinVal, numPersonasVal);

    Stage stage = new Stage();
    stage.setTitle("Crear Reserva");
    stage.setScene(new Scene(root));
    stage.show();
}
}

