
package RestoreHotelRural;

import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

public class RestoreDataBaseCont {
  
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
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
