package Utilidades;


import java.math.BigDecimal;
import java.util.Date;

public class ReservaInfo {
    private int idReserva;
    private int idCliente;
    private int idHabitacion;
    private int idUsuario;
    private int numPersonas;
    private BigDecimal precioPersona;
   
    private BigDecimal totalEstancia;
  
    private String numeroTarjetaCredito;
    private Date fechaEntrada;
    private Date fechaSalida;

    public ReservaInfo(int idReserva, int idCliente, int idHabitacion, int idUsuario, int numPersonas,
                       BigDecimal precioPersona, BigDecimal totalEstancia,
                        String numeroTarjetaCredito, Date fechaEntrada, Date fechaSalida) {
        this.idReserva = idReserva;
        this.idCliente = idCliente;
        this.idHabitacion = idHabitacion;
        this.idUsuario = idUsuario;
        this.numPersonas = numPersonas;
        this.precioPersona = precioPersona;
        
        this.totalEstancia = totalEstancia;
        
        this.numeroTarjetaCredito = numeroTarjetaCredito;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
    }

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdHabitacion() {
        return idHabitacion;
    }

    public void setIdHabitacion(int idHabitacion) {
        this.idHabitacion = idHabitacion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getNumPersonas() {
        return numPersonas;
    }

    public void setNumPersonas(int numPersonas) {
        this.numPersonas = numPersonas;
    }

    public BigDecimal getPrecioPersona() {
        return precioPersona;
    }

    public void setPrecioPersona(BigDecimal precioPersona) {
        this.precioPersona = precioPersona;
    }

   

    public BigDecimal getTotalEstancia() {
        return totalEstancia;
    }

    public void setTotalEstancia(BigDecimal totalEstancia) {
        this.totalEstancia = totalEstancia;
    }

    
    public String getNumeroTarjetaCredito() {
        return numeroTarjetaCredito;
    }

    public void setNumeroTarjetaCredito(String numeroTarjetaCredito) {
        this.numeroTarjetaCredito = numeroTarjetaCredito;
    }

    public Date getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(Date fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }
}
