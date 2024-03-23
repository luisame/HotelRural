package Reservas;

import Inicio.InicioController;
import java.io.IOException;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
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
    @FXML
    private Label descripcionHabitacionLabel;
    @FXML
    private Label labelPrecioTotal;

    private UsuarioInfo usuarioInfo;
    private ReservaController reservaController;

    public void setUsuarioInfo(UsuarioInfo usuarioInfo) {
        this.usuarioInfo = usuarioInfo;
    }

    public void setReservaController(ReservaController reservaController) {
        this.reservaController = reservaController;
    }

    @FXML
    public void initialize() {
        fechaInicio.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
                if (date.equals(LocalDate.now())) {
                    setStyle("-fx-background-color: lightgreen;");
                }
            }
        });

        tablaHabitacionesDisponibles.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                LocalDate fechaInicioValue = fechaInicio.getValue();
                LocalDate fechaFinValue = fechaFin.getValue();
                int numPersonasValue = Integer.parseInt(numeroPersonas.getText());
                mostrarFXMLReservas(newSelection, fechaInicioValue, fechaFinValue, numPersonasValue);
            }
        });
    }

    @FXML
    private void limpiarFormulario(ActionEvent event) {
        fechaInicio.setValue(null);
        fechaFin.setValue(null);
        numeroPersonas.clear();
        tablaHabitacionesDisponibles.getSelectionModel().clearSelection();
    }

    @FXML
    private void volverAlInicio(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Inicio/InicioFXML.fxml"));
            Parent root = loader.load();

            InicioController inicioController = loader.getController();
            inicioController.setUsuarioInfo(usuarioInfo);

            Stage stageActual = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stageActual.close();

            Stage stageInicio = new Stage();
            stageInicio.setTitle("Inicio");
            stageInicio.setScene(new Scene(root));
            stageInicio.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        ObservableList<Habitacion> habitacionesDisponibles = buscarHabitacionesDisponibles(inicio, fin, numPersonas);
        tablaHabitacionesDisponibles.setItems(habitacionesDisponibles);
        
    }

   private ObservableList<Habitacion> buscarHabitacionesDisponibles(LocalDate inicio, LocalDate fin, int numPersonas) {
    ObservableList<Habitacion> habitacionesDisponibles = FXCollections.observableArrayList();
// Calcular el precio total
    BigDecimal precioTotal = BigDecimal.ZERO;
    for (Habitacion habitacion : habitacionesDisponibles) {
        precioTotal = precioTotal.add(habitacion.getPrecio());
    }

    // Establecer el texto del precio total en el label correspondiente
    labelPrecioTotal.setText("Precio Total: $" + precioTotal.toString());

    try (Connection conn = DataSourceManager.getConnection();
         PreparedStatement stmt = conn.prepareStatement(
                 "SELECT id_habitacion, descripcion, capacidad, estado, precio " +
                         "FROM habitaciones " +
                         "WHERE estado = 'Libre' AND capacidad >= ? " +
                         "AND NOT EXISTS (SELECT 1 FROM reservas WHERE habitaciones.id_habitacion = reservas.id_habitacion " +
                         "AND (? BETWEEN FechaEntrada AND FechaSalida OR ? BETWEEN FechaEntrada AND FechaSalida))")) {

        stmt.setInt(1, numPersonas);
        stmt.setDate(2, java.sql.Date.valueOf(inicio));
        stmt.setDate(3, java.sql.Date.valueOf(fin));

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Habitacion habitacion = new Habitacion(
                        rs.getInt("id_habitacion"),
                        rs.getString("descripcion"),
                        rs.getInt("capacidad"),
                        rs.getString("estado"),
                        rs.getBigDecimal("precio"));
                habitacionesDisponibles.add(habitacion);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
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

    private void mostrarFXMLReservas(Habitacion habitacionSeleccionada, LocalDate fechaEntrada, LocalDate fechaSalida,
                                      int numPersonas) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservas/ReservaFXML.fxml"));
            Parent root = loader.load();

            ReservaController controller = loader.getController();
            controller.prepararDatosDeReserva(fechaEntrada, fechaSalida, numPersonas);
            controller.cargarDatosHabitacion(habitacionSeleccionada);
            Stage stage = new Stage();
            stage.setTitle("Detalles de la Reserva");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
