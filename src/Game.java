import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
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
import javafx.scene.layout.TilePane;
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
		
		BorderPane pane1 = new BorderPane();
		pane1.setPrefSize(800, 600);
		

		FlowPane handCards = new FlowPane(Orientation.HORIZONTAL);
		handCards.setPrefWrapLength(700);
		BorderPane.setMargin(handCards, new Insets(5,30,50,30));
	
		
		TilePane pile = new TilePane();
		pile.setPrefColumns(2);

		FileInputStream deck_input = new FileInputStream("C:\\Users\\HQ\\Desktop\\Y2_SEM3\\OOPP\\Assignment\\Secret\\Secret-Jan-2018\\src\\img\\card_back.png");
		Image deck_img = new Image(deck_input);
		ImageView deck_imgview = new ImageView(deck_img);
		deck_imgview.setFitWidth(78);
		deck_imgview.setFitHeight(109);
		pile.getChildren().add(deck_imgview);

		FileInputStream pile_input = new FileInputStream("C:\\Users\\HQ\\Desktop\\Y2_SEM3\\OOPP\\Assignment\\Secret\\Secret-Jan-2018\\src\\img\\r_1.png");
		Image pile_img = new Image(pile_input);
		ImageView pile_imgview = new ImageView(pile_img);
		pile_imgview.setFitWidth(78);
		pile_imgview.setFitHeight(109);
		pile.getChildren().add(pile_imgview);
		pile.setHgap(20);

		for (int i = 0; i < 10; i++) {
			FileInputStream inputstream = new FileInputStream(
					"C:\\Users\\HQ\\Desktop\\Y2_SEM3\\OOPP\\Assignment\\Secret\\Secret-Jan-2018\\src\\img\\b_"+i+".png");
			Image image = new Image(inputstream);
			ImageView imageview = new ImageView(image);
			imageview.setFitWidth(78);
			imageview.setFitHeight(109);

			StackPane card = new StackPane();
			card.setPadding(new Insets(40, 10, 10, 10));
			Button bt = new Button("Green "+i);
			bt.setTranslateY(75);
			card.getChildren().addAll(imageview, bt);
			handCards.getChildren().add(card);
		}
		pile.setAlignment(Pos.CENTER);
		handCards.setAlignment(Pos.CENTER);
		pane1.setBottom(handCards);
		pane1.setCenter(pile);

		Scene scene = new Scene(pane1, 850, 600);
		primaryStage.setResizable(false);
		primaryStage.setTitle("UNO Card GUI");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
