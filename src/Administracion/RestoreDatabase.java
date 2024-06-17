package Administracion;

import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.IOException;

/**
 * Clase que maneja la restauración de la base de datos a partir de un archivo de respaldo.
 */
public class RestoreDatabase {

    /**
     * Realiza la restauración de una base de datos a partir de un archivo de respaldo.
     * 
     * @param host El host donde se encuentra la base de datos.
     * @param port El puerto en el que el servidor de base de datos está escuchando.
     * @param targetDatabase El nombre de la base de datos a restaurar.
     * @param username El nombre de usuario para acceder a la base de datos.
     * @param password La contraseña para acceder a la base de datos.
     * @param backupFilePath La ruta completa al archivo de respaldo que se utilizará para la restauración.
     * @return true si la restauración se completó con éxito, false en caso contrario.
     */
    public static boolean performRestore(String host, String port, String targetDatabase, String username, String password, String backupFilePath) {
        // Ruta completa al comando mysql
        String mysqlPath = "C:\\Program Files\\MariaDB 11.1\\bin\\mysql";

        // Crear el comando mysql con la ruta completa
        String[] command = {
            mysqlPath,
            "--host=" + host,
            "--port=" + port,
            "--user=" + username,
            "--password=" + password,
            targetDatabase,
            "-e",
            "source " + backupFilePath
        };

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Restore completed successfully.");
                return true;
            } else {
                showErrorAlert("Restore failed. Exit code: " + exitCode);
                return false;
            }
        } catch (IOException | InterruptedException e) {
            showErrorAlert("Error during restore: " + e.getMessage());
            return false;
        }
    }

    /**
     * Muestra una alerta de error con el mensaje especificado en el hilo de la interfaz de usuario.
     * 
     * @param message El mensaje de error a mostrar en la alerta.
     */
    public static void showErrorAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Restauración");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
