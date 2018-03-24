import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Card implements Comparable<Card> {
	private String color;
	private String symbol;

	public Card() {
		this.color = "";
		this.symbol = "";// asdsad
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

	public int compareBigger(Card c) {
		String color2[] = { "Y", "B", "G", "R" };
		String num2[] = { "9", "8", "7", "6", "5", "4", "3", "2", "1", "0" };

		if (Arrays.asList(color2).indexOf(color) > Arrays.asList(color2).indexOf(c.getColor())) {
			return 1;
		} else if (Arrays.asList(color2).indexOf(color) == Arrays.asList(color2).indexOf(c.getColor())) {
			if (Arrays.asList(num2).indexOf(symbol) > Arrays.asList(num2).indexOf(c.getSymbol())) {
				return 1;
			} else {
				return -1;
			}
		} else {
			return -1;

		}

	}

	public int getIndex(List<String> list, String color) {

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(color)) {
				return i;
			}
		}
		return -1;

	}

}
