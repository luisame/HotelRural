
package Utilidades;

import javafx.beans.property.*;

public class HabitacionInfo {
    private final IntegerProperty idHabitacion;
   
    private final IntegerProperty capacidad;
    private final StringProperty descripcion;
    public final ObjectProperty<EstadoEnum> estado;
    private final DoubleProperty precio;

    public HabitacionInfo(int idHabitacion, String descripcion, int capacidad, EstadoEnum estado, double precio) {
        this.idHabitacion = new SimpleIntegerProperty(idHabitacion);
      
        this.capacidad = new SimpleIntegerProperty(capacidad);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.estado = new SimpleObjectProperty<>(estado);
        this.precio = new SimpleDoubleProperty(precio);
    }

  





    public int getIdHabitacion() {
        return idHabitacion.get();
    }

   

    public int getCapacidad() {
        return capacidad.get();
    }

    public String getDescripcion() {
        return descripcion.get();
    }

    public EstadoEnum getEstado() {
        return estado.get();
    }

    public ObjectProperty<EstadoEnum> estadoProperty() {
        return estado;
    }

    public double getPrecio() {
        return precio.get();
    }

    // Propiedades JavaFX
    public IntegerProperty idHabitacionProperty() {
        return idHabitacion;
    }

   

    public IntegerProperty capacidadProperty() {
        return capacidad;
    }

    public StringProperty descripcionProperty() {
        return descripcion;
    }

    public DoubleProperty precioProperty() {
        return precio;
    }

    // Enum deben ser public para ser accesibles fuera del paquete
    public enum EstadoEnum {
        Confirmada,
        Cancelada,
        Reservada,
        Libre;
    }
    }

