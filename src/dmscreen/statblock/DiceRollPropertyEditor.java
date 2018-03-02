package dmscreen.statblock;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import dmscreen.data.base.DiceRoll;

public class DiceRollPropertyEditor extends PropertyEditor<DiceRoll> {

	public static final Pattern DIE_ROLL = Pattern.compile("^(\\d+)d(\\d+)([+\\-]\\d+)?$");

	private final TextField value;

	public DiceRollPropertyEditor(final String name, final DiceRoll initialValue) {
		super(name);

		final TextFlow label = new TextFlow(new Text(name + ":"));
		value = new TextField(initialValue.toString());
		value.textProperty().addListener((observable, oldValue, newValue) -> {
			final String text = newValue.toLowerCase().replaceAll("[^\\dd +\\-]+", "");
			value.setText(text);

			value.setStyle("-fx-control-inner-background: " + (isValidDiceRoll(text) ? "white;" : "#FFCCCC;"));
		});
		addRow(0, label, value);
	}

	public boolean isValidDiceRoll(final String diceRoll) {
		return DIE_ROLL.matcher(diceRoll.toLowerCase().replaceAll("[^\\dd+\\-]+", "")).find();
	}

	@Override
	public DiceRoll getValue() {
		final Matcher matcher = DIE_ROLL.matcher(value.getText().toLowerCase().replaceAll("[^\\dd+\\-]+", ""));
		if (matcher.find()) {
			return new DiceRoll(Integer.parseInt(matcher.group(0)), Integer.parseInt(matcher.group(1)), matcher.group(2) == null ? 0 : Integer.parseInt(matcher.group(2)));
		}

		return null;
	}

}
