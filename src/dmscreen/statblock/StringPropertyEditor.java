package dmscreen.statblock;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class StringPropertyEditor extends PropertyEditor<String> {

	private final TextField value;

	public StringPropertyEditor(final String name, final String initialValue) {
		super(name);

		value = new TextField(initialValue);

		addRow(0, new Text(name + ":"), value);
	}

	@Override
	public String getValue() {
		return value.getText();
	}

}
