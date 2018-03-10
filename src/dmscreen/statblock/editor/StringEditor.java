package dmscreen.statblock.editor;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class StringEditor extends Editor<String> {

	private final TextField value;

	public StringEditor(final String name, final String initialValue, final String prompt) {
		super(name);

		value = new TextField(initialValue);
		if (prompt != null) value.setPromptText(prompt);

		addRow(0, new Text(name + ":"), value);
	}

	public StringEditor(final String name, final String initialValue) {
		this(name, initialValue, null);
	}

	@Override
	public String getValue() {
		return value.getText();
	}

}
