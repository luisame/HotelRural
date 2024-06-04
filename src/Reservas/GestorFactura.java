
package Reservas;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

/**
 *
 * @author luisa
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javafx.scene.control.Alert;
import utilidades.DataSourceManager;

public class GestorFactura {

    /**
     * Genera una factura en formato PDF para una reserva específica y la envía por correo electrónico al cliente.
     * @param idReserva El ID de la reserva para la cual generar la factura.
     */
    public void generarYEnviarFactura(int idReserva) throws java.io.IOException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DataSourceManager.getConnection();
            String query = "SELECT c.nombre_cliente, c.email, r.precio_total, r.fecha_entrada, r.fecha_salida, h.descripcion "
                         + "FROM reservas r "
                         + "JOIN clientes c ON r.id_cliente = c.id_cliente "
                         + "JOIN habitaciones h ON r.id_habitacion = h.id_habitacion "
                         + "WHERE r.id_reserva = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, idReserva);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String nombreCliente = rs.getString("nombre_cliente");
                String emailCliente = rs.getString("email");
                double precioTotal = rs.getDouble("precio_total");
                java.sql.Date fechaEntrada = rs.getDate("fecha_entrada");
                java.sql.Date fechaSalida = rs.getDate("fecha_salida");
                String descripcionHabitacion = rs.getString("descripcion");

                // Aquí se podría integrar con una herramienta de generación de PDF como JasperReports
                String archivoPDF = generarPDF( nombreCliente,  descripcionHabitacion,  precioTotal,  fechaEntrada, fechaSalida) ;
                
                

            } else {
                mostrarAlerta("Error", "No se encontró la reserva con el ID proporcionado.");
            }
        } catch (SQLException e) {
            mostrarAlerta("Error de Base de Datos", "Error al obtener datos para la factura: " + e.getMessage());
        } finally {
            cerrarRecursos(rs, pstmt, conn);
        }
    }
    
private void cerrarRecursos(ResultSet rs, PreparedStatement pstmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
}
// Método para generar un archivo PDF con la información de la factura
    public String generarPDF(String nombreCliente, String descripcionHabitacion, double precioTotal, java.sql.Date fechaEntrada, java.sql.Date fechaSalida) throws java.io.IOException {
        String rutaArchivoPDF = "ruta/al/archivo/factura_" + nombreCliente + ".pdf"; // Asegúrate de que esta ruta es accesible y escrita correctamente
        
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // Crea un flujo de contenido para agregar información al PDF
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN), 12);
                contentStream.newLineAtOffset(100, 700);
                
                // Formatea las fechas para mostrarlas en el PDF
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String fechaEntradaStr = dateFormat.format(fechaEntrada);
                String fechaSalidaStr = dateFormat.format(fechaSalida);

                // Agrega el texto que quieres en el PDF
                contentStream.showText("Factura para: " + nombreCliente);
                contentStream.newLineAtOffset(0, -15); // Mueve a la siguiente línea
                contentStream.showText("Habitación: " + descripcionHabitacion);
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Precio Total: " + precioTotal);
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Fecha de Entrada: " + fechaEntradaStr);
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Fecha de Salida: " + fechaSalidaStr);
                contentStream.endText();
            }
            
            // Guarda el documento PDF en la ruta especificada
            document.save(rutaArchivoPDF);
     
        // Retorna la ruta al archivo PDF si se ha generado correctamente
        return rutaArchivoPDF;
    }
    }
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
   

   
}
