package dmscreen.statblock;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class EditableMapCollectionEditor<K, T> extends EditableMapEditor<K, Collection<T>> {

	private final String keyTitle, valueTitle;
	private final BiFunction<String, K, Editor<K>> keyEditor;
	private final BiFunction<String, Collection<T>, Editor<Collection<T>>> valueEditor;

	public EditableMapCollectionEditor(final String name, final String keyTitle, final String valueTitle, final String addButtonName, final Map<K, ? extends Collection<T>> initialValue, final BiFunction<String, K, Editor<K>> keyEditor, final BiFunction<String, Collection<T>, Editor<Collection<T>>> valueEditor) {
		super(name, null, null);

		setAddButtonName(addButtonName);

		this.keyTitle = keyTitle;
		this.valueTitle = valueTitle;
		this.keyEditor = keyEditor;
		this.valueEditor = valueEditor;

		setVgap(8);

		initialValue.forEach((k, c) -> {
			addMapRow(makeRow(k, c));
		});
	}

	public EditableMapCollectionEditor(final String name, final String keyTitle, final String valueTitle, final Map<K, ? extends Collection<T>> initialValue, final BiFunction<String, K, Editor<K>> keyEditor, final BiFunction<String, Collection<T>, Editor<Collection<T>>> valueEditor) {
		this(name, keyTitle, valueTitle, DEFAULT_ADD_BUTTON_NAME, initialValue, keyEditor, valueEditor);
	}

	@Override
	public Map<K, Collection<T>> getValue() {
		final Map<K, Collection<T>> ret = new LinkedHashMap<>();
		getRows().forEach(row -> {
			final K key = row.keyGetter.get();
			final Collection<T> value = row.valueGetter.get();

			if (ret.containsKey(key)) {
				ret.get(key).addAll(value);
			} else {
				ret.put(key, value);
			}
		});
		return ret;
	}

	@Override
	protected MapRow<K, Collection<T>> makeRow() {
		return makeRow(null, null);
	}

	private MapRow<K, Collection<T>> makeRow(final K initialKey, final Collection<T> initialValue) {
		final Editor<K> key = keyEditor.apply(keyTitle, initialKey);
		final Editor<Collection<T>> value = valueEditor.apply(valueTitle, initialValue);
		return new MapRow<K, Collection<T>>(key, value, key::getValue, value::getValue, true);
	}

}
