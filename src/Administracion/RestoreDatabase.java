package Administracion;

import java.io.IOException;

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
     * @throws IOException Si ocurre un error de entrada/salida durante la ejecución del proceso de restauración.
     * @throws InterruptedException Si el proceso es interrumpido mientras espera.
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
                System.err.println("Restore failed. Exit code: " + exitCode);
                return false;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
