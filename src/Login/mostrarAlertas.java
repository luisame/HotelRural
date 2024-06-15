/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reservas;

import javafx.scene.control.Alert;

/**
 *
 * @author luisa
 */
public class mostrarAlertas {
    //*
    private void mostrarAlerta(String titulo, String mensaje) {
    // Comprueba si estamos en el hilo de la interfaz de usuario de JavaFX.
    if (javafx.application.Platform.isFxApplicationThread()) {
        // Si ya estamos en el hilo de la interfaz de usuario, mostramos la alerta directamente.
        crearYMostrarAlerta(titulo, mensaje);
    } else {
        // Si no estamos en el hilo de la interfaz de usuario, la mostramos usando Platform.runLater.
        javafx.application.Platform.runLater(() -> crearYMostrarAlerta(titulo, mensaje));
    }
}

private void crearYMostrarAlerta(String titulo, String mensaje) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(titulo);
    alert.setHeaderText(null); // Puedes cambiar esto si quieres un texto en la cabecera
    alert.setContentText(mensaje);
    alert.showAndWait();
}

    
}
