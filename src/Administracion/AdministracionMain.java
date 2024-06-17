package Administracion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilidades.UsuarioInfo;
import java.io.IOException;
import javafx.scene.control.Alert;

/**
 * Clase principal de la aplicación de administración.
 * Extiende la clase Application de JavaFX.
 */
public class AdministracionMain extends Application {
    private UsuarioInfo usuarioInfo;
    private int nivelAcceso;

    /**
     * Método principal de la aplicación.
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Método start que se ejecuta al iniciar la aplicación.
     * Comprueba el nivel de acceso del usuario y lanza la ventana de administración
     * si el nivel de acceso es adecuado.
     * 
     * @param primaryStage El escenario principal de la aplicación.
     */
    @Override
    public void start(Stage primaryStage) {
        if (usuarioInfo != null) {
            nivelAcceso = usuarioInfo.getNivelAcceso();
            System.out.println("Nivel de acceso para Administración: " + nivelAcceso);
            System.out.println("Información del usuario: " + usuarioInfo.toString());

            if (nivelAcceso == 1) {
                launchAdministracion(primaryStage); // Lanzar ventana de administración
            } else {
                showNoAccessAlert();
            }
        } else {
            System.err.println("La información del usuario no se ha establecido.");
        }
    }

    /**
     * Establece el nivel de acceso del usuario.
     * @param nivelAcceso El nivel de acceso del usuario.
     */
    public void setNivelAcceso(int nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }

    /**
     * Establece la información del usuario.
     * @param usuarioInfo La información del usuario.
     */
    public void setUsuarioInfo(UsuarioInfo usuarioInfo) {
        this.usuarioInfo = usuarioInfo;
    }

    /**
     * Lanza la ventana de administración.
     * Carga la interfaz de usuario desde el archivo FXML y configura el controlador.
     * 
     * @param stage El escenario en el que se mostrará la ventana de administración.
     */
    public void launchAdministracion(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdministracionFXML.fxml"));
            Parent root = loader.load();

            BBDDController controller = loader.getController();
            controller.setUsuarioInfo(usuarioInfo);

            Scene scene = new Scene(root);
            stage.setTitle("Administración");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra una alerta indicando que el acceso está denegado.
     */
    private void showNoAccessAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Acceso denegado");
        alert.setHeaderText(null);
        alert.setContentText("No tienes permisos para acceder a esta funcionalidad.");
        alert.showAndWait();
    }
}
