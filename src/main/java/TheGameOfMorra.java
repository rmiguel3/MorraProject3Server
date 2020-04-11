import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.HashMap;

public class TheGameOfMorra extends Application {

	//declares all variables needed
	HashMap<String, Scene> sceneMap = new HashMap<String, Scene>();
	Scene startScene;
	BorderPane startPane;
	HBox buttonBox;
	Button serverStart = new Button("Click this button to start The Game of Morra");
	MorraServer serverConnection;
	ListView<String> listItems;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("(Server) Let's Play Morra!!!");

		//grabs the start screen image
		Image morraImage = new Image("morraImage.jpg",500,400,false,false);
		ImageView morraImageView = new ImageView(morraImage);
		startPane = new BorderPane();
		listItems = new ListView<String>();
		buttonBox = new HBox(serverStart);

		//server
		serverStart.setOnAction(e->{ primaryStage.setScene(sceneMap.get("server"));
			primaryStage.setTitle("This is the Server");
			serverConnection = new MorraServer(data -> {
				Platform.runLater(()->{
					listItems.getItems().add(data.toString());
				});
			});
		});

		//adds the items into the startPane
		startPane.getChildren().add(morraImageView);
		startPane.getChildren().add(buttonBox);
		buttonBox.resize(300,300);
		buttonBox.relocate(100,300);
		startScene = new Scene(startPane, 500,400);

		sceneMap.put("server",  createServerGui());
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


	//creates the GUI server
	public Scene createServerGui() {

		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(70));
		pane.setStyle("-fx-background-color: coral");

		pane.setCenter(listItems);

		return new Scene(pane, 500, 400);

	}


}
