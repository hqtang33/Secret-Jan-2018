import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Card implements Comparable<Card> {
	private String color;
	private String symbol;
	private ImageView cardImage;

	public Card() {};

	public Card(String color, String symbol) {
		this.color = color;
		this.symbol = symbol;
		loadImage(color, symbol);
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

	public int compareTo(Card c) {
		if ((this.color == c.getColor()) || (this.symbol == c.getSymbol())||this.getColor()=="W")
			return 1;
		else
			return 0;

	}

	public int compareBigger(Card c) {
		String color2[] = { "R", "Y", "G", "B", "W" };
		String num2[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "S", "R", "T", "F", "C" };

		if (Arrays.asList(color2).indexOf(color) > Arrays.asList(color2).indexOf(c.getColor())) {
			return 1;
		} else if (Arrays.asList(color2).indexOf(color) == Arrays.asList(color2).indexOf(c.getColor())) {
			if (Arrays.asList(num2).indexOf(symbol) > Arrays.asList(num2).indexOf(c.getSymbol())) {
				return 1;
			} else {
				return -1;
			}
		} else { return -1; }

	}

	public boolean checkPlayable(Card c) {
		if (color.equals(new String(c.getColor())) || symbol.equals(new String(c.getSymbol()))||this.getColor()=="W"||c.getColor()=="W") 
			return true;
		return false;
	}

//	public int getIndex(List<String> list, String color) {
//
//		for (int i = 0; i < list.size(); i++) {
//			if (list.get(i).equals(color)) {
//				return i;
//			}
//		}
//		return -1;
//
//	}

	public void loadImage(String c, String r) {
		FileInputStream inputstream;
		Image temp;
		try {
			inputstream = new FileInputStream("src/img/" + c+"_"+r + ".png");
			System.out.println(c+"_"+r+".png");
			temp = new Image(inputstream);
			this.cardImage = new ImageView(temp);
			cardImage.setFitWidth(60);
			cardImage.setFitHeight(87);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public ImageView getImage() {
		return cardImage;
	}
}
