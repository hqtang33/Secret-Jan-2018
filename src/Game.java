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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Game extends Application {
	private abstract class SwitchView extends BorderPane {
		private SwitchView next;

		public SwitchView(SwitchView next) throws FileNotFoundException {
			this.next = next;
			createGUI();
		}
		public void setNext(SwitchView next) {
			this.next = next;
		}

		abstract void createGUI() throws FileNotFoundException;
				
		protected void callNext() {
			getScene().setRoot(next);
		}
	}

	private class PlayGame extends SwitchView {

		public PlayGame(SwitchView next) throws FileNotFoundException {
			super(next);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		void createGUI() throws FileNotFoundException {
			int playerindex=1;
			setPrefSize(1024, 768);
			setStyle("-fx-background-color: rgba(6, 136, 148)");
			
			// setMargin(p1HandCards, new Insets(5, 20, 50, 20));
			HBox hb_pile = new HBox();
			hb_pile.setSpacing(20);
			
			//Deck Declaration
			Deck cardDeck = new Deck();
			Deck discardPile = new Deck();
			
			cardDeck.initializeCards();
			cardDeck.ShuffleCards();
			
			discardPile.push(cardDeck.pop(0));
			
			//Deck GUI
			FileInputStream deck_input = new FileInputStream("src/img/card_back.png");
			Image deck_img = new Image(deck_input);
			ImageView deck_imgview = new ImageView(deck_img);
			deck_imgview.setFitWidth(78);
			deck_imgview.setFitHeight(109);
			Button deck_btn = new Button(null, deck_imgview);
			deck_btn.setId("img-btn");
			hb_pile.getChildren().add(deck_btn);
			
			

			FileInputStream pile_input = new FileInputStream("src/img/" + discardPile.getName(0) + ".png");
			Image pile_img = new Image(pile_input);
			ImageView pile_imgview = new ImageView(pile_img);
			pile_imgview.setFitWidth(78);
			pile_imgview.setFitHeight(109);
			hb_pile.getChildren().add(pile_imgview);
			
			//Player 1 Object Declaration
			Player p1 = new Player("P1");
			for (int i=0;i<5;i++)
				p1.drawCard(cardDeck);
			p1.getHandCards().sort();
			
			//Player 2 Object Declaration
			Player p2 = new Player("");
			for (int i=0;i<5;i++)
				p2.drawCard(cardDeck);
			p2.getHandCards().sort();
			
			//Player 3 Object Declaration
			Player p3 = new Player("");
			for (int i=0;i<5;i++)
				p3.drawCard(cardDeck);
			p3.getHandCards().sort();
			
			//Player 4 Object Declaration
			Player p4 = new Player("P4");
			for (int i=0;i<5;i++)
				p4.drawCard(cardDeck);
			p4.getHandCards().sort();
			
			
			
			//Player 1 - GUI Declaration
			Text p1name = new Text("Player 1");
			p1name.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
			p1name.setFill(Color.WHITE);
			FlowPane p1HandCards = new FlowPane(Orientation.HORIZONTAL);
			p1HandCards.setPrefWrapLength(900);
			
			//Player 1 - p1HandCards
			VBox p1Vbox = new VBox();
			p1Vbox.setAlignment(Pos.CENTER);
			
			if(playerindex != 1) {
				closeCards(p1, p1HandCards);
			} else {
				openCards(p1, p1HandCards, cardDeck, discardPile, hb_pile);
			}
			
			

			
			p1HandCards.setAlignment(Pos.CENTER);
			p1Vbox.getChildren().add(p1HandCards);
			p1Vbox.getChildren().add(p1name);
			p1Vbox.setMargin(p1name, new Insets(10, 0, 20, 0));
			setBottom(p1Vbox);
			
			
			
			
			
			//Player 2 - GUI Declaration
			Text p2name = new Text("Player 2");
			p2name.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
			p2name.setFill(Color.WHITE);
			FlowPane p2HandCards = new FlowPane(Orientation.HORIZONTAL);
			p2HandCards.setPrefWrapLength(650);
			
			//Player 2 - p2HandCards
			VBox p2Vbox = new VBox();
			p2Vbox.setAlignment(Pos.BOTTOM_CENTER);
			
			if(playerindex != 2) {
				closeCards(p2, p2HandCards);
			} else {
				for (int i = 0; i < p2.getHandCards().length(); i++) {
					String s = p2.getHandCards().getName(i);
					FileInputStream inputstream = new FileInputStream("src/img/" + s + ".png");
					Image image = new Image(inputstream);
					ImageView imageview = new ImageView(image);
					imageview.setFitWidth(60);
					imageview.setFitHeight(87);
					Button imgbtn = new Button(null, imageview);

					imgbtn.setId("img-btn");

					imgbtn.getStyleClass().add("p2-" + s.charAt(0) + "-" + s.charAt(2));
					imgbtn.setOnMouseEntered(e -> {
						imgbtn.setTranslateY(-15);
					});
					imgbtn.setOnMouseExited(e -> {
						imgbtn.setTranslateY(0);
					});
					imgbtn.setOnMouseClicked(e -> {

						String temp = imgbtn.getStyleClass().get(1).substring(3, 6);
						Deck tempdeck = p2.getHandCards();
						if (tempdeck.checkPlayable(discardPile, tempdeck.findIndexByName(temp))) {
							p2HandCards.getChildren().remove(imgbtn);
							discardPile.push(tempdeck.pop(tempdeck.findIndexByName(temp)));
							String tempname = discardPile.getName(0);

							try {
								addChild(hb_pile, tempname);
							} catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} else {
							System.out.println("Cannot use this card!");
						}

					});
					p2HandCards.getChildren().add(imgbtn);
				}
			}

			
			
			
			p2HandCards.setAlignment(Pos.CENTER);
			p2Vbox.getChildren().add(p2HandCards);
			p2Vbox.getChildren().add(p2name);
			p2Vbox.setRotate(90);
			p2Vbox.setMargin(p2name, new Insets(10, 0, 20, 0));
			setLeft(p2Vbox);
			
			
			
			
			//Player 3 - GUI Declaration
			Text p3name = new Text("Player 3");
			p3name.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
			p3name.setFill(Color.WHITE);
			FlowPane p3HandCards = new FlowPane(Orientation.HORIZONTAL);
			p3HandCards.setPrefWrapLength(900);
			
			//Player 3 - p3HandCards
			VBox p3Vbox = new VBox();
			p3Vbox.setAlignment(Pos.BOTTOM_CENTER);
			
			if(playerindex != 3) {
				closeCards(p3, p3HandCards);
			} else {
				for (int i = 0; i < p3.getHandCards().length(); i++) {
					String s = p3.getHandCards().getName(i);
					FileInputStream inputstream = new FileInputStream("src/img/" + s + ".png");
					Image image = new Image(inputstream);
					ImageView imageview = new ImageView(image);
					imageview.setFitWidth(60);
					imageview.setFitHeight(87);
					Button imgbtn = new Button(null, imageview);

					imgbtn.setId("img-btn");

					imgbtn.getStyleClass().add("p3-" + s.charAt(0) + "-" + s.charAt(2));
					imgbtn.setOnMouseEntered(e -> {
						imgbtn.setTranslateY(-15);
					});
					imgbtn.setOnMouseExited(e -> {
						imgbtn.setTranslateY(0);
					});
					imgbtn.setOnMouseClicked(e -> {

						String temp = imgbtn.getStyleClass().get(1).substring(3, 6);
						Deck tempdeck = p3.getHandCards();
						if (tempdeck.checkPlayable(discardPile, tempdeck.findIndexByName(temp))) {
							p3HandCards.getChildren().remove(imgbtn);
							discardPile.push(tempdeck.pop(tempdeck.findIndexByName(temp)));
							String tempname = discardPile.getName(0);

							try {
								addChild(hb_pile, tempname);
							} catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} else {
							System.out.println("Cannot use this card!");
						}

					});
					p3HandCards.getChildren().add(imgbtn);
				}
			}

			
			
			
			p3HandCards.setAlignment(Pos.CENTER);
			p3Vbox.getChildren().add(p3HandCards);
			p3Vbox.getChildren().add(p3name);
			//p3Vbox.setRotate(180);
			p3name.toBack();
			p3Vbox.setMargin(p3name, new Insets(10, 0, 20, 0));
			setTop(p3Vbox);
			
			
			
			
			//Player 4 - GUI Declaration
			Text p4name = new Text("Player 4");
			p4name.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
			p4name.setFill(Color.WHITE);
			FlowPane p4HandCards = new FlowPane(Orientation.HORIZONTAL);
			p4HandCards.setPrefWrapLength(650);
			
			//Player 4 - p4HandCards
			VBox p4Vbox = new VBox();
			p4Vbox.setAlignment(Pos.BOTTOM_CENTER);

			if(playerindex != 3) {
				closeCards(p4, p4HandCards);
			} else {
				for (int i = 0; i < p4.getHandCards().length(); i++) {
					String s = p4.getHandCards().getName(i);
					FileInputStream inputstream = new FileInputStream("src/img/" + s + ".png");
					Image image = new Image(inputstream);
					ImageView imageview = new ImageView(image);
					imageview.setFitWidth(60);
					imageview.setFitHeight(87);
					Button imgbtn = new Button(null, imageview);

					imgbtn.setId("img-btn");

					imgbtn.getStyleClass().add("p4-" + s.charAt(0) + "-" + s.charAt(2));
					imgbtn.setOnMouseEntered(e -> {
						imgbtn.setTranslateY(-15);
					});
					imgbtn.setOnMouseExited(e -> {
						imgbtn.setTranslateY(0);
					});
					imgbtn.setOnMouseClicked(e -> {

						String temp = imgbtn.getStyleClass().get(1).substring(3, 6);
						Deck tempdeck = p4.getHandCards();
						if (tempdeck.checkPlayable(discardPile, tempdeck.findIndexByName(temp))) {
							p4HandCards.getChildren().remove(imgbtn);
							discardPile.push(tempdeck.pop(tempdeck.findIndexByName(temp)));
							String tempname = discardPile.getName(0);

							try {
								addChild(hb_pile, tempname);
							} catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} else {
							System.out.println("Cannot use this card!");
						}

					});
					p4HandCards.getChildren().add(imgbtn);
				}
			}
			
			
			
			p4HandCards.setAlignment(Pos.CENTER);
			p4Vbox.getChildren().add(p4HandCards);
			p4Vbox.getChildren().add(p4name);
			p4Vbox.setRotate(-90);

			p4Vbox.setMargin(p4name, new Insets(10, 0, 20, 0));
			setRight(p4Vbox);
			

			deck_btn.setOnMouseClicked(e->{
				System.out.println("ok");
				p1.getHandCards().push(cardDeck.pop(0));
				p1.getHandCards().sort();
				
				try {
					openCards(p1, p1HandCards, cardDeck, discardPile, hb_pile);
					
					//closeCards(p1, p1HandCards);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//String tempname = p1.getHandCards().getName(0);
				
				//p1HandCards.getChildren().add(arg0)
				
			});
			
			hb_pile.setAlignment(Pos.CENTER);
			setCenter(hb_pile);
			BorderPane.setMargin(hb_pile, new Insets(10, 0, 20, 0));

		}
	}
	
	public void closeCards(Player p, FlowPane pHandCards) throws FileNotFoundException {
		pHandCards.getChildren().clear();
		for (int i = 0; i < p.getHandCards().length(); i++) {
			
			FileInputStream inputstream = new FileInputStream("src/img/card_back.png");
			Image image = new Image(inputstream);
			ImageView imageview = new ImageView(image);
			imageview.setFitWidth(60);
			imageview.setFitHeight(87);
			Button imgbtn = new Button(null, imageview);
			imgbtn.setId("img-btn");
			pHandCards.getChildren().add(imgbtn);
		}
	}
	
	public void openCards(Player p, FlowPane pHandCards, Deck cardDeck, Deck discardPile, HBox pile) throws FileNotFoundException {
		pHandCards.getChildren().clear();
		for (int i = 0; i < p.getHandCards().length(); i++) {
			String s = p.getHandCards().getName(i);
			FileInputStream inputstream = new FileInputStream("src/img/" + s + ".png");
			Image image = new Image(inputstream);
			ImageView imageview = new ImageView(image);
			imageview.setFitWidth(60);
			imageview.setFitHeight(87);
			Button imgbtn = new Button(null, imageview);

			imgbtn.setId("img-btn");

			imgbtn.getStyleClass().add("p4-" + s.charAt(0) + "-" + s.charAt(2));
			imgbtn.setOnMouseEntered(e -> {
				imgbtn.setTranslateY(-15);
			});
			imgbtn.setOnMouseExited(e -> {
				imgbtn.setTranslateY(0);
			});
			imgbtn.setOnMouseClicked(e -> {

				String temp = imgbtn.getStyleClass().get(1).substring(3, 6);
				Deck tempdeck = p.getHandCards();
				if (tempdeck.checkPlayable(discardPile, tempdeck.findIndexByName(temp))) {
					pHandCards.getChildren().remove(imgbtn);
					discardPile.push(tempdeck.pop(tempdeck.findIndexByName(temp)));
					String tempname = discardPile.getName(0);

					try {
						addChild(pile, tempname);
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					System.out.println("Cannot use this card!");
				}

			});
			pHandCards.getChildren().add(imgbtn);
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
		SwitchView test = new PlayGame(null);
		


		Scene scene = new Scene(test, 1024, 768);
		scene.getStylesheets().add("style.css");
		primaryStage.setMinWidth(1024);
		primaryStage.setMinHeight(768);
		//primaryStage.setResizable(false);
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
