import java.io.FileNotFoundException;
import javafx.scene.layout.BorderPane;

public abstract class SwitchView extends BorderPane {
	private SwitchView next;

	public SwitchView(SwitchView next) throws FileNotFoundException {
		this.next = next;
		createGUI();
	}

	abstract void createGUI() throws FileNotFoundException;

	protected void callNext() {
		getScene().setRoot(next);
	}
}