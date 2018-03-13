package dmscreen.data.creature.feature.template;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dmscreen.Util;
import dmscreen.data.base.DamageType;
import dmscreen.data.base.DiceRoll;
import dmscreen.data.creature.feature.Attack;

public class AttackTemplate extends Template<Attack> {

	public final String descFormat;

	public AttackTemplate(final String name, final Collection<TemplateField> fields, final String descFormat) {
		super(name, Stream.concat(Stream.of( //
				new TemplateField("name", FieldType.STRING), //
				new TemplateField("note", FieldType.STRING), //
				new TemplateField("attackType", FieldType.ATTACK_TYPE), //
				new TemplateField("attackModifier", FieldType.INTEGER, -10, 20), //
				new TemplateField("params", "Range/Targets", FieldType.STRING), //
				new TemplateField("damage", FieldType.MAP, FieldType.DAMAGE_TYPE, "Damage Type", FieldType.DICE_ROLL, null, "Amount", true) //
				), fields.stream()).collect(Collectors.toList()));

		this.descFormat = descFormat;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Attack make(final Map<String, Object> values) {
		final Map<DamageType, DiceRoll> damageMap = new LinkedHashMap<>();
		try {
			damageMap.putAll(Util.castOrDefault(Map.class, values.get("damage"), new HashMap<>()));
		} catch (final Exception e) {}

		final Attack.Damage[] damages = damageMap.keySet().stream().map(k -> new Attack.Damage(damageMap.get(k), k)).toArray(size -> new Attack.Damage[size]);

		return new Attack(Util.castOrDefault(CharSequence.class, values.get("name"), "").toString(), Util.castOrDefault(CharSequence.class, values.get("note"), "").toString(), Util.castOrDefault(Attack.Type.class, values.get("attackType"), Attack.Type.MELEE_WEAPON), Util.castOrDefault(Integer.class, values.get("attackModifier"), 0), Util.castOrDefault(CharSequence.class, values.get("params"), "").toString(), format(descFormat, values), damages);
	}
}
