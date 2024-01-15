/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clientes;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.scene.control.Alert;
import utilidades.DataSourceManager;


public class ClienteDAO {

    private DataSourceManager dataSourceManager;

    public ClienteDAO(DataSourceManager dataSourceManager) {
        this.dataSourceManager = dataSourceManager;
    }
public ObservableList<ClientesClass> buscarClientes(String dni, String nombre, String apellidos) throws SQLException {
    ObservableList<ClientesClass> listaClientes = FXCollections.observableArrayList();
    // Se crea la base de la consulta SQL
    String queryBase = "SELECT * FROM clientes WHERE ";
    String query = "";
    ArrayList<String> conditions = new ArrayList<>();
// Se agregan condiciones a la consulta dependiendo de si los campos están llenos
    if (!dni.isEmpty()) {
        conditions.add("dni LIKE ?");
    }
    if (!nombre.isEmpty()) {
        conditions.add("nombre_cliente LIKE ?");
}
if (!apellidos.isEmpty()) {
    conditions.add("apellido_cliente LIKE ?");
}

if (conditions.isEmpty()) {
    // Si no hay condiciones, devuelve una lista vacía
    return listaClientes;
} else {
    // Combina la consulta base con las condiciones
    query = queryBase + String.join(" OR ", conditions);
}
// Prepara la conexión y ejecuta la consulta
try (Connection connection = dataSourceManager.getConnection();
     PreparedStatement preparedStatement = connection.prepareStatement(query)) {

    int index = 1;
    // Establece los valores de las condiciones
    if (!dni.isEmpty()) {
        preparedStatement.setString(index++, "%" + dni + "%");
    }
    if (!nombre.isEmpty()) {
        preparedStatement.setString(index++, "%" + nombre + "%");
    }
    if (!apellidos.isEmpty()) {
        preparedStatement.setString(index, "%" + apellidos + "%");
    }
// Ejecuta la consulta y procesa los resultados
    try (ResultSet resultSet = preparedStatement.executeQuery()) {
    while (resultSet.next()) {
        ClientesClass cliente = new ClientesClass();
        // Aquí se asignan los valores de cada columna del ResultSet al objeto cliente
        cliente.setDni(resultSet.getString("dni"));
        cliente.setNombreCliente(resultSet.getString("nombre_cliente"));
        cliente.setApellidoCliente(resultSet.getString("apellido_cliente"));
        cliente.setDireccion(resultSet.getString("direccion"));
        cliente.setCiudad(resultSet.getString("ciudad"));
        cliente.setCodPostal(resultSet.getInt("codpostal"));
        cliente.setTelefono(resultSet.getString("telefono"));
        cliente.setEmail(resultSet.getString("email"));
        cliente.setOtraInformacion(resultSet.getString("otra_informacion"));

        // Después de asignar los valores, agregas el objeto a la lista
        listaClientes.add(cliente);
    }
}

}
return listaClientes;

    
}

    /**
     * Obtiene una lista de todos los clientes en la base de datos.y los almacena en una ObservableList.
     * 
     * @return Una ObservableList de objetos ClientesClass con los datos de todos los clientes.
     * @throws SQLException Si ocurre un error durante la operación de la base de datos.
     */
    public ObservableList<ClientesClass> obtenerTodosLosClientes() throws SQLException {
        ObservableList<ClientesClass> listaClientes = FXCollections.observableArrayList();
        String query = "SELECT * FROM clientes";

        try (Connection connection = dataSourceManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                ClientesClass cliente = new ClientesClass();
                cliente.setDni(resultSet.getString("dni"));
                cliente.setNombreCliente(resultSet.getString("nombre_cliente"));
                cliente.setApellidoCliente(resultSet.getString("apellido_cliente"));
                cliente.setDireccion(resultSet.getString("direccion"));
                cliente.setCiudad(resultSet.getString("ciudad"));
                cliente.setCodPostal(resultSet.getInt("codpostal"));
                cliente.setTelefono(resultSet.getString("telefono"));
                cliente.setEmail(resultSet.getString("email"));
                cliente.setOtraInformacion(resultSet.getString("otra_informacion"));
                listaClientes.add(cliente);
            }
            
        

        return listaClientes;
    }
        catch (SQLException e) {
    // Manejo específico de SQLException
    if (e.getSQLState().startsWith("23")) { // Errores de restricción de integridad
        Utilidades.MensajeError.mostrarMensaje("Error de integridad al agregar un cliente: " + e.getMessage(), Alert.AlertType.ERROR);
                } else {
                  Utilidades.MensajeError.mostrarMensaje("Error de SQL al agregar un cliente: " + e.getMessage(), Alert.AlertType.ERROR);
            }
    e.printStackTrace(); // Para propósitos de depuración,  en un archivo de log.
     return null; // Devuelve null si hubo una excepción}
        }
    }

   /**
 * Agrega un nuevo cliente a la base de datos.
 * 
 * Este método toma una instancia de ClientesClass, que contiene la información del cliente,
 * y la inserta en la base de datos en la tabla 'clientes'. Utiliza un PreparedStatement
 * para configurar los valores de los campos y ejecutar la consulta de inserción.
 * 
 * @param cliente La instancia de ClientesClass que contiene los datos del cliente a agregar.
 * @throws SQLException Si ocurre un error durante la operación de la base de datos.
 */
