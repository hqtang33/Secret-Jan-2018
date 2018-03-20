public class Player {
	private String name;
	private CardFactory myCards = new CardFactory();

	public void drawCard(CardFactory deck) {
		myCards.push(deck.pop());
	}

	public void displayDeck() {
		myCards.displayCards();
	}
	
	public void checkPlayable(CardFactory deck) {
		CardFactory tempDeck = new CardFactory();
		for(int i=0; i<myCards.length(); i++) {
			if(myCards.atIndex(i).compareTo(deck.atIndex(0)) == 1) {
				tempDeck.push(myCards.atIndex(i));
			}
		}
		tempDeck.displayCards();
	}
}
