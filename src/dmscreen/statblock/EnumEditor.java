package dmscreen.statblock;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import dmscreen.Util;

public class EnumEditor<T extends Enum<?>> extends Editor<T> {

	private final ComboBox<T> value;

	public EnumEditor(final Class<T> clazz, final String name, final T initialValue) {
		super(name);

		value = createEditorComboBox(clazz, initialValue);
		addRow(0, new Text(name + ":"), value);
	}

	@Override
	public T getValue() {
		return value.getValue();
	}

	public static <E extends Enum<?>> ComboBox<E> createEditorComboBox(final Class<E> clazz, final E initialValue) {
		final ComboBox<E> ret = new ComboBox<>();
		try {
			List.class.getMethod("forEach", Consumer.class).invoke(Arrays.class.getMethod("asList", Object[].class).invoke(null, clazz.getMethod("values").invoke(null)), (Consumer<E>) ret.getItems()::add);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException(e);
		}
		ret.setCellFactory(param -> new TextFieldListCell<>(new StringConverter<E>() {

			@Override
			public String toString(final E object) {
				return Util.titleCase(Util.getName(object));
			}

			@Override
			public E fromString(final String string) {
				return null;
			}

		}));
		ret.setButtonCell(ret.getCellFactory().call(null));
		if (initialValue != null) ret.setValue(initialValue);

		return ret;
	}

}
