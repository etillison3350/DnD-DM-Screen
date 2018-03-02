package dmscreen.statblock;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.StringConverter;
import dmscreen.Util;

public class EnumPropertyEditor<T extends Enum<?>> extends PropertyEditor<T> {

	private final ComboBox<T> value;

	public EnumPropertyEditor(final String name, final T initialValue) {
		super(name);

		value = new ComboBox<>();
		try {
			List.class.getMethod("forEach", Consumer.class).invoke(Arrays.class.getMethod("asList", Object[].class).invoke(null, initialValue.getClass().getMethod("values").invoke(null)), (Consumer<T>) value.getItems()::add);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException(e);
		}
		value.setCellFactory(param -> new TextFieldListCell<>(new StringConverter<T>() {

			@Override
			public String toString(final T object) {
				return Util.titleCase(Util.getName(object));
			}

			@Override
			public T fromString(final String string) {
				return null;
			}

		}));
		value.setButtonCell(value.getCellFactory().call(null));
		value.setValue(initialValue);
		addRow(0, new TextFlow(new Text(name + ":")), value);
	}

	@Override
	public T getValue() {
		return value.getValue();
	}

}
