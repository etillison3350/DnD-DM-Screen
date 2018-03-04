package dmscreen.statblock;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public abstract class CollectionEditor<T> extends Editor<Collection<T>> {

	private final Map<T, Node> collection;
	private final FlowPane values;
	private final Button add;
	private final HBox bottomPane;

	public CollectionEditor(final String name, final Collection<T> initialValues) {
		super(name);

		collection = new HashMap<>();

		values = new FlowPane(4, 4);
		add = new Button("+");
		add.setOnAction(event -> {
			this.add(getEditorValue());
		});

		bottomPane = new HBox(4, new Pane(), add);

		int row = 0;
		if (name != null) {
			this.add(StatBlock.smallCaps(name, "header-small"), 0, row++, 2, 1);
			this.add(new Separator(), 0, row++, 2, 1);
		}
		this.add(values, 0, row++, 2, 1);
		this.add(bottomPane, 0, row++, 2, 1);

		if (initialValues != null) initialValues.forEach(this::add);
	}

	private boolean add(final T t) {
		if (t == null) return false;

		final Node v = new Label(convertToString(t)); // TODO add delete buttons
		values.getChildren().remove(collection.put(t, v));
		values.getChildren().add(v);
		return true;
	}

	protected void setEditor(final Node editor) {
		HBox.setHgrow(editor, Priority.ALWAYS);
		bottomPane.getChildren().set(0, editor);
	}

	protected abstract T getEditorValue();

	protected String convertToString(final T value) {
		return value.toString();
	}

	@Override
	public Collection<T> getValue() {
		return new HashSet<>(collection.keySet());
	}

// private final List<ListItem<T>> items = new ArrayList<>();
// private final Hyperlink add;
// private final FlowPane pane;
//
// public CollectionEditor(final String name) {
// super(name);
//
// setPadding(new Insets(8, 0, 8, 0));
//
// this.add(StatBlock.smallCaps(name, "header-small"), 0, 0, 2, 1);
// this.add(new Separator(), 0, 1, 2, 1);
//
// pane = new FlowPane(4, 4);
// add(pane, 0, 2, 2, 1);
//
// add = new Hyperlink("Add");
// add(add, 0, 3, 2, 1);
// add.setOnAction(event -> {
// add.setVisited(false);
// addListItem(makeItem());
// });
// }
//
// protected abstract ListItem<T> makeItem();
//
// @Override
// public Collection<T> getValue() {
// return items.stream().map(r -> r.valueGetter.get()).collect(Collectors.toList());
// }
//
// protected void addListItem(final ListItem<T> item) {
// items.add(item);
// pane.getChildren().add(item.value);
// // addRow(minRow++, row.value);
// }
//
// public static class ListItem<T> {
// public final Node value;
// public final Supplier<T> valueGetter;
//
// public ListItem(final Node value, final Supplier<T> valueGetter) {
// this.value = value;
// this.valueGetter = valueGetter;
// }
//
// }

}
