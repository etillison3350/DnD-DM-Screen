package dmscreen.statblock.editor.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import dmscreen.Util;
import dmscreen.data.Data;
import dmscreen.data.base.BlockEntry;
import dmscreen.data.creature.feature.template.Template;
import dmscreen.statblock.StatBlock;
import dmscreen.statblock.TemplateEditor;
import dmscreen.statblock.editor.Editor;

public class BlockEntryCollectionEditor<T extends BlockEntry> extends Editor<Collection<T>> {

	public final Class<T> clazz;

	private final LinkedHashMap<T, Node> values = new LinkedHashMap<>();

	private final Hyperlink add;

	private int minRow = 0;

	private TemplateEditor<T> activeEditor;

	public BlockEntryCollectionEditor(final Class<T> clazz, final String name, final String addButtonName, final Collection<? extends T> initialValue) {
		super(name);

		this.clazz = clazz;

		this.add(StatBlock.smallCaps(name, "header-small"), 0, minRow++, 2, 1);
		this.add(new Separator(), 0, minRow++, 2, 1);

		add = new Hyperlink(addButtonName);
		add(add, 0, minRow, 2, 1);
		add.setOnAction(event -> {
			add.setVisited(false);

			final T t = getFromDialog();
			if (t != null) addRow(t);
		});

		if (initialValue != null) initialValue.forEach(this::addRow);
	}

	public BlockEntryCollectionEditor(final Class<T> clazz, final String name, final Collection<? extends T> initialValue) {
		this(clazz, name, String.format("Add %s", clazz.getSimpleName()), initialValue);
	}

	private void addRow(final T obj) {
		GridPane.setRowIndex(add, minRow + 1);
		final ScrollPane node = new ScrollPane(obj.getNode());
		node.setFitToWidth(true);
		node.setHbarPolicy(ScrollBarPolicy.NEVER);
		node.setVbarPolicy(ScrollBarPolicy.NEVER);
		addRow(minRow++, node);
		values.put(obj, node);
	}

	@SuppressWarnings("unchecked")
	private T getFromDialog() {
		final Dialog<T> dialog = new Dialog<>();

		final DialogPane dialogPane = new DialogPane();

		final Map<TreeItem<Object>, TreeItem<Object>> parents = new LinkedHashMap<>();
		final TreeItem<Object> rootItem = new TreeItem<>();

		Data.getData().forEach((name, set) -> {
			final TreeItem<Object> setItem = new TreeItem<>(set);
			parents.put(setItem, rootItem);
			set.templates.forEach(template -> {
				try {
					if (clazz.isAssignableFrom(template.getClass().getMethod("make", Map.class).getReturnType())) {
						final TreeItem<Object> templateItem = new TreeItem<>(template);
						parents.put(templateItem, setItem);
					}
				} catch (final Exception e) {}
			});
		});

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

		final TreeView<Object> treeView = new TreeView<Object>(rootItem);

		final StackPane editorPane = new StackPane();

		treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			final Object value = newValue.getValue();
			if (value instanceof Template) {
				activeEditor = new TemplateEditor<T>((Template<T>) value, true);
				editorPane.getChildren().clear();
				editorPane.getChildren().add(activeEditor);
			}
		});

		final SplitPane root = new SplitPane(new BorderPane(treeView, searchBar, null, null, null), editorPane);

		dialogPane.setContent(root);

		dialogPane.getButtonTypes().add(new ButtonType("Add", ButtonData.OK_DONE));
		dialogPane.getButtonTypes().add(new ButtonType("Cancel", ButtonData.CANCEL_CLOSE));
		dialog.setDialogPane(dialogPane);

		dialog.setResultConverter(button -> {
			if (button.getButtonData() == ButtonData.OK_DONE) {
				return activeEditor.getValue();
			} else {
				return null;
			}
		});

		final Optional<T> ret = dialog.showAndWait();

		if (ret.isPresent()) return ret.get();
		return null;
	}

	@Override
	public List<T> getValue() {
		return new ArrayList<>(values.keySet());
	}

}
