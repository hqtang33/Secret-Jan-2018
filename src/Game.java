public class Game {
	public static void main(String args[]) {
		CardFactory Deck = new CardFactory();
		CardFactory Pile = new CardFactory();
		Player p1 = new Player();
		
		Deck.initializeCards();
		Deck.displayCards();
		Deck.ShuffleCards();
		
		System.out.println();
		p1.drawCard(Deck);
		p1.drawCard(Deck);
		p1.drawCard(Deck);
		p1.drawCard(Deck);
		p1.drawCard(Deck);
		p1.drawCard(Deck);
		p1.displayDeck();
		System.out.println();
		Deck.displayCards();
		
	
	
		
	}
}
