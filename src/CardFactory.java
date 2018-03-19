import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CardFactory {
	private List<Card> Deck = new ArrayList<Card>();

	public void initializeCards() {
		String[] color = {"R","G","B","Y"};
		String[] rank = {"1","2","3","4","5","6","7","8","9","0"};
		
		//Initialize normal cards
		for(String c: color) {
			for(String r: rank) {
				Card temp = new Card(c,r);
				Deck.add(temp);
			}
		}
		
		//Initialize skip cards
		for(String c: color) {
			Card d = new Skip(c);
			Deck.add(d);
		}
	}
	
	public void displayCards() {
		for(Card d: Deck) {
			System.out.println(d.getColor()+", "+ d.getSymbol());
		}
		
	}
	
	public Card drawCard() {
		Card temp = Deck.get(0);
		Deck.remove(0);
		return temp;
	}
	
	public void ShuffleCards() {
		Collections.shuffle(Deck);
	}

}
