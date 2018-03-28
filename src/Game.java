import java.awt.Dimension;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Game extends Application {
	
	public static void main(String args[]) {

		launch(args);

	}

	@Override
	public void init() throws Exception {
		super.init();
		System.out.println("Inside init() method! Perform necessary initializations here.");
	}

	@Override
	public void start(Stage primaryStage) throws FileNotFoundException {
		// Game
		SwitchView win = new DisplayWin(null);
		SwitchView game = new PlayGame(win);
		SwitchView main = new MainMenu(game);
		

		Scene scene = new Scene(win, 1600, 768);
		scene.getStylesheets().clear();
		scene.getStylesheets().add("style.css");
		primaryStage.setMinWidth(1024);
		primaryStage.setMinHeight(768);
		primaryStage.setMaxWidth(1600);
		primaryStage.setResizable(false);
		primaryStage.setTitle("UNO Card GUI");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		System.out.println("Inside stop() method! Destroy resources. Perform Cleanup.");
	}

	public StackPane displayChooseColor() {
		StackPane wildColor = new StackPane();
		Arc red = new Arc(100.0f, 100.0f, 120.0f, 120.0f, 0.0f, 90.0f);
		red.setFill(Color.rgb(244, 67, 54));
		red.setType(ArcType.ROUND);
		red.setTranslateX(60);
		red.setTranslateY(-60);

		red.setOnMouseEntered(e -> {
			red.setTranslateX(70);
			red.setTranslateY(-70);
		});
		red.setOnMouseExited(e -> {
			red.setTranslateX(60);
			red.setTranslateY(-60);
		});

		Arc green = new Arc(100.0f, 100.0f, 120.0f, 120.0f, 90.0f, 90.0f);
		green.setFill(Color.rgb(105, 240, 174));
		green.setType(ArcType.ROUND);
		green.setTranslateX(-60);
		green.setTranslateY(-60);

		green.setOnMouseEntered(e -> {
			green.setTranslateX(-70);
			green.setTranslateY(-70);
		});
		green.setOnMouseExited(e -> {
			green.setTranslateX(-60);
			green.setTranslateY(-60);
		});
		green.setOnMouseClicked(e -> {

		});

		Arc blue = new Arc(100.0f, 100.0f, 120.0f, 120.0f, 180.0f, 90.0f);
		blue.setFill(Color.rgb(79, 195, 247));
		blue.setType(ArcType.ROUND);
		blue.setTranslateX(-60);
		blue.setTranslateY(60);

		blue.setOnMouseEntered(e -> {
			blue.setTranslateX(-70);
			blue.setTranslateY(70);
		});
		blue.setOnMouseExited(e -> {
			blue.setTranslateX(-60);
			blue.setTranslateY(60);
		});

		Arc yellow = new Arc(100.0f, 100.0f, 120.0f, 120.0f, 270.0f, 90.0f);
		yellow.setFill(Color.rgb(255, 238, 88));
		yellow.setType(ArcType.ROUND);
		yellow.setTranslateX(60);
		yellow.setTranslateY(60);

		yellow.setOnMouseEntered(e -> {
			yellow.setTranslateX(70);
			yellow.setTranslateY(70);
		});
		yellow.setOnMouseExited(e -> {
			yellow.setTranslateX(60);
			yellow.setTranslateY(60);
		});
		wildColor.getChildren().addAll(blue, green, red, yellow);
		wildColor.setAlignment(Pos.CENTER);
		return wildColor;
	}

	

}
