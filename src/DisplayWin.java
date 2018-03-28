import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class DisplayWin extends SwitchView {

		public DisplayWin(SwitchView next) throws FileNotFoundException {
			super(next);
		}

		@Override
		void createGUI() throws FileNotFoundException {

			setPrefSize(1024, 768);
			setStyle("-fx-background-color : rgb(0,145,234) ;-fx-font-size:50;-text-fill:black");

			VBox winbox = new VBox();

			FileInputStream win_input = new FileInputStream("src/img/t.png");
			Image win_img = new Image(win_input);
			ImageView win_imgview = new ImageView(win_img);
			Text winner = new Text("YOU WIN!!!!");

			Button EndBtn = new Button("Exit game");
			EndBtn.setMinWidth(250);
			EndBtn.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 25));
			EndBtn.setId("win-btn");
			EndBtn.setOnAction(e -> Platform.exit());

			HBox winhbox = new HBox();
			// EndBtn.setMinWidth(20);
			winhbox.getChildren().add(EndBtn);
			winhbox.setSpacing(100);
			winhbox.setAlignment(Pos.CENTER);
			winbox.getChildren().addAll(win_imgview, winner, winhbox);

			winbox.setAlignment(Pos.CENTER);
			winbox.setSpacing(30);
			setCenter(winbox);
			BorderPane.setAlignment(winbox, Pos.CENTER);

		}

	}