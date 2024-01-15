package Administracion;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import utilidades.UsuarioInfo;

/**
 * Controlador para la vista de administración.
 * Este controlador gestiona las interacciones dentro de la vista de administración y responde a los eventos del usuario.
 * 
 * Dependencias:
 * - UsuarioInfo: Se requiere para la autenticación y autorización del usuario.
 * - AdministracionMain: Se utiliza para invocar acciones de nivel superior como cerrar la aplicación.
 */
public class AdministracionController {

    /**
     * Información del usuario actualmente autenticado en la aplicación.
     * Se utiliza para verificar el nivel de acceso del usuario antes de realizar operaciones administrativas.
     */
    private UsuarioInfo usuarioInfo;

    /**
     * Constructor sin argumentos para AdministracionController.
     * Inicializa el controlador para la vista de administración.
     */
    public AdministracionController() {
        // El constructor podría inicializar componentes adicionales si fuera necesario.
    }

    /**
     * Establece la información del usuario actual. Esta información es fundamental para las operaciones de administración.
     * 
     * @param usuarioInfo Información del usuario autenticado.
     */
    public void setUsuarioInfo(UsuarioInfo usuarioInfo) {
        this.usuarioInfo = usuarioInfo;
    }
    
    /**
     * Manejador del evento de clic para el botón de restaurar base de datos.
     * Solo los usuarios con el nivel de acceso adecuado pueden ejecutar la restauración.
     *
     * @param event El evento de acción generado al hacer clic en el botón.
     */
    @FXML
    private void handleRestoreDatabase(ActionEvent event) {
        // Verifica que el usuario tenga el nivel de acceso requerido para restaurar la base de datos.
        if (usuarioInfo != null && usuarioInfo.getNivelAcceso() == 1) {
            // Restaura la base de datos aquí. (la implementación de la restauración iría aquí)
            // ...
        } else {
            showNoAccessAlert();
        }
    }

    /**
     * Muestra una alerta de acceso denegado cuando el usuario intenta realizar una acción para la cual no tiene permisos suficientes.
     * Se invoca cuando un usuario sin el nivel de acceso requerido intenta restaurar la base de datos.
     */
    private void showNoAccessAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Acceso denegado");
        alert.setHeaderText(null);
        alert.setContentText("No tienes permisos para acceder a esta funcionalidad.");
        alert.showAndWait();
    }
}
