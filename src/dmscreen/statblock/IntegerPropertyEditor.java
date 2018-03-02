package dmscreen.statblock;

import javafx.scene.control.Spinner;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class IntegerPropertyEditor extends PropertyEditor<Integer> {

	private final Spinner<Integer> value;

	public IntegerPropertyEditor(final String name, final int min, final int max, final int value) {
		super(name);

		this.value = createEditorSpinner(min, max, value);

		addRow(0, new TextFlow(new Text(name + ":")), this.value);
	}

	@Override
	public Integer getValue() {
		return value.getValue();
	}

	public static Spinner<Integer> createEditorSpinner(final int min, final int max, final int value) {
		final Spinner<Integer> ret = new Spinner<>(min, max, value);
		ret.setEditable(true);
		ret.focusedProperty().addListener((observable, oldValue, newValue) -> {
			try {
				if (!newValue) ret.increment(0);
			} catch (final NumberFormatException e) {
				ret.getEditor().setText(Integer.toString(ret.getValue()));
			}
		});

		return ret;
	}

}
