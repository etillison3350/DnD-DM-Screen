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
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public abstract class CollectionEditor<T> extends Editor<Collection<T>> {

	private final Map<T, Node> collection;
	private final FlowPane values;
	private final Button add;
	private final HBox bottomPane;
	private final EventHandler<ActionEvent> onAddAction = event -> {
		this.add(getEditorValue());
		this.clearEditor();
	};

	public CollectionEditor(final String name, final Collection<T> initialValues) {
		super(name);

		setPadding(new Insets(4, 0, 4, 0));

		collection = new HashMap<>();

		values = new FlowPane(4, 4);
		add = new Button("+");
		add.setOnAction(getOnAddAction());
		add.setDisable(true);

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

		final HBox v = new HBox(5);

		v.setBackground(new Background(new BackgroundFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.REFLECT, new Stop(0, Color.gray(0.95)), new Stop(1, Color.gray(0.8))), new CornerRadii(3), Insets.EMPTY)));
		v.setBorder(new Border(new BorderStroke(Color.gray(0.7), BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(1))));
		v.setPadding(new Insets(3, 6, 3, 4));

		final Label label = new Label(convertToString(t));
		label.setMaxWidth(96);
		label.setTooltip(new Tooltip(label.getText()));
		v.getChildren().add(label);

		final StackPane x = new StackPane();
		final Line line1 = new Line(0, 0, 6, 6);
		line1.setStroke(Color.gray(0.5));
		line1.setStrokeWidth(2);
		x.getChildren().add(line1);
		final Line line2 = new Line(0, 6, 6, 0);
		line2.setStroke(Color.gray(0.5));
		line2.setStrokeWidth(2);
		x.getChildren().add(line2);
		x.setOnMouseClicked(event -> {
			values.getChildren().remove(collection.remove(t));
		});

		v.getChildren().add(x);

		if (collection.isEmpty()) {
			collection.put(t, v);
			values.getChildren().set(0, v); // Replace the "(empty)" text
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
		return onAddAction;
	}

	protected String convertToString(final T value) {
		return value.toString();
	}

	protected void setAddDisable(final boolean disable) {
		if (disable && this.add.isFocused()) bottomPane.requestFocus();
		this.add.setDisable(disable);
	}

	@Override
	public Collection<T> getValue() {
		return new HashSet<>(collection.keySet());
	}

}
