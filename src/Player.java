
public class Player {
	private String name;
	private CardFactory myCards = new CardFactory();

	public void drawCard(CardFactory deck) {
		myCards.push(deck.pop());
	}

	public void displayDeck() {
		myCards.displayCards();
	}
}
