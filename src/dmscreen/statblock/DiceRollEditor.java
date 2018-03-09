package dmscreen.statblock;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import dmscreen.data.base.DiceRoll;

public class DiceRollEditor extends Editor<DiceRoll> {

	public static final Pattern DIE_ROLL = Pattern.compile("^(\\d+)d(\\d+)([+\\-]\\d+)?$");
	public static final Pattern VALUE_DIE_ROLL = Pattern.compile("^(\\d+)\\((\\d+)d(\\d+)([+\\-]\\d+)?\\)$");

	private final TextField value;

	public DiceRollEditor(final String name, final DiceRoll initialValue) {
		super(name);

		final Text label = new Text(name + ":");
		value = new TextField(String.format(initialValue.overridesExpectedValue() ? "%.0f (%s)" : "%s", Math.floor(initialValue.expectedValue()), initialValue.toString()));
		value.textProperty().addListener((observable, oldValue, newValue) -> {
			final String text = newValue.toLowerCase().replaceAll("[^\\dd +\\-\\(\\)]+", "");
			value.setText(text);

			value.setStyle("-fx-control-inner-background: " + (isValidDiceRoll(text) ? "white;" : "#FFCCCC;"));
		});
		addRow(0, label, value);
	}

	public boolean isValidDiceRoll(final String diceRoll) {
		final String formatted = diceRoll.toLowerCase().replaceAll("[^\\dd+\\-\\(\\)]+", "");
		return DIE_ROLL.matcher(formatted).find() || VALUE_DIE_ROLL.matcher(formatted).find();
	}

	@Override
	public DiceRoll getValue() {
		final String formatted = value.getText().toLowerCase().replaceAll("[^\\dd+\\-\\(\\)]+", "");
		Matcher matcher = DIE_ROLL.matcher(formatted);
		if (matcher.find()) {
			return new DiceRoll(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), matcher.group(3) == null ? 0 : Integer.parseInt(matcher.group(3)));
		}
		matcher = VALUE_DIE_ROLL.matcher(formatted);
		if (matcher.find()) {
			return new DiceRoll(Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)), matcher.group(4) == null ? 0 : Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(1)));
		}

		return null;
	}

}
