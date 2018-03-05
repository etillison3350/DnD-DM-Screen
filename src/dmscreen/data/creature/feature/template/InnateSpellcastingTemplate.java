package dmscreen.data.creature.feature.template;

import java.util.Map;

import dmscreen.data.base.Ability;
import dmscreen.data.creature.feature.InnateSpellcasting;

public class InnateSpellcastingTemplate extends Template<InnateSpellcasting> {

	private InnateSpellcastingTemplate(final String name) {
		super(name);

		fields.put("short_name", String.class);
		fields.put("spellcasting_ability", Ability.class);
		fields.put("attack_modifier", int.class);
		fields.put("save_DC", int.class);
		fields.put("pronoun", String.class);
		fields.put("spells", Map<String, String>.class);
	}

	@Override
	public InnateSpellcasting make(final Map<String, Object> values) {
		return null;
	}

}
