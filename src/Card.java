public class Card implements Comparable<Card> {
	private String color;
	private String symbol;

	public Card() {
		this.color = "";
		this.symbol = "";
	}

	public Card(String color, String symbol) {
		this.color = color;
		this.symbol = symbol;
	}

	public String getColor() {
		return this.color;
	}

	public String getSymbol() {
		return this.symbol;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public void showCardInfo() {
		System.out.println("Color: " + color);
		System.out.println("Symbol: " + symbol);
	}

	public int compareTo(Card c) {
		if ((this.color == c.getColor()) || (this.symbol == c.getSymbol()))
			return 1;
		else
			return 0;

	}

}
