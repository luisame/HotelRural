/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BackupHotelRural;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BackupDatabase {
    public static boolean performBackup(String host, String port, String database, String username, String password) {
        // Obtener la fecha actual
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String backupFileName = "backup_" + dateFormat.format(currentDate) + ".sql";

        // Ruta de la carpeta donde se almacenará el archivo de respaldo
        String desktopPath = System.getProperty("user.home") + "\\Desktop\\";
        String backupFolderPath = desktopPath + "Backup\\";

        // Crear la carpeta si no existe
        File backupFolder = new File(backupFolderPath);
        if (!backupFolder.exists()) {
            if (backupFolder.mkdirs()) {
                System.out.println("Backup folder created.");
            } else {
                System.err.println("Failed to create backup folder.");
                return false; // Salir del programa si no se pudo crear la carpeta
            }
        }

        // Ruta completa al archivo de respaldo
        String backupFilePath = backupFolderPath + backupFileName;
        System.out.println("Backup file path: " + backupFilePath);

        // Ruta completa al comando mysqldump
        String mysqldumpPath = "C:\\Program Files\\MariaDB 11.1\\bin\\mysqldump";

        // Crear el comando mysqldump con la ruta completa
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

            if (exitCode == 0) {
                System.out.println("Backup completed successfully.");
                return true;
            } else {
                System.err.println("Backup failed. Exit code: " + exitCode);
                return false;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
