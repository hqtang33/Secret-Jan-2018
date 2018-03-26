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
import javafx.scene.shape.Circle;
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

		abstract void createGUI() throws FileNotFoundException;

		protected void callNext() {
			getScene().setRoot(next);
		}
	}

	private class PlayGame extends SwitchView {
		public PlayGame(SwitchView next) throws FileNotFoundException {
			super(next);
		}

		@Override
		void createGUI() throws FileNotFoundException {
			setPrefSize(1024, 768);
			setStyle("-fx-background-color: rgba(6, 136, 148)");

			HBox hb_pile = new HBox();
			hb_pile.setSpacing(20);

			// Deck Declaration
			Deck cardDeck = new Deck();
			Deck discardPile = new Deck();

			cardDeck.initializeCards();
			cardDeck.ShuffleCards();

			discardPile.push(cardDeck.pop(0));
			

			// Deck GUI
			FileInputStream deck_input = new FileInputStream("src/img/card_back.png");
			Image deck_img = new Image(deck_input);
			ImageView deck_imgview = new ImageView(deck_img);
			deck_imgview.setFitWidth(60);
			deck_imgview.setFitHeight(87);
			Button deck_btn = new Button(null, deck_imgview);
			deck_btn.setId("img-btn");
			hb_pile.getChildren().add(deck_btn);

			FileInputStream pile_input = new FileInputStream("src/img/" + discardPile.getName(0) + ".png");
			Image pile_img = new Image(pile_input);
			ImageView pile_imgview = new ImageView(pile_img);
			pile_imgview.setFitWidth(60);
			pile_imgview.setFitHeight(87);
			hb_pile.getChildren().add(pile_imgview);

			// Player Array
			int n = 4;
			Player[] players = new Player[n];
			for (int i = 0; i < n; i++) {
				players[i] = new Player("p" + i);
				for (int num = 0; num < 5; num++)
					players[i].drawCard(cardDeck);
				players[i].getHandCards().sort();
			}

			FlowPane[] FlowPaneArray = new FlowPane[n];

			for (int i = 0; i < n; i++) {
				Text pname = new Text("Player " + (i + 1));
				pname.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
				pname.setFill(Color.WHITE);

				FlowPaneArray[i] = new FlowPane(Orientation.HORIZONTAL);

				VBox pVbox = new VBox();
				pVbox.setAlignment(Pos.CENTER);
				FlowPaneArray[i].setAlignment(Pos.CENTER);
				pVbox.getChildren().add(FlowPaneArray[i]);
				pVbox.getChildren().add(pname);
				pVbox.setMargin(pname, new Insets(10, 0, 20, 0));
				if (i == 0)
					setBottom(pVbox);
				else if (i == 1) {
					pVbox.setRotate(90);
					setLeft(pVbox);

				} else if (i == 2) {
					setTop(pVbox);
					pname.toBack();
				} else {
					pVbox.setRotate(-90);
					setRight(pVbox);
				}
				closeCards(players[i], FlowPaneArray[i]);
			}

			openCards(players[0], FlowPaneArray[0], players[1], FlowPaneArray[1], players[2], FlowPaneArray[2],
					players[3], FlowPaneArray[3], cardDeck, discardPile, hb_pile, deck_btn);

			// StackPane wildColor = new StackPane();
			// Circle red = new Circle(100,100, _height, Color.RED);
			//
			// Circle green = new Circle(100,100, Color.GREEN);
			// Circle blue = new Circle(100,100, Color.BLUE);
			// Rectangle yellow = new Rectangle();
			// green.setTranslateX(100);
			// blue.setTranslateY(100);
			// yellow.setTranslateX(100);
			// yellow.setTranslateY(100);
			// wildColor.getChildren().addAll(red, green, blue, yellow);

			hb_pile.setAlignment(Pos.CENTER);
			setCenter(hb_pile);
			// wildColor.setAlignment(Pos.CENTER);
			// setCenter(wildColor);
			BorderPane.setMargin(hb_pile, new Insets(10, 0, 20, 0));

		}
	}

	private class MainMenu extends SwitchView {

		public MainMenu(SwitchView next) throws FileNotFoundException {
			super(next);
			// TODO Auto-generated constructor stub
		}

		@Override
		void createGUI() throws FileNotFoundException {

			setPrefSize(1024, 768);
			setStyle("-fx-background-color : rgba(183,14,163) ;-fx-font-size:50;-text-fill:black");

			VBox menubox = new VBox();

			FileInputStream menu_input = new FileInputStream("src/img/logo.png");
			Image menu_img = new Image(menu_input);
			ImageView menu_imgview = new ImageView(menu_img);

			menubox.getChildren().add(menu_imgview);

			Button TwoPlayerBtn = new Button("TWO PLAYER");
			TwoPlayerBtn.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 10));
			TwoPlayerBtn.setId("menu-btn");

			TwoPlayerBtn.setOnMouseEntered(e -> {
				TwoPlayerBtn.setTranslateX(20);
			});

			TwoPlayerBtn.setOnMouseExited(e -> {
				TwoPlayerBtn.setTranslateX(0);
			});

			TwoPlayerBtn.setOnMouseClicked(e -> {
				callNext();
			});

			menubox.getChildren().add(TwoPlayerBtn);

			Button ThreePlayerBtn = new Button("THREE PLAYER");
			ThreePlayerBtn.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 10));
			ThreePlayerBtn.setId("menu-btn");

			ThreePlayerBtn.setOnMouseEntered(e -> {
				ThreePlayerBtn.setTranslateX(20);
			});

			ThreePlayerBtn.setOnMouseExited(e -> {
				ThreePlayerBtn.setTranslateX(0);
			});

			menubox.getChildren().add(ThreePlayerBtn);

			Button FourPlayerBtn = new Button("FOUR PLAYER");
			FourPlayerBtn.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 10));
			FourPlayerBtn.setId("menu-btn");

			FourPlayerBtn.setOnMouseEntered(e -> {
				FourPlayerBtn.setTranslateX(20);
			});

			FourPlayerBtn.setOnMouseExited(e -> {
				FourPlayerBtn.setTranslateX(0);
			});
			menubox.getChildren().add(FourPlayerBtn);

			Button btnEnd = new Button("Exit");
			btnEnd.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 10));
			btnEnd.setId("menu-btn");
			btnEnd.setOnAction(e -> Platform.exit());

			btnEnd.setOnMouseEntered(e -> {
				btnEnd.setTranslateX(20);
			});

			btnEnd.setOnMouseExited(e -> {
				btnEnd.setTranslateX(0);
			});
			menubox.getChildren().add(btnEnd);
			menubox.setAlignment(Pos.CENTER);
			menubox.setSpacing(30);
			setCenter(menubox);
			BorderPane.setAlignment(menubox, Pos.CENTER);

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

	// Main open cards function
	public void openCards(Player p1, FlowPane p1HandCards, Player p2, FlowPane p2HandCards, Player p3,
			FlowPane p3HandCards, Player p4, FlowPane p4HandCards, Deck cardDeck, Deck discardPile, HBox pile,
			Button btn) throws FileNotFoundException {
		
		p1HandCards.getChildren().clear();
		for (int i = 0; i < p1.getHandCards().length(); i++) {
			int cardIndex=i;
			Button imgbtn = new Button(null, p1.getHandCards().atIndex(cardIndex).getImage());
			imgbtn.setId("img-btn");
			imgbtn.setOnMouseEntered(e -> {
				imgbtn.setTranslateY(-15);
			});
			imgbtn.setOnMouseExited(e -> {
				imgbtn.setTranslateY(0);
			});
			imgbtn.setOnMouseClicked(e -> {

				if(p1.getHandCards().atIndex(cardIndex).checkPlayable(discardPile.atIndex(0))) {
					p1HandCards.getChildren().remove(imgbtn);
					discardPile.push(p1.getHandCards().pop(cardIndex));
					
					// Skip Function
					if (discardPile.getList().get(0).getSymbol().equals("S")) {
						try {
							openCards(p3, p3HandCards, p4, p4HandCards, p1, p1HandCards, p2, p2HandCards, cardDeck,
									discardPile, pile, btn);
							closeCards(p1, p1HandCards);
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
						// Reverse function
					} else if (discardPile.getList().get(0).getSymbol().equals("R")) {
						try {
							openCards(p4, p4HandCards, p3, p3HandCards, p2, p2HandCards, p1, p1HandCards, cardDeck,
									discardPile, pile, btn);
							closeCards(p1, p1HandCards);
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
						// Draw 2
					} else if (discardPile.getList().get(0).getSymbol().equals("T")) {
						try {
							p2.drawCard(cardDeck);
							p2.drawCard(cardDeck);
							closeCards(p2, p2HandCards);
							openCards(p3, p3HandCards, p4, p4HandCards, p1, p1HandCards, p2, p2HandCards, cardDeck,
									discardPile, pile, btn);
							closeCards(p1, p1HandCards);
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}

					} else {
						try {
							openCards(p2, p2HandCards, p3, p3HandCards, p4, p4HandCards, p1, p1HandCards, cardDeck,
									discardPile, pile, btn);
							closeCards(p1, p1HandCards);
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
					}
					pile.getChildren().remove(1);
					pile.getChildren().add(discardPile.atIndex(0).getImage());

				} else {
					System.out.println("Cannot use this card!");
				}
			});
			p1HandCards.getChildren().add(imgbtn);
		}

		btn.setOnMouseClicked(e -> {
			System.out.println(cardDeck.length());
			discardPile.displayCards();
			if (discardPile.length() > 20) {
				for (int i = 20; i > 0; i--) {
					cardDeck.push(discardPile.pop(discardPile.length() - 1));
				}
				cardDeck.ShuffleCards();
			}
			p1.getHandCards().push(cardDeck.pop(0));
			p1.getHandCards().sort();
			try {
				openCards(p1, p1HandCards, p2, p2HandCards, p3, p3HandCards, p4, p4HandCards, cardDeck, discardPile,
						pile, btn);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		});
	}

	// Overloaded for 3 Players
	public void openCards(Player p1, FlowPane p1HandCards, Player p2, FlowPane p2HandCards, Player p3,
			FlowPane p3HandCards, Deck cardDeck, Deck discardPile, HBox pile, Button btn) throws FileNotFoundException {

		p1HandCards.getChildren().clear();

		for (int i = 0; i < p1.getHandCards().length(); i++) {
			String s = p1.getHandCards().getName(i);
			FileInputStream inputstream = new FileInputStream("src/img/" + s + ".png");
			Image image = new Image(inputstream);
			ImageView imageview = new ImageView(image);
			imageview.setFitWidth(60);
			imageview.setFitHeight(87);
			Button imgbtn = new Button(null, imageview);
			imgbtn.setId("img-btn");

			imgbtn.getStyleClass().add(s.charAt(0) + "-" + s.charAt(2));
			imgbtn.setOnMouseEntered(e -> {
				imgbtn.setTranslateY(-15);
			});
			imgbtn.setOnMouseExited(e -> {
				imgbtn.setTranslateY(0);
			});
			imgbtn.setOnMouseClicked(e -> {

				String temp = imgbtn.getStyleClass().get(1).substring(0, 3);
				Deck tempdeck = p1.getHandCards();
				int tempindex = tempdeck.findIndexByName(temp);
				if (tempdeck.atIndex(tempindex).checkPlayable(discardPile.atIndex(0))) {
					p1HandCards.getChildren().remove(imgbtn);
					discardPile.push(tempdeck.pop(tempdeck.findIndexByName(temp)));

					String tempname = discardPile.getName(0);

					try {
						addChild(pile, tempname);
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}

					// Skip Function
					if (discardPile.getList().get(0).getSymbol().equals("S")) {
						try {
							openCards(p3, p3HandCards, p1, p1HandCards, p2, p2HandCards, cardDeck, discardPile, pile,
									btn);
							closeCards(p1, p1HandCards);
						} catch (FileNotFoundException e1) {
							System.out.print("0");
							e1.printStackTrace();
						}
					} else {
						try {
							openCards(p2, p2HandCards, p3, p3HandCards, p1, p1HandCards, cardDeck, discardPile, pile,
									btn);
							closeCards(p1, p1HandCards);
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
					}
				} else {
					System.out.println("Cannot use this card!");
				}

			});
			p1HandCards.getChildren().add(imgbtn);
		}

		btn.setOnMouseClicked(e -> {
			System.out.println("ok");
			if (discardPile.length() > 5) {
				for (int i = 5; i > 0; i--) {
					cardDeck.push(discardPile.pop(discardPile.length() - 1));
				}
				discardPile.ShuffleCards();
			}
			p1.getHandCards().push(cardDeck.pop(0));
			p1.getHandCards().sort();

			try {
				openCards(p1, p1HandCards, p2, p2HandCards, p3, p3HandCards, cardDeck, discardPile, pile, btn);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		});

	}

	// Overloaded 2 Player
	public void openCards(Player p1, FlowPane p1HandCards, Player p2, FlowPane p2HandCards, Deck cardDeck,
			Deck discardPile, HBox pile, Button btn) throws FileNotFoundException {

		p1HandCards.getChildren().clear();

		for (int i = 0; i < p1.getHandCards().length(); i++) {
			String s = p1.getHandCards().getName(i);
			FileInputStream inputstream = new FileInputStream("src/img/" + s + ".png");
			Image image = new Image(inputstream);
			ImageView imageview = new ImageView(image);
			imageview.setFitWidth(60);
			imageview.setFitHeight(87);
			Button imgbtn = new Button(null, imageview);

			imgbtn.setId("img-btn");

			imgbtn.getStyleClass().add(s.charAt(0) + "-" + s.charAt(2));
			imgbtn.setOnMouseEntered(e -> {
				imgbtn.setTranslateY(-15);
			});
			imgbtn.setOnMouseExited(e -> {
				imgbtn.setTranslateY(0);
			});
			imgbtn.setOnMouseClicked(e -> {
				String temp = imgbtn.getStyleClass().get(1).substring(0, 3);
				Deck tempdeck = p1.getHandCards();
				int tempindex = tempdeck.findIndexByName(temp);
				if (tempdeck.atIndex(tempindex).checkPlayable(discardPile.atIndex(0))) {
					p1HandCards.getChildren().remove(imgbtn);
					discardPile.push(tempdeck.pop(tempdeck.findIndexByName(temp)));
					String tempname = discardPile.getName(0);
					try {
						addChild(pile, tempname);
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					// Skip Function
					if (discardPile.getList().get(0).getSymbol().equals("S")) {
						System.out.println("here");
						try {

							openCards(p1, p1HandCards, p2, p2HandCards, cardDeck, discardPile, pile, btn);
							System.out.println("here2");

							// closeCards(p1, p1HandCards);
							System.out.println("here3");
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							System.out.print("0");
							e1.printStackTrace();
						}
					} else {
						try {
							openCards(p2, p2HandCards, p1, p1HandCards, cardDeck, discardPile, pile, btn);
							closeCards(p1, p1HandCards);
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					}
				} else {
					System.out.println("Cannot use this card!");
				}

			});
			p1HandCards.getChildren().add(imgbtn);
		}

		btn.setOnMouseClicked(e -> {
			System.out.println("ok");
			if (discardPile.length() > 5) {
				for (int i = 5; i > 0; i--) {
					cardDeck.push(discardPile.pop(discardPile.length() - 1));
				}
				discardPile.ShuffleCards();
			}
			p1.getHandCards().push(cardDeck.pop(0));
			p1.getHandCards().sort();

			try {
				openCards(p1, p1HandCards, p2, p2HandCards, cardDeck, discardPile, pile, btn);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		});
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
		SwitchView main = new MainMenu(test);

		Scene scene = new Scene(main, 1600, 768);
		scene.getStylesheets().add("style.css");
		primaryStage.setMinWidth(1024);
		primaryStage.setMinHeight(768);
		primaryStage.setMaxWidth(1600);
		primaryStage.setResizable(false);
		primaryStage.setTitle("UNO Card GUI");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void addChild(HBox pile, String x) throws FileNotFoundException {
		FileInputStream pile_input = new FileInputStream("src/img/" + x + ".png");
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
