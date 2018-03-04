package dmscreen.statblock;

import javafx.scene.control.Spinner;
import javafx.scene.text.Text;

public class IntegerEditor extends Editor<Integer> {

	private final Spinner<Integer> value;

	public IntegerEditor(final String name, final int min, final int max, final int value, final int step) {
		super(name);

		this.value = createEditorSpinner(min, max, value, step);

		addRow(0, new Text(name + ":"), this.value);
	}

	public IntegerEditor(final String name, final int min, final int max, final int value) {
		this(name, min, max, value, 1);
	}

	@Override
	public Integer getValue() {
		return value.getValue();
	}

	public static Spinner<Integer> createEditorSpinner(final int min, final int max, final int value, final int step) {
		final Spinner<Integer> ret = new Spinner<>(min, max, value, step);
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

	public static Spinner<Integer> createEditorSpinner(final int min, final int max, final int value) {
		return createEditorSpinner(min, max, value, 1);
	}

}
