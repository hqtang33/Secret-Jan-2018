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
		
		void createGameUI(int n) throws FileNotFoundException {
			setManaged(true);
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
			
			// Player Array
			Player[] players = new Player[n];
			for (int i = 0; i < n; i++) {
				players[i] = new Player("p" + i);
				for (int num = 0; num < 5; num++)
					players[i].drawCard(cardDeck);
				players[i].getHandCards().sort();
			}

			FlowPane[] FlowPaneArray = new FlowPane[4];

			for (int i = 0; i < 4; i++) {
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
				if (i < n)
					closeCards(players[i], FlowPaneArray[i]);
			}
			if (n == 2)
				openCards(players[0], FlowPaneArray[0], players[1], FlowPaneArray[1], players[0], FlowPaneArray[0],
						players[1], FlowPaneArray[1], cardDeck, discardPile, hb_pile, deck_btn);
			else if (n == 3)
				openCards(players[0], FlowPaneArray[0], players[1], FlowPaneArray[1], players[2], FlowPaneArray[2],
						cardDeck, discardPile, hb_pile, deck_btn);
			else
				openCards(players[0], FlowPaneArray[0], players[1], FlowPaneArray[1], players[2], FlowPaneArray[2],
						players[3], FlowPaneArray[3], cardDeck, discardPile, hb_pile, deck_btn);
			hb_pile.setAlignment(Pos.CENTER);
			setCenter(hb_pile);
			BorderPane.setMargin(hb_pile, new Insets(10, 0, 20, 0));

		}

		@Override
		void createGUI() throws FileNotFoundException {
			HBox choosePlayerNum = new HBox();
			choosePlayerNum.setSpacing(20);
			
			for(int i =0;i<3;i++) {
				int playernum=i+2;
				FileInputStream choose_player_input = new FileInputStream("src/img/"+playernum+"p.png");
				Image choose_player_img = new Image(choose_player_input);
				ImageView choose_player_imgview = new ImageView(choose_player_img);
				choose_player_imgview.setFitWidth(120);
				choose_player_imgview.setFitHeight(174);
				Button choose_player_btn = new Button(null, choose_player_imgview);
				choose_player_btn.setId("img-btn");
				choosePlayerNum.getChildren().add(choose_player_btn);
				
				
				
				choose_player_btn.setOnAction(e -> {
					try {
						createGameUI(playernum);
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
				});
			}
			setCenter(choosePlayerNum);
			choosePlayerNum.setAlignment(Pos.CENTER);

		}
	}

	private class MainMenu extends SwitchView {

		public MainMenu(SwitchView next) throws FileNotFoundException {
			super(next);
		}

		@Override
		void createGUI() throws FileNotFoundException {
			setPrefSize(1024, 768);
			setStyle("-fx-background-color : rgb(69,39,160) ;-fx-font-size:50;-text-fill:black");

			VBox menubox = new VBox();

			FileInputStream menu_input = new FileInputStream("src/img/logo.png");
			Image menu_img = new Image(menu_input);
			ImageView menu_imgview = new ImageView(menu_img);

			menubox.getChildren().add(menu_imgview);

			Button TwoPlayerBtn = new Button("2 PLAYER");
			TwoPlayerBtn.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
			TwoPlayerBtn.setId("menu-btn");
			TwoPlayerBtn.setMinWidth(250);

			TwoPlayerBtn.setOnMouseClicked(e -> {
				callNext();
			});

			menubox.getChildren().add(TwoPlayerBtn);

			Button ThreePlayerBtn = new Button("3 PLAYER");
			ThreePlayerBtn.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
			ThreePlayerBtn.setId("menu-btn");
			ThreePlayerBtn.setMinWidth(250);

			menubox.getChildren().add(ThreePlayerBtn);

			Button FourPlayerBtn = new Button("4 PLAYER");
			FourPlayerBtn.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
			FourPlayerBtn.setId("menu-btn");
			FourPlayerBtn.setMinWidth(250);
			menubox.getChildren().add(FourPlayerBtn);

			Button btnEnd = new Button("EXIT GAME");
			btnEnd.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
			btnEnd.setId("menu-btn");
			btnEnd.setMinWidth(250);
			btnEnd.setOnAction(e -> Platform.exit());

			menubox.getChildren().add(btnEnd);
			menubox.setAlignment(Pos.CENTER);
			menubox.setSpacing(30);
			setCenter(menubox);
			BorderPane.setAlignment(menubox, Pos.CENTER);

		}

	}

	private class Win extends SwitchView {

		public Win(SwitchView next) throws FileNotFoundException {
			super(next);
		}

		@Override
		void createGUI() throws FileNotFoundException {

			setPrefSize(1024, 768);
			setStyle("-fx-background-color : rgb(0,145,234) ;-fx-font-size:50;-text-fill:black");

			VBox winbox = new VBox();

			FileInputStream win_input = new FileInputStream("src/img/t.png");
			Image win_img = new Image(win_input);
			ImageView win_imgview = new ImageView(win_img);
			Text winner = new Text("YOU WIN!!!!");

			Button PlayAgainBtn = new Button("Play again");
			PlayAgainBtn.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 25));
			PlayAgainBtn.setId("win-btn");
			PlayAgainBtn.setMinWidth(250);

			PlayAgainBtn.setOnMouseClicked(e -> {
				callNext();
			});

			Button EndBtn = new Button("Exit game");
			EndBtn.setMinWidth(250);
			EndBtn.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 25));
			EndBtn.setId("win-btn");
			EndBtn.setOnAction(e -> Platform.exit());

			HBox winhbox = new HBox();
			// EndBtn.setMinWidth(20);
			winhbox.getChildren().addAll(PlayAgainBtn, EndBtn);
			winhbox.setSpacing(100);
			winhbox.setAlignment(Pos.CENTER);
			winbox.getChildren().addAll(win_imgview, winner, winhbox);

			winbox.setAlignment(Pos.CENTER);
			winbox.setSpacing(30);
			setCenter(winbox);
			BorderPane.setAlignment(winbox, Pos.CENTER);

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
		pile.getChildren().clear();
		Button discardPileCard = new Button(null, discardPile.atIndex(0).getImage());
		discardPileCard.setId("wild-card-wild");
		if (discardPile.atIndex(0).getSymbol().equals(new String("C"))
				|| discardPile.atIndex(0).getSymbol().equals(new String("F"))) {
			if (discardPile.atIndex(0).getColor().equals(new String("R")))
				discardPileCard.setId("wild-card-red");
			else if (discardPile.atIndex(0).getColor().equals(new String("B")))
				discardPileCard.setId("wild-card-blue");
			else if (discardPile.atIndex(0).getColor().equals(new String("G")))
				discardPileCard.setId("wild-card-green");
			else if (discardPile.atIndex(0).getColor().equals(new String("Y")))
				discardPileCard.setId("wild-card-yellow");
			else 
				discardPileCard.setId("wild-card-wild");
		}
		pile.getChildren().addAll(btn, discardPileCard);

		for (int i = 0; i < p1.getHandCards().length(); i++) {
			int cardIndex = i;
			Button imgbtn = new Button(null, p1.getHandCards().atIndex(cardIndex).getImage());
			imgbtn.setId("img-btn");
			if (p1.getHandCards().atIndex(cardIndex).getSymbol().equals(new String("C"))) // Here
				p1.getHandCards().atIndex(cardIndex).setColor("W");
			imgbtn.setOnMouseEntered(e -> {
				imgbtn.setTranslateY(-15);
			});
			imgbtn.setOnMouseExited(e -> {
				imgbtn.setTranslateY(0);
			});
			imgbtn.setOnMouseClicked(e -> {

				if (p1.getHandCards().atIndex(cardIndex).checkPlayable(discardPile.atIndex(0))) {
					p1HandCards.getChildren().remove(imgbtn);
					discardPile.push(p1.getHandCards().pop(cardIndex));

					// Skip Function
					if (discardPile.getList().get(0).getSymbol().equals("S")) {
						try {
							openCards(p3, p3HandCards, p4, p4HandCards, p1, p1HandCards, p2, p2HandCards, cardDeck,
									discardPile, pile, btn);
							if(p1 != p3) 
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
							if (p1 != p3)
								closeCards(p1, p1HandCards);
							closeCards(p2, p2HandCards);
							openCards(p3, p3HandCards, p4, p4HandCards, p1, p1HandCards, p2, p2HandCards, cardDeck,
									discardPile, pile, btn);

						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
						// Wild Color
					} else if (discardPile.getList().get(0).getSymbol().equals("C")
							|| discardPile.getList().get(0).getSymbol().equals("F")) {
						StackPane wildColor = new StackPane();
						Arc red = new Arc(100.0f, 100.0f, 120.0f, 120.0f, 0.0f, 90.0f);
						red.setFill(Color.rgb(244, 67, 54));
						red.setType(ArcType.ROUND);
						red.setTranslateX(60);
						red.setTranslateY(-60);

						red.setOnMouseEntered(e1 -> {
							red.setTranslateX(70);
							red.setTranslateY(-70);
						});
						red.setOnMouseExited(e1 -> {
							red.setTranslateX(60);
							red.setTranslateY(-60);
						});

						red.setOnMouseClicked(e1 -> draw4("R", p1, p1HandCards, p2, p2HandCards, p3, p3HandCards, p4,
								p4HandCards, cardDeck, discardPile, pile, btn));

						Arc green = new Arc(100.0f, 100.0f, 120.0f, 120.0f, 90.0f, 90.0f);
						green.setFill(Color.rgb(105, 240, 174));
						green.setType(ArcType.ROUND);
						green.setTranslateX(-60);
						green.setTranslateY(-60);

						green.setOnMouseEntered(e1 -> {
							green.setTranslateX(-70);
							green.setTranslateY(-70);
						});
						green.setOnMouseExited(e1 -> {
							green.setTranslateX(-60);
							green.setTranslateY(-60);
						});
						green.setOnMouseClicked(e1 -> draw4("G", p1, p1HandCards, p2, p2HandCards, p3, p3HandCards, p4,
								p4HandCards, cardDeck, discardPile, pile, btn));

						Arc blue = new Arc(100.0f, 100.0f, 120.0f, 120.0f, 180.0f, 90.0f);
						blue.setFill(Color.rgb(79, 195, 247));
						blue.setType(ArcType.ROUND);
						blue.setTranslateX(-60);
						blue.setTranslateY(60);

						blue.setOnMouseEntered(e1 -> {
							blue.setTranslateX(-70);
							blue.setTranslateY(70);
						});
						blue.setOnMouseExited(e1 -> {
							blue.setTranslateX(-60);
							blue.setTranslateY(60);
						});
						blue.setOnMouseClicked(e1 -> draw4("B", p1, p1HandCards, p2, p2HandCards, p3, p3HandCards, p4,
								p4HandCards, cardDeck, discardPile, pile, btn));

						Arc yellow = new Arc(100.0f, 100.0f, 120.0f, 120.0f, 270.0f, 90.0f);
						yellow.setFill(Color.rgb(255, 238, 88));
						yellow.setType(ArcType.ROUND);
						yellow.setTranslateX(60);
						yellow.setTranslateY(60);

						yellow.setOnMouseEntered(e1 -> {
							yellow.setTranslateX(70);
							yellow.setTranslateY(70);
						});
						yellow.setOnMouseExited(e1 -> {
							yellow.setTranslateX(60);
							yellow.setTranslateY(60);
						});
						yellow.setOnMouseClicked(e1 -> draw4("Y", p1, p1HandCards, p2, p2HandCards, p3, p3HandCards, p4,
								p4HandCards, cardDeck, discardPile, pile, btn));

						wildColor.getChildren().addAll(blue, green, red, yellow);
						wildColor.setAlignment(Pos.CENTER);

						pile.getChildren().clear();
						pile.getChildren().add(wildColor);

					} else {
						try {
							openCards(p2, p2HandCards, p3, p3HandCards, p4, p4HandCards, p1, p1HandCards, cardDeck,
									discardPile, pile, btn);
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
		pile.getChildren().clear();
		Button discardPileCard = new Button(null, discardPile.atIndex(0).getImage());
		discardPileCard.setId("wild-card-wild");
		if (discardPile.atIndex(0).getSymbol().equals(new String("C"))
				|| discardPile.atIndex(0).getSymbol().equals(new String("F"))) {
			if (discardPile.atIndex(0).getColor().equals(new String("R")))
				discardPileCard.setId("wild-card-red");
			else if (discardPile.atIndex(0).getColor().equals(new String("B")))
				discardPileCard.setId("wild-card-blue");
			else if (discardPile.atIndex(0).getColor().equals(new String("G")))
				discardPileCard.setId("wild-card-green");
			else if (discardPile.atIndex(0).getColor().equals(new String("Y"))) {
				discardPileCard.setId("wild-card-yellow");
				System.out.print("this");
			}
		}
		pile.getChildren().addAll(btn, discardPileCard);

		for (int i = 0; i < p1.getHandCards().length(); i++) {
			int cardIndex = i;
			Button imgbtn = new Button(null, p1.getHandCards().atIndex(cardIndex).getImage());
			imgbtn.setId("img-btn");
			if (p1.getHandCards().atIndex(cardIndex).getSymbol().equals(new String("C"))) // Here
				p1.getHandCards().atIndex(cardIndex).setColor("W");
			imgbtn.setOnMouseEntered(e -> {
				imgbtn.setTranslateY(-15);
			});
			imgbtn.setOnMouseExited(e -> {
				imgbtn.setTranslateY(0);
			});
			imgbtn.setOnMouseClicked(e -> {

				if (p1.getHandCards().atIndex(cardIndex).checkPlayable(discardPile.atIndex(0))) {
					p1HandCards.getChildren().remove(imgbtn);
					discardPile.push(p1.getHandCards().pop(cardIndex));

					// Skip Function
					if (discardPile.getList().get(0).getSymbol().equals("S")) {
						try {
							openCards(p3, p3HandCards, p1, p1HandCards, p2, p2HandCards, cardDeck, discardPile, pile,
									btn);
							closeCards(p1, p1HandCards);
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
						// Reverse function
					} else if (discardPile.getList().get(0).getSymbol().equals("R")) {
						try {
							openCards(p3, p3HandCards, p2, p2HandCards, p1, p1HandCards, cardDeck, discardPile, pile,
									btn);
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
							openCards(p3, p3HandCards, p1, p1HandCards, p2, p2HandCards, cardDeck, discardPile, pile,
									btn);
							closeCards(p1, p1HandCards);

						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
						// Wild Color
					} else if (discardPile.getList().get(0).getSymbol().equals("C")
							|| discardPile.getList().get(0).getSymbol().equals("F")) {
						StackPane wildColor = new StackPane();
						Arc red = new Arc(100.0f, 100.0f, 120.0f, 120.0f, 0.0f, 90.0f);
						red.setFill(Color.rgb(244, 67, 54));
						red.setType(ArcType.ROUND);
						red.setTranslateX(60);
						red.setTranslateY(-60);

						red.setOnMouseEntered(e1 -> {
							red.setTranslateX(70);
							red.setTranslateY(-70);
						});
						red.setOnMouseExited(e1 -> {
							red.setTranslateX(60);
							red.setTranslateY(-60);
						});

						red.setOnMouseClicked(e1 -> draw4("R", p1, p1HandCards, p2, p2HandCards, p3, p3HandCards,
								cardDeck, discardPile, pile, btn));

						Arc green = new Arc(100.0f, 100.0f, 120.0f, 120.0f, 90.0f, 90.0f);
						green.setFill(Color.rgb(105, 240, 174));
						green.setType(ArcType.ROUND);
						green.setTranslateX(-60);
						green.setTranslateY(-60);

						green.setOnMouseEntered(e1 -> {
							green.setTranslateX(-70);
							green.setTranslateY(-70);
						});
						green.setOnMouseExited(e1 -> {
							green.setTranslateX(-60);
							green.setTranslateY(-60);
						});
						green.setOnMouseClicked(e1 -> draw4("G", p1, p1HandCards, p2, p2HandCards, p3, p3HandCards,
								cardDeck, discardPile, pile, btn));

						Arc blue = new Arc(100.0f, 100.0f, 120.0f, 120.0f, 180.0f, 90.0f);
						blue.setFill(Color.rgb(79, 195, 247));
						blue.setType(ArcType.ROUND);
						blue.setTranslateX(-60);
						blue.setTranslateY(60);

						blue.setOnMouseEntered(e1 -> {
							blue.setTranslateX(-70);
							blue.setTranslateY(70);
						});
						blue.setOnMouseExited(e1 -> {
							blue.setTranslateX(-60);
							blue.setTranslateY(60);
						});
						blue.setOnMouseClicked(e1 -> draw4("B", p1, p1HandCards, p2, p2HandCards, p3, p3HandCards,
								cardDeck, discardPile, pile, btn));

						Arc yellow = new Arc(100.0f, 100.0f, 120.0f, 120.0f, 270.0f, 90.0f);
						yellow.setFill(Color.rgb(255, 238, 88));
						yellow.setType(ArcType.ROUND);
						yellow.setTranslateX(60);
						yellow.setTranslateY(60);

						yellow.setOnMouseEntered(e1 -> {
							yellow.setTranslateX(70);
							yellow.setTranslateY(70);
						});
						yellow.setOnMouseExited(e1 -> {
							yellow.setTranslateX(60);
							yellow.setTranslateY(60);
						});
						yellow.setOnMouseClicked(e1 -> draw4("Y", p1, p1HandCards, p2, p2HandCards, p3, p3HandCards,
								cardDeck, discardPile, pile, btn));

						wildColor.getChildren().addAll(blue, green, red, yellow);
						wildColor.setAlignment(Pos.CENTER);

						pile.getChildren().clear();
						pile.getChildren().add(wildColor);

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
		SwitchView game = new PlayGame(null);
		SwitchView main = new MainMenu(game);
		SwitchView win = new Win(main);

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

	public void draw4(String color, Player p1, FlowPane p1HandCards, Player p2, FlowPane p2HandCards, Player p3,
			FlowPane p3HandCards, Player p4, FlowPane p4HandCards, Deck cardDeck, Deck discardPile, HBox pile,
			Button btn) {
		setWildColor(discardPile, color);
		try {
			if (discardPile.atIndex(0).getSymbol().equals(new String("F"))) {
				p2.drawCard(cardDeck);
				p2.drawCard(cardDeck);
				p2.drawCard(cardDeck);
				p2.drawCard(cardDeck);
				if (p1 != p3)
					closeCards(p1, p1HandCards);
				closeCards(p2, p2HandCards);
				openCards(p3, p3HandCards, p4, p4HandCards, p1, p1HandCards, p2, p2HandCards, cardDeck, discardPile,
						pile, btn);
			} else {
				openCards(p2, p2HandCards, p3, p3HandCards, p4, p4HandCards, p1, p1HandCards, cardDeck, discardPile,
						pile, btn);
				closeCards(p1, p1HandCards);

			}
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}

	}

	// Overload
	public void draw4(String color, Player p1, FlowPane p1HandCards, Player p2, FlowPane p2HandCards, Player p3,
			FlowPane p3HandCards, Deck cardDeck, Deck discardPile, HBox pile, Button btn) {
		setWildColor(discardPile, color);
		try {
			if (discardPile.atIndex(0).getSymbol().equals(new String("F"))) {
				p2.drawCard(cardDeck);
				p2.drawCard(cardDeck);
				p2.drawCard(cardDeck);
				p2.drawCard(cardDeck);
				closeCards(p2, p2HandCards);
				if (p1 != p3)
					closeCards(p1, p1HandCards);
				openCards(p3, p3HandCards, p1, p1HandCards, p2, p2HandCards, cardDeck, discardPile, pile, btn);
			} else {
				openCards(p2, p2HandCards, p3, p3HandCards, p1, p1HandCards, cardDeck, discardPile, pile, btn);
				closeCards(p1, p1HandCards);

			}
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}

	}

	public void setWildColor(Deck discardPile, String color) {
		discardPile.getList().get(0).setColor(color);
	}
	
	public boolean checkWin(Player p1, Player p2, Player p3, Player p4) {
		if(p1.getHandCards().isEmpty() || p2.getHandCards().isEmpty() || p3.getHandCards().isEmpty() || p4.getHandCards().isEmpty())
			return true;
		return false;
			
	}

}
