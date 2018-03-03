package dmscreen.statblock;

import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;

public class BooleanPropertyEditor extends PropertyEditor<Boolean> {

	private final CheckBox value;

	public BooleanPropertyEditor(final String name, final boolean initialValue) {
		super(name);

		value = new CheckBox();
		value.setSelected(initialValue);
		value.setPadding(new Insets(3));

		addRow(0, new Text(name), value);
	}

	@Override
	public Boolean getValue() {
		return value.isSelected();
	}

}
