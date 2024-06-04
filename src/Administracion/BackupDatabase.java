

package Administracion;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import javafx.fxml.FXML;

public class BackupDatabase {

    // controlador de eventos que interactúa con la UI
    @FXML
    private void handleBackup(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Respaldo");
        alert.setHeaderText(null);
        alert.setContentText("¿Estás seguro de que quieres realizar el respaldo de la base de datos?");

        ButtonType buttonTypeYes = new ButtonType("Sí", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeYes) {
            performBackup("localhost", "3306", "myDatabase", "username", "password"); // Ajustar con tus parámetros reales
        }
    }

    // Este método inicia la tarea de respaldo en un hilo en segundo plano
    public static boolean performBackup(String host, String port, String database, String username, String password) {
       // Realiza el respaldo y devuelve true si fue exitoso, o false si hubo un fallo.
        boolean success = false;
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return backupDatabase(host, port, database, username, password);
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                showAlert("Respaldo completado exitosamente.");
            }

            @Override
            protected void failed() {
                super.failed();
                showAlert("Error: el respaldo ha fallado.");
            }
        };

        new Thread(task).start();
        return success;
    }

    // Realiza el respaldo de la base de datos y devuelve un booleano indicando el éxito
    private static boolean backupDatabase(String host, String port, String database, String username, String password) {
        // Tu implementación actual de respaldo aquí...
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String backupFileName = "backup_" + dateFormat.format(currentDate) + ".sql";
        String desktopPath = System.getProperty("user.home") + "\\Desktop\\";
        String backupFolderPath = desktopPath + "Backup\\";
        File backupFolder = new File(backupFolderPath);
        if (!backupFolder.exists() && !backupFolder.mkdirs()) {
            System.err.println("Failed to create backup folder.");
            return false;
        }

        String backupFilePath = backupFolderPath + backupFileName;
        String mysqldumpPath = "C:\\Program Files\\MariaDB 11.1\\bin\\mysqldump";
        String[] command = {
            mysqldumpPath,
            "--host=" + host,
            "--port=" + port,
            "--user=" + username,
            "--password=" + password,
            database,
            "--result-file=" + backupFilePath
        };

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Muestra una alerta en el hilo de la interfaz de usuario
    public static void showAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Resultado del Respaldo");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
