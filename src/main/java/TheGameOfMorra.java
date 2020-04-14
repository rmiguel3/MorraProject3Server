import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
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

	Media backgroundMusic = new Media(getClass().getClassLoader().getResource("Lukes_sick_club_beat.mp3").toString());
	MediaPlayer backgroundSong = new MediaPlayer(backgroundMusic);

	HBox buttonBox;
	Button serverStart = new Button("Click to start the game");

	TextField portTextField = new TextField();

	Text textForPort = new Text("Enter port:");

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


		//server functionality and background music
		serverStart.setOnAction(e->{ primaryStage.setScene(sceneMap.get("Main Server View"));
			primaryStage.setTitle("This is the Server");

			backgroundSong.setVolume(0.25);
			backgroundSong.setCycleCount(MediaPlayer.INDEFINITE);
			backgroundSong.play();

			serverConnection = new MorraServer(data -> {
				Platform.runLater(()->{
					listItems.getItems().add(data.toString());
					int lastMessage = listItems.getItems().size();
					listItems.scrollTo(lastMessage);
				});
			});
		});

		HBox portBox = new HBox(portTextField);
		portBox.resize(100,50);

		// prevent user from being able to enter more than 4 characters for port
		portTextField.setTextFormatter(new TextFormatter<String>(change ->
				change.getControlNewText().length() <= 4 ? change : null));

		// format "Enter port:"
		textForPort.setFont(Font.font ("Verdana", 20));
		textForPort.setStyle("-fx-font-weight: bold");
		textForPort.setFill(Color.INDIGO);

		//adds the items into the startPane
		startPane.getChildren().add(morraImageView);
		startPane.getChildren().add(buttonBox);
		startPane.getChildren().add(textForPort);
		startPane.getChildren().add(portBox);

		textForPort.relocate(191,120);
		portBox.relocate(200, 150);

		buttonBox.resize(300,300);
		buttonBox.relocate(170,300);

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
