package Clientes;

import Inicio.InicioMain;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import utilidades.DataSourceManager;
import utilidades.UsuarioInfo;

public class ClientesControlador {
    // Campos de entrada para agregar un nuevo cliente
    @FXML private TextField dniInputAgregar, nombreInputAgregar, apellidosInputAgregar, direccionInputAgregar, ciudadInputAgregar, CPInputAgregar, telefonoInputAgregar, emailInputAgregar, otrainfoInputAgregar;

    // Campos para modificar un cliente
    @FXML private TextField dniInputModificar, nombreInputModificar, apellidosInputModificar, direccionInputModificar, ciudadInputModificar, CPInputModificar, telefonoInputModificar, emailInputModificar, otrainfoInputModificar;

    // Campos para buscar clientes
    @FXML private TextField dniField, nombreField, apellidosField;

    @FXML private TableView<ClientesClass> tablaClientes;

    private ClienteDAO clienteDAO;
    private UsuarioInfo usuarioInfo;

    @FXML
    //Inicializa la instancia de ClienteDAO que se usará para interactuar con la base de datos.
    private void initialize() {
        clienteDAO = new ClienteDAO(new DataSourceManager());
      // Aplicar restricciones para agregar un nuevo cliente
        TextFieldLimitador.aplicarLongitudMaxima(dniInputAgregar, 10);
        TextFieldLimitador.aplicarLongitudMaxima(nombreInputAgregar, 40);
        TextFieldLimitador.aplicarLongitudMaxima(apellidosInputAgregar, 80);
        TextFieldLimitador.aplicarLongitudMaxima(direccionInputAgregar, 256);
        TextFieldLimitador.aplicarLongitudMaxima(ciudadInputAgregar, 100);
        TextFieldLimitador.aplicarLongitudMaxima(CPInputAgregar, 11); // Aunque es un int, limitamos la longitud de entrada
        TextFieldLimitador.aplicarLongitudMaxima(telefonoInputAgregar, 9);
        TextFieldLimitador.aplicarLongitudMaxima(emailInputAgregar, 26);
        
        TextFieldLimitador.aplicarLongitudMaxima(dniInputModificar, 10);
        TextFieldLimitador.aplicarLongitudMaxima(nombreInputModificar, 40);
        TextFieldLimitador.aplicarLongitudMaxima(apellidosInputModificar, 80);
        TextFieldLimitador.aplicarLongitudMaxima(direccionInputModificar, 256);
        TextFieldLimitador.aplicarLongitudMaxima(ciudadInputModificar, 100);
        TextFieldLimitador.aplicarLongitudMaxima(CPInputModificar, 11);
        TextFieldLimitador.aplicarLongitudMaxima(telefonoInputModificar, 9);
        TextFieldLimitador.aplicarLongitudMaxima(emailInputModificar, 26);
    }
// Método para establecer la información del usuario
    public void setUsuarioInfo(UsuarioInfo usuarioInfo) {
        this.usuarioInfo = usuarioInfo;
    }
    @FXML
    private void agregarCliente() {
        try {
            ClientesClass nuevoCliente = obtenerDatosClienteDesdeFormularioAgregar();
            clienteDAO.agregarCliente(nuevoCliente);
            mostrarDialogoExito("Cliente agregado exitosamente.");
            //cargarClientes();
            limpiarCamposAgregar();
        } catch (Exception e) {
            mostrarDialogoError("Error al agregar cliente: " + e.getMessage());
        }
    }

    @FXML
    private void modificarCliente() {
        try {
            ClientesClass clienteSeleccionado = obtenerDatosClienteDesdeFormularioModificar();
            clienteDAO.actualizarCliente(clienteSeleccionado);
            mostrarDialogoExito("Cliente modificado exitosamente.");
            //cargarClientes();
            limpiarCamposModificar();
        } catch (Exception e) {
            mostrarDialogoError("Error al modificar cliente: " + e.getMessage());
        }
    }
  // Método para trasladar los datos a los campos de modificación
    @FXML
    private TabPane miTabPane;

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
    @FXML
    private void eliminarCliente() {
        try {
            ClientesClass clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();
            if (clienteSeleccionado != null) {
                clienteDAO.eliminarCliente(clienteSeleccionado.getDni());
                mostrarDialogoExito("Cliente eliminado exitosamente.");
                cargarClientes();
            }
        } catch (Exception e) {
            mostrarDialogoError("Error al eliminar cliente: " + e.getMessage());
        }
    }

