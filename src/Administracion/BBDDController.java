
package Administracion;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import utilidades.UsuarioInfo;

/**
 * Controlador para la interfaz de usuario definida en BBDD.fxml.
 */
public class BBDDController {

    private UsuarioInfo usuarioInfo;

    public void setUsuarioInfo(UsuarioInfo usuarioInfo) {
        this.usuarioInfo = usuarioInfo;
    }

    @FXML
    private void volverAlInicio(ActionEvent event) {
        // Código para manejar el regreso a la vista anterior
    }

    @FXML
    private void handleBackupButton(ActionEvent event) {
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

            backupTask.setOnSucceeded(e -> showInfoAlert("Backup completed successfully."));
            backupTask.setOnFailed(e -> showErrorAlert("Backup failed due to an error."));

            new Thread(backupTask).start();
        }
    }

    @FXML
    private void handleRestoreButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Backup File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("SQL files (*.sql)", "*.sql");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            Task<Boolean> restoreTask = new Task<Boolean>() {
                @Override
                protected Boolean call() {
                    // El método performRestore ya maneja las excepciones internamente
                    // y devuelve true si la restauración fue exitosa o false en caso contrario.
                    return RestoreDatabase.performRestore("localhost", "3306", "hotelr_test", "root", "?7Ash€r", file.getAbsolutePath());
                }
            };

            restoreTask.setOnSucceeded(e -> showInfoAlert("Restore completed successfully."));
            restoreTask.setOnFailed(e -> showErrorAlert("Restore failed due to an error."));

            new Thread(restoreTask).start();
        }
    }

    private void showInfoAlert(String message) {
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setTitle("Information");
        infoAlert.setHeaderText(null);
        infoAlert.setContentText(message);
        infoAlert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error");
        errorAlert.setHeaderText(null);
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }
}
