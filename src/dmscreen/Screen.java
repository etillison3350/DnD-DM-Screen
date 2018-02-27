package dmscreen;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import dmscreen.data.Data;
import dmscreen.statblock.StatBlock;
import dmscreen.statblock.StatBlockEditor;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;

public class Screen extends Application {

	public static void main(final String[] args) {
		try {
			Font.loadFont(new FileInputStream(Paths.get("Cormorant_Garamond/CormorantGaramond-Medium.ttf").toFile()), 12);
			Font.loadFont(new FileInputStream(Paths.get("Cormorant_Garamond/CormorantGaramond-Bold.ttf").toFile()), 12);
		} catch (final FileNotFoundException e) {}

		Application.launch(args);
	}

	private Data data;
	private StackPane blockPane;
	private TreeView<Object> dataTree;
	private final Map<TreeItem<Object>, TreeItem<Object>> parents = new LinkedHashMap<>();
	private TextField searchBar;
	private SplitPane rootPane;
	private Button editButton;
	private boolean isEditing = false;

	@Override
	public void start(final Stage stage) throws Exception {
		data = new Data(Paths.get("resources"));

		dataTree = createTree();
		searchBar = createSearchBar();

		blockPane = new StackPane();
		blockPane.setPadding(new Insets(8));
		final ScrollPane scroll = new ScrollPane(blockPane);
		scroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		scroll.setFitToWidth(true);

		rootPane = new SplitPane(new BorderPane(dataTree, searchBar, null, null, null), new StackPane(), scroll);
		rootPane.setDividerPositions(0.25, 0.625);

		editButton = new Button("Edit");
		editButton.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		editButton.setOnAction(event -> setEditing(!isEditing));

		final Scene scene = new Scene(rootPane, 768, 960);
		scene.getStylesheets().add("dmscreen/statblock/statBlock.css");

		stage.setScene(scene);
		stage.setTitle("Dungeon Master's Screen");
		stage.show();

		final Stage playerStage = createPlayerStage();
		stage.showingProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) // If the main window is closed, close the player window
				playerStage.hide();
		});
		// playerStage.show();
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
		final TreeItem<Object> treeRoot = constructTreeValues();
		final TreeView<Object> dataTree = new TreeView<>(treeRoot);
		dataTree.setShowRoot(false);
		dataTree.setCellFactory(param -> new TextFieldTreeCell<>(new StringConverter<Object>() {

			@Override
			public String toString(final Object object) {
				final String name = Util.getName(object);

				if (object instanceof Enum<?>) return Util.titleCase(name);

				if (object instanceof AccessibleObject) return Util.titleCaseFromCamelCase(name);

				return name;
			}

			@Override
			public Object fromString(final String string) {
				return new Object();
			}

		}));
		dataTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null && newValue.isLeaf()) {
				setEditing(false);

				blockPane.getChildren().clear();
				final Pane statBlock = StatBlock.getStatBlock(newValue.getValue());
				if (StatBlockEditor.isEditable(newValue.getValue())) {
					final Node topNode = statBlock.getChildrenUnmodifiable().get(0);
					final HBox top = new HBox(8, topNode, editButton);
					HBox.setHgrow(topNode, Priority.ALWAYS);
					statBlock.getChildren().set(0, top);
				}
				blockPane.getChildren().add(statBlock);
			}
		});
		dataTree.setStyle("-fx-focus-color: transparent;");

		return dataTree;
	}

	private TreeItem<Object> constructTreeValues() {
		parents.clear();

		final TreeItem<Object> treeRoot = new TreeItem<>();
		data.getData().forEach((name, dataSet) -> {
			final TreeItem<Object> setRoot = new TreeItem<>(dataSet);
			parents.put(setRoot, treeRoot);

			for (final Field field : dataSet.getClass().getFields()) {
				try {
					if (field.getName().equals("name") || (Boolean) field.getType().getMethod("isEmpty").invoke(field.get(dataSet))) continue;

					final TreeItem<Object> sectionRoot = new TreeItem<>(field);
					parents.put(sectionRoot, setRoot);

					field.getType().getMethod("forEach", Consumer.class).invoke(field.get(dataSet), (Consumer<Object>) o -> {
						parents.put(new TreeItem<>(o), sectionRoot);
					});
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {}
			}
		});
		return treeRoot;
	}

	private Stage createPlayerStage() {
		final Stage playerStage = new Stage();

		// playerStage.show();

		return playerStage;
	}

	public boolean setEditing(final boolean isEditing) {
		if (this.isEditing != isEditing) {
			this.isEditing = isEditing;
			if (this.isEditing) {
				editButton.setText("Done");
				final Object item = dataTree.getSelectionModel().getSelectedItem().getValue();
				if (!StatBlockEditor.isEditable(item)) {
					this.isEditing = false;
					return false;
				}

				rootPane.getItems().add(StatBlockEditor.getEditor(item));
				rootPane.setDividerPosition(2, 1.0);
				final double[] divs = rootPane.getDividerPositions();
				final Transition openPane = new Transition() {
					{
						setCycleDuration(Duration.millis(300));
					}

					@Override
					protected void interpolate(final double pct) {
						rootPane.setDividerPositions((1 - 3 * pct / 11) * divs[0], (1 - 3 * pct / 11) * divs[1], 1 - 3 * pct / 11);
					}
				};
				openPane.play();
			} else {
				editButton.setText("Edit");
				final double[] divs = rootPane.getDividerPositions();
				final Transition closePane = new Transition() {
					{
						setCycleDuration(Duration.millis(300));
						setOnFinished(event -> {
							rootPane.getItems().remove(3);
						});
					}

					@Override
					protected void interpolate(final double pct) {
						rootPane.setDividerPositions(divs[0] * (1 - pct) + pct * divs[0] / divs[2], divs[1] * (1 - pct) + pct * divs[1] / divs[2], divs[2] * (1 - pct) + pct);
					}
				};
				closePane.play();
			}
		}

		return this.isEditing;
	}

}
