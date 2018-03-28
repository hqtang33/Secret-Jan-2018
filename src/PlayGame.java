import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class PlayGame extends SwitchView {
		public PlayGame(SwitchView next) throws FileNotFoundException {
			super(next);
		}
		
		private void createGameUI(int n) throws FileNotFoundException {
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
		
		public void openCards(Player p1, FlowPane p1HandCards, Player p2, FlowPane p2HandCards, Player p3,
				FlowPane p3HandCards, Player p4, FlowPane p4HandCards, Deck cardDeck, Deck discardPile, HBox pile,
				Button btn) throws FileNotFoundException {
			if(checkWin(p1,p2,p3,p4))
				callNext();

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
		
		public void draw4(String color, Player p1, FlowPane p1HandCards, Player p2, FlowPane p2HandCards, Player p3,
				FlowPane p3HandCards, Player p4, FlowPane p4HandCards, Deck cardDeck, Deck discardPile, HBox pile,
				Button btn) {
			setWildColor(discardPile, color);
			try {
				if (discardPile.atIndex(0).getSymbol().equals(new String("F"))) {
					
					VBox challenge_box = new VBox();
					
					Text challenge = new Text("Do you want to CHALLENGE?!!");
					challenge.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

					Button YesBtn = new Button("YES!");
					YesBtn.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
					YesBtn.setId("win-btn");
					YesBtn.setMinWidth(125);
					
					Button NoBtn = new Button("NO!");
					NoBtn.setMinWidth(125);
					NoBtn.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
					NoBtn.setId("win-btn");
			
					
					HBox YesNoBox = new HBox();
					YesNoBox.getChildren().addAll(YesBtn, NoBtn);
					YesNoBox.setSpacing(30);
					challenge_box.setSpacing(20);
					YesNoBox.setAlignment(Pos.CENTER);
					challenge_box.getChildren().addAll(challenge, YesNoBox);
					
					pile.getChildren().clear();
					pile.getChildren().add(challenge_box);
					challenge_box.setAlignment(Pos.CENTER);
					
					NoBtn.setOnMouseClicked(e->{
						p2.drawCard(cardDeck);
						p2.drawCard(cardDeck);
						p2.drawCard(cardDeck);
						p2.drawCard(cardDeck);

						
						
						try {
							if (p1 != p3)
								closeCards(p1, p1HandCards);
							closeCards(p2, p2HandCards);
							openCards(p3, p3HandCards, p4, p4HandCards, p1, p1HandCards, p2, p2HandCards, cardDeck, discardPile,
									pile, btn);
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					});
					
					YesBtn.setOnMouseClicked(e -> {
						if(p1.getHandCards().playableDeck(discardPile.atIndex(1))) {
							p1.drawCard(cardDeck);
							p1.drawCard(cardDeck);
							p1.drawCard(cardDeck);
							p1.drawCard(cardDeck);
							try {
								closeCards(p1, p1HandCards);
								openCards(p2, p2HandCards, p3, p3HandCards, p4, p4HandCards, p1, p1HandCards, cardDeck, discardPile,
										pile, btn);
							} catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} else {
							p2.drawCard(cardDeck);
							p2.drawCard(cardDeck);
							p2.drawCard(cardDeck);
							p2.drawCard(cardDeck);
							p2.drawCard(cardDeck);
							p2.drawCard(cardDeck);
							
							
							try {
								if (p1 != p3)
									closeCards(p1, p1HandCards);
								closeCards(p2, p2HandCards);
								openCards(p3, p3HandCards, p4, p4HandCards, p1, p1HandCards, p2, p2HandCards, cardDeck, discardPile,
										pile, btn);
							} catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							
						}
					});
					
					
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
		
		
		void createGUI() throws FileNotFoundException {
			VBox chooseVbox=new VBox();
			Text chooseP = new Text("Choose Player");
			chooseVbox.getChildren().add(chooseP);
			HBox choosePlayerNum = new HBox();
			choosePlayerNum.setSpacing(120);
			chooseVbox.setSpacing(80);
			chooseP.setFill(Color.WHITE);
			
			setPrefSize(1024, 768);
			setStyle("-fx-background-color : rgb(194,24,91) ;-fx-font-size:50;-text-fill:black");
			
			
			for(int i =0;i<3;i++) {
				int playernum=i+2;
				FileInputStream choose_player_input = new FileInputStream("src/img/"+playernum+"p.png");
				Image choose_player_img = new Image(choose_player_input);
				ImageView choose_player_imgview = new ImageView(choose_player_img);
				choose_player_imgview.setFitWidth(156);
				choose_player_imgview.setFitHeight(226);
				Button choose_player_btn = new Button(null, choose_player_imgview);
				choose_player_btn.setId("choose-btn");
				choosePlayerNum.getChildren().add(choose_player_btn);
				
				
				choose_player_btn.setOnAction(e -> {
					try {
						createGameUI(playernum);
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
				});
			}
			chooseVbox.getChildren().add(choosePlayerNum);
			setCenter(chooseVbox);
			choosePlayerNum.setAlignment(Pos.CENTER);
			chooseVbox.setAlignment(Pos.CENTER);

		}
	}