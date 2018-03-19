import java.util.List;

public class Game {
	public static void main(String args[]) {
		Card card1 = new Card("Green", "Nine");
		Card card2 = new Skip("Green");

		card1.showCardInfo();
		card2.showCardInfo();


	}
}
