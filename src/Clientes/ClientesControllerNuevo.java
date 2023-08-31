package Clientes;

import Inicio.InicioMain;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.scene.control.TableView;
import java.sql.ResultSet;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import utilidades.DataSourceManager;
import utilidades.UsuarioInfo;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ClientesControllerNuevo {
    // Campos de entrada para agregar un nuevo cliente
    @FXML
    private TextField dniInputAgregar;
    @FXML
    private TextField nombreInputAgregar;
    @FXML
    private TextField apellidosInputAgregar;
    @FXML
    private TextField direccionInputAgregar;
    @FXML
    private TextField ciudadInputAgregar;
    @FXML
    private TextField CPInputAgregar;
    @FXML
    private TextField telefonoInputAgregar;
    @FXML
    private TextField emailInputAgregar;
    @FXML
    private TextField otrainfoInputAgregar;
    @FXML
    private Button agregarCliente;

    // Conexión a la base de datos
    private Connection connection;
    
    // Panel de pestañas
    @FXML
    private TabPane miTabPane;

    // Tabla para mostrar los clientes
    @FXML
    public TableView<ClientesClass> tablaClientes;

    // Información del usuario y nivel de acceso
    private UsuarioInfo usuarioInfo;
    private int nivelAcceso;
    
    // Método para establecer el nivel de acceso
    public void setNivelAcceso(int nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }

    // Método para establecer la información del usuario
    public void setUsuarioInfo(UsuarioInfo usuarioInfo) {
        this.usuarioInfo = usuarioInfo;
    }

    // Método llamado al inicializar el controlador
    @FXML
    private void initialize() {
        try {
            // Establecer la conexión a la base de datos
            connection = DataSourceManager.getConnection();

            // Configurar la acción cuando se selecciona un cliente en la tabla
            tablaClientes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    // Mostrar los detalles del cliente seleccionado en los campos de edición
                    dniInputModificar.setText(newValue.getDni());
                    nombreInputModificar.setText(newValue.getNombreCliente());
                    apellidosInputModificar.setText(newValue.getApellidoCliente());
                    direccionInputModificar.setText(newValue.getDireccion());
                    ciudadInputModificar.setText(newValue.getCiudad());
                    CPInputModificar.setText(String.valueOf(newValue.getCodPostal()));
                    telefonoInputModificar.setText(newValue.getTelefono());
                    emailInputModificar.setText(newValue.getEmail());
                    otrainfoInputModificar.setText(newValue.getOtraInformacion());
                } else {
                    // Limpiar los campos si no se selecciona ningún cliente
                    dniInputModificar.clear();
                    nombreInputModificar.clear();
                    apellidosInputModificar.clear();
                    direccionInputModificar.clear();
                    ciudadInputModificar.clear();
                    CPInputModificar.clear();
                    telefonoInputModificar.clear();
                    emailInputModificar.clear();
                    otrainfoInputModificar.clear();
                }
            });
        } catch (SQLException e) {
             e.printStackTrace();
             Utilidades.MensajeError.mostrarMensaje("Error al acceder a la base de datos", AlertType.ERROR);
        } catch (Exception e) {
             e.printStackTrace();
             Utilidades.MensajeError.mostrarMensaje("Error inesperado: " + e.getMessage(), AlertType.ERROR);
         }
    }

    // Método para agregar un nuevo cliente
    @FXML
    private void agregarCliente() throws SQLException {
        try {
            connection = DataSourceManager.getConnection();
            String dni = dniInputAgregar.getText();
            String nombre = nombreInputAgregar.getText();
            String apellidos = apellidosInputAgregar.getText();
            String direccion = direccionInputAgregar.getText();
            String ciudad = ciudadInputAgregar.getText();
            Integer codPostal = Integer.valueOf(CPInputAgregar.getText());
            String telefono = telefonoInputAgregar.getText();
            String email = emailInputAgregar.getText();
            String otraInformacion = otrainfoInputAgregar.getText();

            String query = "INSERT INTO clientes (dni, nombre_cliente, apellido_cliente, direccion, ciudad, codPostal, telefono, email, otra_informacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, dni);
            preparedStatement.setString(2, nombre);
            preparedStatement.setString(3, apellidos);
            preparedStatement.setString(4, direccion);
            preparedStatement.setString(5, ciudad);
            preparedStatement.setInt(6, codPostal);
            preparedStatement.setString(7, telefono);
            preparedStatement.setString(8, email);
            preparedStatement.setString(9, otraInformacion);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Registro insertado exitosamente");
                limpiarCamposAgregar();
            } else {
                System.out.println("No se pudo insertar el registro");
            }
        } catch (SQLException e) {
             e.printStackTrace();
             Utilidades.MensajeError.mostrarMensaje("Error al acceder a la base de datos", AlertType.ERROR);
        } catch (Exception e) {
             e.printStackTrace();
             Utilidades.MensajeError.mostrarMensaje("Error inesperado: " + e.getMessage(), AlertType.ERROR);
         }
    }

    // Método para limpiar los campos al modificar un cliente
    private void limpiarCamposModificar() {
        dniInputModificar.clear();
        nombreInputModificar.clear();
        apellidosInputModificar.clear();
        direccionInputModificar.clear();
        ciudadInputModificar.clear();
        CPInputModificar.clear();
        telefonoInputModificar.clear();
        emailInputModificar.clear();
        otrainfoInputModificar.clear();
    }

    // Método para limpiar los campos al agregar un cliente
    private void limpiarCamposAgregar() {
        dniInputAgregar.clear();
        nombreInputAgregar.clear();
        apellidosInputAgregar.clear();
        direccionInputAgregar.clear();
        ciudadInputAgregar.clear();
        CPInputAgregar.clear();
        telefonoInputAgregar.clear();
        emailInputAgregar.clear();
        otrainfoInputAgregar.clear();
    }

    // Campos de entrada para la búsqueda de clientes
    @FXML
    private TextField dniField;
    @FXML
    private TextField nombreField;
    @FXML
    private TextField apellidosField;

    // Método para buscar clientes en la base de datos
    @FXML
    public void buscarClientes() throws SQLException {
        String dni = dniField.getText();
        String nombre = nombreField.getText();
        String apellidos = apellidosField.getText();

        try {
            connection = DataSourceManager.getConnection();

            String query = "SELECT * FROM clientes WHERE";
            boolean isFirst = true;

            if (!dni.isEmpty()) {
                query += " dni LIKE ?";
                isFirst = false;
            }

            if (!nombre.isEmpty()) {
                if (!isFirst) {
                    query += " OR";
                }
                query += " nombre_cliente LIKE ?";
                isFirst = false;
            }

            if (!apellidos.isEmpty()) {
                if (!isFirst) {
                    query += " OR";
                }
                query += " apellido_cliente LIKE ?";
            }

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            int parameterIndex = 1;

            if (!dni.isEmpty()) {
                preparedStatement.setString(parameterIndex++, "%" + dni + "%");
            }

            if (!nombre.isEmpty()) {
                preparedStatement.setString(parameterIndex++, "%" + nombre + "%");
            }

            if (!apellidos.isEmpty()) {
                preparedStatement.setString(parameterIndex, "%" + apellidos + "%");
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            tablaClientes.getItems().clear();

            while (resultSet.next()) {
                // Obtener los detalles del cliente desde la base de datos
                String nombreCliente = resultSet.getString("nombre_cliente");
                String apellidosCliente = resultSet.getString("apellido_cliente");
                String direccion = resultSet.getString("direccion");
                String ciudad = resultSet.getString("ciudad");
                Integer codPostal = resultSet.getInt("codPostal");
                String telefono = resultSet.getString("telefono");
                String email = resultSet.getString("email");
                String otraInformacion = resultSet.getString("otra_informacion");

                // Crear un objeto ClienteClass y agregarlo a la tabla
                ClientesClass cliente = new ClientesClass();
                cliente.setDni(dni);
                cliente.setNombreCliente(nombreCliente);
                cliente.setApellidoCliente(apellidosCliente);
                cliente.setDireccion(direccion);
                cliente.setCiudad(ciudad);
                cliente.setCodPostal(codPostal);
                cliente.setTelefono(telefono);
                cliente.setEmail(email);
                cliente.setOtraInformacion(otraInformacion);

                tablaClientes.getItems().add(cliente);
            }

            preparedStatement.close();
            connection.close();
            limpiarCamposAgregar();
         } catch (SQLException e) {
             e.printStackTrace();
             Utilidades.MensajeError.mostrarMensaje("Error al acceder a la base de datos", AlertType.ERROR);
        } catch (Exception e) {
             e.printStackTrace();
             Utilidades.MensajeError.mostrarMensaje("Error inesperado: " + e.getMessage(), AlertType.ERROR);
        }
    }

    // Campos de entrada para modificar un cliente
    @FXML
    private TextField dniInputModificar;
    @FXML
    private TextField nombreInputModificar;
    @FXML
    private TextField apellidosInputModificar;
    @FXML
    private TextField direccionInputModificar;
    @FXML
    private TextField ciudadInputModificar;
    @FXML
    private TextField CPInputModificar;
    @FXML
    private TextField telefonoInputModificar;
    @FXML
    private TextField emailInputModificar;
    @FXML
    private TextField otrainfoInputModificar;
    @FXML
    private Button modificarClienteButton;

    // Método para modificar un cliente
    @FXML
    private void modificarCliente() {
        // Obtener el cliente seleccionado en la tabla
        ClientesClass clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado != null) {
            try {
                connection = DataSourceManager.getConnection();
                // Obtener los datos modificados desde los campos de entrada
                String dniModificado = dniInputModificar.getText();
                String nombreModificado = nombreInputModificar.getText();
                String apellidosModificado = apellidosInputModificar.getText();
                String direccionModificada = direccionInputModificar.getText();
                String ciudadModificada = ciudadInputModificar.getText();
                Integer codPostalModificado = Integer.valueOf(CPInputModificar.getText());
                String telefonoModificado = telefonoInputModificar.getText();
                String emailModificado = emailInputModificar.getText();
                String otraInformacionModificada = otrainfoInputModificar.getText();

                // Consulta SQL para actualizar los datos del cliente
                String query = "UPDATE clientes SET dni = ?, nombre_cliente = ?, apellido_cliente = ?, direccion = ?, ciudad = ?, codPostal = ?, telefono = ?, email = ?, otra_informacion = ? WHERE dni = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, dniModificado);
                    preparedStatement.setString(2, nombreModificado);
                    preparedStatement.setString(3, apellidosModificado);
                    preparedStatement.setString(4, direccionModificada);
                    preparedStatement.setString(5, ciudadModificada);
                    preparedStatement.setInt(6, codPostalModificado);
                    preparedStatement.setString(7, telefonoModificado);
                    preparedStatement.setString(8, emailModificado);
                    preparedStatement.setString(9, otraInformacionModificada);
                    preparedStatement.setString(10, clienteSeleccionado.getDni());
                    
                    // Ejecutar la consulta y verificar el resultado
                    int rowsUpdated = preparedStatement.executeUpdate();
                    if (rowsUpdated > 0) {
                        mostrarDialogoExito("Registro actualizado exitosamente");
                    } else {
                        mostrarDialogoError("No se pudo actualizar el registro");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (connection != null) {
                        connection.close(); // Cerrar la conexión en el bloque finally
                        limpiarCamposModificar();
                    }
                } catch (SQLException e) {
                e.printStackTrace();
                Utilidades.MensajeError.mostrarMensaje("Error al acceder a la base de datos", AlertType.ERROR);
               } catch (Exception e) {
                e.printStackTrace();
                Utilidades.MensajeError.mostrarMensaje("Error inesperado: " + e.getMessage(), AlertType.ERROR);
               }
            }
        } else {
            System.out.println("Por favor selecciona un cliente de la tabla para modificar.");
        }
    }

    // Método para mostrar un diálogo de éxito
    private void mostrarDialogoExito(String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Método para mostrar un diálogo de error
    private void mostrarDialogoError(String mensaje) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Método para trasladar los datos a los campos de modificación
    @FXML
    private void trasladarDatosAModificar() {
        // Obtener el cliente seleccionado en la tabla
        ClientesClass clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();
    
        if (clienteSeleccionado != null) {
            // Llenar los campos de modificación con los datos del cliente seleccionado
            dniInputModificar.setText(clienteSeleccionado.getDni());
            nombreInputModificar.setText(clienteSeleccionado.getNombreCliente());
            apellidosInputModificar.setText(clienteSeleccionado.getApellidoCliente());
            direccionInputModificar.setText(clienteSeleccionado.getDireccion());
            ciudadInputModificar.setText(clienteSeleccionado.getCiudad());
            CPInputModificar.setText(String.valueOf(clienteSeleccionado.getCodPostal()));
            telefonoInputModificar.setText(clienteSeleccionado.getTelefono());
            emailInputModificar.setText(clienteSeleccionado.getEmail());
            otrainfoInputModificar.setText(clienteSeleccionado.getOtraInformacion());
        
            // Cambiar a la pestaña de modificación
            miTabPane.getSelectionModel().select(1);
        } else {
            System.out.println("Por favor selecciona un cliente de la tabla.");
        }
    }

    // Método para cerrar la ventana actual y mostrar la ventana principal
    @FXML
    private void cerrarMenu(ActionEvent event) {
        try {
            // Obtener la ventana actual y cerrarla
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
            // Abrir la ventana principal
            InicioMain inicioMain = new InicioMain();
            inicioMain.start(new Stage());
             } catch (SQLException e) {
             e.printStackTrace();
             Utilidades.MensajeError.mostrarMensaje("Error al acceder a la base de datos", AlertType.ERROR);
            } catch (Exception e) {
             e.printStackTrace();
             Utilidades.MensajeError.mostrarMensaje("Error inesperado: " + e.getMessage(), AlertType.ERROR);
         }
    }

}

