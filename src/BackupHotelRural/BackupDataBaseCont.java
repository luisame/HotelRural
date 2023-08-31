package BackupHotelRural;




import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class BackupDataBaseCont {
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
