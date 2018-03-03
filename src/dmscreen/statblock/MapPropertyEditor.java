package dmscreen.statblock;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;

public class MapPropertyEditor<K, V> extends PropertyEditor<Map<K, V>> {

	private final List<MapRow<K, V>> rows = new ArrayList<>();
	private int minRow = 3;

	public MapPropertyEditor(final String name, final String keyTitle, final String valueTitle) {
		super(name);

		setPadding(new Insets(8, 0, 8, 0));

		this.add(StatBlock.smallCaps(name, "header-small"), 0, 0, 2, 1);
		this.add(new Separator(), 0, 1, 2, 1);

		final RowConstraints header = new RowConstraints();
		header.setMinHeight(20);
		getRowConstraints().addAll(new RowConstraints(), new RowConstraints(), header);

		addRow(2, new Text(keyTitle), new Text(valueTitle));
	}

	protected void addMapRow(final MapRow<K, V> row) {
		this.rows.add(row);
		addRow(minRow++, row.key, row.value);
	}

	@Override
	public Map<K, V> getValue() {
		final Map<K, V> ret = new LinkedHashMap<>(rows.size());
		rows.forEach(row -> {
			ret.put(row.keyGetter.get(), row.valueGetter.get());
		});
		return ret;
	}

	public static class MapRow<K, V> {
		public final Node key, value;
		public final Supplier<K> keyGetter;
		public final Supplier<V> valueGetter;

		public MapRow(final Node key, final Node value, final Supplier<K> keyGetter, final Supplier<V> valueGetter) {
			this.key = key;
			this.value = value;
			this.keyGetter = keyGetter;
			this.valueGetter = valueGetter;
		}

	}

}
