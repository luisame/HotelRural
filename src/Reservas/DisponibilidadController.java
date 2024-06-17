package Reservas;

import Inicio.InicioController;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utilidades.DataSourceManager;
import utilidades.UsuarioInfo;

/**
 * Controlador para manejar la disponibilidad de habitaciones.
 */
public class DisponibilidadController {

    // Componentes de la interfaz de usuario
    @FXML
    private DatePicker fechaInicio;  // Campo de la interfaz de usuario
    @FXML
    private DatePicker fechaFin;  // Campo de la interfaz de usuario
    @FXML
    private TextField numeroPersonas;  // Campo de la interfaz de usuario
    @FXML
    private TableView<Habitacion> tablaHabitacionesDisponibles;  // Campo de la interfaz de usuario
    @FXML
    private Label descripcionHabitacionLabel;  // Campo de la interfaz de usuario
    @FXML
    private Label precioTotalLabel;  // Campo de la interfaz de usuario
    @FXML
    private TextField numNinosMenores6;  // Campo de la interfaz de usuario
    @FXML
    private TextField numNinos612;  // Campo de la interfaz de usuario
    @FXML
    private TableColumn<Habitacion, BigDecimal> precioHabitacionNocheColumn;  // Campo de la interfaz de usuario
    @FXML
    private Label precioBaseLabel;  // Campo de la interfaz de usuario
    @FXML
    private Label numPersonasLabel;  // Campo de la interfaz de usuario
    @FXML
    private Label numNinosLabel;  // Campo de la interfaz de usuario
    @FXML
    private Label numBebesLabel;  // Campo de la interfaz de usuario
    @FXML
    private Label suplementoLabel;  // Campo de la interfaz de usuario

    // Variables de control
    private BigDecimal precioBase = BigDecimal.ZERO;  // Variable de instancia
    private int numNinos = 0;  // Variable de instancia
    private int numBebes = 0;  // Variable de instancia
    private BigDecimal suplementoTemporada;  // Variable de instancia
    private UsuarioInfo usuarioInfo;  // Variable de instancia
    private Stage reservaStage = null;  // Variable de instancia para rastrear la ventana de reserva
    public ReservaController reservaController;  // Variable de instancia
    public Object inicioController;  // Variable de instancia
    private int numPersonas;
    // Descuento para los niños entre 6 y 12 años
    private final BigDecimal DESCUENTO_NINOS_612 = BigDecimal.valueOf(0.5);  // Variable de instancia

    /**
     * Establece la información del usuario.
     * 
     * @param usuarioInfo La información del usuario.
     */
    public void setUsuarioInfo(UsuarioInfo usuarioInfo) {
        this.usuarioInfo = usuarioInfo;
    }

