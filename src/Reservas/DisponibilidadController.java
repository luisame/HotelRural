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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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

public class DisponibilidadController {

    static Object disponibilidadController;
    // Componentes de la interfaz de usuario
    @FXML
    private DatePicker fechaInicio;
    @FXML
    private DatePicker fechaFin;
    @FXML
    private TextField numeroPersonas;
    @FXML
    private TableView<Habitacion> tablaHabitacionesDisponibles;
    @FXML
    private Label descripcionHabitacionLabel;
    @FXML
    private Label precioTotal; // Corregido: Debe ser un Label para mostrar el precio total
    @FXML
    private Label precioTotalLabel; // Añade un campo para el label del precio total
    // Variables de control
    private UsuarioInfo usuarioInfo;
    @FXML
    private TextField numNinosMenores6;
    @FXML
    private TextField numNinos612;
    @FXML
    private TableColumn<Habitacion, BigDecimal> precioHabitacionNocheColumn;
    private ReservaController reservaController;


    public void setUsuarioInfo(UsuarioInfo usuarioInfo) {
        this.usuarioInfo = usuarioInfo;
        // Aquí puedes actualizar la UI o hacer lo que necesites con la información del usuario
    
}
    @FXML
    
    private final BigDecimal PRECIO_NINOS_6 = BigDecimal.ZERO; // Precio para niños menores de 6 años (gratis)
    private final BigDecimal DESCUENTO_NINOS_612 = BigDecimal.valueOf(0.5); // Descuento del 50% para niños mayores de 6 y menores de 12 años
private BigDecimal precioSuplemento = BigDecimal.ZERO; // Valor por defecto para evitar NullPointerException
    private ObservableList<Habitacion> habitacionesDisponibles;
    
    private long numNoches;
    private int numPersonas;
private SuplementoSeleccionado suplementoSeleccionado;
   private BigDecimal suplemento = BigDecimal.ZERO;
@FXML
    private Label precioBaseLabel;
    @FXML
    private Label numPersonasLabel;
    @FXML
    private Label numNinosLabel;
    @FXML
    private Label numBebesLabel;
    @FXML
    private Label suplementoLabel;
     private BigDecimal suplementoTemporada;
      @FXML
   
    private BigDecimal precioBaseLabelValue= BigDecimal.ZERO;
    private int numPersonasLabelValue=0;
    private int numNinosLabelValue = 0;
    private int numBebesLabelValue = 0;
    private BigDecimal suplementoLabelValue = BigDecimal.ZERO;

    // Variables para los datos de reserva
    private BigDecimal precioBase = BigDecimal.ZERO;
    private int numNinos = 0;
    private int numBebes = 0;

 

    

    // Setter para ReservaController
    public void setReservaController(ReservaController reservaController) {
        this.reservaController = reservaController;
    }
   public void setPrecioTotal(BigDecimal precioTotal) {
    // Asegúrate de que precioTotalLabel está correctamente vinculado al componente en tu archivo FXML
    if(precioTotalLabel != null) {
        precioTotalLabel.setText(String.format("€%s", precioTotal.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()));
    } else {
        System.err.println("precioTotalLabel no está inicializado.");
    }
}

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

