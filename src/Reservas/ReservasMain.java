package Reservas;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilidades.UsuarioInfo;

import java.io.IOException;
import javafx.scene.control.Alert;
/**
 * La clase ReservasMain es el punto de entrada principal para la aplicación de gestión
 * de reservas de un hotel rural. Extiende la clase Application de JavaFX y configura 
 * la interfaz de usuario inicial y la lógica principal de la aplicación.
 */
public class ReservasMain extends Application {
     private UsuarioInfo usuarioInfo;
    private int nivelAcceso;
/**
     * El método main es el punto de entrada estándar para la aplicación Java.
     * Llama al método launch para iniciar la aplicación JavaFX.
     *
     * @param args Los argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        launch(args);
    }
 /**
     * El método start se invoca cuando la aplicación JavaFX comienza. Configura el 
     * escenario principal y carga la interfaz de usuario desde un archivo FXML.
     *
     * @param primaryStage El escenario principal para esta aplicación.
     * @throws Exception Si ocurre un error durante la carga del archivo FXML.
     */
    @Override
    public void start(Stage primaryStage) {
        if (usuarioInfo != null) {
            nivelAcceso = usuarioInfo.getNivelAcceso();
            System.out.println("nivel_acceso_ Reservas= " + nivelAcceso);
            
            if (nivelAcceso == 1) {
                launchReservas(primaryStage); // Lanzar ventana de clientes
            } else {
                showNoAccessAlert();
            }
        } else {
            System.err.println("La informaciÃ³n del usuario no se ha establecido.");
        }
    }
/**
 * Establece el nivel de acceso para el usuario actual.
 * Este nivel de acceso determina las funcionalidades disponibles para el usuario.
 *
 * @param nivelAcceso El nivel de acceso del usuario.
 */
    public void setNivelAcceso(int nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
        System.out.println("Esto es " + nivelAcceso);
    }
/**
 * Establece la información del usuario actual.
 * La información del usuario se utiliza para personalizar la experiencia del usuario dentro de la aplicación.
 *
 * @param usuarioInfo Un objeto UsuarioInfo que contiene la información del usuario.
 */
    public void setUsuarioInfo(UsuarioInfo usuarioInfo) {
        this.usuarioInfo = usuarioInfo;
    }
/**
 * Lanza la interfaz de gestión de reservas en la ventana proporcionada.
 * Carga el archivo FXML correspondiente, configura el controlador y muestra la escena.
 *
 * @param stage El escenario en el que se mostrará la interfaz de gestión de reservas.
 */
    public void launchReservas(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DisponibilidadFXML.fxml"));
            Parent root = loader.load();

            DisponibilidadController controller = loader.getController();
            controller.setUsuarioInfo(usuarioInfo);

            Scene scene = new Scene(root);
            stage.setTitle("Clientes");
            stage.setScene(scene);
            stage.show();
         } catch (IOException e) {
        mostrarErrorCarga();
         }
    }
    /**
 * Muestra una alerta de error indicando que hubo un problema al cargar la interfaz.
 */
    private void mostrarErrorCarga() {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error de Carga");
    alert.setHeaderText(null);
    alert.setContentText("Hubo un problema al cargar la interfaz de gestión de reservas. Por favor, intente nuevamente.");
    alert.showAndWait();
}
/**
 * Muestra una alerta de advertencia indicando que el acceso a una funcionalidad está denegado.
 * La alerta informa al usuario que no tiene permisos suficientes para acceder a la funcionalidad solicitada.
 */
    private void showNoAccessAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Acceso denegado");
        alert.setHeaderText(null);
        alert.setContentText("No tienes permisos para acceder a esta funcionalidad.");
        alert.showAndWait();
    }
}
