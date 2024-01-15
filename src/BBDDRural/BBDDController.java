
package BBDDRural;

import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

/**
 *
 * @author luisa
 */
/**
 * @file   BBDDController.java
 * @brief  Controlador para la interfaz de usuario definida en BBDD.fxml.
 *
 * Este archivo contiene la clase controladora para la interfaz de usuario de la aplicación,
 * definida en el archivo BBDD.fxml. Incluye manejadores para los eventos de la interfaz,
 * como los botones para realizar copias de seguridad y restaurar datos.
 *
 * @see    BBDDFXML.fxml
 */
public class BBDDController {
    
    
     @FXML
    private void handleBackupButton() {
        String host = "localhost";
        String port = "3306";
        String database = "hotelr_test";
        String username = "root";
        String password = "?7Ash€r";

        boolean backupSuccess = BackupDatabase.performBackup(host, port, database, username, password);

        if (backupSuccess) {
            showInfoAlert("Backup completed successfully (Cont).");
        } else {
            showErrorAlert("Backup failed (Cont).");
        }
    }

   

@FXML
    private void handleRestoreButton() {
        String host = "localhost";
        String port = "3306";
        String targetDatabase = "hotelr_test";
        String username = "root";
        String password = "?7Ash€r";

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Backup File");
        // Establecer el directorio inicial del FileChooser
        String desktopPath = System.getProperty("user.home") + "\\Desktop\\";
        String backupFolderPath = desktopPath + "Backup\\";
        File initialDirectory = new File(backupFolderPath);
        System.out.println("Initial Directory: " + initialDirectory.getAbsolutePath()); // Imprimir la ruta en la consola

        fileChooser.setInitialDirectory(initialDirectory);
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            String backupFilePath = selectedFile.getAbsolutePath();
            boolean restoreSuccess = RestoreDatabase.performRestore(host, port, targetDatabase, username, password, backupFilePath);

            if (restoreSuccess) {
                showInfoAlert("Restore completed successfully.");
            } else {
                showErrorAlert("Restore failed.");
            }
        }
    
    }
    private void showInfoAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
