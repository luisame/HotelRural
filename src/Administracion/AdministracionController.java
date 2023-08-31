package Administracion;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import utilidades.UsuarioInfo;

public class AdministracionController {
    private UsuarioInfo usuarioInfo;

    public void setUsuarioInfo(UsuarioInfo usuarioInfo) {
        this.usuarioInfo = usuarioInfo;
    }
    
    @FXML
private void handleRestoreDatabase(ActionEvent event) {
    if (usuarioInfo != null && usuarioInfo.getNivelAcceso() == 1) {
        AdministracionMain adminMain = (AdministracionMain) ((Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow()).getProperties().get("administracionMain");
        adminMain.setUsuarioInfo(usuarioInfo);
        
        Stage currentStage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();
        currentStage.close();
    } else {
        showNoAccessAlert();
    }
}

    

    private void showNoAccessAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Acceso denegado");
        alert.setHeaderText(null);
        alert.setContentText("No tienes permisos para acceder a esta funcionalidad.");
        alert.showAndWait();
    }
}
