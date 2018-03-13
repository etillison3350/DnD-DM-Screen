package dmscreen.statblock.editor;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import dmscreen.data.base.DiceRoll;

public class DiceRollEditor extends Editor<DiceRoll> {

	private final TextField value;

	public DiceRollEditor(final String name, final DiceRoll initialValue) {
		super(name);

		final Text label = new Text(name + ":");
		value = makeEditorField(initialValue);
		addRow(0, label, value);
	}

	public static TextField makeEditorField(final DiceRoll initialValue) {
		final TextField value = new TextField(initialValue == null ? null : String.format(initialValue.overridesExpectedValue() ? "%.0f (%s)" : "%s", Math.floor(initialValue.expectedValue()), initialValue.toString()));
		value.textProperty().addListener((observable, oldValue, newValue) -> {
			final String text = newValue.toLowerCase().replaceAll("[^\\dd +\\-\\(\\)]+", "");
			value.setText(text);

			value.setStyle("-fx-control-inner-background: " + (DiceRoll.isValidDiceRoll(text) ? "white;" : "#FFCCCC;"));
		});
		return value;
	}

	@Override
	public DiceRoll getValue() {
		return DiceRoll.fromString(value.getText());
	}

}
