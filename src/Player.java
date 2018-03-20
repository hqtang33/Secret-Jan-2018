import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Player {
	private String name;
	private Deck handCards = new Deck();
	
	public Player() {
		this.name = "noname";
	}

	public void drawCard(Deck d) {
		handCards.push(d.pop());
	}

	public void displayDeck() {
		handCards.displayCards();
	}
	
	public void checkPlayable(Deck d) {
		Deck tempDeck = new Deck();
		for(int i=0; i<handCards.length(); i++) {
			if(handCards.atIndex(i).compareTo(d.atIndex(0)) == 1) {
				tempDeck.push(handCards.atIndex(i));
			}
		}
		tempDeck.displayCards();
	}
	
	public List<String> cardList() {
		List<String> cardlist = new ArrayList();
		for(int i=0; i<handCards.length(); i++) {
			cardlist.add(this.handCards.getName(i));
		}
		return cardlist;
		
	}
}
