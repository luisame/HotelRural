package Reservas;

import java.math.BigDecimal;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Habitacion {
    // Atributos de la clase Habitacion
    private final IntegerProperty id;
    private final StringProperty descripcion;
    private final IntegerProperty capacidad;
    private final StringProperty estado;
    private ObjectProperty<BigDecimal> precioPorNoche; // Propiedad para el precio por noche
    private BigDecimal precioTotal; // Precio total de la estancia

    // Constructor de la clase Habitacion
    public Habitacion(int id, String descripcion, int capacidad, String estado, BigDecimal precio) {
        this.id = new SimpleIntegerProperty(id);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.capacidad = new SimpleIntegerProperty(capacidad);
        this.estado = new SimpleStringProperty(estado);
        this.precioPorNoche = new SimpleObjectProperty<>(precio); // Inicialización de la propiedad de precio por noche
    }

    // Getters y setters de los atributos de la clase Habitacion
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
public BigDecimal getPrecio() {
    return getPrecioPorNoche();
}

    // Getter y setter para la propiedad de precio por noche
    public final ObjectProperty<BigDecimal> precioPorNocheProperty() {
        if (precioPorNoche == null) precioPorNoche = new SimpleObjectProperty<>(this, "precioPorNoche");
        return precioPorNoche;
    }

    public final BigDecimal getPrecioPorNoche() {
        return precioPorNocheProperty().get();
    }

    public final void setPrecioPorNoche(BigDecimal precioPorNoche) {
        precioPorNocheProperty().set(precioPorNoche);
    }

    // Método para obtener el precio total
    public final BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    // Método para establecer el precio total
    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }
}
