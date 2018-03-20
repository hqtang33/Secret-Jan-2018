import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
		//Game
		Deck cardDeck = new Deck();
		Deck discardPile = new Deck();
		cardDeck.initializeCards();
		cardDeck.ShuffleCards();
		discardPile.push(cardDeck.pop());
		
		String x = discardPile.getName(0);
		
		Player p1 = new Player();
		p1.drawCard(cardDeck);
		p1.drawCard(cardDeck);
		p1.drawCard(cardDeck);
		p1.drawCard(cardDeck);
		p1.drawCard(cardDeck);
		p1.drawCard(cardDeck);
		p1.drawCard(cardDeck);
		
		List<String> mylist = p1.cardList();
		
		
		//GUI
		BorderPane pane = new BorderPane();
		pane.setPrefSize(800, 600);
		pane.setStyle("-fx-background-color: rgba(6, 136, 148)");
		Scene scene = new Scene(pane, 850, 600);
		scene.getStylesheets().add("style.css");
		
		FlowPane handCards = new FlowPane(Orientation.HORIZONTAL);
		handCards.setPrefWrapLength(700);
		BorderPane.setMargin(handCards, new Insets(5, 30, 50, 30));
		

		VBox pile = new VBox();
		pile.setSpacing(20);

		FileInputStream deck_input = new FileInputStream(
				"C:\\Users\\HQ\\Desktop\\Y2_SEM3\\OOPP\\Assignment\\Secret\\Secret-Jan-2018\\src\\img\\card_back.png");
		Image deck_img = new Image(deck_input);
		ImageView deck_imgview = new ImageView(deck_img);
		deck_imgview.setFitWidth(78);
		deck_imgview.setFitHeight(109);
		Button deck_btn = new Button(null, deck_imgview);
		deck_btn.setId("img-btn");
		pile.getChildren().add(deck_btn);
		

		FileInputStream pile_input = new FileInputStream(
				"C:\\Users\\HQ\\Desktop\\Y2_SEM3\\OOPP\\Assignment\\Secret\\Secret-Jan-2018\\src\\img\\"+x+".png");
		Image pile_img = new Image(pile_input);
		ImageView pile_imgview = new ImageView(pile_img);
		pile_imgview.setFitWidth(78);
		pile_imgview.setFitHeight(109);
		pile.getChildren().add(pile_imgview);
		
		for(String s:mylist) {
			FileInputStream inputstream = new FileInputStream(
					"C:\\Users\\HQ\\Desktop\\Y2_SEM3\\OOPP\\Assignment\\Secret\\Secret-Jan-2018\\src\\img\\" + s
							+ ".png");
			Image image = new Image(inputstream);
			ImageView imageview = new ImageView(image);
			imageview.setFitWidth(78);
			imageview.setFitHeight(109);
			Button imgbtn = new Button(null, imageview);
			imgbtn.setId("img-btn");
			handCards.getChildren().add(imgbtn);
		}

		pile.setAlignment(Pos.CENTER);
		handCards.setAlignment(Pos.CENTER);
		pane.setBottom(handCards);
		pane.setCenter(pile);
		BorderPane.setMargin(pile, new Insets(10,0,20,0));

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

}
