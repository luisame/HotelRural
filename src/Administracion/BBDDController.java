
package Administracion;

import Inicio.InicioController;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utilidades.UsuarioInfo;

/**
 * Controlador para la gestión de la base de datos.
 */
public class BBDDController {

    private UsuarioInfo usuarioInfo;

    @FXML
    public Button botonVolver;

    @FXML
    public Button restaurarButton;

    @FXML
    public Button backupButton;

    /**
     * Establece la información del usuario.
     * 
     * @param usuarioInfo La información del usuario.
     */
    public void setUsuarioInfo(UsuarioInfo usuarioInfo) {
        this.usuarioInfo = usuarioInfo;
    }

    /**
     * Maneja la acción de volver al inicio.
     * 
     * @param event El evento de acción que desencadena este método.
     */
    @FXML
    public void volverAlInicio(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Inicio/InicioFXML.fxml"));
            Parent root = loader.load();
            InicioController inicioController = loader.getController();

            // Validación nula para usuarioInfo
            if (this.usuarioInfo != null) {
                inicioController.setUsuarioInfo(this.usuarioInfo);
            } else {
                mostrarAlerta("Usuario no autenticado. Por favor, inicie sesión de nuevo.");
                return;
            }

            Stage stage = new Stage();
            stage.setTitle("Inicio");
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar la ventana actual
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } catch (IOException e) {
            mostrarAlerta("No se pudo cargar la pantalla de inicio.");
        }
    }

    /**
     * Maneja la acción del botón de respaldo de la base de datos.
     * 
     * @param event El evento de acción que desencadena este método.
     */
    @FXML
    public void handleBackupButton(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Respaldo");
        alert.setHeaderText(null);
        alert.setContentText("¿Estás seguro de que deseas realizar el respaldo de la base de datos?");
        
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            // Realizar el respaldo
            Task<Boolean> backupTask = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    // Aquí debes llamar a la lógica real de respaldo, asegurándote de que devuelva true o false.
                    return BackupDatabase.performBackup("localhost", "3306", "hotelr_test", "root", "?7Ash€r");
                }
            };

            backupTask.setOnSucceeded(e -> mostrarAlerta("Backup completed successfully."));
            backupTask.setOnFailed(e -> mostrarAlerta("Backup failed due to an error."));

            new Thread(backupTask).start();
        }
    }

    /**
     * Maneja la acción del botón de restauración de la base de datos.
     * 
     * @param event El evento de acción que desencadena este método.
     */
    @FXML
    public void handleRestoreButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Backup File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("SQL files (*.sql)", "*.sql");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            Task<Boolean> restoreTask = new Task<Boolean>() {
                @Override
                protected Boolean call() {
                    return RestoreDatabase.performRestore("localhost", "3306", "hotelr_test", "root", "?7Ash€r", file.getAbsolutePath());
                }
            };

            restoreTask.setOnSucceeded(e -> mostrarAlerta("Restore exitoso."));
            restoreTask.setOnFailed(e -> mostrarAlerta("Restore falló por un error."));

            new Thread(restoreTask).start();
        }
    }

    /**
     * Muestra una alerta con un mensaje especificado.
     * 
     * @param message El mensaje a mostrar en la alerta.
     */
    private void mostrarAlerta(String message) {
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setTitle("Información");
        infoAlert.setHeaderText(null);
        infoAlert.setContentText(message);
        infoAlert.showAndWait();
    }
}
