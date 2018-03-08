package dmscreen.statblock;

import java.util.Arrays;

import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;

public class StringSelectEditor extends Editor<String> {

	private final ComboBox<String> value;

	public StringSelectEditor(final String name, final int initialIndex, final String... values) {
		super(name);

		value = new ComboBox<>();
		Arrays.stream(values).forEach(value.getItems()::add);
		addRow(0, new Text(name + ":"), value);
	}

	@Override
	public String getValue() {
		return value.getValue();
	}

}
