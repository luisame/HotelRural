

package BackupHotelRural;



import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BackupDataBaseMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BackupFXML.fxml"));
        primaryStage.setTitle("Backup Hotel Rural");
        primaryStage.setScene(new Scene(loader.load(), 300, 200));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
