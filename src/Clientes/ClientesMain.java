package Clientes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import utilidades.UsuarioInfo;

import java.io.IOException;

public class ClientesMain extends Application {
    private UsuarioInfo usuarioInfo;
    private int nivelAcceso;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        if (usuarioInfo != null) {
            nivelAcceso = usuarioInfo.getNivelAcceso();
            System.out.println("nivel_acceso_ Clientes= " + nivelAcceso);
            
            if (nivelAcceso == 1) {
                launchClientes(primaryStage); // Lanzar ventana de clientes
            } else {
                showNoAccessAlert();
            }
        } else {
            System.err.println("La información del usuario no se ha establecido.");
        }
    }

    public void setNivelAcceso(int nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
        System.out.println("Esto es " + nivelAcceso);
    }

    public void setUsuarioInfo(UsuarioInfo usuarioInfo) {
        this.usuarioInfo = usuarioInfo;
    }

    public void launchClientes(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientesFXML.fxml"));
            Parent root = loader.load();

            ClientesControlador controller = loader.getController();
            controller.setUsuarioInfo(usuarioInfo);

            Scene scene = new Scene(root);
            stage.setTitle("Clientes");
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
