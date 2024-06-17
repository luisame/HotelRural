/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reservas;

/**
 *
 * @author luisa
 */
import java.math.BigDecimal;
/**
 * La clase SuplementoSeleccionado representa un suplemento que ha sido seleccionado
 * con su precio asociado.
 */
public class SuplementoSeleccionado {
    private BigDecimal precio;

    // Constructor que acepta el precio como argumento
    public SuplementoSeleccionado(BigDecimal precio) {
        this.precio = precio;
    }

    // Getter para el precio
    public BigDecimal getPrecio() {
        return precio;
    }

    // Setter para el precio
    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }
}
