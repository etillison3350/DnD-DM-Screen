package dmscreen.statblock;

import java.util.Map;

public class MapEnumStringPropertyEditor<T extends Enum<?>> extends PropertyEditor<Map<T, String>> {

	public MapEnumStringPropertyEditor(final String name, final Map<T, String> initialValue, final boolean listAll) {
		super(name);

	}

	@Override
	public Map<T, String> getValue() {
		// TODO Auto-generated method stub
		return null;
	}

}
