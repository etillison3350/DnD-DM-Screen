package dmscreen.statblock;

import javafx.scene.control.Spinner;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class IntegerPropertyEditor extends PropertyEditor<Integer> {

	private final Spinner<Integer> value;

	public IntegerPropertyEditor(final String name, final int min, final int max, final int value) {
		super(name);

		this.value = new Spinner<>(min, max, value);
		this.value.setEditable(true);
		this.value.focusedProperty().addListener((observable, oldValue, newValue) -> {
			try {
				if (!newValue) this.value.increment(0);
			} catch (final NumberFormatException e) {
				this.value.getEditor().setText(Integer.toString(this.value.getValue()));
			}
		});

		addRow(0, new TextFlow(new Text(name + ":")), this.value);
	}

	@Override
	public Integer getValue() {
		return value.getValue();
	}

}
