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



 }
  


