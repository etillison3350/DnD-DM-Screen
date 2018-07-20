package dmscreen.statblock.editor.collection;

import java.util.Collection;

import dmscreen.statblock.editor.EnumEditor;
import dmscreen.util.Util;
import javafx.scene.control.ComboBox;

public class CollectionEnumEditor<T extends Enum<?>> extends CollectionEditor<T> {

	private final ComboBox<T> editor;

	public CollectionEnumEditor(final Class<T> clazz, final String name, final Collection<T> initialValue) {
		super(name, initialValue);

		this.editor = EnumEditor.createEditorComboBox(clazz, null);
		editor.valueProperty().addListener((observable, oldValue, newValue) -> setAddDisable(newValue == null));
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
