import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class MainMenu extends SwitchView {

		public MainMenu(SwitchView next) throws FileNotFoundException {
			super(next);
		}

		@Override
		void createGUI() throws FileNotFoundException {
			setPrefSize(1024, 768);
			setStyle("-fx-background-color : rgb(69,39,160) ;-fx-font-size:50;-text-fill:black");

			VBox menubox = new VBox();

			FileInputStream menu_input = new FileInputStream("src/img/logo.png");
			Image menu_img = new Image(menu_input);
			ImageView menu_imgview = new ImageView(menu_img);

			menubox.getChildren().add(menu_imgview);

			Button TwoPlayerBtn = new Button("PLAY GAME!");
			TwoPlayerBtn.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
			TwoPlayerBtn.setId("menu-btn");
			TwoPlayerBtn.setMinWidth(250);

			TwoPlayerBtn.setOnMouseClicked(e -> {
				callNext();
			});

			menubox.getChildren().add(TwoPlayerBtn);

			
			Button btnEnd = new Button("EXIT GAME");
			btnEnd.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
			btnEnd.setId("menu-btn");
			btnEnd.setMinWidth(250);
			btnEnd.setOnAction(e -> Platform.exit());

			menubox.getChildren().add(btnEnd);
			menubox.setAlignment(Pos.CENTER);
			menubox.setSpacing(30);
			setCenter(menubox);
			BorderPane.setAlignment(menubox, Pos.CENTER);

		}

	}