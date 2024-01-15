package Utilidades;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MensajeError {

    public static void mostrarMensaje( String contenido) {
        AlertType tipo = null;
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
     
    }

    public static void mostrarMensaje(String string, AlertType alertType) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
