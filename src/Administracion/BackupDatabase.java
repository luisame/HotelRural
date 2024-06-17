

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

/**
 * Clase que maneja el respaldo de la base de datos.
 */
public class BackupDatabase {

    /**
     * Controlador de eventos que interactúa con la UI para iniciar el respaldo de la base de datos.
     *
     * @param event El evento de acción que desencadena este método.
     */
    @FXML
    public void handleBackup(ActionEvent event) {
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

    /**
     * Inicia la tarea de respaldo en un hilo en segundo plano.
     *
     * @param host     El host de la base de datos.
     * @param port     El puerto de la base de datos.
     * @param database El nombre de la base de datos.
     * @param username El nombre de usuario de la base de datos.
     * @param password La contraseña de la base de datos.
     * @return true si el respaldo fue exitoso, false en caso de fallo.
     */
    public static boolean performBackup(String host, String port, String database, String username, String password) {
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

    /**
     * Realiza el respaldo de la base de datos y devuelve un booleano indicando el éxito.
     *
     * @param host     El host de la base de datos.
     * @param port     El puerto de la base de datos.
     * @param database El nombre de la base de datos.
     * @param username El nombre de usuario de la base de datos.
     * @param password La contraseña de la base de datos.
     * @return true si el respaldo fue exitoso, false en caso de fallo.
     */
    private static boolean backupDatabase(String host, String port, String database, String username, String password) {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String backupFileName = "backup_" + dateFormat.format(currentDate) + ".sql";
        String desktopPath = System.getProperty("user.home") + "\\Desktop\\";
        String backupFolderPath = desktopPath + "Backup\\";
        File backupFolder = new File(backupFolderPath);
        if (!backupFolder.exists() && !backupFolder.mkdirs()) {
            showErrorAlert("Failed to create backup folder.");
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
            showErrorAlert("Error al realizar el respaldo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Muestra una alerta con el mensaje especificado en el hilo de la interfaz de usuario.
     *
     * @param message El mensaje a mostrar en la alerta.
     */
    public static void showAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Resultado del Respaldo");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    /**
     * Muestra una alerta de error con el mensaje especificado en el hilo de la interfaz de usuario.
     *
     * @param message El mensaje de error a mostrar en la alerta.
     */
    public static void showErrorAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Respaldo");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
