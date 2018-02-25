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
import dmscreen.data.creature.feature.Spellcasting;

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
		drow.name = "Drow Priestess of Lolth";
		drow.shortName = "the drow";
		drow.size = Size.MEDIUM;
		drow.type = CreatureType.HUMANOID;
		drow.subtype = "elf";
		drow.alignment = Alignment.NEUTRAL_EVIL;
		drow.ac = 18;
		drow.armorNote = "scale mail";
		drow.hitDice = new DiceRoll(13, 8, 13);
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
		drow.challengeRating = 8;

		final List<Feature> features = new ArrayList<>();
		features.add(new Feature("Fey Ancestry", "The drow has advantage on saving throws against being charmed, and magic can't put the drow to sleep."));

		final Map<String, Map<String, String>> innateSpells = new LinkedHashMap<>();
		final Map<String, String> atWill = new HashMap<>();
		atWill.put("dancing lights", "");
		innateSpells.put("At will", atWill);

		final Map<String, String> onePerDay = new HashMap<>();
		onePerDay.put("darkness", "");
		onePerDay.put("faerie fire", "");
		onePerDay.put("levitate", "self only");
		innateSpells.put("1/day each", onePerDay);

		features.add(new InnateSpellcasting("", "the drow", Ability.CHARISMA, 12, Integer.MIN_VALUE, "she", "", innateSpells));

		final Map<String, Map<String, String>> spells = new LinkedHashMap<>();

		final Map<String, String> cantrips = new HashMap<>();
		Stream.of("guidance", "poison spray", "resistance", "spare the dying", "thaumaturgy").forEach(s -> cantrips.put(s, ""));
		spells.put("Cantrips (at will)", cantrips);

		final Map<String, String> level1 = new HashMap<>();
		Stream.of("animal friendship", "cure wounds", "detect poison and disease", "ray of sickness").forEach(s -> level1.put(s, ""));
		spells.put("Level 1 (4 slots)", level1);

		final Map<String, String> level2 = new HashMap<>();
		Stream.of("lesser restoration", "protection from poison", "web").forEach(s -> level2.put(s, ""));
		spells.put("Level 2 (3 slots)", level2);

		final Map<String, String> level3 = new HashMap<>();
		level3.put("dispel magic", "");
		level3.put("conjure animals", "2 giant spiders");
		spells.put("Level 3 (3 slots)", level3);

		final Map<String, String> level4 = new HashMap<>();
		Stream.of("divination", "freedom of movement").forEach(s -> level4.put(s, ""));
		spells.put("Level 4 (3 slots)", level4);

		final Map<String, String> level5 = new HashMap<>();
		Stream.of("insect plague", "mass cure wounds").forEach(s -> level5.put(s, ""));
		spells.put("Level 5 (2 slots)", level5);

		features.add(new Spellcasting("", drow.shortName, 10, Ability.CHARISMA, 14, "her", 6, "", "cleric", "", spells));

		features.add(new Feature("Sunlight Sensitivity", "While in sunlight, the drow has disadvantage on attack rolls, as well as on Wisdom (Perception) checks that rely on sight."));
		drow.features = features;

		drow.actions = Arrays.asList(new Action("Multiattack", "The drow makes two scourge attacks."), new Attack("Scourge", Attack.Type.MELEE_WEAPON, 5, "range 5 ft., one target", "", new Attack.Damage(new DiceRoll(1, 6, 2), DamageType.PIERCING), new Attack.Damage(new DiceRoll(5, 6), DamageType.POISON)),
				new Action("Summon Demon", "1/Day", "The drow attempts to magically summon a yochlol with a 30 percent chance of success. If the attempt fails, the drow takes 5 (ldl0) psychic damage. Otherwise, the summoned demon appears in an unoccupied space within 60 feet of its summoner, acts as an ally of its summoner, and can't summon other demons. It remains for 10 minutes, until it or its summoner dies, or until its summoner dismisses it as an action."));
	}

}
