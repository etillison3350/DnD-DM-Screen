package dmscreen.statblock;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.text.Text;
import dmscreen.Util;

public class MapEnumIntegerPropertyEditor<T extends Enum<?>> extends MapPropertyEditor<T, Integer> {

	public MapEnumIntegerPropertyEditor(final Class<T> clazz, final String name, final String keyTitle, final String valueTitle, final int min, final int max, final int step, final Map<T, Integer> initialValue, final boolean listAll) {
		super(name, keyTitle, valueTitle);

		if (listAll) {
			try {
				List.class.getMethod("forEach", Consumer.class).invoke(Arrays.class.getMethod("asList", Object[].class).invoke(null, clazz.getMethod("values").invoke(null)), (Consumer<T>) t -> {
					Integer i = initialValue.get(t);
					if (i == null) i = 0;
					addMapRow(makeRow(t, min, max, step, i, false));
				});
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {}
		} else {
			initialValue.forEach((t, i) -> {
				addMapRow(makeRow(t, min, max, step, i, true));
			});
		}
	}

	public MapEnumIntegerPropertyEditor(final Class<T> clazz, final String name, final String keyTitle, final String valueTitle, final int min, final int max, final Map<T, Integer> initialValue, final boolean listAll) {
		this(clazz, name, keyTitle, valueTitle, min, max, 1, initialValue, listAll);
	}

	private static <T extends Enum<?>> MapRow<T, Integer> makeRow(final T key, final int min, final int max, final int step, final int initialValue, final boolean keyEditable) {
		final Spinner<Integer> valueNode = IntegerPropertyEditor.createEditorSpinner(min, max, initialValue, step);
		if (keyEditable) {
			final ComboBox<T> keyNode = EnumPropertyEditor.createEditorComboBox(key);
			return new MapRow<>(keyNode, valueNode, keyNode::getValue, valueNode::getValue);
		} else {
			return new MapRow<>(new Text(Util.titleCase(key.name())), valueNode, () -> key, valueNode::getValue);
		}
	}

}