    @FXML
private void buscarClientes() {
    try {
        String dni = dniField.getText().trim();
        String nombre = nombreField.getText().trim();
        String apellidos = apellidosField.getText().trim();
    // Limpiar la tabla antes de agregar nuevos resultados
        tablaClientes.getItems().clear();
        ObservableList<ClientesClass> listaClientes = clienteDAO.buscarClientes(dni, nombre, apellidos);
        tablaClientes.setItems(listaClientes);
    } catch (Exception e) {
        mostrarDialogoError("Error al buscar clientes: " + e.getMessage());
    }
}

   private ClientesClass obtenerDatosClienteDesdeFormularioAgregar() {
    ClientesClass cliente = new ClientesClass();
    cliente.setDni(dniInputAgregar.getText());
    cliente.setNombreCliente(nombreInputAgregar.getText());
    cliente.setApellidoCliente(apellidosInputAgregar.getText());
    cliente.setDireccion(direccionInputAgregar.getText());
    cliente.setCiudad(ciudadInputAgregar.getText());

    // Convertir a entero si es necesario y manejar posibles excepciones
    try {
    int codigoPostal = Integer.parseInt(CPInputAgregar.getText());
    cliente.setCodPostal(codigoPostal);
} catch (NumberFormatException e) {
    mostrarDialogoError("El código postal debe ser un número válido.");
    // Puedes decidir cómo manejar este error, por ejemplo, limpiar el campo de entrada:
    CPInputAgregar.clear();
}
    


    cliente.setTelefono(telefonoInputAgregar.getText());
    cliente.setEmail(emailInputAgregar.getText());
    cliente.setOtraInformacion(otrainfoInputAgregar.getText());
    return cliente;
}


    private ClientesClass obtenerDatosClienteDesdeFormularioModificar() {
    ClientesClass cliente = new ClientesClass();
    cliente.setDni(dniInputModificar.getText());
    cliente.setNombreCliente(nombreInputModificar.getText());
    cliente.setApellidoCliente(apellidosInputModificar.getText());
    cliente.setDireccion(direccionInputModificar.getText());
    cliente.setCiudad(ciudadInputModificar.getText());

    // Convertir a entero si es necesario y manejar posibles excepciones
    try {
    int codigoPostal = Integer.parseInt(CPInputModificar.getText());
    cliente.setCodPostal(codigoPostal);
} catch (NumberFormatException e) {
    mostrarDialogoError("El código postal debe ser un número válido.");
    // Puedes decidir cómo manejar este error, por ejemplo, limpiar el campo de entrada:
    CPInputAgregar.clear();
}
    cliente.setTelefono(telefonoInputModificar.getText());
    cliente.setEmail(emailInputModificar.getText());
    cliente.setOtraInformacion(otrainfoInputModificar.getText());
    return cliente;
}

    private void cargarClientes() throws SQLException {
        ObservableList<ClientesClass> listaClientes = clienteDAO.obtenerTodosLosClientes();
        tablaClientes.setItems(listaClientes);
    }

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


private void mostrarDialogoExito(String mensaje) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Éxito");
    alert.setHeaderText(null);
    alert.setContentText(mensaje);
    alert.showAndWait();
}

private void mostrarDialogoError(String mensaje) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText(null);
    alert.setContentText(mensaje);
    alert.showAndWait();
}

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
             Utilidades.MensajeError.mostrarMensaje("Error al acceder a la base de datos");
            } catch (Exception e) {
             Utilidades.MensajeError.mostrarMensaje("Error inesperado: " +  AlertType.ERROR);
         }
    }
}