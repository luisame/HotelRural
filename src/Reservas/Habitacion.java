package Reservas;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Habitacion {
    private final IntegerProperty id;
    private StringProperty descripcion;
    private final IntegerProperty capacidad;
    private final StringProperty estado;

    public Habitacion(int id, String descripcion, int capacidad, String estado) {
        this.id = new SimpleIntegerProperty(id);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.capacidad = new SimpleIntegerProperty(capacidad);
        this.estado = new SimpleStringProperty(estado);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getDescripcion() {
        return descripcion.get();
    }

    public StringProperty descripcionProperty() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    public int getCapacidad() {
        return capacidad.get();
    }

    public IntegerProperty capacidadProperty() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad.set(capacidad);
    }

    public String getEstado() {
        return estado.get();
    }

    public StringProperty estadoProperty() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado.set(estado);
    }
}
