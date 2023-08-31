package Administracion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import utilidades.UsuarioInfo;

import java.io.IOException;

public class AdministracionMain extends Application {
    private UsuarioInfo usuarioInfo;
    private int nivelAcceso;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        int nivelAcceso = usuarioInfo.getNivelAcceso();
        System.out.println("nivel_acceso_ Administración= " + nivelAcceso);
        if (usuarioInfo != null && nivelAcceso == 1) {
            launchAdministracion(primaryStage); // Lanzar ventana de administración
        } else {
            showNoAccessAlert();
        }
    }

    public void setNivelAcceso(int nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
        System.out.println("Esto es   " + nivelAcceso);
    }

    public void setUsuarioInfo(UsuarioInfo usuarioInfo) {
        this.usuarioInfo = usuarioInfo;
    }

    public void launchAdministracion(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdministracionFXML.fxml"));
            Parent root = loader.load();

            AdministracionController controller = loader.getController();
            controller.setUsuarioInfo(usuarioInfo);

            Scene scene = new Scene(root);
            stage.setTitle("Administración");
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
