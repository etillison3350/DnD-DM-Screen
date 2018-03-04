package dmscreen.statblock;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javafx.scene.control.Spinner;
import javafx.scene.text.Text;
import dmscreen.Util;

public class MapEnumIntegerEditor<T extends Enum<?>> extends MapEditor<T, Integer> {

	public MapEnumIntegerEditor(final Class<T> clazz, final String name, final String keyTitle, final String valueTitle, final int min, final int max, final int step, final Map<T, Integer> initialValue) {
		super(name, keyTitle, valueTitle);

		try {
			List.class.getMethod("forEach", Consumer.class).invoke(Arrays.class.getMethod("asList", Object[].class).invoke(null, clazz.getMethod("values").invoke(null)), (Consumer<T>) t -> {
				Integer i = initialValue.get(t);
				if (i == null) i = 0;
				addMapRow(makeRow(t, min, max, step, i));
			});
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {}

	}

	public MapEnumIntegerEditor(final Class<T> clazz, final String name, final String keyTitle, final String valueTitle, final int min, final int max, final Map<T, Integer> initialValue) {
		this(clazz, name, keyTitle, valueTitle, min, max, 1, initialValue);
	}

	private static <T extends Enum<?>> MapRow<T, Integer> makeRow(final T key, final int min, final int max, final int step, final int initialValue) {
		final Spinner<Integer> valueNode = IntegerEditor.createEditorSpinner(min, max, initialValue, step);
		return new MapRow<>(new Text(Util.titleCase(key.name())), valueNode, () -> key, valueNode::getValue);
	}

}
