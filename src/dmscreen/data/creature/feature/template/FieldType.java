package dmscreen.data.creature.feature.template;

import java.util.List;
import java.util.Map;

import dmscreen.data.base.Ability;
import dmscreen.data.base.DamageType;
import dmscreen.data.base.DiceRoll;
import dmscreen.data.base.Size;
import dmscreen.data.base.Skill;
import dmscreen.data.creature.Alignment;
import dmscreen.data.creature.Condition;
import dmscreen.data.creature.CreatureType;
import dmscreen.data.creature.MovementType;
import dmscreen.data.creature.VisionType;
import dmscreen.data.creature.feature.Attack;
import dmscreen.data.spell.SpellType;

public enum FieldType {

	STRING(String.class),
	INTEGER(Integer.class),
	BOOLEAN(Boolean.class),
	ABILITY(Ability.class),
	DAMAGE_TYPE(DamageType.class),
	ATTACK_TYPE(Attack.Type.class),
	SIZE(Size.class),
	SKILL(Skill.class),
	ALIGNMENT(Alignment.class),
	CONDITION(Condition.class),
	CREATURE_TYPE(CreatureType.class),
	MOVEMENT_TYPE(MovementType.class),
	VISION_TYPE(VisionType.class),
	SPELL_TYPE(SpellType.class),
	DICE_ROLL(DiceRoll.class),
	LIST(List.class),
	MAP(Map.class);

	public final Class<?> clazz;

	private FieldType(final Class<?> clazz) {
		this.clazz = clazz;
	}

}
