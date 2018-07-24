package dmscreen.statblock.editor.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import dmscreen.statblock.StatBlock;
import dmscreen.statblock.editor.Editor;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;

public abstract class MapEditor<K, V> extends Editor<Map<K, V>> {

	private final List<MapRow<K, V>> rows = new ArrayList<>();
	private int minRow = 2;

	public MapEditor(final String name, final String keyTitle, final String valueTitle) {
		super(name);

		setPadding(new Insets(8, 0, 8, 0));

		this.add(StatBlock.smallCaps(name, "header-small"), 0, 0, 2, 1);
		this.add(new Separator(), 0, 1, 2, 1);

		final RowConstraints header = new RowConstraints();
		header.setMinHeight(20);
		getRowConstraints().addAll(new RowConstraints(), new RowConstraints(), header);

		if (keyTitle != null && valueTitle != null) addRow(minRow++, new Text(keyTitle), new Text(valueTitle));
	}

	protected void addMapRow(final MapRow<K, V> row) {
		this.rows.add(row);
		if (row.separateRows) {
			add(row.key, 0, minRow++, 2, 1);
			add(row.value, 0, minRow++, 2, 1);
			add(new Text(), 0, minRow++, 2, 1);
		} else {
			addRow(minRow++, row.key, row.value);
		}
	}

	protected Collection<MapRow<K, V>> getRows() {
		return new ArrayList<>(rows);
	}

	@Override
	public Map<K, V> getValue() {
		final Map<K, V> ret = new LinkedHashMap<>(rows.size());
		rows.forEach(row -> {
			ret.put(row.keyGetter.get(), row.valueGetter.get());
		});
		return ret;
	}

	@Override
	public void setValue(final Map<K, V> value) {
		throw new IllegalStateException("Cannot set value of MapEditors");
	}

	protected int getMinRow() {
		return minRow;
	}

	public static class MapRow<K, V> {
		public final Node key, value;
		public final Supplier<K> keyGetter;
		public final Supplier<V> valueGetter;
		public final boolean separateRows;

		public MapRow(final Node key, final Node value, final Supplier<K> keyGetter, final Supplier<V> valueGetter, final boolean separateRows) {
			this.key = key;
			this.value = value;
			this.keyGetter = keyGetter;
			this.valueGetter = valueGetter;
			this.separateRows = separateRows;
		}

		public MapRow(final Node key, final Node value, final Supplier<K> keyGetter, final Supplier<V> valueGetter) {
			this(key, value, keyGetter, valueGetter, false);
		}
	}

}
