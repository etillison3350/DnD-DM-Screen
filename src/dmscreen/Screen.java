package dmscreen;

import dmscreen.data.Creature;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Screen extends Application {

	public static void main(final String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(final Stage stage) throws Exception {
		// final TreeView<Object> segments = new TreeView<>();

		final Creature creature = new Creature();
		creature.name = "Stat Block";

		final WebView statBlock = new WebView();
		statBlock.getEngine().getDocument().setTextContent(creature.getHTML());
		// statBlock.setHtmlText(creature.getHTML());

		final SplitPane root = new SplitPane(new StackPane(), statBlock);

		stage.setScene(new Scene(root, 1280, 720));
		stage.show();

		createPlayerStage();
	}

	private void createPlayerStage() {

	}

}
