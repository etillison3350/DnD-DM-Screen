package dmscreen.statblock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Separator;
import javafx.scene.layout.FlowPane;

public abstract class CollectionEditor<T> extends Editor<Collection<T>> {

	private final List<ListItem<T>> items = new ArrayList<>();
	private final Hyperlink add;
	private final FlowPane pane;

	public CollectionEditor(final String name) {
		super(name);

		setPadding(new Insets(8, 0, 8, 0));

		this.add(StatBlock.smallCaps(name, "header-small"), 0, 0, 2, 1);
		this.add(new Separator(), 0, 1, 2, 1);

		pane = new FlowPane(4, 4);
		add(pane, 0, 2, 2, 1);

		add = new Hyperlink("Add");
		add(add, 0, 3, 2, 1);
		add.setOnAction(event -> {
			add.setVisited(false);
			addListItem(makeItem());
		});
	}

	protected abstract ListItem<T> makeItem();

	@Override
	public Collection<T> getValue() {
		return items.stream().map(r -> r.valueGetter.get()).collect(Collectors.toList());
	}

	protected void addListItem(final ListItem<T> item) {
		items.add(item);
		pane.getChildren().add(item.value);
		// addRow(minRow++, row.value);
	}

	public static class ListItem<T> {
		public final Node value;
		public final Supplier<T> valueGetter;

		public ListItem(final Node value, final Supplier<T> valueGetter) {
			this.value = value;
			this.valueGetter = valueGetter;
		}

	}

}
