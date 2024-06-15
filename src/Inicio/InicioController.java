package Inicio;

import Administracion.AdministracionMain;
import Habitaciones.HabitacionesMain;
import Clientes.ClientesMain;
import Reservas.ReservasMain;
import java.util.Optional;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import utilidades.UsuarioInfo;

/**
 * Controlador para la ventana de inicio de la aplicación.
 */
public class InicioController {

    @FXML
    private Button btnHabitaciones;

    @FXML
    private Button btnClientes;

    @FXML
    private Button btnAdministracion;

    @FXML
    private Button btnExit;

    public UsuarioInfo usuarioInfo;

    /**
     * Establece la información del usuario en el controlador.
     *
     * @param usuarioInfo la información del usuario a establecer.
     */
    public void setUsuarioInfo(UsuarioInfo usuarioInfo) {
        // Obtiene la instancia única de UsuarioInfo, actualizando los campos
        UsuarioInfo.getInstance().setIdUsuario(usuarioInfo.getIdUsuario());
        UsuarioInfo.getInstance().setNivelAcceso(usuarioInfo.getNivelAcceso());
        UsuarioInfo.getInstance().setNombreEmpleado(usuarioInfo.getNombreEmpleado());
        UsuarioInfo.getInstance().setApellidoEmpleado(usuarioInfo.getApellidoEmpleado());

        this.usuarioInfo = usuarioInfo;
        System.out.println("UsuarioInfo: " + usuarioInfo);
        Platform.runLater(() -> {
            // Aquí puedes actualizar la interfaz de usuario si es necesario
        });
    }

    /**
     * Maneja el evento del botón de habitaciones.
     *
     * @param event el evento de acción.
     */
    @FXML
    private void handleBtnHabitaciones(ActionEvent event) {
        HabitacionesMain habitaciones = new HabitacionesMain();
        habitaciones.setUsuarioInfo(usuarioInfo);
        habitaciones.start(new Stage());

        closeCurrentStage(event);
    }

    /**
     * Maneja el evento del botón de clientes.
     *
     * @param event el evento de acción.
     */
    @FXML
    private void handleBtnClientes(ActionEvent event) {
        ClientesMain clientes = new ClientesMain();
        clientes.setUsuarioInfo(usuarioInfo);
        clientes.start(new Stage());

        closeCurrentStage(event);
    }

    /**
     * Maneja el evento del botón de reservas.
     *
     * @param event el evento de acción.
     * @throws Exception si ocurre un error durante el inicio de la ventana de reservas.
     */
    @FXML
    private void handleBtnReservas(ActionEvent event) throws Exception {
        ReservasMain reservas = new ReservasMain();
        reservas.setUsuarioInfo(usuarioInfo);
        reservas.start(new Stage());

        closeCurrentStage(event);
    }

    /**
     * Maneja el evento del botón de administración.
     *
     * @param event el evento de acción.
     */
    @FXML
    private void handleBtnAdministracion(ActionEvent event) {
        AdministracionMain administracion = new AdministracionMain();
        administracion.setUsuarioInfo(usuarioInfo);
        System.out.println("Datos " + usuarioInfo);
        administracion.start(new Stage());

        closeCurrentStage(event);
    }

    /**
     * Maneja el evento del botón de salida.
     *
     * @param event el evento de acción.
     */
    @FXML
    private void handleBtnExit(ActionEvent event) {
        // Crear una alerta de confirmación
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Salida");
        alert.setHeaderText(null); // Puedes poner aquí un texto de cabecera si lo deseas
        alert.setContentText("¿Estás seguro de que quieres salir?");

        // Personalización del botón
        ButtonType buttonTypeYes = new ButtonType("Sí", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.NO);

        // Añadir botones al cuadro de diálogo
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Capturar la respuesta del usuario
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeYes) {
            Stage stage = (Stage) btnExit.getScene().getWindow();
            stage.close();
        }
        // Si el usuario selecciona "No", simplemente se cierra el cuadro de diálogo y no sucede nada más.
    }

    /**
     * Cierra la ventana actual.
     *
     * @param event el evento de acción.
     */
    private void closeCurrentStage(ActionEvent event) {
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
}
