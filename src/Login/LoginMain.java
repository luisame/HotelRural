package Login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utilidades.UsuarioInfo;

/**
 * Clase principal para la aplicación de inicio de sesión.
 * Extiende la clase Application de JavaFX.
 */
public class LoginMain extends Application {

    private static final Logger logger = LoggerFactory.getLogger(LoginMain.class);

    /**
     * Método de inicio de JavaFX.
     * Carga y muestra la interfaz de usuario.
     * 
     * @param primaryStage El escenario principal de la aplicación.
     * @throws Exception Si ocurre un error durante la carga del archivo FXML.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        logger.info("Iniciando la aplicación...");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginFXML.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");
        primaryStage.show();
        logger.info("Interfaz de usuario cargada y mostrada.");
    }

    /**
     * Método principal de la aplicación.
     * Inicializa la información del usuario y lanza la aplicación JavaFX.
     * 
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        int idUsuario = 0; 
        String nombre = ""; 
        String apellido = "";
        int nivelAcceso = 0; 
        UsuarioInfo.initializeInstance(idUsuario, nombre, apellido, nivelAcceso);
        logger.info("Información del usuario inicializada.");
        launch(args);
    }
}
