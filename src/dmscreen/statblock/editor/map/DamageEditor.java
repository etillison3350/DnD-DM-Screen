package dmscreen.statblock.editor.map;

import java.util.Map;

import dmscreen.data.base.DamageType;
import dmscreen.data.base.DiceRoll;
import dmscreen.statblock.editor.DiceRollEditor;
import dmscreen.statblock.editor.EnumEditor;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class DamageEditor extends EditableMapEditor<DamageType, DiceRoll> {

	public DamageEditor(final String name, final String keyTitle, final String valueTitle, final Map<DamageType, DiceRoll> initialValue) {
		super(name, keyTitle, valueTitle);

		if (initialValue != null) {
			initialValue.forEach((k, v) -> {
				addMapRow(makeRow(k, v));
			});
		}
	}

	@Override
	protected MapRow<DamageType, DiceRoll> makeRow(final DamageType initialKey, final DiceRoll initialValue) {
		final ComboBox<DamageType> keyNode = EnumEditor.createEditorComboBox(DamageType.class, initialKey);
		final TextField valueNode = DiceRollEditor.makeEditorField(initialValue);
		return new MapRow<>(keyNode, valueNode, keyNode::getValue, () -> DiceRoll.fromString(valueNode.getText()));
	}

}
