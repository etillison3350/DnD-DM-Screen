package dmscreen.statblock;

import java.util.Map;

public class StringMapPropertyEditor<V> extends PropertyEditor<Map<String, V>> {

	public StringMapPropertyEditor(final String name, final Map<String, V> initialValue) {
		super(name);

	}

	@Override
	public Map<String, V> getValue() {
		return null;
	}

}
