package dmscreen.statblock.editor;

import java.util.function.Predicate;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class StringEditor extends Editor<String> {

	private final TextField value;
	private final Predicate<String> validator;

	public StringEditor(final String name, final String initialValue, final String prompt, final Predicate<String> validator) {
		super(name);

		value = new TextField(initialValue);
		if (prompt != null) value.setPromptText(prompt);
		if (validator != null) value.textProperty().addListener((observable, oldValue, newValue) -> value.setStyle("-fx-control-inner-background: " + (validator.test(newValue) ? "white;" : "#FFCCCC;")));

		this.validator = validator;

		addRow(0, new Text(name + ":"), value);
	}

	public StringEditor(final String name, final String initialValue, final String prompt) {
		this(name, initialValue, prompt, null);
	}

	public StringEditor(final String name, final String initialValue) {
		this(name, initialValue, null);
	}

	public boolean isInputValid() {
		return validator == null || validator.test(getValue());
	}

	@Override
	public String getValue() {
		return value.getText();
	}

	@Override
	public void setValue(final String value) {
		this.value.setText(value);
	}

}
