import java.util.ArrayList;
import java.util.List;

public class Game {
	public static void main(String args[]) {
		CardFactory Deck = new CardFactory();
		List<Card> Pile = new ArrayList<Card>();
		Deck.initializeCards();
						
		Deck.displayCards();
		Deck.ShuffleCards();
		System.out.println();
		Deck.displayCards();
		
	
		
	}
}
