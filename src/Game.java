import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Game extends Application {
	public static void main(String args[]) {
		CardFactory Deck = new CardFactory();
		CardFactory Pile = new CardFactory();
		Player p1 = new Player();

		Deck.initializeCards();
		Deck.ShuffleCards();
		Pile.push(Deck.pop());

		Pile.displayCards();

		p1.drawCard(Deck);
		p1.drawCard(Deck);
		p1.drawCard(Deck);
		p1.drawCard(Deck);
		p1.drawCard(Deck);
		p1.drawCard(Deck);
		System.out.println();
		p1.displayDeck();

		System.out.println();
		p1.checkPlayable(Pile);

		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws FileNotFoundException {
		
		Pane pane = new Pane();
		pane.setPrefSize(800, 600);
		

		FlowPane handCards = new FlowPane(Orientation.HORIZONTAL);
		handCards.setPrefWrapLength(700);
		handCards.setVgap(45);
		handCards.setHgap(10);
		handCards.setPadding(new Insets(5,5,5,5));


		for (int i = 0; i < 9; i++) {
			FileInputStream inputstream = new FileInputStream(
					"C:\\Users\\HQ\\Desktop\\Y2_SEM3\\OOPP\\Assignment\\Secret\\Secret-Jan-2018\\src\\img\\blue_1.png");
			Image image = new Image(inputstream);
			ImageView imageview = new ImageView(image);
			imageview.setFitWidth(91);
			imageview.setFitHeight(126);

			StackPane card = new StackPane();
			//card.setPadding(new Insets(0, 0, 10, 10));
			Button bt = new Button("Blue 1");
			bt.setTranslateY(80);
			card.getChildren().addAll(imageview, bt);
			handCards.getChildren().add(card);
		}
		
		pane.getChildren().add(handCards);

		Scene scene = new Scene(pane, 800, 600);
		primaryStage.setTitle("UNO Card GUI");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
