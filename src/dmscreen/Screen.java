package dmscreen;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import javafx.animation.PauseTransition;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import dmscreen.data.Data;
import dmscreen.statblock.StatBlock;
import dmscreen.statblock.StatBlockEditor;

public class Screen extends Application {

	public static void main(final String[] args) {
		try {
			Font.loadFont(new FileInputStream(Paths.get("Cormorant_Garamond/CormorantGaramond-Medium.ttf").toFile()), 12);
			Font.loadFont(new FileInputStream(Paths.get("Cormorant_Garamond/CormorantGaramond-Bold.ttf").toFile()), 12);
		} catch (final FileNotFoundException e) {}

		Application.launch(args);
	}

	private Data data;
	private VBox blockPane;
	private TreeView<Object> dataTree;
	private final Map<TreeItem<Object>, TreeItem<Object>> parents = new LinkedHashMap<>();
	private TextField searchBar;

	@Override
	public void start(final Stage stage) throws Exception {
		data = new Data(Paths.get("resources"));

		dataTree = createTree();
		searchBar = createSearchBar();

		blockPane = new VBox();
		blockPane.setAlignment(Pos.TOP_RIGHT);
		blockPane.setPadding(new Insets(8));
		final ScrollPane scroll = new ScrollPane(blockPane);
		scroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		scroll.setFitToWidth(true);

		final SplitPane root = new SplitPane(new BorderPane(dataTree, searchBar, null, null, null), new StackPane(), scroll);
		root.setDividerPositions(0.25, 0.625);

		final Scene scene = new Scene(root, 768, 960);
		scene.getStylesheets().add("dmscreen/statblock/statBlock.css");

		stage.setScene(scene);
		stage.setTitle("Dungeon Master's Screen");
		stage.show();

		final Transition t = new Transition() {
			{
				setCycleDuration(Duration.seconds(1));
			}

			@Override
			protected void interpolate(final double pct) {
				root.setDividerPositions(.25 - pct * 3 / 44, .625 - pct * 15 / 88, 1 - pct * 3 / 11);
			}
		};

		final PauseTransition wait = new PauseTransition(Duration.seconds(5));
		wait.setOnFinished(event -> {
			root.getItems().add(new StackPane());
			root.setDividerPositions(0.25, 0.625, 1.0);
			t.play();
		});
		wait.play();

		final Stage playerStage = createPlayerStage();
		stage.showingProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) // If the main window is closed, close the player window
				playerStage.hide();
		});
		playerStage.show();
	}

	private TextField createSearchBar() {
		final TextField searchBar = new TextField("-");
		searchBar.setPromptText("Search");
		searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
			final Set<TreeItem<Object>> pars = new HashSet<>(parents.values()), hasChildren = new LinkedHashSet<>();
			pars.forEach(item -> item.getChildren().clear());

			parents.forEach((child, parent) -> {
				if (!pars.contains(child) && (searchBar.getText().isEmpty() || Util.getName(child.getValue()).toLowerCase().contains(searchBar.getText().toLowerCase()))) {
					parent.getChildren().add(child);

					TreeItem<Object> item = parent, par;
					while ((par = parents.get(item)) != null) {
						hasChildren.add(item);
						item = par;
					}
				}
			});

			hasChildren.forEach(parent -> parents.get(parent).getChildren().add(parent));
		});
		searchBar.setText("");

		return searchBar;
	}

	private TreeView<Object> createTree() {
		final TreeItem<Object> treeRoot = constructTree();
		final TreeView<Object> dataTree = new TreeView<>(treeRoot);
		dataTree.setShowRoot(false);
		dataTree.setCellFactory(param -> new TextFieldTreeCell<>(new StringConverter<Object>() {

			@Override
			public String toString(final Object object) {
				final String name = Util.getName(object);
				return object instanceof Enum<?> ? Util.titleCase(name) : name;
			}

			@Override
			public Object fromString(final String string) {
				return new Object();
			}

		}));
		dataTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null && newValue.isLeaf()) {
				blockPane.getChildren().clear();
				if (StatBlockEditor.isEditable(newValue.getValue())) blockPane.getChildren().add(new Button("Edit"));
				blockPane.getChildren().add(StatBlock.getStatBlock(newValue.getValue()));
			}
		});
		dataTree.setStyle("-fx-focus-color: transparent;");

		return dataTree;
	}

	private TreeItem<Object> constructTree() {
		parents.clear();

		final TreeItem<Object> treeRoot = new TreeItem<>();
		data.getData().forEach((name, dataSet) -> {
			final TreeItem<Object> setRoot = new TreeItem<>(Util.sentenceCase(name));
			parents.put(setRoot, treeRoot);

			for (final Field field : dataSet.getClass().getFields()) {
				try {
					if ((Boolean) field.getType().getMethod("isEmpty").invoke(field.get(dataSet))) continue;

					final TreeItem<Object> sectionRoot = new TreeItem<>(Util.sentenceCase(field.getName()));
					parents.put(sectionRoot, setRoot);

					field.getType().getMethod("forEach", Consumer.class).invoke(field.get(dataSet), (Consumer<Object>) o -> {
						parents.put(new TreeItem<>(o), sectionRoot);
					});
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
			}
		});
		return treeRoot;
	}

	private Stage createPlayerStage() {
		final Stage playerStage = new Stage();

		// playerStage.show();

		return playerStage;
	}

}
