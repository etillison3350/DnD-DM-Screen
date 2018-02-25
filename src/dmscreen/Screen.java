package dmscreen;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.function.Consumer;

import dmscreen.data.Data;
import dmscreen.misc.TestCreatures;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Screen extends Application {

	public static void main(final String[] args) {
		try {
			Font.loadFont(new FileInputStream(Paths.get("Cormorant_Garamond/CormorantGaramond-Medium.ttf").toFile()), 12);
			Font.loadFont(new FileInputStream(Paths.get("Cormorant_Garamond/CormorantGaramond-Bold.ttf").toFile()), 12);
		} catch (final FileNotFoundException e) {}

		Application.launch(args);
	}

	private Data data;

	@Override
	public void start(final Stage stage) throws Exception {
		data = new Data(Paths.get("resources"));

		final TreeItem<Object> treeRoot = new TreeItem<>();
		data.getData().forEach((name, dataSet) -> {
			final TreeItem<Object> setRoot = new TreeItem<>(Util.sentenceCase(name));
			treeRoot.getChildren().add(setRoot);

			for (final Field field : dataSet.getClass().getFields()) {
				try {
					if ((Boolean) field.getType().getMethod("isEmpty").invoke(field.get(dataSet))) continue;

					final TreeItem<Object> sectionRoot = new TreeItem<>(Util.sentenceCase(field.getName()));
					setRoot.getChildren().add(sectionRoot);

					field.getType().getMethod("forEach", Consumer.class).invoke(field.get(dataSet), (Consumer<Object>) o -> {
						sectionRoot.getChildren().add(new TreeItem<>(o));
					});
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
			}
		});
		final TreeView<Object> dataTree = new TreeView<>(treeRoot);
		dataTree.setShowRoot(false);

		final Node statBlock = StatBlockUtils.getStatBlock(TestCreatures.drow);
		final StackPane blockPane = new StackPane(statBlock);
		blockPane.setPadding(new Insets(8));
		final ScrollPane scroll = new ScrollPane(blockPane);
		scroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		scroll.setFitToWidth(true);
		final SplitPane root = new SplitPane(dataTree, new StackPane(), scroll);

		final Scene scene = new Scene(root, 1280, 720);
		scene.getStylesheets().add("dmscreen/statBlock.css");

		stage.setScene(scene);
		stage.setTitle("Dungeon Master's Screen");
		stage.show();

		createPlayerStage();
	}

	private void createPlayerStage() {

	}

}
