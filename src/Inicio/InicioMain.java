package Inicio;


import utilidades.UsuarioInfo;  

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase principal para la aplicación de inicio.
 * Extiende la clase Application de JavaFX.
 */
public class InicioMain extends Application {

    /**
     * Método principal para iniciar la aplicación.
     * @param args los argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Inicia la aplicación y muestra la ventana principal.
     * @param primaryStage la ventana principal de la aplicación.
     * @throws Exception si ocurre un error durante la carga de la ventana.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Carga la ventana InicioFXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Inicio/InicioFXML.fxml"));
        Parent root = loader.load();
        InicioController inicioController = loader.getController();

        // Obtén los datos del usuario si están disponibles
        UsuarioInfo usuarioInfo = UsuarioInfo.getInstance(); // Utiliza el método getInstance de UsuarioInfo

        if (usuarioInfo != null) {
            inicioController.setUsuarioInfo(usuarioInfo);
        }

        // Mostrar la ventana de inicio
        Stage stage = new Stage();
        stage.setTitle("Inicio");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
