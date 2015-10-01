package gifunogi.tui4javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Tui4JavaFX extends Application {

	public static void main(String[] args) {
		// GUIの起動
		Application.launch(args);
		System.exit(0);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Tui4JavaFX");
		Parent root = FXMLLoader.load(getClass().getResource("/Tui4JavaFX.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
