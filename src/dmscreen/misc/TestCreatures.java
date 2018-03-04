package dmscreen.misc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dmscreen.data.Data;
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
import dmscreen.data.creature.feature.LegendaryAction;
import dmscreen.data.creature.feature.Spellcasting;
import dmscreen.data.creature.feature.Subfeatures;

public class TestCreatures {

	private TestCreatures() {}

	public static void main(final String[] args) {
		try {
			final Path path = Paths.get("resources/source/creatures.json");
			if (Files.exists(path)) return;

			Files.write(path, Data.GSON.toJson(Arrays.asList(drow, vampire)).getBytes());
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

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

		final Map<String, String> weaknesses = new TreeMap<>();
		weaknesses.put("Forbiddance", "The vampire can't enter a residence without an invitation from one of the occupants.");
		weaknesses.put("Harmed by Running Water", "The vampire takes 20 acid damage if it ends its turn in running water.");
		weaknesses.put("Stake to the Heart", "If a piercing weapon made of wood is driven into the vampire's heart while the vampire is incapacitated in its resting place, the vampire is paralyzed until the stake is removed.");
		weaknesses.put("Sunlight Hypersensitivity", "The vampire takes 20 radiant damage when it starts its turn in sunlight. While in sunlight, it has disadvantage on attack rolls and ability checks.");

		vampire.features = Arrays.asList(new Feature("Shapechanger",
				"If the vampire isn't in sunlight or running water, it can use its action to polymorph in to a Tiny bat or a Medium cloud of mist, or back into its true form.\n   While in bat form, the vampire can't speak, its walking speed is 5 feet, and it has a flying speed of 30 feet. Its statistics, other than its size and speed, are unchanged. Anything it is wearing transforms with it, but nothing it is carrying does. It reverts to its true form if it dies.\n   While in mist form, the vampire can't take any actions, speak, or manipulate objects. It is weightless, has a flying speed of 20 feet, can hover, and can enter a hostile creature's space and stop there. In addition, if air can pass through a space, the mist can do so without squeezing, and it can't pass through water. It has advantage on Strength, Dexterity, and Constitution saving throws, and it is immune to all nonmagical damage, except the damage it takes from sunlight."), //
				new Feature("Legendary Resistance", "3/Day", "If the vampire fails a saving throw, it can choose to succeed instead."), //
				new Feature("Misty Escape",
						"When it drops to 0 hit points outside its resting place, the vampire transforms into a cloud of mist (as in the Shapechanger trait) instead of falling unconscious, provided that it isn't in sunlight or running water. If it can't transform, it is destroyed.\n   While it has 0 hit points in mist form, it can't revert to its vampire form, and it must reach its resting place within 2 hours or be destroyed. Once in its resting place, it reverts to its vampire form. It is then paralyzed until it regains at least 1 hit point. After spending 1 hour in its resting place with 0 hit points, it regains 1 hit point."), //
				new Feature("Regeneration", "The vampire regains 20 hit points at the start of its turn if it has at least 1 hit point and isn't in sunlight or running water. If the vampire takes radiant damage or damage from holy water, this trait doesn't function at the start of the vampire's next turn."), //
				new Feature("Spider Climb", "The vampire can climb difficult surfaces, including upside down on ceilings, without needing to make an ability check."), //
				new Subfeatures("Vampire Weaknesses", "The vampire has the following flaws:", weaknesses));
		vampire.actions = Arrays.asList(new Action("Multiattack", "Vampire Form Only", "The vampire makes two attacks, only one of which can be a bite attack."), //
				new Attack("Unarmed Strike", "Vampire Form Only", Attack.Type.MELEE_WEAPON, 9, "reach 5 ft., one creature", ". Instead of dealing damage, the vampire can grapple the target (escape DC 18).", new Attack.Damage(new DiceRoll(1, 8, 4), DamageType.BLUDGEONING)), //
				new Attack("Bite", "Bat or Vampire Form Only", Attack.Type.MELEE_WEAPON, 9, "range 5 ft., one willing creature, or a creature that is grappled by the vampire, incapcitated, or restrained",
						". The target's hit point maximum is reduced by an amount equal to the necrotic damage taken, and the vampire regains hit points equal to that amount. The reduction lasts until the target finishes a long rest. The target dies if this effect reduces its hit point maximum to 0. A humanoid slain in this way and then buried in the ground rises the following night as a vampire spawn under the vampire's control.", new Attack.Damage(new DiceRoll(1, 6, 4), DamageType.BLUDGEONING),
						new Attack.Damage(new DiceRoll(3, 6), DamageType.NECROTIC)), //
				new Action("Charm",
						"The vampire targets one humanoid it can see within 30 feet of it. If the target can see the vampire, the target must succeed on a DC 17 Wisdom saving throw against this magic or be charmed by the vampire. The charmed target regards the vampire as a trusted friend to be heeded and protected. Although the target isn't under the vampire's control, it takes the vampire's requests or actions in the most favorable way it can, and it is a willing target for the vampire's bite attack.\n   Each time the vampire or the vampire's companions do anything harmful to the target, it can repeat the saving throw, ending the effect on itself on a success. Otherwise, the effect lasts 24 hours or until the vampire is destroyed, is on a different plane of existence than the target, or takes a bonus action to end the effect."), //
				new Action("Children of the Night", "1/Day", "The vampire magically calls 2d4 swarms of bats or rats, provided that the sun isn't up. While outdoors, the vampire can call 3d6 wolves instead. The called creatures arrive in 1d4 rounds, acting as allies of the vampire and obeying its spoken commands. The beasts remain for 1 hour, until the vampire dies, or until the vampire dismisses them as a bonus action."));
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
				new Action("Summon Demon", "1/Day", "The drow attempts to magically summon a yochlol with a 30 percent chance of success. If the attempt fails, the drow takes 5 (1d10) psychic damage. Otherwise, the summoned demon appears in an unoccupied space within 60 feet of its summoner, acts as an ally of its summoner, and can't summon other demons. It remains for 10 minutes, until it or its summoner dies, or until its summoner dismisses it as an action."));
	}

}
