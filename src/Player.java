import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Player {
	private String name;
	private Deck handCards = new Deck();
	private Player next;
	
	public Player(String name) {
		this.name = name;
	}
	
	public void setNext(Player next) {
		this.next = next;
	}

	public void drawCard(Deck d) {
		handCards.push(d.pop(0));
	}

	public void displayDeck() {
		handCards.displayCards();
	}
	
	public List<String> cardList() {
		List<String> cardlist = new ArrayList();
		for(int i=0; i<handCards.length(); i++) {
			cardlist.add(this.handCards.getName(i));
		}
		return cardlist;
		
	}
	
	public Deck getHandCards() {
		return this.handCards;
	}
}
