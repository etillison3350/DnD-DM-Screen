package dmscreen.data.creature.feature.template;

import java.util.Map;

import dmscreen.Util;
import dmscreen.data.base.Ability;
import dmscreen.data.creature.feature.Spellcasting;

public class SpellcastingTemplate extends InnateSpellcastingTemplate {

	public SpellcastingTemplate() {
		super("Spellcasting");

		fields.set(7, new TemplateField("details2", FieldType.STRING));
		fields.add(7, new TemplateField("details1", FieldType.STRING));
		fields.add(7, new TemplateField("spellcastingClass", FieldType.STRING));
		fields.add(3, new TemplateField("level", FieldType.INTEGER, 1, 30));
		fields.set(2, new TemplateField("Pronoun", "Pronoun (his/her/etc.)", FieldType.STRING));
	}

	@Override
	public Spellcasting make(final Map<String, Object> values) {
		return new Spellcasting(Util.castOrDefault(CharSequence.class, values.get("note"), "").toString(), Util.castOrDefault(CharSequence.class, values.get("shortName"), "It").toString(), Util.castOrDefault(Integer.class, values.get("level"), 1), Util.castOrDefault(Ability.class, values.get("ability"), null), Util.castOrDefault(Integer.class, values.get("saveDC"), 10), Util.castOrDefault(CharSequence.class, values.get("pronoun"), "its").toString(),
				Util.castOrDefault(Boolean.class, values.get("includeAttackMod"), false) ? Util.castOrDefault(Integer.class, values.get("attackModifier"), 0) : Integer.MIN_VALUE, Util.castOrDefault(CharSequence.class, values.get("details1"), "").toString(), Util.castOrDefault(CharSequence.class, values.get("spellcastingClass"), "").toString(), Util.castOrDefault(CharSequence.class, values.get("details2"), "").toString(), getSpells(values));
	}

}
