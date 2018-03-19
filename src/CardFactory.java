import java.util.ArrayList;
import java.util.List;

public abstract class CardFactory {

	private String Color[] = { "R", "G", "B", "Y" };
	private String Rank[] = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0" };

	private List<Card> Deck = new ArrayList<Card>();

	public List<Card> initializeDeck() {
		for (int i = 0; i < Color.length; i++) {
			for (int j = 0; j < Rank.length; j++) {
				Card c = new Card(Color[i], Rank[j]);
				Deck.add(c);
			}
		}
		return Deck;
	}

	public void displayDeck() {
		for (Card d : Deck) {
			System.out.println(d.getColor() + ", " + d.getSymbol());
		}

	}
	
}
