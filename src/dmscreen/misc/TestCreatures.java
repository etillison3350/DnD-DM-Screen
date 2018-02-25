package dmscreen.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dmscreen.LegendaryAction;
import dmscreen.data.base.Ability;
import dmscreen.data.base.DamageType;
import dmscreen.data.base.DiceRoll;
import dmscreen.data.base.Size;
import dmscreen.data.base.Skill;
import dmscreen.data.creature.Alignment;
import dmscreen.data.creature.Creature;
import dmscreen.data.creature.CreatureType;
import dmscreen.data.creature.VisionType;
import dmscreen.data.creature.feature.Action;
import dmscreen.data.creature.feature.Attack;
import dmscreen.data.creature.feature.Feature;
import dmscreen.data.creature.feature.InnateSpellcasting;

public class TestCreatures {

	private TestCreatures() {}

	public static final Creature vampire, drow;

	static {
		vampire = new Creature();
		vampire.name = "Vampire";
		vampire.shortName = "the vampire";
		vampire.size = Size.MEDIUM;
		vampire.type = CreatureType.UNDEAD;
		vampire.subtype = "shapechanger";
		vampire.alignment = Alignment.LAWFUL_EVIL;
		vampire.ac = 16;
		vampire.armorNote = "natural armor";
		vampire.hitDice = new DiceRoll(17, 8, 68);
		vampire.speed = 30;
		vampire.abilityScores.put(Ability.STRENGTH, 18);
		vampire.abilityScores.put(Ability.DEXTERITY, 18);
		vampire.abilityScores.put(Ability.CONSTITUTION, 18);
		vampire.abilityScores.put(Ability.INTELLIGENCE, 17);
		vampire.abilityScores.put(Ability.WISDOM, 15);
		vampire.abilityScores.put(Ability.CHARISMA, 18);
		vampire.savingThrows.put(Ability.DEXTERITY, 9);
		vampire.savingThrows.put(Ability.WISDOM, 7);
		vampire.savingThrows.put(Ability.CHARISMA, 9);
		vampire.skills.put(Skill.PERCEPTION, 7);
		vampire.skills.put(Skill.STEALTH, 9);
		vampire.resistances.get(null).add(DamageType.NECROTIC);
		vampire.resistances.put("nonmagical weapons", Stream.of(DamageType.BLUDGEONING, DamageType.PIERCING, DamageType.SLASHING).collect(Collectors.toCollection(HashSet::new)));
		vampire.senses.put(VisionType.DARKVISION, 120);
		vampire.languages.add("the languages it knew in life");
		vampire.challengeRating = 13;
		vampire.features = Arrays.asList(new Feature("Legendary Resistance", "3/Day", "If the vampire fails a saving throw, it can choose to succeed instead."), new Feature("Regeneration", "The vampire regains 20 hit points at the start of its turn if it has at least 1 hit point and isn't in sunlight or running water. If the vampire takes radiant damage or damage from holy water, this trait doesn't function at the start of the vampire's next turn."), new Feature("Spell Storing",
				"A spellcaster who wears the shield guardian's amulet can cause the guardian to store one spell of 4th level or lower. To do so, the wearer must cast the spell on the guardian. The spell has no effect but is stored within the guardian. When commanded to do so by the wearer or when a situation arises that was predefined by the spellcaster, the guardian casts the stored spell with any parameters set by the original caster, requiring no components. When the spell is cast or a new spell is stored, any previously stored spell is lost."));
		vampire.actions = Arrays.asList(new Action("Multiattack", "Vampire Form Only", "The vampire makes two attacks, only one of which can be a bite attack."),
				new Attack("Bite", "Bat or Vampire Form Only", Attack.Type.MELEE_WEAPON, 9, "range 5 ft., one willing creature, or a creature that is grappled by the vampire, incapcitated, or restrained",
						". The target's hit point maximum is reduced by an amount equal to the necrotic damage taken, and the vampire regains hit points equal to that amount. The reduction lasts until the target finishes a long rest. The target dies if this effect reduces its hit point maximum to 0. A humanoid slain in this way and then buried in the ground rises the following night as a vampire spawn under the vampire's control.", new Attack.Damage(new DiceRoll(1, 6, 4), DamageType.BLUDGEONING),
						new Attack.Damage(new DiceRoll(3, 6), DamageType.NECROTIC)));
		vampire.legendaryActions = Arrays.asList(new LegendaryAction("Move", "The vampire moves up to half its speed without provoking opportunity attacks."), new LegendaryAction("Unarmed Strike", "The vampire makes one unarmed strike."), new LegendaryAction("Bite", 2, "The vampire makes one bite attack."));

		drow = new Creature();
		drow.name = "Drow Elite Warrior";
		drow.shortName = "the drow";
		drow.size = Size.MEDIUM;
		drow.type = CreatureType.HUMANOID;
		drow.subtype = "elf";
		drow.alignment = Alignment.NEUTRAL_EVIL;
		drow.ac = 18;
		drow.armorNote = "studded leather, shield";
		drow.hitDice = new DiceRoll(11, 8, 22);
		drow.speed = 30;
		drow.abilityScores.put(Ability.STRENGTH, 13);
		drow.abilityScores.put(Ability.DEXTERITY, 18);
		drow.abilityScores.put(Ability.CONSTITUTION, 14);
		drow.abilityScores.put(Ability.INTELLIGENCE, 11);
		drow.abilityScores.put(Ability.WISDOM, 13);
		drow.abilityScores.put(Ability.CHARISMA, 12);
		drow.savingThrows.put(Ability.DEXTERITY, 7);
		drow.savingThrows.put(Ability.CONSTITUTION, 5);
		drow.savingThrows.put(Ability.WISDOM, 4);
		drow.skills.put(Skill.PERCEPTION, 4);
		drow.skills.put(Skill.STEALTH, 10);
		drow.senses.put(VisionType.DARKVISION, 120);
		drow.languages.add("Elvish");
		drow.languages.add("Undercommon");
		drow.challengeRating = 5;

		final List<Feature> features = new ArrayList<>();
		features.add(new Feature("Fey Ancestry", "The drow has advantage on saving throws against being charmed, and magic can't put the drow to sleep."));

		final Map<String, Map<String, String>> spells = new LinkedHashMap<>();
		final Map<String, String> atWill = new HashMap<>();
		atWill.put("dancing lights", "");
		spells.put("At will", atWill);

		final Map<String, String> onePerDay = new HashMap<>();
		onePerDay.put("darkness", "");
		onePerDay.put("faerie fire", "");
		onePerDay.put("levitate", "self only");
		spells.put("1/day each", onePerDay);

		features.add(new InnateSpellcasting("", "the drow", Ability.CHARISMA, 12, "", spells));
		features.add(new Feature("Sunlight Sensitivity", "While in sunlight, the drow has disadvantage on attack rolls, as well as on Wisdom (Perception) checks that rely on sight."));
		drow.features = features;

		drow.actions = Arrays.asList(new Action("Multiattack", "The drow makes two shortsword attacks."), new Attack("Shortsword", Attack.Type.MELEE_WEAPON, 7, "range 5 ft., one target", "", new Attack.Damage(new DiceRoll(1, 6, 4), DamageType.PIERCING), new Attack.Damage(new DiceRoll(3, 6), DamageType.POISON)),
				new Attack("Hand Crossbow", Attack.Type.RANGED_WEAPON, 7, "range 30/120 ft., one target", ", and the target must succeed on a DC 13 Constitution saving throw or be poisoned for 1 hour. If the saving throw fails by 5 or more, the target is also unconscious while poisoned in this way. The target wakes up if it takes damage or if another creature takes an action to shake it awake.", new Attack.Damage(new DiceRoll(1, 6, 4), DamageType.PIERCING)));
		drow.reactions = Arrays.asList(new Action("Parry", "The drow adds 3 to its AC against one melee attack that would hit it. To do so, the drow must see the attacker and be wielding a melee weapon."));

	}

}
