/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reservas;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author luisa
 */
public class Habitacion {
    private IntegerProperty id;
    private StringProperty tipo;
    private IntegerProperty capacidad;
    private StringProperty estado;

    // Constructor, getters y setters

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty tipoProperty() {
        return tipo;
    }

    public IntegerProperty capacidadProperty() {
        return capacidad;
    }

    public StringProperty estadoProperty() {
        return estado;
    }
}
