package Utilidades;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MensajeError {

    public static void mostrarMensaje( String contenido, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
     
    }
}
