package Reservas;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class ReservasController {
    @FXML
    private DatePicker fechaInicio;
    @FXML
    private DatePicker fechaFin;
    @FXML
    private TextField numeroPersonas;
    @FXML
    private TableView<Habitacion> tablaHabitacionesDisponibles;

    // Inicializa tu controlador aquí
    @FXML
    public void initialize() {
        // Puedes inicializar aquí elementos que requieran configuración inicial
    }

    @FXML
    private void buscarDisponibilidad() {
        LocalDate inicio = fechaInicio.getValue();
        LocalDate fin = fechaFin.getValue();
        int numPersonas = Integer.parseInt(numeroPersonas.getText());

        // Aquí implementarías la lógica para buscar habitaciones disponibles
        // Por ejemplo, realizar una consulta a la base de datos

        // Supongamos que este método devuelve una lista de habitaciones disponibles
        ObservableList<Habitacion> habitacionesDisponibles = obtenerHabitacionesDisponibles(inicio, fin, numPersonas);

        tablaHabitacionesDisponibles.setItems(habitacionesDisponibles);
    }

    private ObservableList<Habitacion> obtenerHabitacionesDisponibles(LocalDate inicio, LocalDate fin, int numPersonas) {
        // Simula obtener habitaciones disponibles
        // En un caso real, aquí conectarías con tu base de datos o lógica de negocio
        ObservableList<Habitacion> habitaciones = FXCollections.observableArrayList();
        // Añade habitaciones a la lista
        return habitaciones;
    }

    // Clase interna para representar los datos de una habitación
    public static class Habitacion {
        private String id;
        private String tipo;
        private int capacidad;
        private String estado;

        // Constructor, getters y setters
    }
}
