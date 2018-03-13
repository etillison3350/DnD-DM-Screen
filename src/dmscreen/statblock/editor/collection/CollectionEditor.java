package dmscreen.statblock.editor.collection;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import dmscreen.statblock.StatBlock;
import dmscreen.statblock.editor.Editor;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public abstract class CollectionEditor<T> extends Editor<Collection<T>> {

	private final Map<T, Node> collection;
	private final FlowPane values;
	private final Button add;
	private final HBox bottomPane;

	public CollectionEditor(final String name, final Collection<T> initialValues) {
		super(name);

		setPadding(new Insets(4, 0, 4, 0));

		collection = new HashMap<>();

		values = new FlowPane(4, 4);
		add = new Button("+");
		add.setOnAction(getOnAddAction());

		bottomPane = new HBox(4, new Pane(), add);

		int row = 0;
		if (name != null) {
			this.add(StatBlock.smallCaps(name, "header-small"), 0, row++, 2, 1);
			this.add(new Separator(), 0, row++, 2, 1);
		}
		this.add(values, 0, row++, 2, 1);
		this.add(bottomPane, 0, row++, 2, 1);

		final Text empty = new Text("(empty)");
		empty.setFill(Color.GRAY);
		values.getChildren().add(empty);

		if (initialValues != null) initialValues.forEach(this::add);

		values.getChildrenUnmodifiable().addListener((ListChangeListener<Node>) c -> {
			if (c.next()) {
				if (c.wasRemoved() && c.getList().isEmpty()) {
					final Text placeholder = new Text("(empty)");
					placeholder.setFill(Color.GRAY);
					values.getChildren().add(placeholder);
				}
			}
		});
	}

	private boolean add(final T t) {
		if (t == null) return false;

		final Node v = new Label(convertToString(t)); // TODO add delete buttons
		if (collection.isEmpty()) {
			collection.put(t, v);
			values.getChildren().set(0, v);
		} else {
			values.getChildren().remove(collection.put(t, v));
			values.getChildren().add(v);
		}
		return true;
	}

	protected void setEditor(final Node editor) {
		HBox.setHgrow(editor, Priority.ALWAYS);
		bottomPane.getChildren().set(0, editor);
	}

	protected abstract T getEditorValue();

	protected void clearEditor() {}

	protected final EventHandler<ActionEvent> getOnAddAction() {
		return event -> {
			this.add(getEditorValue());
			this.clearEditor();
		};
	}

	protected String convertToString(final T value) {
		return value.toString();
	}

	@Override
	public Collection<T> getValue() {
		return new HashSet<>(collection.keySet());
	}

}
