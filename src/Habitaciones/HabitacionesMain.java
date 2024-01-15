package Habitaciones;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import utilidades.UsuarioInfo;

import java.io.IOException;

public class HabitacionesMain extends Application {
    private UsuarioInfo usuarioInfo; // Información del usuario
    private int nivelAcceso; // Nivel de acceso del usuario

    public static void main(String[] args) {
        launch(args); // Para iniciar la aplicación JavaFX
    }

    @Override
    public void start(Stage primaryStage) {
        if (usuarioInfo != null) { // Verifica si la información del usuario está disponible
            nivelAcceso = usuarioInfo.getNivelAcceso(); // Obtiene el nivel de acceso del usuario
            System.out.println("nivel_acceso_ Clientes= " + nivelAcceso);
            
            if (nivelAcceso == 1) { // Verifica si el usuario tiene nivel de acceso 1
                launchClientes(primaryStage); // Lanza la ventana de clientes
            } else {
                showNoAccessAlert(); // Muestra alerta de acceso denegado
            }
        } else {
            System.err.println("La información del usuario no se ha establecido."); // Error si no hay información del usuario
        }
    }

    public void setNivelAcceso(int nivelAcceso) { // Establece el nivel de acceso
        this.nivelAcceso = nivelAcceso;
    }

    public void setUsuarioInfo(UsuarioInfo usuarioInfo) { // Establece la información del usuario
        this.usuarioInfo = usuarioInfo;
    }

    public void launchClientes(Stage stage) { // Método para lanzar la ventana de clientes
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HabitacionesFXML.fxml"));
            Parent root = loader.load(); // Cargar el FXML de habitaciones

            HabitacionesController controller = loader.getController(); // Se obtiene el controlador
            controller.setUsuarioInfo(usuarioInfo); // Para pasar la información del usuario al controlador
            Scene scene = new Scene(root);
            stage.setTitle("Habitaciones"); // Establece el título de la ventana
            stage.setScene(scene); // Establece la escena en el escenario
            stage.show(); // Para mostrar la ventana
        } catch (IOException e) {
            e.printStackTrace(); // Manejar excepciones de E/S
        }
    }

    private void showNoAccessAlert() { // Método para mostrar alerta de acceso denegado
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Acceso denegado");
        alert.setHeaderText(null);
        alert.setContentText("No tienes permisos para acceder a esta funcionalidad.");
        alert.showAndWait(); // Para mostrar la alerta y esperar
    }
}
