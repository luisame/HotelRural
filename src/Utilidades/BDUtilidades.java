package utilidades;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import Utilidades.HabitacionInfo;
import Utilidades.ReservaInfo;
import java.math.BigDecimal;
import javafx.scene.control.Alert.AlertType;
import Utilidades.MensajeError;

public class BDUtilidades {

    private static BigDecimal totalEstancia;

    

    public static UsuarioInfo getUsuarioInfo(String username, String password) throws ClassNotFoundException {
        UsuarioInfo usuarioInfo = null;

        try {
            Class.forName("org.mariadb.jdbc.Driver"); // Controlador 

            Properties prop = ConfigLoader.getProperties(); //  Propiedades 

            String url = prop.getProperty("jdbcUrl");
            String user = prop.getProperty("username");
            String dbPassword = prop.getProperty("password");

            try (Connection connection = DataSourceManager.getConnection()) {
                String query = "SELECT u.id_usuario, u.nivel_acceso, e.nombre_empleado, e.apellido_empleado " +
                               "FROM usuarios u " +
                               "LEFT JOIN empleados e ON u.id_empleado = e.id_empleado " +
                               "WHERE u.username = ? AND u.password = ?";

                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, password);

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            int idUsuario = resultSet.getInt("id_usuario");
                            int nivelAcceso = resultSet.getInt("nivel_acceso");
                            String nombre = resultSet.getString("nombre_empleado");
                            String apellido = resultSet.getString("apellido_empleado");

                            usuarioInfo = new UsuarioInfo(idUsuario, nombre, apellido, nivelAcceso);

                            // Actualizar la fecha de último acceso
                            String updateQuery = "UPDATE usuarios SET ultimo_acceso = NOW() WHERE id_usuario = ?";
                            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                                updateStatement.setInt(1, idUsuario);
                                updateStatement.executeUpdate();
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuarioInfo;
    }

    
    



public static int getNivelAccesoUsuario(int codigoUsuario) {
        int nivelAcceso = -1;

        try {
            try (Connection connection = DataSourceManager.getConnection()) {
                String query = "SELECT nivel_acceso FROM usuarios WHERE id_usuario = ?";

                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setInt(1, codigoUsuario);

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            nivelAcceso = resultSet.getInt("nivel_acceso");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nivelAcceso;
    }


public static List<HabitacionInfo> obtenerHabitacionesDesdeDB(LocalDate fechaEntrada, LocalDate fechaSalida, int capacidad) {
    List<HabitacionInfo> habitaciones = new ArrayList<>();

    String query = "SELECT h.id_habitacion, h.descripcion, h.capacidad, h.estado, h.precio " +
                   "FROM habitaciones h " +
                   "WHERE h.capacidad >= ? AND NOT EXISTS (" +
                   "    SELECT 1 " +
                   "    FROM reservas r " +
                   "    WHERE r.id_habitacion = h.id_habitacion " +
                   "      AND r.FechaEntrada <= ? AND r.FechaSalida >= ?" +
                   ")";

    try {
        try (Connection connection = DataSourceManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, capacidad);
            statement.setDate(2, java.sql.Date.valueOf(fechaSalida));
            statement.setDate(3, java.sql.Date.valueOf(fechaEntrada));
            statement.setDate(4, java.sql.Date.valueOf(fechaSalida));

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id_habitacion");
                    String descripcion = resultSet.getString("descripcion");
                    int capacidadHabitacion = resultSet.getInt("capacidad");
                    String estadoDB = resultSet.getString("estado");
                    HabitacionInfo.EstadoEnum estadoEnum = HabitacionInfo.EstadoEnum.valueOf(estadoDB);
                    double precio = resultSet.getDouble("precio");

                    HabitacionInfo habitacionInfo = new HabitacionInfo(id, descripcion, capacidadHabitacion, estadoEnum, precio);
                    habitaciones.add(habitacionInfo);
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return habitaciones;
}
 public static List<ClienteInfo> obtenerClientesDesdeDB(String dni, String nombre, String apellidos) {
        List<ClienteInfo> clientes = new ArrayList<>();

        try {
            try (Connection connection = DataSourceManager.getConnection()) {
                String query = "SELECT dni, nombre_cliente, apellido_cliente, email, telefono " +
                               "FROM clientes " +
                               "WHERE dni LIKE ? OR nombre_cliente LIKE ? OR apellido_cliente LIKE ?";

                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, "%" + dni + "%");
                    preparedStatement.setString(2, "%" + nombre + "%");
                    preparedStatement.setString(3, "%" + apellidos + "%");

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        while (resultSet.next()) {
                            String dniCliente = resultSet.getString("dni");
                            String nombreCliente = resultSet.getString("nombre_cliente");
                            String apellidoCliente = resultSet.getString("apellido_cliente");
                            String emailCliente = resultSet.getString("email");
                            String telefonoCliente = resultSet.getString("telefono");

                            ClienteInfo clienteInfo = new ClienteInfo(dniCliente, nombreCliente, apellidoCliente, emailCliente, telefonoCliente);
                            clientes.add(clienteInfo);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clientes;
 }
   public static void insertarReservaEnBaseDeDatos(ReservaInfo reserva) {
        try {
            Connection connection = DataSourceManager.getConnection();
            String query = "INSERT INTO reservas (id_cliente, id_habitacion, id_usuario, num_personas, " +
                           "precio_persona,  total_estancia" +
                           "numero_tarjeta_credito, FechaEntrada, FechaSalida) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, reserva.getIdCliente());
            preparedStatement.setInt(2, reserva.getIdHabitacion());
            preparedStatement.setInt(3, reserva.getIdUsuario());
            preparedStatement.setInt(4, reserva.getNumPersonas());

            BigDecimal precioPersona = reserva.getPrecioPersona(); // Obtén el precio por persona
           
           // Calcula el total de la estancia
           

            preparedStatement.setBigDecimal(5, precioPersona);
           
            preparedStatement.setBigDecimal(7, totalEstancia);
           

            preparedStatement.setString(9, reserva.getNumeroTarjetaCredito());
            preparedStatement.setDate(10, new java.sql.Date(reserva.getFechaEntrada().getTime()));
            preparedStatement.setDate(11, new java.sql.Date(reserva.getFechaSalida().getTime()));
            
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
               MensajeError.mostrarMensaje("Reserva insertada en la base de datos.");

            } else {
                MensajeError.mostrarMensaje("Error al insertar la reserva.");
            }
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