    /**
     * Establece la fecha de inicio.
     * 
     * @param fechaInicio La fecha de inicio a establecer.
     */
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio.setValue(fechaInicio);
    }

    /**
     * Establece la fecha de fin.
     * 
     * @param fechaFin La fecha de fin a establecer.
     */
    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin.setValue(fechaFin);
    }

    /**
     * Establece el número de personas.
     * 
     * @param numeroPersonas El número de personas a establecer.
     */
    public void setNumeroPersonas(int numeroPersonas) {
        this.numeroPersonas.setText(String.valueOf(numeroPersonas));
    }

    /**
     * Método para abrir la ventana de reserva.
     * 
     * @param habitacionSeleccionada La habitación seleccionada para la reserva.
     */
    private void mostrarFXMLReservas(Habitacion habitacionSeleccionada) {
        if (reservaStage != null && reservaStage.isShowing()) {
            reservaStage.toFront();
            return;
        }
        try {
            LocalDate inicio = fechaInicio.getValue();
            LocalDate fin = fechaFin.getValue();
            int numAdultos = Integer.parseInt(numeroPersonas.getText().isEmpty() ? "0" : numeroPersonas.getText());
            int numNinos612Int = Integer.parseInt(numNinos612.getText().isEmpty() ? "0" : numNinos612.getText());
            int numMenores6 = Integer.parseInt(numNinosMenores6.getText().isEmpty() ? "0" : numNinosMenores6.getText());
            
            UsuarioInfo usuarioInfor = UsuarioInfo.getInstance();
            BigDecimal precioTotalReserva = calcularPrecioTotal(habitacionSeleccionada, inicio, fin, numAdultos, numNinos612Int, numMenores6);
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservas/ReservaFXML.fxml"));
            Parent root = loader.load();
            ReservaController controller = loader.getController();

            controller.setPrecioBase(precioBase); 
            controller.setNumPersonas(numAdultos); 
            controller.setNumNinos(numNinos612Int); 
            controller.setNumBebes(numMenores6); 
            controller.setSuplemento(suplementoTemporada); 
            controller.setPrecioTotal(precioTotalReserva);
            controller.setUsuarioInfo(usuarioInfor);
            controller.iniciarConDatosDeReserva(habitacionSeleccionada, inicio, fin, numAdultos);

            reservaStage = new Stage();
            reservaStage.setTitle("Detalles de la Reserva");
            reservaStage.setScene(new Scene(root));
            reservaStage.show();
        } catch (IOException e) {
            String errorMessage = "Error al cargar la ventana de reserva. Por favor, inténtelo de nuevo más tarde.";
            System.err.println(errorMessage);
            mostrarAlerta("Error de carga", errorMessage + "\nDetalles: " + e.getMessage());
        }
    }

    /**
     * Establece el controlador de reserva.
     * 
     * @param reservaController El controlador de reserva.
     */
    public void setReservaController(ReservaController reservaController) {
        this.reservaController = reservaController;
    }

    /**
     * Establece el precio total.
     * 
     * @param precioTotal El precio total.
     */
    public void setPrecioTotal(BigDecimal precioTotal) {
        if (precioTotalLabel != null) {
            precioTotalLabel.setText(String.format("€%s", precioTotal.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()));
        } else {
            System.err.println("precioTotalLabel no está inicializado.");
        }
    }

    /**
     * Limpia el formulario.
     * 
     * @param event El evento de acción que desencadena este método.
     */
    @FXML
    private void limpiarFormulario(ActionEvent event) {
        fechaInicio.setValue(null);
        fechaFin.setValue(null);
        numeroPersonas.clear();
        numNinosMenores6.clear();
        numNinos612.clear();
        tablaHabitacionesDisponibles.getItems().clear();
        precioTotalLabel.setText("Precio Total: €0.00");
    }

    /**
     * Método de inicialización.
     */
    @FXML
    public void initialize() {
        precioHabitacionNocheColumn.setCellValueFactory(new PropertyValueFactory<>("precioTotal"));

        // Configuración del DatePicker para deshabilitar fechas anteriores a hoy
        fechaInicio.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
                if (date.equals(LocalDate.now())) {
                    setStyle("-fx-background-color: lightgreen;");
                }
            }
        });

        // Listener para actualizar fechaFin cuando se selecciona fechaInicio
        fechaInicio.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fechaFin.setDayCellFactory(picker -> new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        setDisable(empty || date.isBefore(newValue.plusDays(1)));
                        if (date.equals(newValue.plusDays(1))) {
                            setStyle("-fx-background-color: lightgreen;");
                        }
                    }
                });
                // Actualizar el valor de fechaFin para abrir el DatePicker en el mes/año de fechaInicio
                fechaFin.setValue(newValue.plusDays(1));
            }
        });

        // Listener para la selección de habitaciones
        tablaHabitacionesDisponibles.setRowFactory(tv -> {
            TableRow<Habitacion> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY) {
                    Habitacion clickedRow = row.getItem();
                    if (event.getClickCount() == 1) {
                        LocalDate inicio = fechaInicio.getValue();
                        LocalDate fin = fechaFin.getValue();
                        try {
                            int numAdultos = Integer.parseInt(numeroPersonas.getText());
                            int numNinos612Int = Integer.parseInt(numNinos612.getText());
                            int numNinosMenores6Int = Integer.parseInt(numNinosMenores6.getText());
                            calcularPrecioTotal(clickedRow, inicio, fin, numAdultos, numNinos612Int, numNinosMenores6Int);
                        } catch (NumberFormatException e) {
                            mostrarAlerta("Error de entrada", "Por favor, ingrese números válidos.");
                        }
                    } else if (event.getClickCount() == 2) {
                        mostrarFXMLReservas(clickedRow);
                    }
                }
            });
            return row;
        });
    }

    /**
     * Calcula el precio total de la reserva.
     * 
     * @param habitacionSeleccionada La habitación seleccionada.
     * @param inicio La fecha de inicio.
     * @param fin La fecha de fin.
     * @param numAdultos El número de adultos.
     * @param numNinosEntre6Y12 El número de niños entre 6 y 12 años.
     * @param numNinosMenores6Int El número de niños menores de 6 años.
     * @return El precio total de la reserva.
     */
    private BigDecimal calcularPrecioTotal(Habitacion habitacionSeleccionada, LocalDate inicio, LocalDate fin, int numAdultos, int numNinosEntre6Y12, int numNinosMenores6Int) {
        BigDecimal precioPorNoche = habitacionSeleccionada.getPrecioPorNoche();
        long numNoches = ChronoUnit.DAYS.between(inicio, fin);

        // Precio total para adultos
        precioBase = precioPorNoche.multiply(BigDecimal.valueOf(numAdultos));

        // Descuento para los niños entre 6 y 12 años
        BigDecimal precioTotalNinos612 = precioPorNoche.multiply(BigDecimal.valueOf(numNinosEntre6Y12)).multiply(DESCUENTO_NINOS_612);

        // Asignación de valores a las variables de instancia para reflejar en la interfaz de usuario
        this.numPersonas = numAdultos;
        this.numNinos = numNinosEntre6Y12;
        this.numBebes = numNinosMenores6Int;

        // Precio total antes de aplicar los suplementos de temporada
        BigDecimal precioTotal = precioBase.add(precioTotalNinos612).multiply(BigDecimal.valueOf(numNoches));

        // Implementación de la lógica para sumar el suplemento de la temporada
        suplementoTemporada = calcularSuplementoTemporada(inicio, fin);
        precioTotal = precioTotal.add(suplementoTemporada);

        // Actualizar solo el valor del total, manteniendo el símbolo de la moneda
        precioTotalLabel.setText(precioTotal.toPlainString());
        return precioTotal;
    }

    /**
     * Actualiza la información de la reserva en la interfaz de usuario.
     */
    private void actualizarInformacionDeReserva() {
        precioBaseLabel.setText(precioBase.toPlainString());
        numPersonasLabel.setText(String.valueOf(numPersonas));
        numNinosLabel.setText(String.valueOf(numNinos));
        numBebesLabel.setText(String.valueOf(numBebes));
        suplementoLabel.setText(suplementoTemporada.toPlainString());
    }

    /**
     * Calcula los valores de la reserva.
     */
    public void calcularValores() {
        try {
            int numAdultos = Integer.parseInt(numeroPersonas.getText());
            int numNinosMenores6Int = Integer.parseInt(numNinosMenores6.getText());
            int numNinos612Int = Integer.parseInt(numNinos612.getText());
            actualizarInformacionDeReserva();
        } catch (NumberFormatException e) {
            System.err.println("Error al parsear la entrada del usuario.");
        }
    }

    /**
     * Maneja el evento de clic en una habitación disponible.
     * 
     * @param event El evento de ratón.
     */
    @FXML
    public void handleTablaHabitacionesDisponiblesMouseClicked(MouseEvent event) {
        Habitacion habitacionSeleccionada = tablaHabitacionesDisponibles.getSelectionModel().getSelectedItem();
        if (habitacionSeleccionada != null && event.getButton() == MouseButton.PRIMARY) {
            if (event.getClickCount() == 2) {
                mostrarFXMLReservas(habitacionSeleccionada);
            } else if (event.getClickCount() == 1) {
                LocalDate inicio = fechaInicio.getValue();
                LocalDate fin = fechaFin.getValue();
                if (inicio != null && fin != null) {
                    try {
                        calcularValores();
                        int numAdultos = Integer.parseInt(numeroPersonas.getText());
                        int numNinosEntre6Y12 = Integer.parseInt(numNinos612.getText());
                        int numNinosMenores6Int = Integer.parseInt(numNinosMenores6.getText());
                        calcularPrecioTotal(habitacionSeleccionada, inicio, fin, numAdultos, numNinosEntre6Y12, numNinosMenores6Int);
                    } catch (NumberFormatException e) {
                        mostrarAlerta("Error de entrada", "Por favor, ingrese números válidos.");
                    }
                } else {
                    mostrarAlerta("Error de fechas", "Por favor, seleccione las fechas de inicio y fin.");
                }
            }
        }
    }

    /**
     * Calcula el suplemento de temporada.
     * 
     * @param inicio La fecha de inicio.
     * @param fin La fecha de fin.
     * @return El suplemento total de la temporada.
     */
    private BigDecimal calcularSuplementoTemporada(LocalDate inicio, LocalDate fin) {
        BigDecimal suplementoTotal = BigDecimal.ZERO;
        String sql = "SELECT rango_fechas_desde, rango_fechas_hasta, precio_temporada " +
                     "FROM temporadas " +
                     "WHERE rango_fechas_desde <= ? AND rango_fechas_hasta >= ?";

        try (Connection conn = DataSourceManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, java.sql.Date.valueOf(fin));
            stmt.setDate(2, java.sql.Date.valueOf(inicio));

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LocalDate fechaDesde = rs.getDate("rango_fechas_desde").toLocalDate();
                LocalDate fechaHasta = rs.getDate("rango_fechas_hasta").toLocalDate();

                LocalDate solapamientoInicio = inicio.isAfter(fechaDesde) ? inicio : fechaDesde;
                LocalDate solapamientoFin = fin.isBefore(fechaHasta) ? fin : fechaHasta;

                long diasSolapamiento = ChronoUnit.DAYS.between(solapamientoInicio, solapamientoFin.plusDays(1));

                BigDecimal suplementoPorDia = rs.getBigDecimal("precio_temporada");
                BigDecimal suplementoParaRango = suplementoPorDia.multiply(BigDecimal.valueOf(diasSolapamiento));
                suplementoTotal = suplementoTotal.add(suplementoParaRango);
            }
            
            rs.close();
        } catch (SQLException e) {
            String errorMessage = "Error al calcular el suplemento de temporada. Por favor, inténtelo de nuevo más tarde.";
            System.err.println(errorMessage);
            mostrarAlerta("Error de base de datos", errorMessage + "\nDetalles: " + e.getMessage());
        }
        return suplementoTotal;
    }

    /**
     * Método para volver al inicio.
     * 
     * @param event El evento de acción que desencadena este método.
     */
    @FXML
    public void volverAlInicio(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Inicio/InicioFXML.fxml"));
            Parent root = loader.load();
            InicioController inicioController = loader.getController();
            
            if (this.usuarioInfo != null) {
                inicioController.setUsuarioInfo(this.usuarioInfo);
            } else {
                mostrarAlerta("Error", "Usuario no autenticado. Por favor, inicie sesión de nuevo.");
                return;
            }

            Stage stage = new Stage();
            stage.setTitle("Inicio");
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } catch (IOException e) {
            String errorMessage = "Error al cargar la pantalla de inicio. Por favor, inténtelo de nuevo más tarde.";
            System.err.println(errorMessage);
            mostrarAlerta("Error de carga", errorMessage + "\nDetalles: " + e.getMessage());
        }
    }

    /**
     * Método para buscar disponibilidad.
     * 
     * @param event El evento de acción que desencadena este método.
     */
    @FXML
    private void buscarDisponibilidad(ActionEvent event) {
        LocalDate inicio = fechaInicio.getValue();
        LocalDate fin = fechaFin.getValue();
        int numPersonas = 0;
        try {
            numPersonas = Integer.parseInt(numeroPersonas.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Por favor, ingrese un número válido para el número de personas.");
            return;
        }

        if (inicio == null || fin == null) {
            mostrarAlerta("Error", "Por favor, seleccione las fechas de inicio y fin.");
            return;
        }
        if (inicio.isAfter(fin)) {
            mostrarAlerta("Error", "La fecha de inicio no puede ser posterior a la fecha de fin.");
            return;
        }

        ObservableList<Habitacion> habitacionesDisponibles = buscarHabitacionesDisponibles(inicio, fin, numPersonas);
        tablaHabitacionesDisponibles.setItems(habitacionesDisponibles);

        // Refrescar la tabla después de actualizar los datos
        tablaHabitacionesDisponibles.refresh();
    }

    /**
     * Busca habitaciones disponibles.
     * 
     * @param inicio La fecha de inicio.
     * @param fin La fecha de fin.
     * @param numPersonas El número de personas.
     * @return La lista de habitaciones disponibles.
     */
    private ObservableList<Habitacion> buscarHabitacionesDisponibles(LocalDate inicio, LocalDate fin, int numPersonas) {
        ObservableList<Habitacion> habitacionesDisponibles = FXCollections.observableArrayList();

        String sql = "SELECT h.id_habitacion, h.descripcion, h.capacidad, h.estado, h.precio " +
                     "FROM habitaciones h " +
                     "WHERE h.capacidad >= ? " +
                     "AND h.id_habitacion NOT IN (SELECT r.id_habitacion " +
                                                 "FROM reservas r " +
                                                 "WHERE (? BETWEEN r.fecha_entrada AND r.fecha_salida) " +
                                                 "OR (? BETWEEN r.fecha_entrada AND r.fecha_salida) " +
                                                 "OR (r.fecha_entrada BETWEEN ? AND ?) " +
                                                 "OR (r.fecha_salida BETWEEN ? AND ?))";

        try (Connection conn = DataSourceManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, numPersonas);
            stmt.setDate(2, java.sql.Date.valueOf(inicio));
            stmt.setDate(3, java.sql.Date.valueOf(fin));
            stmt.setDate(4, java.sql.Date.valueOf(inicio));
            stmt.setDate(5, java.sql.Date.valueOf(fin));
            stmt.setDate(6, java.sql.Date.valueOf(inicio));
            stmt.setDate(7, java.sql.Date.valueOf(fin));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Habitacion habitacion = new Habitacion(
                            rs.getInt("id_habitacion"),
                            rs.getString("descripcion"),
                            rs.getInt("capacidad"),
                            rs.getString("estado"),
                            rs.getBigDecimal("precio"));

                    habitacionesDisponibles.add(habitacion);
                }
            }
        } catch (SQLException e) {
            String errorMessage = "Error al buscar habitaciones disponibles. Por favor, inténtelo de nuevo más tarde.";
            System.err.println(errorMessage);
            mostrarAlerta("Error de base de datos", errorMessage + "\nDetalles: " + e.getMessage());
        }
        return habitacionesDisponibles;
    }

    /**
     * Muestra una alerta.
     * 
     * @param titulo El título de la alerta.
     * @param mensaje El mensaje de la alerta.
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
