package Administracion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import utilidades.UsuarioInfo;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase principal para la aplicación de administración que gestiona el proceso de inicio y control de acceso.
 * Se encarga de iniciar la interfaz de usuario principal y manejar la autenticación del usuario.
 */
public class AdministracionMain extends Application {
    /**
     * Información del usuario que está utilizando la aplicación.
     * Esta información es crucial para la gestión de acceso y control de usuario.
     */
    private UsuarioInfo usuarioInfo;

    /**
     * Nivel de acceso del usuario actual. Determina qué funcionalidades están disponibles para el usuario.
     * Por ejemplo, un nivel de acceso 1 podría permitir el acceso a funcionalidades administrativas.
     */
    private int nivelAcceso;

    /**
     * Constructor de la clase AdministracionMain.
     */
    public AdministracionMain() {
        // Este constructor podría ser más complejo con la inicialización de la configuración.
    }

    /**
     * Punto de entrada principal de la aplicación JavaFX.
     * 
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Inicia la aplicación y muestra la ventana de administración si el usuario tiene el nivel de acceso requerido.
     *
     * @param primaryStage El escenario principal para esta aplicación, en el cual se debe mostrar la interfaz de usuario.
     * @throws IllegalStateException Si la información del usuario no está presente o es inválida.
     */
    @Override
    public void start(Stage primaryStage) {
        if (usuarioInfo == null) {
            throw new IllegalStateException("Información de usuario no inicializada.");
        }

        int nivelAcceso = usuarioInfo.getNivelAcceso();
        if (nivelAcceso == 1) {
            try {
                launchAdministracion(primaryStage); // Lanzar ventana de administración
            } catch (IOException ex) {
                Logger.getLogger(AdministracionMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            showNoAccessAlert(); // Mostrar alerta de acceso denegado
        }
    }

    /**
     * Establece el nivel de acceso del usuario.
     * 
     * @param nivelAcceso Nivel de acceso del usuario, donde niveles más altos indican mayor acceso.
     */
    public void setNivelAcceso(int nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }

    /**
     * Establece la información del usuario actual.
     * 
     * @param usuarioInfo La información del usuario autenticado.
     */
    public void setUsuarioInfo(UsuarioInfo usuarioInfo) {
        this.usuarioInfo = usuarioInfo;
    }

    /**
     * Lanza la ventana de administración utilizando el FXML especificado.
     * 
     * @param stage El escenario donde se mostrará la ventana de administración.
     * @throws IOException Si el archivo FXML no se puede cargar correctamente.
     */
    public void launchAdministracion(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AdministracionFXML.fxml"));
        Parent root = loader.load();

        AdministracionController controller = loader.getController();
        controller.setUsuarioInfo(usuarioInfo);

        Scene scene = new Scene(root);
        stage.setTitle("Administración");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Muestra una alerta de acceso denegado cuando el usuario no tiene el nivel de acceso requerido.
     */
    private void showNoAccessAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Acceso denegado");
        alert.setHeaderText(null);
        alert.setContentText("No tienes permisos para acceder a esta funcionalidad.");
        alert.showAndWait();
    }
}
