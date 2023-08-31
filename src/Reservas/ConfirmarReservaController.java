package Reservas;

import java.math.BigDecimal;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ConfirmarReservaController {
    // Etiquetas para mostrar que se inyectan desde el archivo FXML
    @FXML private Label habitacionesLabel;
    @FXML private Label fechaEntradaLabel;
    @FXML private Label fechaSalidaLabel;
    @FXML private Label idReservaLabel;
    @FXML private Label nombre_cliente; 
    @FXML private Label numPersonasLabel;
    @FXML private Label precioPersonaLabel;
    @FXML private Label totalEstanciaLabel;
    @FXML private Label numeroTarjetaLabel;

    // Método para configurar la información de la reserva en las etiquetas
    public void setInfoReserva(
            String numHabitaciones, 
            LocalDate entrada, 
            LocalDate salida, 
            int idReserva,
            String cliente,  // Nombre del cliente
            int numPersonas,
            BigDecimal precioPersona,
            BigDecimal totalEstancia,
            String numeroTarjetaCredito,
            LocalDate fechaEntrada,
            LocalDate fechaSalida) {
        // Se actualizan las etiquetas con los valores proporcionados para mostralos
        habitacionesLabel.setText(numHabitaciones);
        fechaEntradaLabel.setText(entrada.toString());
        fechaSalidaLabel.setText(salida.toString());
        idReservaLabel.setText(String.valueOf(idReserva));
        nombre_cliente.setText(cliente);  
        numPersonasLabel.setText(String.valueOf(numPersonas));
        precioPersonaLabel.setText(precioPersona.toString());
        totalEstanciaLabel.setText(totalEstancia.toString());
        numeroTarjetaLabel.setText(numeroTarjetaCredito);
    }
}
