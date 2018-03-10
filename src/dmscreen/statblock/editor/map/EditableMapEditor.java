package dmscreen.statblock.editor.map;

import javafx.scene.control.Hyperlink;
import javafx.scene.layout.GridPane;

public abstract class EditableMapEditor<K, V> extends MapEditor<K, V> {

	protected static final String DEFAULT_ADD_BUTTON_NAME = "Add";

	private final Hyperlink add;

	public EditableMapEditor(final String name, final String keyTitle, final String valueTitle) {
		super(name, keyTitle, valueTitle);

		add = new Hyperlink(DEFAULT_ADD_BUTTON_NAME);
		add(add, 0, getMinRow(), 2, 1);
		add.setOnAction(event -> {
			add.setVisited(false);
			addMapRow(makeRow());
		});
	}

	protected abstract MapRow<K, V> makeRow();

	protected final void setAddButtonName(final String name) {
		add.setText(name);
	}

	@Override
	protected void addMapRow(final MapRow<K, V> row) {
		GridPane.setRowIndex(add, getMinRow() + (row.separateRows ? 2 : 1));

		super.addMapRow(row);
	}

}
