package Login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilidades.UsuarioInfo;

public class LoginMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginFXML.fxml"));
        Parent root = loader.load();  // Load the FXML file

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Inicializar las variables de UsuarioInfo aquí
        int idUsuario = 0; // Obtén el valor adecuado de alguna fuente
        String nombre = ""; // Obtén el valor adecuado de alguna fuente
        String apellido = ""; // Obtén el valor adecuado de alguna fuente
        int nivelAcceso = 0; // Obtén el valor adecuado de alguna fuente

        // Inicializar la instancia de UsuarioInfo con los valores
        UsuarioInfo.initializeInstance(idUsuario, nombre, apellido, nivelAcceso);

        launch(args);
    }
}

