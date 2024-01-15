package Habitaciones;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author luisa
 */

public class Habitacion {
    private final SimpleIntegerProperty idHabitacion;
    private final SimpleStringProperty tipoHabitacion;
    private final SimpleStringProperty descripcion;
    private final SimpleIntegerProperty capacidad;
    private final SimpleStringProperty estado;

    public Habitacion(int id, String tipo, String descripcion, int capacidad, String estado) {
        this.idHabitacion = new SimpleIntegerProperty(id);
        this.tipoHabitacion = new SimpleStringProperty(tipo);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.capacidad = new SimpleIntegerProperty(capacidad);
        this.estado = new SimpleStringProperty(estado);
    }

    // Getters
    public int getIdHabitacion() {
        return idHabitacion.get();
    }

    public String getTipoHabitacion() {
        return tipoHabitacion.get();
    }

    public String getDescripcion() {
        return descripcion.get();
    }

    public int getCapacidad() {
        return capacidad.get();
    }

    public String getEstado() {
        return estado.get();
    }

    // Setters
    public void setIdHabitacion(int id) {
        this.idHabitacion.set(id);
    }

    public void setTipoHabitacion(String tipo) {
        this.tipoHabitacion.set(tipo);
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    public void setCapacidad(int capacidad) {
        this.capacidad.set(capacidad);
    }

    public void setEstado(String estado) {
        this.estado.set(estado);
    }

    // Property getters (opcional, útil para algunas funcionalidades de JavaFX)
    public SimpleIntegerProperty idHabitacionProperty() {
        return idHabitacion;
    }

    public SimpleStringProperty tipoHabitacionProperty() {
        return tipoHabitacion;
    }

    public SimpleStringProperty descripcionProperty() {
        return descripcion;
    }

    public SimpleIntegerProperty capacidadProperty() {
        return capacidad;
    }

    public SimpleStringProperty estadoProperty() {
        return estado;
    }
}
