package Habitaciones;

import java.io.IOException;

import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utilidades.UsuarioInfo;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;

public class HabitacionesController {
   @FXML
    private ComboBox<String> comboEstadoHabitacion;

    @FXML
    private ComboBox<String> comboNumeroHabitacion;
    
    @FXML
    private TableView<Habitacion> tablaHabitaciones;

    // Campos para modificar habitaciones
    @FXML
    private TextField idHabitacionModificar, descripcionModificar, capacidadModificar;
    @FXML
    private ComboBox<String> tipoHabitacionModificar, estadoModificar;

    // Campos para agregar habitaciones
    @FXML
    private TextField tipoHabitacionAgregar, descripcionAgregar, capacidadAgregar;
    @FXML
    private ComboBox<String> estadoAgregar;
    private UsuarioInfo usuarioInfo;

    @FXML
private Button botonVolver; // Este es el botón definido en tu FXML
@FXML
    public void initialize() {
        comboEstadoHabitacion.setItems(FXCollections.observableArrayList("Libre", "Reservada", "Ocupada"));
        // Aquí puedes configurar comboNumeroHabitacion con números específicos, si es necesario
        cargarDatosTabla();
        colorearFilasSegunEstado();
    }

    @FXML
    private void buscarPorEstado() {
        // Lógica de búsqueda por estado
    }

    @FXML
    private void buscarPorNumero() {
        // Lógica de búsqueda por número
    }

    // Otros métodos y lógica del controlador


// Método para establecer la información del usuario
    public void setUsuarioInfo(UsuarioInfo usuarioInfo) {
        this.usuarioInfo = usuarioInfo;
    }
    

    private void cargarDatosTabla() {
        // Cargar los datos de las habitaciones desde la base de datos
        ObservableList<Habitacion> habitaciones = FXCollections.observableArrayList();
        // Llenar habitaciones con datos reales desde la base de datos
        tablaHabitaciones.setItems(habitaciones);
    }

    private void colorearFilasSegunEstado() {
        tablaHabitaciones.setRowFactory(tv -> new TableRow<Habitacion>() {
            @Override
            protected void updateItem(Habitacion habitacion, boolean empty) {
                super.updateItem(habitacion, empty);
                if (habitacion == null) {
                    setStyle("");
                } else if (habitacion.getEstado().equals("Libre")) {
                    setStyle("-fx-background-color: green;");
                } else if (habitacion.getEstado().equals("Reservada")) {
                    setStyle("-fx-background-color: orange;");
                } else if (habitacion.getEstado().equals("Ocupada")) {
                    setStyle("-fx-background-color: red;");
                } else {
                    setStyle("");
                }
            }
        });
    }
    @FXML
    private void buscarHabitacion() {
        // Implementar la lógica para agregar una nueva habitación
        // Recoger y validar los campos
        // Insertar la nueva habitación en la base de datos
    }
    @FXML
    private void modificarHabitacion() {
        // Implementar la lógica para modificar una habitación existente
        // Recoger y validar los campos
        // Actualizar la habitación en la base de datos
    }

    @FXML
    private void agregarHabitacion() {
        // Implementar la lógica para agregar una nueva habitación
        // Recoger y validar los campos
        // Insertar la nueva habitación en la base de datos
    }
   
     

    @FXML
private void volverAlInicio() {
    try {
        // Cargar la vista principal
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Habitaciones/InicioMain.fxml"));
        Parent root = loader.load();

        // Obtener la escena actual y establecer la nueva vista
        Stage stage = (Stage) botonVolver.getScene().getWindow();
        stage.setScene(new Scene(root));

        // Opcionalmente, establecer el título del escenario y mostrarlo
        stage.setTitle("Inicio");
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
    
}

}
