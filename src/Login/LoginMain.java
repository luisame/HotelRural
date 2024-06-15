package Login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilidades.UsuarioInfo;

/**
 * Clase principal para la aplicación de inicio de sesión.
 * Extiende la clase Application de JavaFX.
 */
public class LoginMain extends Application {

    /**
     * Método de inicio para la aplicación JavaFX.
     * Este método se llama cuando la aplicación se inicia.
     *
     * @param primaryStage el escenario principal para esta aplicación.
     * @throws Exception si hay un error al cargar el archivo FXML.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Cargar el archivo FXML para la interfaz de usuario de inicio de sesión
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginFXML.fxml"));
        Parent root = loader.load();  // Cargar el archivo FXML

        // Crear una nueva escena con el contenido cargado
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);  // Establecer la escena en el escenario principal
        primaryStage.setTitle("Login");  // Establecer el título del escenario principal
        primaryStage.show();  // Mostrar el escenario principal
    }

    /**
     * Método principal que inicia la aplicación.
     * Inicializa las variables de UsuarioInfo y lanza la aplicación JavaFX.
     *
     * @param args los argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        // Inicializar las variables de UsuarioInfo
        int idUsuario = 0; 
        String nombre = ""; 
        String apellido = "";
        int nivelAcceso = 0; 

        // Inicializa la instancia de UsuarioInfo con los valores
        UsuarioInfo.initializeInstance(idUsuario, nombre, apellido, nivelAcceso);

        // Lanza la aplicación JavaFX
        launch(args);
    }
}
