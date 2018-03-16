package dmscreen.statblock.editor.collection;

import java.util.Collection;
import java.util.LinkedHashMap;

import javafx.scene.Node;
import javafx.scene.control.Dialog;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import dmscreen.data.base.BlockEntry;
import dmscreen.statblock.StatBlock;
import dmscreen.statblock.editor.Editor;

public class BlockEntryCollectionEditor<T extends BlockEntry> extends Editor<Collection<T>> {

	public final Class<T> clazz;

	private final LinkedHashMap<T, Node> values = new LinkedHashMap<>();

	private final Hyperlink add;

	private int minRow = 0;

	public BlockEntryCollectionEditor(final Class<T> clazz, final String name, final String addButtonName, final Collection<T> initialValue) {
		super(name);

		this.clazz = clazz;

		this.add(StatBlock.smallCaps(name, "header-small"), 0, minRow++, 2, 1);
		this.add(new Separator(), 0, minRow++, 2, 1);

		add = new Hyperlink(addButtonName);
		add(add, 0, minRow, 2, 1);
		add.setOnAction(event -> {
			add.setVisited(false);

			final Dialog<T> dialog = new Dialog<>();
			dialog.showAndWait();

			// TODO
			addRow(null);
		});

		if (initialValue != null) initialValue.forEach(this::addRow);
	}

	public BlockEntryCollectionEditor(final Class<T> clazz, final String name, final Collection<T> initialValue) {
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

	@Override
	public Collection<T> getValue() {
		return values.keySet();
	}

}
