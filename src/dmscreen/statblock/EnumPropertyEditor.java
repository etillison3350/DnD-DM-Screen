package dmscreen.statblock;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import dmscreen.Util;
import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.StringConverter;

public class EnumPropertyEditor<T extends Enum<?>> extends PropertyEditor<T> {

	private final ComboBox<T> value;

	public EnumPropertyEditor(final String name, final T initialValue) {
		super(name);

		value = createEditorComboBox(initialValue);
		addRow(0, new TextFlow(new Text(name + ":")), value);
	}

	@Override
	public T getValue() {
		return value.getValue();
	}

	public static <E extends Enum<?>> ComboBox<E> createEditorComboBox(final E initialValue) {
		final ComboBox<E> ret = new ComboBox<>();
		try {
			List.class.getMethod("forEach", Consumer.class).invoke(Arrays.class.getMethod("asList", Object[].class).invoke(null, initialValue.getClass().getMethod("values").invoke(null)), (Consumer<E>) ret.getItems()::add);
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
		ret.setValue(initialValue);

		return ret;
	}

}
