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
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utilidades.DataSourceManager;

/**
 * Controlador para manejar la lógica de inicio de sesión.
 */
public class LoginController {

    @FXML
    private TextField userField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ProgressIndicator loginProgress;

    /**
     * Inicializa el controlador de inicio de sesión.
     * 
     * @param url La URL utilizada para resolver rutas relativas para el objeto raíz, o null si no se conoce.
     * @param rb El recurso de localización utilizado para localizar el objeto raíz, o null si no se ha localizado.
     */
    public void initialize(URL url, ResourceBundle rb) {
        userField.setText("");
        passwordField.setText("");
    }

    /**
     * Maneja la acción del botón de inicio de sesión.
     */
    @FXML
    public void handleLoginButtonAction() {
        loginProgress.setVisible(true);

        Task<UsuarioInfo> loginTask = new Task<UsuarioInfo>() {
            @Override
            protected UsuarioInfo call() throws Exception {
                return getUserInfoFromDatabase(userField.getText(), passwordField.getText());
            }
        };

        loginTask.setOnSucceeded(event -> {
            loginProgress.setVisible(false);
            UsuarioInfo userInfoFromDB = loginTask.getValue();
            if (userInfoFromDB != null) {
                postLoginSuccess(userInfoFromDB);
            } else {
                showAlert("Login", "Acceso denegado\nUsuario o contraseña incorrectos", Alert.AlertType.ERROR);
            }
        });

        loginTask.setOnFailed(event -> {
            loginProgress.setVisible(false);
            showAlert("Login", "Error en el inicio de sesión", Alert.AlertType.ERROR);
        });

        new Thread(loginTask).start();
    }

    /**
     * Maneja el proceso después de un inicio de sesión exitoso.
     * 
     * @param userInfoFromDB La información del usuario obtenida de la base de datos.
     */
    private void postLoginSuccess(UsuarioInfo userInfoFromDB) {
        loginProgress.setVisible(false);

        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Login Exitoso");
        successAlert.setHeaderText(null);
        successAlert.setContentText("Acceso permitido\nBienvenido " + userInfoFromDB.getNombreEmpleado());
        successAlert.showAndWait();

        cargarInicio(userInfoFromDB);
    }

    /**
     * Carga la ventana de inicio después de un inicio de sesión exitoso.
     * 
     * @param usuarioInfo La información del usuario que ha iniciado sesión.
     */
    private void cargarInicio(UsuarioInfo usuarioInfo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Inicio/InicioFXML.fxml"));
            Parent root = loader.load();
            InicioController inicioController = loader.getController();
            inicioController.setUsuarioInfo(usuarioInfo);

            Stage stage = new Stage();
            stage.setTitle("Inicio");
            stage.setScene(new Scene(root));
            stage.show();

            Stage loginStage = (Stage) userField.getScene().getWindow();
            loginStage.close();
        } catch (IOException e) {
            showAlert("Error", "No se pudo cargar la ventana de inicio.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Obtiene la información del usuario de la base de datos.
     * 
     * @param username El nombre de usuario.
     * @param password La contraseña del usuario.
     * @return La información del usuario si el inicio de sesión es exitoso, null en caso contrario.
     * @throws Exception Si ocurre un error durante la obtención de la información del usuario.
     */
    private UsuarioInfo getUserInfoFromDatabase(String username, String password) throws Exception {
        try (Connection connection = DataSourceManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT u.id_usuario, u.nivel_acceso, e.nombre_empleado, e.apellido_empleado " +
                             "FROM usuarios u JOIN empleados e ON u.id_empleado = e.id_empleado " +
                             "WHERE u.username = ? AND u.password = ?")) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new UsuarioInfo(
                            resultSet.getInt("id_usuario"),
                            resultSet.getString("nombre_empleado"),
                            resultSet.getString("apellido_empleado"),
                            resultSet.getInt("nivel_acceso"));
                }
            }
        } catch (SQLException e) {
            throw new Exception("Error al obtener la información del usuario: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Muestra una alerta con el mensaje especificado.
     * 
     * @param title El título de la alerta.
     * @param content El contenido de la alerta.
     * @param alertType El tipo de alerta.
     */
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
