package dmscreen.data.creature.feature.template;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dmscreen.Util;
import dmscreen.data.base.Ability;
import dmscreen.data.creature.feature.InnateSpellcasting;

public class InnateSpellcastingTemplate extends Template<InnateSpellcasting> {

	private static final Pattern SPELL_NOTE = Pattern.compile("^(.+?)\\s*\\((.+?)\\)$");

	public InnateSpellcastingTemplate() {
		this("Innate Spellcasting");
	}

	protected InnateSpellcastingTemplate(final String name) {
		super(name, Stream.of(//
				new TemplateField("note", FieldType.STRING), //
				new TemplateField("shortName", "Short Creature Name", FieldType.STRING), //
				new TemplateField("pronoun", "Pronoun (he/she/etc.)", FieldType.STRING), //
				new TemplateField("ability", "Spellcasting Ability", FieldType.ABILITY), //
				new TemplateField("saveDC", "Save DC", FieldType.INTEGER, 0, 40), //
				new TemplateField("attackModifier", FieldType.INTEGER, -10, 20), //
				new TemplateField("includeAttackMod", FieldType.BOOLEAN), //
				new TemplateField("additionalDetails", FieldType.STRING), //
				new TemplateField("spells", "Spell List", FieldType.MAP, FieldType.STRING, "Casting Limit", FieldType.LIST, FieldType.STRING, "Spell List", true) //
		).collect(Collectors.toList()));
	}

	@Override
	public InnateSpellcasting make(final Map<String, Object> values) {
		final Map<String, Map<String, String>> spells = getSpells(values);
		System.out.println("#####" + spells);

		return new InnateSpellcasting(Util.castOrDefault(CharSequence.class, values.get("note"), "").toString(), Util.castOrDefault(CharSequence.class, values.get("shortName"), "It").toString(), Util.castOrDefault(Ability.class, values.get("ability"), null), Util.castOrDefault(Integer.class, values.get("saveDC"), 10), Util.castOrDefault(Boolean.class, values.get("includeAttackMod"), false) ? Util.castOrDefault(Integer.class, values.get("attackModifier"), 0) : Integer.MIN_VALUE,
				Util.castOrDefault(CharSequence.class, values.get("pronoun"), "it").toString(), Util.castOrDefault(CharSequence.class, values.get("additionalDetails"), "").toString(), spells);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Map<String, String>> getSpells(final Map<String, Object> values) {
		final Map<String, Map<String, String>> spells = new LinkedHashMap<>();

		Util.castOrDefault(Map.class, values.get("spells"), new HashMap<>()).forEach((BiConsumer<Object, Object>) (k, v) -> {
			final String key = k.toString();
			final Collection<String> vs = (Collection<String>) v;

			Map<String, String> spellList;
			if (spells.containsKey(key)) {
				spellList = spells.get(key);
			} else {
				spellList = new TreeMap<>();
				spells.put(key, spellList);
			}

			for (final String spell : vs) {
				final Matcher m = SPELL_NOTE.matcher(spell);
				if (m.find()) {
					spellList.put(m.group(1), m.group(2));
				} else {
					spellList.put(spell, null);
				}
			}

		});
		return spells;
	}
}
