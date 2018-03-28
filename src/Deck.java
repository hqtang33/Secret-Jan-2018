import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
	private List<Card> cardDeck = new ArrayList<Card>();;

	public void initializeCards() throws FileNotFoundException {
		String[] color = { "R", "G", "B", "Y" };
		String[] rank = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0" };

		// Initialize normal cards
		for (String c : color) {
			for (String r : rank) {
				Card temp = new Card(c, r);
				cardDeck.add(temp);
			}
		}

		// Initialize skip cards
		for (String c : color) {
			Card d = new Skip(c);
			cardDeck.add(d);
		}

		// Initialize reverse cards
		for (String c : color) {
			Card d = new Reverse(c);
			cardDeck.add(d);
		}

		// Initialize draw 2 cards
		for (String c : color) {
			Card d = new Draw2(c);
			cardDeck.add(d);
		}

		// Initialize wild draw 4 cards
		for (int i = 0; i < 2; i++) {
			Card d = new WildDraw4();
			cardDeck.add(d);
		}

		// Initialize wild color cards
		for (int i = 0; i < 2; i++) {
			Card d = new WildColor();
			cardDeck.add(d);
		}

	}

	public void displayCards() {
		for (Card d : cardDeck) {
			System.out.println(d.getColor() + ", " + d.getSymbol());
		}
	}

	public void convertArrayList(List<Card> list) {
		this.cardDeck = list;
	}

	public Card pop(int n) {
		return cardDeck.remove(n);
	}

	public void push(Card c) {
		cardDeck.add(0, c);
	}

	public void push(Card c, int n) {
		cardDeck.add(n, c);
	}

	public void ShuffleCards() {
		Collections.shuffle(cardDeck);
	}

	public Card atIndex(int n) {
		return cardDeck.get(n);
	}

	public int length() {
		return cardDeck.size();
	}

	public String getName(int n) {
		Card c = cardDeck.get(n);
		return c.getColor() + "_" + c.getSymbol();
	}

	public int findIndexByName(String s) {
		for (int i = 0; i < cardDeck.size(); i++) {
			String temp = new String(cardDeck.get(i).getColor() + "-" + cardDeck.get(i).getSymbol());
			// System.out.println(s + "==" +temp);
			if (temp.equals(s)) {
				System.out.print("ok");
				return i;
			}
		}
		return 999;
	}

	public List<Card> getList() {
		return this.cardDeck;
	}

	public void sort() {
		Collections.sort(cardDeck, (c1, c2) -> c1.compareBigger(c2));
	}

	public boolean isEmpty() {

		if (cardDeck.isEmpty())
			return true;
		else
			return false;

	}

	public boolean playableDeck(Card c) {
	
		for(int i=0;i<cardDeck.size();i++)
	{
		
		if(cardDeck.get(i).checkPlayable(c))
			return true;
				
		
		
	}
		return false;
	}

}
