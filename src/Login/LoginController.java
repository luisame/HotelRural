package Login;

import Inicio.InicioController;
import utilidades.UsuarioInfo;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utilidades.DataSourceManager;

public class LoginController {

    @FXML
    private TextField userField;

    @FXML
    private PasswordField passwordField;

    public void initialize(URL url, ResourceBundle rb) {
        userField.setText("");
        passwordField.setText("");
    }
@FXML
private Label nombreUsuarioLabel;

    @FXML
    private void handleLoginButtonAction() throws IOException, Exception {
        String username = userField.getText();
        String password = passwordField.getText();
        
        // Obtener la instancia de UsuarioInfo
        UsuarioInfo usuarioInfo = UsuarioInfo.getInstance();
       
        // Llamada a getUserInfoFromDatabase para obtener la información del usuario
        UsuarioInfo userInfoFromDB = getUserInfoFromDatabase(username, password);

        if (userInfoFromDB != null) {
            // Mostrar mensaje de acceso permitido
            showAlert("Login", "Acceso permitido\nBienvenido " + userInfoFromDB.getNombreEmpleado(), Alert.AlertType.INFORMATION);

            // Cargar la interfaz de inicio
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Inicio/InicioFXML.fxml"));
            Parent root = loader.load();
            InicioController inicioController = loader.getController();
            inicioController.setUsuarioInfo(userInfoFromDB);

            // Mostrar la ventana de inicio y cerrar la ventana de login
            Stage stage = new Stage();
            stage.setTitle("Inicio");
            stage.setScene(new Scene(root));
            stage.show();

            Stage loginStage = (Stage) userField.getScene().getWindow();
            loginStage.close();
        } else {
            // Mostrar mensaje de acceso denegado
            showAlert("Login", "Acceso denegado\nUsuario o contraseña incorrectos", Alert.AlertType.ERROR);
        }
    }

    private UsuarioInfo getUserInfoFromDatabase(String username, String password) throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        UsuarioInfo usuarioInfo = null;

        try {
            connection = DataSourceManager.getConnection();
            String query = "SELECT u.id_usuario, u.nivel_acceso, e.nombre_empleado, e.apellido_empleado " +
                           "FROM usuarios u " +
                           "JOIN empleados e ON u.id_empleado = e.id_empleado " +
                           "WHERE u.username = ? AND u.password = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int idUsuario = resultSet.getInt("id_usuario");
                String nombre = resultSet.getString("nombre_empleado");
                String apellido = resultSet.getString("apellido_empleado");
                int nivelAcceso = resultSet.getInt("nivel_acceso");

                usuarioInfo = new UsuarioInfo(idUsuario, nombre, apellido, nivelAcceso);
            }
        } catch (SQLException e) {
            throw new Exception("Error al obtener la información del usuario: " + e.getMessage());
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return usuarioInfo;
    }
    
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
