import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.HashMap;

public class TheGameOfMorra extends Application {

	HashMap<String, Scene> sceneMap = new HashMap<String, Scene>();
	Scene startScene;
	BorderPane startPane;
	MorraServer serverConnection;

	ListView<String> listItems;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	// push test
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("(Server) Let's Play Morra!!!");
		serverConnection = new MorraServer(data -> {
			Platform.runLater(()->{
				listItems.getItems().add(data.toString());
			});

		});

		startPane = new BorderPane();
		listItems = new ListView<String>();
		startPane.setCenter(listItems);
		startPane.setPadding(new Insets(70));
		startPane.setStyle("-fx-background-color: coral");
		startScene = new Scene(startPane, 500,400);

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
		});

		primaryStage.setScene(startScene);
		primaryStage.show();
	}


}