    // Método de inicialización
    @FXML
    public void initialize() {
        precioHabitacionNocheColumn.setCellValueFactory(new PropertyValueFactory<>("precioTotal"));
        suplementoSeleccionado = new SuplementoSeleccionado(precioSuplemento);

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
                    int numNinos612Int = Integer.parseInt(numNinos612.getText()); // Corrección: Cambio de nombre para evitar conflicto
                    int numNinosMenores6Int = Integer.parseInt(numNinosMenores6.getText()); // Corrección: Cambio de nombre para evitar conflicto
                    calcularPrecioTotal(clickedRow, inicio, fin, numAdultos, numNinos612Int,numNinosMenores6Int);
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

private BigDecimal calcularPrecioTotal(Habitacion habitacionSeleccionada, LocalDate inicio, LocalDate fin, int numAdultos, int numNinosEntre6Y12, int numNinosMenores6Int) {
  
    BigDecimal precioPorNoche = habitacionSeleccionada.getPrecioPorNoche();
    numNoches = ChronoUnit.DAYS.between(inicio, fin);

    // Precio total para adultos
    precioBase = precioPorNoche.multiply(BigDecimal.valueOf(numAdultos));

    // Descuento para los niños entre 6 y 12 años
    BigDecimal precioTotalNinos612 = precioPorNoche.multiply(BigDecimal.valueOf(numNinosEntre6Y12)).multiply(DESCUENTO_NINOS_612);
    
    // Asignación de valores a las variables de instancia para reflejar en la interfaz de usuario
    this.numPersonas = numAdultos;
    this.numNinos = numNinosEntre6Y12;
   this.numBebes=numNinosMenores6Int;
    // Suponiendo que numBebes se calcula en alguna parte del código, de lo contrario, asegúrese de actualizarlo aquí

    // Precio total antes de aplicar los suplementos de temporada
    BigDecimal precioTotal = precioBase.add(precioTotalNinos612).multiply(BigDecimal.valueOf(numNoches));

    // Implementación de la lógica para sumar el suplemento de la temporada
    suplementoTemporada = calcularSuplementoTemporada(inicio, fin);
    precioTotal = precioTotal.add(suplementoTemporada);

  // Ahora llamamos a actualizarInformacionDeReserva para reflejar los valores en la interfaz de usuario
    actualizarInformacionDeReserva();
 // Actualizar solo el valor del total, manteniendo el símbolo de la moneda
    precioTotalLabel.setText(precioTotal.toPlainString());
    return precioTotal;
}



  

 private void actualizarInformacionDeReserva() {
    // Actualizar solo el valor del precio base
    precioBaseLabel.setText(precioBase.toPlainString());
    // Actualizar solo el valor del número de personas
    numPersonasLabel.setText(String.valueOf(numPersonas));
    // Actualizar solo el valor del número de niños
    numNinosLabel.setText(String.valueOf(numNinos));
    // Actualizar solo el valor del número de bebés
    numBebesLabel.setText(String.valueOf(numBebes));
    // Actualizar solo el valor del suplemento, manteniendo el símbolo de la moneda
    suplementoLabel.setText(suplementoTemporada.toPlainString());
    // Actualizar solo el valor del total, manteniendo el símbolo de la moneda
    
}




public void calcularValores() {
    // Intenta parsear las entradas del usuario a números
    try {
        int numAdultos = Integer.parseInt(numeroPersonas.getText());
        int numNinosMenores6Int = Integer.parseInt(numNinosMenores6.getText());
        int numNinos612Int = Integer.parseInt(numNinos612.getText());

       
        numPersonasLabelValue = numAdultos + numNinosMenores6Int + numNinos612Int;
        numNinosLabelValue =  numNinos612Int; // la suma de todos los niños
        numBebesLabelValue = numNinosMenores6Int; //  para calcular Bebés
       
        // Actualiza la interfaz de usuario con los valores calculados
        actualizarInformacionDeReserva();
    } catch (NumberFormatException e) {
        // Manejo de error: mostrar algún mensaje al usuario o loggear el error
        System.err.println("Error al parsear la entrada del usuario.");
    }
}

// Método que se activa al hacer clic en una habitación disponible
@FXML
private void handleTablaHabitacionesDisponiblesMouseClicked(MouseEvent event) {
    Habitacion habitacionSeleccionada = tablaHabitacionesDisponibles.getSelectionModel().getSelectedItem();
    
   if (habitacionSeleccionada != null && event.getButton() == MouseButton.PRIMARY) {
    if (event.getClickCount() == 2) {
        // Si es doble clic, manejar la apertura de la ventana de reserva.
        mostrarFXMLReservas(habitacionSeleccionada);
    } else if (event.getClickCount() == 1) {
        // Si es un solo clic, calcular el precio total.
        LocalDate inicio = fechaInicio.getValue();
        LocalDate fin = fechaFin.getValue();

        if (inicio != null && fin != null) {
            // Asegúrate de que las fechas de inicio y fin no sean nulas.
            try {
                calcularValores(); // Llama a este método para actualizar valores basados en la entrada del usuario.
                int numAdultos = Integer.parseInt(numeroPersonas.getText());
                int numNinosEntre6Y12 = Integer.parseInt(numNinos612.getText());
                int numNinosMenores6Int=Integer.parseInt(numBebesLabel.getText());
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
private BigDecimal calcularSuplementoTemporada(LocalDate inicio, LocalDate fin) {
    BigDecimal suplementoTotal = BigDecimal.ZERO;
    String sql = "SELECT rango_fechas_desde, rango_fechas_hasta, suplemento_temporada "
               + "FROM temporadas "
               + "WHERE rango_fechas_desde <= ? AND rango_fechas_hasta >= ?";

    try (Connection conn = DataSourceManager.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setDate(1, java.sql.Date.valueOf(fin));
        stmt.setDate(2, java.sql.Date.valueOf(inicio));

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            LocalDate fechaDesde = rs.getDate("rango_fechas_desde").toLocalDate();
            LocalDate fechaHasta = rs.getDate("rango_fechas_hasta").toLocalDate();

            // Calcula el rango de fechas que se solapan con las fechas de la reserva y la temporada
            LocalDate solapamientoInicio = inicio.isAfter(fechaDesde) ? inicio : fechaDesde;
            LocalDate solapamientoFin = fin.isBefore(fechaHasta) ? fin : fechaHasta;

            // Cálculo de días de solapamiento
            long diasSolapamiento = ChronoUnit.DAYS.between(solapamientoInicio, solapamientoFin.plusDays(1));

            // Obtén el suplemento por día para esa temporada y calcule el total
            BigDecimal suplementoPorDia = rs.getBigDecimal("suplemento_temporada");
            BigDecimal suplementoParaRango = suplementoPorDia.multiply(BigDecimal.valueOf(diasSolapamiento));
            suplementoTotal = suplementoTotal.add(suplementoParaRango);
        }
        
        rs.close();
    } catch (SQLException e) {
        e.printStackTrace();
        // Manejar adecuadamente la excepción
    }
    System.out.println("Suplemento: "+suplementoTotal);
    return suplementoTotal;
}



    // Método para volver al inicio
    @FXML
    private void volverAlInicio(ActionEvent event) {
        try {
            // Load the Inicio FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Inicio/InicioFXML.fxml"));
            Parent root = loader.load();

            // Get the controller for the Inicio FXML
            InicioController inicioController = loader.getController();

            // Here you can pass the UsuarioInfo to
            // the InicioController
            inicioController.setUsuarioInfo(this.usuarioInfo);

            // Close the current window
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

            // Open the Inicio window
            Stage inicioStage = new Stage();
            inicioStage.setTitle("Inicio");
            inicioStage.setScene(new Scene(root));
            inicioStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions here, maybe show an alert dialog
        }
    }


    // Método para buscar disponibilidad
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

    // Método para buscar habitaciones disponibles
    private ObservableList<Habitacion> buscarHabitacionesDisponibles(LocalDate inicio, LocalDate fin, int numPersonas) {
        ObservableList<Habitacion> habitacionesDisponibles = FXCollections.observableArrayList();

        try (Connection conn = DataSourceManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT id_habitacion, descripcion, capacidad, estado, precio " +
                     "FROM habitaciones " +
                     "WHERE estado = 'Libre' AND capacidad >= ? " +
                     "AND NOT EXISTS (SELECT 1 FROM reservas WHERE habitaciones.id_habitacion = reservas.id_habitacion " +
                     "AND (? BETWEEN FechaEntrada AND FechaSalida OR ? BETWEEN FechaEntrada AND FechaSalida))")) {

            stmt.setInt(1, numPersonas);
            stmt.setDate(2, java.sql.Date.valueOf(inicio));
            stmt.setDate(3, java.sql.Date.valueOf(fin));

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

            // Establecer el precio en la columna precioHabitacionNocheColumn
            precioHabitacionNocheColumn.setCellValueFactory(new PropertyValueFactory<>("precio")); // Asumiendo que el nombre del atributo en la clase Habitacion es "precio"

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return habitacionesDisponibles;
    }

    // Método para mostrar una alerta
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    // Método para abrir la ventana de reserva
   private void mostrarFXMLReservas(Habitacion habitacionSeleccionada) {
    try {
        LocalDate inicio = fechaInicio.getValue();
        LocalDate fin = fechaFin.getValue();
        
        int numAdultos = 0;
        int numNinos612Int = 0;
        int numNinosMenores6 = 0;
        try {
            numAdultos = Integer.parseInt(numeroPersonas.getText().isEmpty() ? "0" : numeroPersonas.getText());
            numNinos612Int = Integer.parseInt(numNinos612.getText().isEmpty() ? "0" : numNinos612.getText());
            numNinosMenores6 = Integer.parseInt(numBebesLabel.getText().isEmpty() ? "0" : numBebesLabel.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de entrada", "Por favor, ingrese números válidos para el número de personas y niños.");
            return;
        }
        
        // Obtener la instancia de UsuarioInfo justo antes de necesitarla.
        UsuarioInfo usuarioInfo = UsuarioInfo.getInstance(); 

       // Calculas el precio total
        BigDecimal precioTotalReserva = calcularPrecioTotal(habitacionSeleccionada, inicio, fin, numAdultos, numNinos612Int, numNinosMenores6);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservas/ReservaFXML.fxml"));
        Parent root = loader.load();

        // Obtienes el controlador para la ventana de reserva
        ReservaController controller = loader.getController();

        // Pasas los datos a los campos o métodos de ReservaController
        controller.setPrecioBase(precioBase); // suponiendo que precioBase es un BigDecimal
        controller.setNumPersonas(numPersonas); // suponiendo que numPersonas es un int
        controller.setNumNinos(numNinos); // suponiendo que numNinos es un int
        controller.setNumBebes(numBebes); // suponiendo que numBebes es un int
        controller.setSuplemento(suplemento); // suponiendo que suplemento es un BigDecimal
        controller.setPrecioTotal(precioTotalReserva);
        controller.iniciarConDatosDeReserva(habitacionSeleccionada, inicio, fin, numAdultos);

        // Aquí parece haber un malentendido. `disponibilidadController` no está definido en este fragmento.
        // Supongo que querrías pasar `usuarioInfo` al controlador de Reserva, que ya hicimos.
        // Si necesitas pasarlo a otro controlador, asegúrate de que esté accesible desde este contexto.
        
        System.out.println("Niños: " + numNinos);
        System.out.println("Bebés : " + numBebes);
        System.out.println("Suplemento : " + suplemento);
        System.out.println("PVP : " + precioTotalReserva);
        
        Stage stage = new Stage();
        stage.setTitle("Detalles de la Reserva");
        stage.setScene(new Scene(root));
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
        mostrarAlerta("Error", "No se pudo cargar la ventana de reserva.");
    }
}
}