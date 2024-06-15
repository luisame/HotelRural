package Login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utilidades.UsuarioInfo;

public class LoginMain extends Application {

    private static final Logger logger = LoggerFactory.getLogger(LoginMain.class);

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
