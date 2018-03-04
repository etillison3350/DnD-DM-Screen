package dmscreen.statblock;

import javafx.scene.control.Hyperlink;
import javafx.scene.layout.GridPane;

public abstract class EditableMapEditor<K, V> extends MapEditor<K, V> {

	private final Hyperlink add;

	public EditableMapEditor(final String name, final String keyTitle, final String valueTitle) {
		super(name, keyTitle, valueTitle);

		add = new Hyperlink("Add");
		add(add, 0, getMinRow(), 2, 1);
		add.setOnAction(event -> {
			add.setVisited(false);
			addMapRow(makeRow());
		});
	}

	protected abstract MapRow<K, V> makeRow();

	@Override
	protected void addMapRow(final MapRow<K, V> row) {
		GridPane.setRowIndex(add, getMinRow() + 1);

		super.addMapRow(row);
	}

}
