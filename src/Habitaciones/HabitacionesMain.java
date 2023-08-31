package Habitaciones;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilidades.UsuarioInfo;

import java.io.IOException;
import javafx.scene.control.Alert;

public class HabitacionesMain extends Application {
    private UsuarioInfo usuarioInfo;
    private int nivelAcceso;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        if (usuarioInfo != null && nivelAcceso >= 1) {
            launchHabitaciones(primaryStage); // Lanzar ventana de habitaciones
        } else {
            showNoAccessAlert();
        }
    }

    public void setNivelAcceso(int nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }

    public void setUsuarioInfo(UsuarioInfo usuarioInfo) {
        this.usuarioInfo = usuarioInfo;
    }

    public void launchHabitaciones(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HabitacionesFXML.fxml"));
            Parent root = loader.load();

            HabitacionesController controller = loader.getController();
            controller.setUsuarioInfo(usuarioInfo);

            Scene scene = new Scene(root);
            stage.setTitle("Habitaciones");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   private void showNoAccessAlert() {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle("Acceso denegado");
    alert.setHeaderText(null);
    alert.setContentText("No tienes permisos para acceder a esta funcionalidad.");
    alert.showAndWait();


    }
}
