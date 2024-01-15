/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clientes;
import java.awt.Toolkit;
 import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextField;
import java.util.function.UnaryOperator;
import javafx.scene.control.Alert;
/**
 *
 * @author luisa
 */
public class TextFieldLimitador {

    public static void aplicarLongitudMaxima(TextField textField, int longitudMaxima) {
        UnaryOperator<TextFormatter.Change> maxLengthFilter = change -> {
            if (change.getControlNewText().length() > longitudMaxima) {
                emitirSonidoAlerta();
                mostrarAlerta("Longitud máxima excedida", "No puedes ingresar más de " + longitudMaxima + " caracteres.");
                return null;
            }
            return change;
        };

        textField.setTextFormatter(new TextFormatter<>(maxLengthFilter));
    }

    private static void emitirSonidoAlerta() {
        Toolkit.getDefaultToolkit().beep();
    }

    private static void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}