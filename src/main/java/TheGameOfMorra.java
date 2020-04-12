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
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.HashMap;

public class TheGameOfMorra extends Application {

	//declares all variables needed
	HashMap<String, Scene> sceneMap = new HashMap<>();
	Scene startScene;
	BorderPane startPane;
	HBox buttonBox;
	Media backgroundMusic = new Media(getClass().getClassLoader().getResource("Lukes_sick_club_beat.mp3").toString());
	MediaPlayer backgroundSong = new MediaPlayer(backgroundMusic);
	Button serverStart = new Button("Click to start the game");
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
		Image morraImage = new Image("morraImage.jpg",500,500,false,false);
		ImageView morraImageView = new ImageView(morraImage);
		startPane = new BorderPane();
		listItems = new ListView<String>();
		listItems.setStyle("-fx-font-family: Verdana; -fx-font-weight: bold");
		buttonBox = new HBox(serverStart);


		//server
		serverStart.setOnAction(e->{ primaryStage.setScene(sceneMap.get("Main Server View"));
			primaryStage.setTitle("This is the Server");

			backgroundSong.setVolume(0.25);
			backgroundSong.setCycleCount(MediaPlayer.INDEFINITE);
			backgroundSong.play();

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
		buttonBox.relocate(180,250);
		startScene = new Scene(startPane, 500,500);

		// main view for server
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(50));
		pane.setBackground(new Background(new BackgroundImage(new Image("mainServerBackground.jpeg", 1000, 749, false,true), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,  BackgroundSize.DEFAULT)));

		pane.setCenter(listItems);

		Scene mainServerView = new Scene(pane, 750, 750);

		sceneMap.put("Main Server View",  mainServerView);
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
