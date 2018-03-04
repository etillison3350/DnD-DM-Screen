package dmscreen.statblock;

import java.util.Collection;

import javafx.scene.control.TextField;

public class CollectionStringEditor extends CollectionEditor<String> {

	private final TextField editor;

	public CollectionStringEditor(final String name, final String prompt, final Collection<String> initialValues) {
		super(name, initialValues);

		editor = new TextField();
		if (prompt != null) editor.setPromptText(prompt);
		editor.setOnAction(getOnAddAction());
		setEditor(editor);
	}

	public CollectionStringEditor(final String name, final Collection<String> initialValue) {
		this(name, null, initialValue);
	}

	@Override
	protected String getEditorValue() {
		return editor.getText();
	}

	@Override
	protected void clearEditor() {
		editor.setText("");
	}

}
