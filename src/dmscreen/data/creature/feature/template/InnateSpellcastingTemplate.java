package dmscreen.data.creature.feature.template;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dmscreen.Util;
import dmscreen.data.base.Ability;
import dmscreen.data.creature.feature.InnateSpellcasting;

public class InnateSpellcastingTemplate extends Template<InnateSpellcasting> {

	public InnateSpellcastingTemplate(final String name) {
		super(name, Stream.of(//
				new TemplateField("psionics", FieldType.BOOLEAN), //
				new TemplateField("shortName", "Short Creatue Name", FieldType.STRING), //
				new TemplateField("pronoun", FieldType.STRING, "its", "his", "her", "their"), //
				new TemplateField("ability", "Spellcasting Ability", FieldType.ABILITY), //
				new TemplateField("saveDC", "Save DC", FieldType.INTEGER, 0, 40), //
				new TemplateField("attackModifier", FieldType.INTEGER, -10, 20), //
				new TemplateField("includeAttackMod", FieldType.BOOLEAN), //
				new TemplateField("additionalDetails", FieldType.STRING), //
				new TemplateField("spells", FieldType.MAP, FieldType.STRING, "Time Limit", FieldType.LIST, FieldType.STRING, "Spells", true)//
				).collect(Collectors.toList()));
		// TODO Auto-generated constructor stub
	}

	@Override
	public InnateSpellcasting make(final Map<String, Object> values) {

		return new InnateSpellcasting(Util.castOrDefault(Boolean.class, values.get("psionics"), false) ? "Psionics" : null, Util.castOrDefault(CharSequence.class, values.get("shortName"), "It").toString(), Util.castOrDefault(Ability.class, values.get("ability"), null), Util.castOrDefault(Integer.class, values.get("saveDC"), 10), Util.castOrDefault(Boolean.class, values.get("includeAttackMod"), false) ? Util.castOrDefault(Integer.class, values.get("attackModifier"), 0) : Integer.MIN_VALUE, Util.castOrDefault(CharSequence.class, values.get("pronoun"), "its").toString(), Util.castOrDefault(CharSequence.class, values.get("additionalDetails"), "").toString(), spells);
	}
}
