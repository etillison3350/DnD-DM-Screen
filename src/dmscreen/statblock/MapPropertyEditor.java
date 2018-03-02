package dmscreen.statblock;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class MapPropertyEditor<K, V> extends PropertyEditor<Map<K, V>> {

	private final List<MapRow<K, V>> rows = new ArrayList<>();
	private int minRow = 2;

	public MapPropertyEditor(final String name, final String keyTitle, final String valueTitle) {
		super(name);

		this.add(new TextFlow(new Text(name)), 0, 0, 2, 1);
		addRow(1, new TextFlow(new Text(keyTitle)), new TextFlow(new Text(valueTitle)));
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
