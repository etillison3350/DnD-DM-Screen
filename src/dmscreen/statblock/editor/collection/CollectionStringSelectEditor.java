package dmscreen.statblock.editor.collection;

import java.util.Collection;

import javafx.scene.control.ComboBox;

public class CollectionStringSelectEditor extends CollectionEditor<String> {

	private final ComboBox<String> editor;

	public CollectionStringSelectEditor(final String name, final Collection<String> initialValues, final Collection<String> possibleValues) {
		super(name, initialValues);

		editor = new ComboBox<String>();
		editor.getItems().addAll(possibleValues);
		editor.valueProperty().addListener((observable, oldValue, newValue) -> setAddDisable(newValue == null));
		setEditor(editor);
	}

	@Override
	protected String getEditorValue() {
		return editor.getSelectionModel().getSelectedItem();
	}

	@Override
	protected void clearEditor() {
		editor.getSelectionModel().clearSelection();
	}

}
