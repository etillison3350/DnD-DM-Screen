package dmscreen.statblock.editor.map;

import java.util.Map;

import dmscreen.statblock.editor.EnumEditor;
import dmscreen.statblock.editor.IntegerEditor;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;

public class EditableMapEnumIntegerEditor<T extends Enum<?>> extends EditableMapEditor<T, Integer> {

	private final Class<T> clazz;
	private final int min, max, step;

	public EditableMapEnumIntegerEditor(final Class<T> clazz, final String name, final String keyTitle, final String valueTitle, final int min, final int max, final int step, final Map<T, Integer> initialValue) {
		super(name, keyTitle, valueTitle);

		this.clazz = clazz;
		this.min = min;
		this.max = max;
		this.step = step;

		initialValue.forEach((t, i) -> {
			addMapRow(makeRow(clazz, t, min, max, step, i));
		});
	}

	public EditableMapEnumIntegerEditor(final Class<T> clazz, final String name, final String keyTitle, final String valueTitle, final int min, final int max, final Map<T, Integer> initialValue) {
		this(clazz, name, keyTitle, valueTitle, min, max, 1, initialValue);
	}

	private static <T extends Enum<?>> MapRow<T, Integer> makeRow(final Class<T> clazz, final T key, final int min, final int max, final int step, final int initialValue) {
		final Spinner<Integer> valueNode = IntegerEditor.createEditorSpinner(min, max, initialValue, step);
		final ComboBox<T> keyNode = EnumEditor.createEditorComboBox(clazz, key);
		return new MapRow<>(keyNode, valueNode, keyNode::getValue, valueNode::getValue);
	}

	@Override
	protected MapRow<T, Integer> makeRow() {
		return makeRow(clazz, null, min, max, step, Math.max(0, min));
	}

}
