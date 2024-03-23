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
import javafx.animation.PauseTransition;
import javafx.application.Platform;
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
import javafx.util.Duration;
import utilidades.DataSourceManager;

public class LoginController {

    @FXML
    private TextField userField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ProgressIndicator loginProgress;

    public void initialize(URL url, ResourceBundle rb) {
        userField.setText("");
        passwordField.setText("");
    }

    @FXML
    private void handleLoginButtonAction() {
       
        
        loginProgress.setVisible(true);

        Task<UsuarioInfo> loginTask = new Task<UsuarioInfo>() {
            @Override
            protected UsuarioInfo call() throws Exception {
                return getUserInfoFromDatabase(userField.getText(), passwordField.getText());
            }
        };

        loginTask.setOnSucceeded(event -> {
            loginProgress.setVisible(true);
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

    private void postLoginSuccess(UsuarioInfo userInfoFromDB) {
        PauseTransition pause = new PauseTransition(Duration.seconds(2)); // Ajusta este valor según necesites
        loginProgress.setVisible(true);
        pause.setOnFinished(event -> Platform.runLater(() -> {
           loginProgress.setVisible(false);
           
            showAlert("Login", "Acceso permitido\nBienvenido " + userInfoFromDB.getNombreEmpleado(), Alert.AlertType.INFORMATION);
            cargarInicio(userInfoFromDB);
        }));
        pause.play();
    }

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
        }
    }

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

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