public void agregarCliente(ClientesClass cliente) throws SQLException {
    // La consulta SQL con signos de interrogación como marcadores de posición para los valores.
    String query = "INSERT INTO clientes (dni, nombre_cliente, apellido_cliente, direccion, ciudad, codpostal, telefono, email, otra_informacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    // Utiliza un try-with-resources para asegurarte de que los recursos se cierren después de su uso.
    try (Connection connection = dataSourceManager.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

        // Configura los parámetros del PreparedStatement basándote en los valores de la instancia de cliente.
        preparedStatement.setString(1, cliente.getDni());
        preparedStatement.setString(2, cliente.getNombreCliente());
        preparedStatement.setString(3, cliente.getApellidoCliente());
        preparedStatement.setString(4, cliente.getDireccion());
        preparedStatement.setString(5, cliente.getCiudad());
        preparedStatement.setInt(6, cliente.getCodPostal());
        preparedStatement.setString(7, cliente.getTelefono());
        preparedStatement.setString(8, cliente.getEmail());
        preparedStatement.setString(9, cliente.getOtraInformacion());

        // Ejecuta la actualización, que insertará el nuevo cliente en la base de datos.
        preparedStatement.executeUpdate();
    }
    // Al final del bloque try, el try-with-resources cierra automáticamente la conexión y el preparedStatement.
}


   /**
 * Actualiza un cliente en la base de datos con la información proporcionada en el objeto ClienteClass.
 * 
 * @param cliente El objeto ClienteClass con la información actualizada del cliente.
 * @throws SQLException Si ocurre un error durante la operación de actualización en la base de datos.
 */
public void actualizarCliente(ClientesClass cliente) throws SQLException {
    String query = "UPDATE clientes SET nombre_cliente = ?, apellido_cliente = ?, direccion = ?, ciudad = ?, codPostal = ?, telefono = ?, email = ?, otra_informacion = ? WHERE dni = ?";

    try (Connection connection = dataSourceManager.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

        preparedStatement.setString(1, cliente.getNombreCliente());
        preparedStatement.setString(2, cliente.getApellidoCliente());
        preparedStatement.setString(3, cliente.getDireccion());
        preparedStatement.setString(4, cliente.getCiudad());
        preparedStatement.setInt(5, cliente.getCodPostal());
        preparedStatement.setString(6, cliente.getTelefono());
        preparedStatement.setString(7, cliente.getEmail());
        preparedStatement.setString(8, cliente.getOtraInformacion());
        preparedStatement.setString(9, cliente.getDni());

        preparedStatement.executeUpdate();
    }
}



/**
 * Elimina un cliente de la base de datos utilizando el número de DNI como identificador.
 * 
 * @param dni El número de DNI del cliente que se desea eliminar.
 * @throws SQLException Si ocurre un error durante la operación de eliminación en la base de datos.
 */
public void eliminarCliente(String dni) throws SQLException {
    String query = "DELETE FROM clientes WHERE dni = ?";
    try (Connection connection = dataSourceManager.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setString(1, dni);
        preparedStatement.executeUpdate();
    }
}

}
