package dmscreen.statblock.editor;

import dmscreen.data.base.DiceRoll;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class DiceRollEditor extends Editor<DiceRoll> {

	private final TextField value;

	public DiceRollEditor(final String name, final DiceRoll initialValue) {
		super(name);

		final Text label = new Text(name + ":");
		value = makeEditorField(initialValue);
		addRow(0, label, value);
	}

	public static TextField makeEditorField(final DiceRoll initialValue) {
		final TextField value = new TextField(rollToString(initialValue));
		value.textProperty().addListener((observable, oldValue, newValue) -> {
			final String text = newValue.toLowerCase().replaceAll("[^\\dd +\\-\\(\\)]+", "");
			value.setText(text);

			value.setStyle("-fx-control-inner-background: " + (DiceRoll.isValidDiceRoll(text) ? "white;" : "#FFCCCC;"));
		});
		return value;
	}

	private static String rollToString(final DiceRoll value) {
		return value == null ? null : String.format(value.overridesExpectedValue() ? "%.0f (%s)" : "%2$s", Math.floor(value.expectedValue()), value.toString());
	}

	@Override
	public DiceRoll getValue() {
		if (value.getText() == null) return null;
		return DiceRoll.fromString(value.getText());
	}

	@Override
	public void setValue(final DiceRoll value) {
		this.value.setText(rollToString(value));
	}

}
