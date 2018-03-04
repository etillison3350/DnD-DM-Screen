package dmscreen.statblock;

import java.util.Collection;

import javafx.scene.control.ComboBox;
import dmscreen.Util;

public class CollectionEnumEditor<T extends Enum<?>> extends CollectionEditor<T> {

	private final ComboBox<T> editor;

	public CollectionEnumEditor(final Class<T> clazz, final String name, final Collection<T> initialValue) {
		super(name, initialValue);

		this.editor = EnumEditor.createEditorComboBox(clazz, null);
		setEditor(editor);
	}

	@Override
	protected T getEditorValue() {
		return editor.getValue();
	}

	@Override
	protected String convertToString(final T value) {
		return Util.titleCase(Util.getName(value));
	}

}
