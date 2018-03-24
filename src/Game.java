import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Game extends Application {
	private abstract class SwitchStage extends BorderPane {
		private SwitchStage next;

//		public SwitchStage(SwitchStage next) {
//			this.next = next;
//			// createGUI(deck, pile, p);
//		}
		public void setNext(SwitchStage next) {
			this.next = next;
		}

		abstract void createGUI(Deck deck, Deck pile, Player p) throws FileNotFoundException;
				
		protected void callNext() {
			getScene().setRoot(next);
		}
	}

	private class MainMenu extends SwitchStage {
		public MainMenu(SwitchStage next) {
			super();
		}

		@Override
		void createGUI(Deck deck, Deck pile, Player p) {
			// TODO Auto-generated method stub gg

		}
		
	}

	private class PlayGame extends SwitchStage {

		@Override
		void createGUI(Deck deck, Deck pile, Player p) throws FileNotFoundException {
			setPrefSize(1024, 768);
			setStyle("-fx-background-color: rgba(6, 136, 148)");
			FlowPane handCards = new FlowPane(Orientation.HORIZONTAL);
			handCards.setPrefWrapLength(700);
			// setMargin(handCards, new Insets(5, 20, 50, 20));
			HBox hb_pile = new HBox();
			hb_pile.setSpacing(20);

			HBox firstHandCards = new HBox();

			FileInputStream firstHandCards_input = new FileInputStream("src/img/card_back.png");
			Image firstHandCards_img = new Image(firstHandCards_input);
			ImageView firstHandCards_imgview = new ImageView(firstHandCards_img);
			firstHandCards_imgview.setFitWidth(78);
			firstHandCards_imgview.setFitHeight(109);
			Button firstHandCards_btn = new Button(null, firstHandCards_imgview);
			firstHandCards_btn.setId("img-btn");
			firstHandCards.getChildren().add(firstHandCards_btn);

			Text playername = new Text("Player 1");
			playername.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
			playername.setFill(Color.WHITE);
			// playername.setRotate(-90);
			VBox anotherVBox = new VBox();

			anotherVBox.setAlignment(Pos.CENTER);

			FileInputStream deck_input = new FileInputStream("src/img/card_back.png");
			Image deck_img = new Image(deck_input);
			ImageView deck_imgview = new ImageView(deck_img);
			deck_imgview.setFitWidth(78);
			deck_imgview.setFitHeight(109);
			Button deck_btn = new Button(null, deck_imgview);
			deck_btn.setId("img-btn");
			hb_pile.getChildren().add(deck_btn);

			FileInputStream pile_input = new FileInputStream("src/img/" + pile.getName(0) + ".png");
			Image pile_img = new Image(pile_input);
			ImageView pile_imgview = new ImageView(pile_img);
			pile_imgview.setFitWidth(78);
			pile_imgview.setFitHeight(109);
			hb_pile.getChildren().add(pile_imgview);

			for (int i = 0; i < p.getHandCards().length(); i++) {
				String s = p.getHandCards().getName(i);
				FileInputStream inputstream = new FileInputStream("src/img/" + s + ".png");
				Image image = new Image(inputstream);
				ImageView imageview = new ImageView(image);
				imageview.setFitWidth(60);
				imageview.setFitHeight(87);
				Button imgbtn = new Button(null, imageview);

				imgbtn.setId("img-btn");

				imgbtn.getStyleClass().add("p1-" + s.charAt(0) + "-" + s.charAt(2));
				imgbtn.setOnMouseEntered(e -> {
					imgbtn.setTranslateY(-15);
				});
				imgbtn.setOnMouseExited(e -> {
					imgbtn.setTranslateY(0);
				});
				imgbtn.setOnMouseClicked(e -> {

					String temp = imgbtn.getStyleClass().get(1).substring(3, 6);
					Deck tempdeck = p.getHandCards();
					if (tempdeck.checkPlayable(pile, tempdeck.findIndexByName(temp))) {
						handCards.getChildren().remove(imgbtn);
						pile.push(tempdeck.pop(tempdeck.findIndexByName(temp)));
						String tempname = pile.getName(0);

						try {
							addChild(hb_pile, tempname);
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						callNext();
					} else {
						System.out.println("Cannot use this card!");
					}

				});
				handCards.getChildren().add(imgbtn);
			}

			hb_pile.setAlignment(Pos.CENTER);

			anotherVBox.getChildren().add(handCards);
			anotherVBox.getChildren().add(playername);
			anotherVBox.setMargin(playername, new Insets(10, 0, 20, 0));
			handCards.setAlignment(Pos.CENTER);
			firstHandCards.setAlignment(Pos.TOP_LEFT);
			setBottom(anotherVBox);
			setCenter(hb_pile);

			setTop(firstHandCards);
			BorderPane.setMargin(hb_pile, new Insets(10, 0, 20, 0));

		}
	}

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
		Deck cardDeck = new Deck();
		Deck discardPile = new Deck();
		cardDeck.initializeCards();
		cardDeck.ShuffleCards();
		discardPile.push(cardDeck.pop(0));

		String x = discardPile.getName(0);

		Player p1 = new Player();
		p1.drawCard(cardDeck);
		p1.drawCard(cardDeck);
		p1.drawCard(cardDeck);
		p1.drawCard(cardDeck);
		p1.drawCard(cardDeck);
		p1.drawCard(cardDeck);
		p1.drawCard(cardDeck);
		p1.drawCard(cardDeck);
		p1.drawCard(cardDeck);
		p1.drawCard(cardDeck);
		p1.drawCard(cardDeck);
		p1.getHandCards().sort();

		Player p2 = new Player();
		p2.drawCard(cardDeck);
		p2.drawCard(cardDeck);
		p2.drawCard(cardDeck);
		p2.drawCard(cardDeck);
		

		List<Player> PlayerList = new ArrayList<Player>();

		PlayerList.add(p1);
		PlayerList.add(p2);


		SwitchStage first = new PlayGame();
		SwitchStage second = new PlayGame();
		first.setNext(second);
		second.setNext(null);
		first.createGUI(cardDeck, discardPile, p1);
		second.createGUI(cardDeck, discardPile, p2);
		
		


		Scene scene = new Scene(first, 1024, 768);
		scene.getStylesheets().add("style.css");

		primaryStage.setResizable(false);
		primaryStage.setTitle("UNO Card GUI");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void addChild(HBox pile, String x) throws FileNotFoundException {
		FileInputStream pile_input = new FileInputStream(
				"src/img/" + x + ".png");
		Image pile_img = new Image(pile_input);
		ImageView pile_imgview = new ImageView(pile_img);
		pile_imgview.setFitWidth(78);
		pile_imgview.setFitHeight(109);
		pile.getChildren().remove(1);
		pile.getChildren().add(pile_imgview);
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		System.out.println("Inside stop() method! Destroy resources. Perform Cleanup.");
	}

}
