

package BBDDRural;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BackupDatabase {
    /**
     * Realiza un respaldo de la base de datos especificada.
     * 
     * Este método genera un archivo de respaldo SQL de la base de datos especificada
     * y lo guarda en una carpeta 'Backup' en el escritorio del usuario.
     * 
     * @param host El host donde se encuentra la base de datos.
     * @param port El puerto en el que el servidor de base de datos está escuchando.
     * @param database El nombre de la base de datos a respaldar.
     * @param username El nombre de usuario para acceder a la base de datos.
     * @param password La contraseña para acceder a la base de datos.
     * @return true si el respaldo se completó con éxito, false en caso contrario.
     * @throws IOException Si ocurre un error de entrada/salida durante la ejecución del proceso de respaldo.
     * @throws InterruptedException Si el proceso es interrumpido mientras espera.
     */
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
