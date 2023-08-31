package RestoreHotelRural;

import java.io.IOException;

public class RestoreDatabase {
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
