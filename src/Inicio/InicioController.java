package Inicio;

import Administracion.AdministracionMain;
import Habitaciones.HabitacionesMain;
import Clientes.ClientesMain;
import Reservas.ReservasMain;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import utilidades.UsuarioInfo;

public class InicioController {
    @FXML
    private Label nombreUsuarioLabel;

    @FXML
    private Button btnHabitaciones;

    @FXML
    private Button btnClientes;

    @FXML
    private Button btnAdministracion;

    @FXML
    private Button btnReservas;

    @FXML
    private Button btnExit;

    @FXML
    private Label etiquetaNombre;

    @FXML
    private Label etiquetaApellido;

    public  UsuarioInfo usuarioInfo;
     public void setUsuarioInfo(UsuarioInfo usuarioInfo) {
        UsuarioInfo.getInstance().setIdUsuario(usuarioInfo.getIdUsuario());
        UsuarioInfo.getInstance().setNivelAcceso(usuarioInfo.getNivelAcceso());
        UsuarioInfo.getInstance().setNombreEmpleado(usuarioInfo.getNombreEmpleado());
        UsuarioInfo.getInstance().setApellidoEmpleado(usuarioInfo.getApellidoEmpleado());

        this.usuarioInfo = usuarioInfo;
        System.out.println("UsuarioInfo: "+ usuarioInfo);
        Platform.runLater(() -> {
            nombreUsuarioLabel.setText("Bienvenido, " + usuarioInfo.getNombreEmpleado());
            etiquetaNombre.setText(usuarioInfo.getNombreEmpleado());
            etiquetaApellido.setText(usuarioInfo.getApellidoEmpleado());
            
            
        });
    }

    @FXML
    private void handleBtnHabitaciones(ActionEvent event) {
        HabitacionesMain habitaciones = new HabitacionesMain();
        habitaciones.setUsuarioInfo(usuarioInfo);
        habitaciones.start(new Stage());

        closeCurrentStage(event);
    }

    @FXML
    private void handleBtnClientes(ActionEvent event) {
        ClientesMain clientes = new ClientesMain();
        clientes.setUsuarioInfo(usuarioInfo);
        clientes.start(new Stage());

        closeCurrentStage(event);
    }

    @FXML
    private void handleBtnReservas(ActionEvent event) throws Exception {
        ReservasMain reservas = new ReservasMain();
        reservas.setUsuarioInfo(usuarioInfo);
        reservas.start(new Stage());

        closeCurrentStage(event);
    }

    @FXML
    private void handleBtnAdministracion(ActionEvent event) {
        AdministracionMain administracion = new AdministracionMain();
        administracion.setUsuarioInfo(usuarioInfo);
        System.out.println("Datos  "+ (usuarioInfo) );
        administracion.start(new Stage());

        closeCurrentStage(event);
    }

    @FXML
    private void handleBtnExit(ActionEvent event) {
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.close();
    }

    private void closeCurrentStage(ActionEvent event) {
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
}
