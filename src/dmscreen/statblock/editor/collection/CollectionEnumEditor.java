package dmscreen.statblock.editor.collection;

import java.util.Collection;

import javafx.scene.control.ComboBox;
import dmscreen.statblock.editor.EnumEditor;
import dmscreen.util.Util;

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
	protected void clearEditor() {
		editor.setValue(null);
	}

	@Override
	protected String convertToString(final T value) {
		return Util.titleCase(Util.getName(value));
	}

}
