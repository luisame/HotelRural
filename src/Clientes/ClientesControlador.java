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

/**
 * Controlador para gestionar las operaciones relacionadas con los clientes.
 */
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

    /**
     * Inicializa la instancia de ClienteDAO que se usará para interactuar con la base de datos.
     */
    @FXML
    private void initialize() {
        clienteDAO = new ClienteDAO(new DataSourceManager());

        // Aplicar restricciones de longitud máxima para los campos de entrada
        TextFieldLimitador.aplicarLongitudMaxima(dniInputAgregar, 10);
        TextFieldLimitador.aplicarLongitudMaxima(nombreInputAgregar, 40);
        TextFieldLimitador.aplicarLongitudMaxima(apellidosInputAgregar, 80);
        TextFieldLimitador.aplicarLongitudMaxima(direccionInputAgregar, 256);
        TextFieldLimitador.aplicarLongitudMaxima(ciudadInputAgregar, 100);
        TextFieldLimitador.aplicarLongitudMaxima(CPInputAgregar, 11);
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

    /**
     * Establece la información del usuario.
     *
     * @param usuarioInfo La información del usuario.
     */
    public void setUsuarioInfo(UsuarioInfo usuarioInfo) {
        this.usuarioInfo = usuarioInfo;
    }

    /**
     * Agrega un nuevo cliente a la base de datos.
     */
    @FXML
    private void agregarCliente() {
        try {
            ClientesClass nuevoCliente = obtenerDatosClienteDesdeFormularioAgregar();
            clienteDAO.agregarCliente(nuevoCliente);
            mostrarDialogoExito("Cliente agregado exitosamente.");
            limpiarCamposAgregar();
        } catch (Exception e) {
            mostrarDialogoError("Error al agregar cliente: " + e.getMessage());
        }
    }

    /**
     * Modifica la información de un cliente existente en la base de datos.
     */
    @FXML
    private void modificarCliente() {
        try {
            ClientesClass clienteSeleccionado = obtenerDatosClienteDesdeFormularioModificar();
            clienteDAO.actualizarCliente(clienteSeleccionado);
            mostrarDialogoExito("Cliente modificado exitosamente.");
            limpiarCamposModificar();
        } catch (Exception e) {
            mostrarDialogoError("Error al modificar cliente: " + e.getMessage());
        }
    }

    @FXML
    private TabPane miTabPane;

    /**
     * Traslada los datos del cliente seleccionado a los campos de modificación.
     */
    @FXML
    private void trasladarDatosAModificar() {
        ClientesClass clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();
    
        if (clienteSeleccionado != null) {
            dniInputModificar.setText(clienteSeleccionado.getDni());
            nombreInputModificar.setText(clienteSeleccionado.getNombreCliente());
            apellidosInputModificar.setText(clienteSeleccionado.getApellidoCliente());
            direccionInputModificar.setText(clienteSeleccionado.getDireccion());
            ciudadInputModificar.setText(clienteSeleccionado.getCiudad());
            CPInputModificar.setText(String.valueOf(clienteSeleccionado.getCodPostal()));
            telefonoInputModificar.setText(clienteSeleccionado.getTelefono());
            emailInputModificar.setText(clienteSeleccionado.getEmail());
            otrainfoInputModificar.setText(clienteSeleccionado.getOtraInformacion());
        
            miTabPane.getSelectionModel().select(1);
        } else {
            System.out.println("Por favor selecciona un cliente de la tabla.");
        }
    }

    /**
     * Elimina el cliente seleccionado de la base de datos.
     */
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

    /**
     * Busca clientes en la base de datos según los criterios de búsqueda.
     */
    @FXML
    private void buscarClientes() {
        try {
            String dni = dniField.getText().trim();
            String nombre = nombreField.getText().trim();
            String apellidos = apellidosField.getText().trim();

            tablaClientes.getItems().clear();
            ObservableList<ClientesClass> listaClientes = clienteDAO.buscarClientes(dni, nombre, apellidos);
            tablaClientes.setItems(listaClientes);
        } catch (Exception e) {
            mostrarDialogoError("Error al buscar clientes: " + e.getMessage());
        }
    }

    /**
     * Obtiene los datos del cliente desde el formulario de agregar.
     *
     * @return Un objeto ClientesClass con los datos del cliente.
     */
    private ClientesClass obtenerDatosClienteDesdeFormularioAgregar() {
        ClientesClass cliente = new ClientesClass();
        cliente.setDni(dniInputAgregar.getText());
        cliente.setNombreCliente(nombreInputAgregar.getText());
        cliente.setApellidoCliente(apellidosInputAgregar.getText());
        cliente.setDireccion(direccionInputAgregar.getText());
        cliente.setCiudad(ciudadInputAgregar.getText());

        try {
            int codigoPostal = Integer.parseInt(CPInputAgregar.getText());
            cliente.setCodPostal(codigoPostal);
        } catch (NumberFormatException e) {
            mostrarDialogoError("El código postal debe ser un número válido.");
            CPInputAgregar.clear();
        }

        cliente.setTelefono(telefonoInputAgregar.getText());
        cliente.setEmail(emailInputAgregar.getText());
        cliente.setOtraInformacion(otrainfoInputAgregar.getText());
        return cliente;
    }

    /**
     * Obtiene los datos del cliente desde el formulario de modificar.
     *
     * @return Un objeto ClientesClass con los datos del cliente.
     */
    private ClientesClass obtenerDatosClienteDesdeFormularioModificar() {
        ClientesClass cliente = new ClientesClass();
        cliente.setDni(dniInputModificar.getText());
        cliente.setNombreCliente(nombreInputModificar.getText());
        cliente.setApellidoCliente(apellidosInputModificar.getText());
        cliente.setDireccion(direccionInputModificar.getText());
        cliente.setCiudad(ciudadInputModificar.getText());

        try {
            int codigoPostal = Integer.parseInt(CPInputModificar.getText());
            cliente.setCodPostal(codigoPostal);
        } catch (NumberFormatException e) {
            mostrarDialogoError("El código postal debe ser un número válido.");
            CPInputModificar.clear();
        }

        cliente.setTelefono(telefonoInputModificar.getText());
        cliente.setEmail(emailInputModificar.getText());
        cliente.setOtraInformacion(otrainfoInputModificar.getText());
        return cliente;
    }

    /**
     * Carga todos los clientes desde la base de datos y los muestra en la tabla.
     *
     * @throws SQLException Si ocurre un error durante la operación de la base de datos.
     */
    private void cargarClientes() throws SQLException {
        ObservableList<ClientesClass> listaClientes = clienteDAO.obtenerTodosLosClientes();
        tablaClientes.setItems(listaClientes);
    }

    /**
     * Limpia los campos del formulario de agregar.
     */
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

    /**
     * Limpia los campos del formulario de modificar.
     */
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

    /**
     * Muestra un diálogo de éxito con un mensaje dado.
     *
     * @param mensaje El mensaje a mostrar.
     */
    private void mostrarDialogoExito(String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Muestra un diálogo de error con un mensaje dado.
     *
     * @param mensaje El mensaje a mostrar.
     */
    private void mostrarDialogoError(String mensaje) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Cierra el menú actual y abre la ventana principal.
     *
     * @param event El evento de acción que desencadena el cierre del menú.
     */
    @FXML
    private void cerrarMenu(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            InicioMain inicioMain = new InicioMain();
            inicioMain.start(new Stage());
        } catch (SQLException e) {
            Utilidades.MensajeError.mostrarMensaje("Error al acceder a la base de datos");
        } catch (Exception e) {
            Utilidades.MensajeError.mostrarMensaje("Error inesperado: " + AlertType.ERROR);
        }
    }
}
