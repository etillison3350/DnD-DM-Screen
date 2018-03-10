package dmscreen.statblock.editor.map;

import java.util.Map;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import dmscreen.data.base.DamageType;
import dmscreen.data.base.DiceRoll;
import dmscreen.statblock.editor.DiceRollEditor;
import dmscreen.statblock.editor.EnumEditor;

public class DamageEditor extends EditableMapEditor<DamageType, DiceRoll> {

	public DamageEditor(final String name, final String keyTitle, final String valueTitle, final Map<DamageType, DiceRoll> initialValue) {
		super(name, keyTitle, valueTitle);

		initialValue.forEach((k, v) -> {
			addMapRow(makeRow(k, v));
		});
	}

	private static MapRow<DamageType, DiceRoll> makeRow(final DamageType initialKey, final DiceRoll initialValue) {
		final ComboBox<DamageType> keyNode = EnumEditor.createEditorComboBox(DamageType.class, initialKey);
		final TextField valueNode = DiceRollEditor.makeEditorField(initialValue);
		return new MapRow<>(keyNode, valueNode, keyNode::getValue, () -> DiceRoll.fromString(valueNode.getText()));
	}

	@Override
	protected MapRow<DamageType, DiceRoll> makeRow() {
		return makeRow(null, null);
	}

}
