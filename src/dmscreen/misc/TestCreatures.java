package dmscreen.misc;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dmscreen.data.Data;
import dmscreen.data.base.Ability;
import dmscreen.data.base.DamageType;
import dmscreen.data.base.DiceRoll;
import dmscreen.data.base.Size;
import dmscreen.data.base.Skill;
import dmscreen.data.creature.Alignment;
import dmscreen.data.creature.Condition;
import dmscreen.data.creature.Creature;
import dmscreen.data.creature.CreatureType;
import dmscreen.data.creature.MovementType;
import dmscreen.data.creature.VisionType;
import dmscreen.data.creature.feature.Action;
import dmscreen.data.creature.feature.Attack;
import dmscreen.data.creature.feature.Attack.Type;
import dmscreen.data.creature.feature.Feature;
import dmscreen.data.creature.feature.InnateSpellcasting;
import dmscreen.data.creature.feature.LegendaryAction;
import dmscreen.data.creature.feature.Spellcasting;
import dmscreen.data.creature.feature.Subfeatures;

public class TestCreatures {

	private TestCreatures() {}

	public static void main(final String[] args) {
		try {
			Files.createDirectories(Paths.get("resources/source/"));

			final List<Object> toJson = new ArrayList<>();
			for (final Field field : TestCreatures.class.getDeclaredFields()) {
				if (Modifier.isStatic(field.getModifiers())) {
					try {
						toJson.add(field.get(null));
					} catch (IllegalArgumentException | IllegalAccessException e) {}
				}
			}

			Files.write(Paths.get("resources/source/creatures.json"), Data.GSON.toJson(toJson).getBytes());
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public static final Creature vampire, drow, demilich, zombie, ogreZombie, otyugh, grick, grell, gibberingMouther, orc, gnoll, troll, wight, skeleton, behir, peryton, gelatinousCube, efreeti;

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

		demilich = new Creature();
		demilich.name = "Acererak";
		demilich.shortName = "the demilich";
		demilich.size = Size.TINY;
		demilich.type = CreatureType.UNDEAD;
		demilich.alignment = Alignment.NEUTRAL_EVIL;
		demilich.ac = 20;
		demilich.armorNote = "natural armor";
		demilich.hitDice = new DiceRoll(20, 4, 0, 80);
		demilich.speed = 0;
		demilich.speeds.put(MovementType.FLY, 30);
		demilich.abilityScores.put(Ability.STRENGTH, 1);
		demilich.abilityScores.put(Ability.DEXTERITY, 20);
		demilich.abilityScores.put(Ability.CONSTITUTION, 10);
		demilich.abilityScores.put(Ability.INTELLIGENCE, 20);
		demilich.abilityScores.put(Ability.WISDOM, 17);
		demilich.abilityScores.put(Ability.CHARISMA, 20);
		demilich.savingThrows.put(Ability.CONSTITUTION, 6);
		demilich.savingThrows.put(Ability.INTELLIGENCE, 11);
		demilich.savingThrows.put(Ability.WISDOM, 9);
		demilich.savingThrows.put(Ability.CHARISMA, 11);
		demilich.resistances.put("magic weapons", Stream.of(DamageType.BLUDGEONING, DamageType.SLASHING, DamageType.PIERCING).collect(Collectors.toCollection(TreeSet::new)));
		demilich.immunities.put(null, Stream.of(DamageType.NECROTIC, DamageType.POISON, DamageType.PSYCHIC).collect(Collectors.toCollection(TreeSet::new)));
		demilich.immunities.put("nonmagical weapons", Stream.of(DamageType.BLUDGEONING, DamageType.SLASHING, DamageType.PIERCING).collect(Collectors.toCollection(TreeSet::new)));
		Stream.of(Condition.CHARMED, Condition.DEAFENED, Condition.EXHAUSTION, Condition.FRIGHTENED, Condition.PARALYZED, Condition.POISONED, Condition.PRONE, Condition.STUNNED).forEach(demilich.conditionImmunities::add);
		demilich.senses.put(VisionType.TRUESIGHT, 120);
		demilich.challengeRating = 21;
		demilich.features = Arrays.asList(new Feature("Avoidance", "If the demilich is subjected to an effect that allows it to make a saving throw to take only half damage, it instead takes no damage if it succeeds on the saving throw, and only half damage if it fails."), //
				new Feature("Legendary Resistance", "3/Day", "If the demilich fails a saving throw, it can choose to succeed instead."), //
				new Feature("Turn Immunity", "The demilich is immune to effects that turn undead."));
		demilich.actions = Arrays.asList(new Action("Howl", "Recharge 5-6", "The demilich emits a bloodcurdling howl. Each creature within 30 feet of the demilich that can hear the howl must succeed on a DC 15 Constitution saving throw or drop to 0 hit points. On a successful save, the creature is frightened until the end of its next turn."), //
				new Action("Life Drain", "The demilich targets up to three creatures that it can see within 10 feet of it. Each target must succeed on a DC 19 Constitution saving throw or take 21 (6d6) necrotic damage, and the demilich regains hit points equal to the total damage dealt to all targets."), //
				new Action("Trap Soul",
						"The demilich targets one creature that it can see within 30 feet of it. The target must make a DC 19 Charisma saving throw. On a failed save, the target's soul is magically trapped inside one of the demilich's gems. While the soul is trapped, the target's body and all the equipment it is carrying cease to exist. On a successful save, the target takes 24 (7d6) necrotic damage, and if this damage reduces the target to 0 hit points, its soul is trapped as if it failed the saving throw. A soul trapped in a gem for 24 hours is devoured and ceases to exist.\nlfthe demilich drops to 0 hit points, it is destroyed and turns to powder, leaving behind its gems. Crushing a gem releases any soul trapped within, at which point the target's body re-forms in an unoccupied space nearest to the gem and in the same state as when it was trapped."));
		demilich.legendaryActions = Arrays.asList(new LegendaryAction("Flight", "The demilich flies up to half its movement speed."), //
				new LegendaryAction("Cloud of Dust", "The demilich magically swirls its dusty remains. Each creature within 10 feet of the demilich, including around a corner, must succeed on a DC 15 Constitution saving throw or be blinded until the end of the demilich's next turn. A creature that succeeds on the saving throw is immune to this effect until the end of the demilich's next turn."), //
				new LegendaryAction("Energy Drain", 2, "Each creature with in 30 feet of the demilich must make a DC 15 Constitution saving throw. On a failed save, the creature's hit point maximum is magically reduced by 10 (3d6). If a creature's hit point maximum is reduced to 0 by this effect, the creature dies. A creature's hit point maximum can be restored with the greater restoration spell or similar magic."), //
				new LegendaryAction("Vile Curse", 3, "The demilich targets one creature it can see within 30 feet of it. The target must succeed on a DC 15 Wisdom saving throw or be magically cursed. Until the curse ends, the target has disadvantage on attack rolls and saving throws. The target can repeat the saving throw at the end of each of its turns, ending the curse on a success."));

		zombie = new Creature();
		zombie.name = "Zombie";
		zombie.shortName = "The zombie";
		zombie.size = Size.MEDIUM;
		zombie.type = CreatureType.UNDEAD;
		zombie.alignment = Alignment.NEUTRAL_EVIL;
		zombie.ac = 8;
		zombie.hitDice = new DiceRoll(3, 8, 9);
		zombie.speed = 20;
		zombie.abilityScores.put(Ability.STRENGTH, 13);
		zombie.abilityScores.put(Ability.DEXTERITY, 6);
		zombie.abilityScores.put(Ability.CONSTITUTION, 16);
		zombie.abilityScores.put(Ability.INTELLIGENCE, 3);
		zombie.abilityScores.put(Ability.WISDOM, 6);
		zombie.abilityScores.put(Ability.CHARISMA, 5);
		zombie.savingThrows.put(Ability.WISDOM, 0);
		zombie.immunities.put(null, new HashSet<>(Arrays.asList(DamageType.POISON)));
		zombie.conditionImmunities.add(Condition.POISONED);
		zombie.senses.put(VisionType.DARKVISION, 60);
		zombie.languages.add("understands the languages it knew in life but can't speak");
		zombie.challengeRating = -2;
		zombie.features = Arrays.asList(new Feature("Undead Fortitude", "If damage reduces the zombie to 0 hit points, it must make a Constitution saving throw with a DC of 5 + the damage taken, unless the damage is radiant or from a critcal hit. On a success, the zombie drops to 1 hit point instead."));
		zombie.actions = Arrays.asList(new Attack("Slam", Type.MELEE_WEAPON, 3, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(1, 6, 1), DamageType.BLUDGEONING)));

		ogreZombie = new Creature();
		ogreZombie.name = "Ogre Zombie";
		ogreZombie.shortName = "The zombie";
		ogreZombie.size = Size.LARGE;
		ogreZombie.type = CreatureType.UNDEAD;
		ogreZombie.alignment = Alignment.NEUTRAL_EVIL;
		ogreZombie.ac = 8;
		ogreZombie.hitDice = new DiceRoll(9, 10, 36);
		ogreZombie.speed = 30;
		ogreZombie.abilityScores.put(Ability.STRENGTH, 19);
		ogreZombie.abilityScores.put(Ability.DEXTERITY, 6);
		ogreZombie.abilityScores.put(Ability.CONSTITUTION, 18);
		ogreZombie.abilityScores.put(Ability.INTELLIGENCE, 3);
		ogreZombie.abilityScores.put(Ability.WISDOM, 6);
		ogreZombie.abilityScores.put(Ability.CHARISMA, 5);
		ogreZombie.savingThrows.put(Ability.WISDOM, 0);
		ogreZombie.immunities.put(null, new HashSet<>(Arrays.asList(DamageType.POISON)));
		ogreZombie.conditionImmunities.add(Condition.POISONED);
		ogreZombie.senses.put(VisionType.DARKVISION, 60);
		ogreZombie.languages.add("understands Common and Giant but can't speak");
		ogreZombie.challengeRating = 2;
		ogreZombie.features = Arrays.asList(new Feature("Undead Fortitude", "If damage reduces the zombie to 0 hit points, it must make a Constitution saving throw with a DC of 5 + the damage taken, unless the damage is radiant or from a critcal hit. On a success, the zombie drops to 1 hit point instead."));
		ogreZombie.actions = Arrays.asList(new Attack("Morningstar", Type.MELEE_WEAPON, 6, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(2, 8, 4), DamageType.BLUDGEONING)));

		otyugh = new Creature();
		otyugh.name = "Otyugh";
		otyugh.shortName = "The otyugh";
		otyugh.size = Size.LARGE;
		otyugh.type = CreatureType.ABERRATION;
		otyugh.alignment = Alignment.NEUTRAL;
		otyugh.ac = 14;
		otyugh.armorNote = "natural armor";
		otyugh.hitDice = new DiceRoll(12, 10, 48);
		otyugh.speed = 30;
		otyugh.abilityScores.put(Ability.STRENGTH, 16);
		otyugh.abilityScores.put(Ability.DEXTERITY, 11);
		otyugh.abilityScores.put(Ability.CONSTITUTION, 19);
		otyugh.abilityScores.put(Ability.INTELLIGENCE, 6);
		otyugh.abilityScores.put(Ability.WISDOM, 13);
		otyugh.abilityScores.put(Ability.CHARISMA, 6);
		otyugh.savingThrows.put(Ability.CONSTITUTION, 7);
		otyugh.senses.put(VisionType.DARKVISION, 120);
		otyugh.languages.add("Otyugh");
		otyugh.challengeRating = 5;
		otyugh.features = Arrays.asList(new Feature("Limited Telepathy", "The otyugh can magically transmit simple messages and images to any creature within 120 feet of it that can understand a language. This form of telepathy doesn't allow the receiving creature to telepathically respond."));
		otyugh.actions = Arrays.asList(new Action("Multiattack", "The otyugh makes three attacks: one with its bite, and two with its tentacles"), //
				new Attack("Bite", Type.MELEE_WEAPON, 6, "reach 5 ft., one target",
						". If the target is a creature, it must succeed on a DC 15 Constitution saving throw against disease or become poisoned until the disease is cured. Every 24 hours that elapse, the target must repeat the saving throw, reducing its hit point maximum by 5 (1d10) on a failure. The disease is cured on a success. The target dies if the disease reduces its hit point maximum to 0. This reduction to the target's hit point maximum lasts until the disease is cured.",
						new Attack.Damage(new DiceRoll(2, 8, 3), DamageType.PIERCING)), //
				new Attack("Tentacle", Type.MELEE_WEAPON, 6, "reach 10 ft., one target", ". If the target is Medium or smaller, it is grappled (escape DC 13) and restrained until the grapple ends. The otyugh has two tentacles, each of which can grapple one target.", new Attack.Damage(new DiceRoll(1, 8, 3), DamageType.BLUDGEONING), new Attack.Damage(new DiceRoll(1, 8), DamageType.PIERCING)), //
				new Action("Tentacle Slam", "The otyugh slams creatures grappled by it into each other or a solid surface. Each creature must succeed on a DC 14 Strength saving throw or take 10 (2d6 + 3) bludgeoning damage and be stunned until the end of the otyugh's next turn. On a successful save, the target takes half the bludgeoning damage and isn't stunned."));

		grick = new Creature();
		grick.name = "Grick";
		grick.shortName = "The grick";
		grick.size = Size.MEDIUM;
		grick.type = CreatureType.MONSTROSITY;
		grick.alignment = Alignment.NEUTRAL;
		grick.ac = 14;
		grick.armorNote = "natural armor";
		grick.hitDice = new DiceRoll(6, 8);
		grick.speed = 30;
		grick.speeds.put(MovementType.CLIMB, 30);
		grick.abilityScores.put(Ability.STRENGTH, 14);
		grick.abilityScores.put(Ability.DEXTERITY, 14);
		grick.abilityScores.put(Ability.CONSTITUTION, 11);
		grick.abilityScores.put(Ability.INTELLIGENCE, 3);
		grick.abilityScores.put(Ability.WISDOM, 14);
		grick.abilityScores.put(Ability.CHARISMA, 5);
		grick.resistances.put("from nonmagical weapons", new HashSet<>(Arrays.asList(DamageType.BLUDGEONING, DamageType.PIERCING, DamageType.SLASHING)));
		grick.senses.put(VisionType.DARKVISION, 60);
		grick.challengeRating = 2;
		grick.features = Arrays.asList(new Feature("Stone Camouflage", "The grick has advantage on Dexterity (Stealth) checks made to hide in rocky terrain."));
		grick.actions = Arrays.asList(new Action("Multiattack", "The grick makes one attack with its tentacles. If that attack hits, the grick can make one beak attack against the same target."), //
				new Attack("Tentacles", Type.MELEE_WEAPON, 4, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(2, 6, 2), DamageType.SLASHING)), //
				new Attack("Beak", Type.MELEE_WEAPON, 4, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(1, 6, 2), DamageType.PIERCING)));

		grell = new Creature();
		grell.name = "Grell";
		grell.shortName = "The grell";
		grell.size = Size.MEDIUM;
		grell.type = CreatureType.ABERRATION;
		grell.alignment = Alignment.NEUTRAL_EVIL;
		grell.ac = 12;
		grell.hitDice = new DiceRoll(10, 8, 10);
		grell.speed = 10;
		grell.speeds.put(MovementType.FLY, 30);
		grell.abilityScores.put(Ability.STRENGTH, 15);
		grell.abilityScores.put(Ability.DEXTERITY, 14);
		grell.abilityScores.put(Ability.CONSTITUTION, 13);
		grell.abilityScores.put(Ability.INTELLIGENCE, 12);
		grell.abilityScores.put(Ability.WISDOM, 11);
		grell.abilityScores.put(Ability.CHARISMA, 9);
		grell.immunities.put(null, new HashSet<>(Arrays.asList(DamageType.LIGHTNING)));
		grell.conditionImmunities.add(Condition.BLINDED);
		grell.conditionImmunities.add(Condition.PRONE);
		grell.senses.put(VisionType.BLINDSIGHT, 60);
		grell.languages.add("Grell");
		grell.challengeRating = 3;
		grell.actions = Arrays.asList(new Action("Multiattack", "The grell makes two attacks: one with its tentacles, and one with its beak."), //
				new Attack("Tentacles", Type.MELEE_WEAPON, 4, "reach 5 ft., one target",
						", and the target must succeed on a DC 11 Constitution saving throw or be poisoned for 1 minute. The poisoned target is paralyzed, and it can repeat the saving throw at the end of each of its turns, ending the effect on itself on a success.\n    The target is also grappled (escape DC 15). If the target is Medium or smaller, it is also restrained until this grapple ends. While grappling the target, the grell has advantage on attack rolls against it and can't use this attack against other targets. When the grell moves, any Medium or smaller target it is grappling moves with it.",
						new Attack.Damage(new DiceRoll(1, 10, 2), DamageType.PIERCING)), //
				new Attack("Beak", Type.MELEE_WEAPON, 4, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(2, 4, 2), DamageType.PIERCING)));

		gibberingMouther = new Creature();
		gibberingMouther.name = "Gibbering Mouther";
		gibberingMouther.shortName = "The gibbering mouther";
		gibberingMouther.size = Size.MEDIUM;
		gibberingMouther.type = CreatureType.ABERRATION;
		gibberingMouther.alignment = Alignment.NEUTRAL;
		gibberingMouther.ac = 9;
		gibberingMouther.hitDice = new DiceRoll(9, 8, 27);
		gibberingMouther.speed = 10;
		gibberingMouther.speeds.put(MovementType.SWIM, 10);
		gibberingMouther.abilityScores.put(Ability.STRENGTH, 10);
		gibberingMouther.abilityScores.put(Ability.DEXTERITY, 8);
		gibberingMouther.abilityScores.put(Ability.CONSTITUTION, 16);
		gibberingMouther.abilityScores.put(Ability.INTELLIGENCE, 3);
		gibberingMouther.abilityScores.put(Ability.WISDOM, 10);
		gibberingMouther.abilityScores.put(Ability.CHARISMA, 6);
		gibberingMouther.conditionImmunities.add(Condition.PRONE);
		gibberingMouther.senses.put(VisionType.DARKVISION, 60);
		gibberingMouther.challengeRating = 2;
		gibberingMouther.features = Arrays.asList(new Feature("Aberrant Ground", "The ground in a 10-foot radius around the mouther is doughlike difficult terrain. Each creature that starts its turn in that area must succeed on a DC 10 Strength saving throw or have its speed reduced to 0 until the start of its next turn."), //
				new Feature("Gibbering",
						"The mouther babbles incoherently while it can see any creature and isn't incapacitated. Each creature that starts its turn within 20 feet of the mouther and can hear the gibbering must succeed on a DC 10 Wisdom saving throw. On a failure, the creature can't take reactions until the start of its next turn and rolls a d8 to determine what it does du~ing its turn. On a 1 to 4, the creature does nothing. On a 5 or 6, the creature takes no action or bonus action and uses all its movement to move in a randomly determined direction. On a 7 or 8, the creature makes a melee attack against a randomly determined creature within its reach or does nothing if it can't make such an attack."));
		gibberingMouther.actions = Arrays.asList(new Action("Multiattack", "The gibbering mouther makes one bite attack, and if it can, uses its Blinding Spittle."), //
				new Attack("Bites", Type.MELEE_WEAPON, 2, "reach 5 ft., one creature", ". If the target is Medium or smaller, it must succeed on a DC 10 Strength saving throw or be knocked prone. If the target is killed by this damage, it is absorbed into the mouther.", new Attack.Damage(new DiceRoll(5, 6), DamageType.PIERCING)), //
				new Action("Blinding Spittle", "Recharge 5-6", "The mouther spits a chemical glob at a point it can see within 15 feet of it. The glob explodes in a blinding flash of light on impact. Each creature within 5 feet of the flash must succeed on a DC 13 Dexterity saving throw or be blinded until the end of the mouther's next turn."));

		orc = new Creature();
		orc.name = "Orc";
		orc.shortName = "The orc";
		orc.size = Size.MEDIUM;
		orc.type = CreatureType.HUMANOID;
		orc.subtype = "orc";
		orc.alignment = Alignment.CHAOTIC_EVIL;
		orc.ac = 13;
		orc.armorNote = "hide armor";
		orc.hitDice = new DiceRoll(2, 8, 6);
		orc.speed = 30;
		orc.abilityScores.put(Ability.STRENGTH, 16);
		orc.abilityScores.put(Ability.DEXTERITY, 12);
		orc.abilityScores.put(Ability.CONSTITUTION, 16);
		orc.abilityScores.put(Ability.INTELLIGENCE, 7);
		orc.abilityScores.put(Ability.WISDOM, 11);
		orc.abilityScores.put(Ability.CHARISMA, 10);
		orc.skills.put(Skill.INTIMIDATION, 2);
		orc.senses.put(VisionType.DARKVISION, 60);
		orc.languages.add("Common");
		orc.languages.add("Orc");
		orc.challengeRating = -1;
		orc.features = Arrays.asList(new Feature("Agressive", "As a bonus action, the orc can move up to its speed toward a hostile creature it can see."));
		orc.actions = Arrays.asList(new Attack("Greataxe", Type.MELEE_WEAPON, 5, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(1, 12, 3), DamageType.SLASHING)), //
				new Attack("Javelin", Type.MELEE_OR_RANGED_WEAPON, 5, "reach 5 ft. or range 30/120 ft., one target", null, new Attack.Damage(new DiceRoll(1, 6, 3), DamageType.PIERCING)));

		gnoll = new Creature();
		gnoll.name = "Gnoll";
		gnoll.shortName = "The gnoll";
		gnoll.size = Size.MEDIUM;
		gnoll.type = CreatureType.HUMANOID;
		gnoll.subtype = "gnoll";
		gnoll.alignment = Alignment.CHAOTIC_EVIL;
		gnoll.ac = 15;
		gnoll.armorNote = "hide armor, shield";
		gnoll.hitDice = new DiceRoll(5, 8);
		gnoll.speed = 30;
		gnoll.abilityScores.put(Ability.STRENGTH, 14);
		gnoll.abilityScores.put(Ability.DEXTERITY, 12);
		gnoll.abilityScores.put(Ability.CONSTITUTION, 11);
		gnoll.abilityScores.put(Ability.INTELLIGENCE, 6);
		gnoll.abilityScores.put(Ability.WISDOM, 10);
		gnoll.abilityScores.put(Ability.CHARISMA, 7);
		gnoll.senses.put(VisionType.DARKVISION, 60);
		gnoll.languages.add("Gnoll");
		gnoll.challengeRating = -1;
		gnoll.features = Arrays.asList(new Feature("Rampage", "When the gnoll reduces a creature to 0 hit points with a melee attack on its turn, the gnoll can take a bonus action to move up to half its speed and make a bite attack."));
		gnoll.actions = Arrays.asList(new Attack("Bite", Type.MELEE_WEAPON, 4, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(1, 4, 2), DamageType.PIERCING)), //
				new Attack("Spear", Type.MELEE_OR_RANGED_WEAPON, 4, "reach 5 ft. or range 20/60 ft., one target", ", or 6 (1d8 + 2) piercing damage if used with two hands to make a melee attack.", new Attack.Damage(new DiceRoll(1, 6, 2), DamageType.PIERCING)), //
				new Attack("Longbow", Type.RANGED_WEAPON, 3, "range 150/600 ft., one target", null, new Attack.Damage(new DiceRoll(1, 8, 1), DamageType.PIERCING)));

		troll = new Creature();
		troll.name = "Troll";
		troll.shortName = "The troll";
		troll.size = Size.LARGE;
		troll.type = CreatureType.GIANT;
		troll.alignment = Alignment.CHAOTIC_EVIL;
		troll.ac = 15;
		troll.armorNote = "natural armor";
		troll.hitDice = new DiceRoll(8, 10, 40);
		troll.speed = 30;
		troll.abilityScores.put(Ability.STRENGTH, 18);
		troll.abilityScores.put(Ability.DEXTERITY, 13);
		troll.abilityScores.put(Ability.CONSTITUTION, 20);
		troll.abilityScores.put(Ability.INTELLIGENCE, 7);
		troll.abilityScores.put(Ability.WISDOM, 9);
		troll.abilityScores.put(Ability.CHARISMA, 7);
		troll.skills.put(Skill.PERCEPTION, 1);
		troll.senses.put(VisionType.DARKVISION, 60);
		troll.languages.add("Giant");
		troll.challengeRating = 5;
		troll.features = Arrays.asList(new Feature("Keen Smell", "The troll has advantage on Wisdom (Perception) checks that rely on smell."), //
				new Feature("Regeneration", "The troll regains 10 hit points at the start of its turn. If the troll takes acid or fire damage, this trait doesn't function at the start of the troll's next turn. The troll dies only if it starts its turn with 0 hit points and doesn't regenerate."), //
				new Feature("Loathsome Limbs",
						"Whenever the troll takes at least 15 slashing damage at one time, roll a d20 to determine what else happens to it:\n\n1-10: Nothing happens.\n11-14: One leg is severed from the troll if it has any legs left.\n15-18: One arm is severed from the troll if it has any arms left.\n19-20: The troll is decapitated, but the troll dies only if it can't regenerate. If it dies, so does the severed head.\n\nIf the troll finishes a short or long rest without reattaching a severed limb or head, the part regrows. At that point, the severed part dies. Until then, a severed part acts on the troll's initiative and has its own action and movement. A severed part has AC 13, 10 hit points, and the troll's Regeneration trait.\n    A severed leg is unable to attack and has a speed of 5 feet.\n    A severed arm has a speed of 5 feet and can make one claw attack on its turn, with disadvantage on the attack roll unless the troll can see the arm and its target. Each time the troll loses an arm, it loses a claw attack.\n\nIf its head is severed, the troll loses its bite attack and its body is blinded unless the head can see it. The severed head has a speed of 0 feet and the troll's Keen Smell trait. It can make a bite attack, but only against a creature in its space.\nThe troll's speed is halved if it is mmissing a leg. If it loses both legs, it falls prone. If it still has both arms, it can crawl. With only one arm, it can still crawl, but its speed is halved. With no arms or legs, its speed is 0, and it can't benefit from bonuses to its speed."));
		troll.actions = Arrays.asList(new Action("Multiattack", "The troll makes three attacks: one with its bite, and two with its claws. "), //
				new Attack("Bite", Type.MELEE_WEAPON, 7, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(1, 6, 4), DamageType.PIERCING)), //
				new Attack("Claw", Type.MELEE_WEAPON, 7, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(2, 6, 4), DamageType.SLASHING)));

		wight = new Creature();
		wight.name = "Wight";
		wight.shortName = "The wight";
		wight.size = Size.MEDIUM;
		wight.type = CreatureType.UNDEAD;
		wight.alignment = Alignment.NEUTRAL_EVIL;
		wight.ac = 14;
		wight.armorNote = "studded leather";
		wight.hitDice = new DiceRoll(6, 8, 18);
		wight.speed = 30;
		wight.abilityScores.put(Ability.STRENGTH, 15);
		wight.abilityScores.put(Ability.DEXTERITY, 14);
		wight.abilityScores.put(Ability.CONSTITUTION, 16);
		wight.abilityScores.put(Ability.INTELLIGENCE, 10);
		wight.abilityScores.put(Ability.WISDOM, 13);
		wight.abilityScores.put(Ability.CHARISMA, 15);
		wight.skills.put(Skill.STEALTH, 4);
		wight.skills.put(Skill.PERCEPTION, 3);
		wight.resistances.put(null, new HashSet<>(Arrays.asList(DamageType.NECROTIC)));
		wight.resistances.put("from nonmagical weapons that aren't silvered", new HashSet<>(Arrays.asList(DamageType.BLUDGEONING, DamageType.PIERCING, DamageType.SLASHING)));
		wight.immunities.put(null, new HashSet<>(Arrays.asList(DamageType.POISON)));
		wight.conditionImmunities.add(Condition.POISONED);
		wight.conditionImmunities.add(Condition.EXHAUSTION);
		wight.languages.add("the languages it knew in life");
		wight.challengeRating = 3;
		wight.features = Arrays.asList(new Feature("Sunlight Sensitivity", "While in sunlight, the wight has disadvantage on attack rolls, as well as on Wisdom (Perception) checks that rely on sight."));
		wight.actions = Arrays.asList(new Action("Multiattack", "The wight makes two longsword attacks or two longbow attacks. It can use its life drain im place of one longsword attack."), //
				new Attack("Life Drain", Type.MELEE_WEAPON, 4, "reach 5 ft., one creature",
						". The target must succeed on a DC 13 Constitution saving throw or its hit point maximum is reduced by an amount equal to the damage taken . This reduction lasts until the target finishes a long rest. The target dies if this effect reduces its hit point maximum to 0.\n    A humanoid slain by this attack rises 24 hours later as a zombie under the wight's control, unless the humanoid is restored to life or its body is destroyed. The wight can have no more than twelve zombies under its control at one time.",
						new Attack.Damage(new DiceRoll(1, 6, 2), DamageType.NECROTIC)), //
				new Attack("Longsword", Type.MELEE_WEAPON, 4, "reach 5 ft., one target", ", or 7 (1d10 + 2) slashing damage if used with two hands.", new Attack.Damage(new DiceRoll(1, 8, 2), DamageType.SLASHING)), //
				new Attack("Longbow", Type.RANGED_WEAPON, 4, "range 150/600 ft., one target", null, new Attack.Damage(new DiceRoll(1, 8, 2), DamageType.PIERCING)));

		skeleton = new Creature();
		skeleton.name = "Skeleton";
		skeleton.shortName = "The skeleton";
		skeleton.size = Size.MEDIUM;
		skeleton.type = CreatureType.UNDEAD;
		skeleton.alignment = Alignment.LAWFUL_EVIL;
		skeleton.ac = 13;
		skeleton.armorNote = "armor scraps";
		skeleton.hitDice = new DiceRoll(2, 8, 4);
		skeleton.speed = 30;
		skeleton.abilityScores.put(Ability.STRENGTH, 10);
		skeleton.abilityScores.put(Ability.DEXTERITY, 14);
		skeleton.abilityScores.put(Ability.CONSTITUTION, 15);
		skeleton.abilityScores.put(Ability.INTELLIGENCE, 6);
		skeleton.abilityScores.put(Ability.WISDOM, 8);
		skeleton.abilityScores.put(Ability.CHARISMA, 5);
		skeleton.vulnerabilities.put(null, new HashSet<>(Arrays.asList(DamageType.BLUDGEONING)));
		skeleton.immunities.put(null, new HashSet<>(Arrays.asList(DamageType.POISON)));
		skeleton.conditionImmunities.add(Condition.POISONED);
		skeleton.conditionImmunities.add(Condition.EXHAUSTION);
		skeleton.senses.put(VisionType.DARKVISION, 60);
		skeleton.languages.add("understands all languages it knew in life but can't speak");
		skeleton.challengeRating = -2;
		skeleton.actions = Arrays.asList(new Attack("Shortsword", Type.MELEE_WEAPON, 4, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(1, 6, 2), DamageType.SLASHING)), //
				new Attack("Shortbow", Type.RANGED_WEAPON, 4, "range 80/320 ft., one target", null, new Attack.Damage(new DiceRoll(1, 6, 2), DamageType.PIERCING)));

		behir = new Creature();
		behir.name = "Behir";
		behir.shortName = "The behir";
		behir.size = Size.HUGE;
		behir.type = CreatureType.MONSTROSITY;
		behir.alignment = Alignment.NEUTRAL_EVIL;
		behir.ac = 17;
		behir.armorNote = "natural armor";
		behir.hitDice = new DiceRoll(16, 12, 64);
		behir.speed = 50;
		behir.speeds.put(MovementType.CLIMB, 40);
		behir.abilityScores.put(Ability.STRENGTH, 23);
		behir.abilityScores.put(Ability.DEXTERITY, 16);
		behir.abilityScores.put(Ability.CONSTITUTION, 18);
		behir.abilityScores.put(Ability.INTELLIGENCE, 7);
		behir.abilityScores.put(Ability.WISDOM, 14);
		behir.abilityScores.put(Ability.CHARISMA, 12);
		behir.immunities.put(null, new HashSet<>(Arrays.asList(DamageType.LIGHTNING)));
		behir.senses.put(VisionType.DARKVISION, 90);
		behir.languages.add("Draconic");
		behir.challengeRating = 11;
		behir.actions = Arrays.asList(new Action("Multiattack", "The behir makes two attacks: one with its bite, and one to constrict."), //
				new Attack("Bite", Type.MELEE_WEAPON, 10, "reach 10 ft., one target", null, new Attack.Damage(new DiceRoll(3, 10, 6), DamageType.PIERCING)), //
				new Attack("Constrict", Type.MELEE_WEAPON, 10, "reach 5 ft., one Large or smaller creature", ". The target is grappled (escape DC 16) if the behir isn't already constricting a creature, and the target is restrained until this grapple ends.", new Attack.Damage(new DiceRoll(2, 10, 6), DamageType.BLUDGEONING), new Attack.Damage(new DiceRoll(2, 10, 6), DamageType.SLASHING)), //
				new Action("Lightning Breath", "Recharge 5-6", "The behir exhales a line of lightning that is 20 feet long and 5 feet wide. Each creature in that line must make a DC 16 Dexterity saving throw, taking 66 (12d10) lightning damage on a failed save, or half as much damage on a successful one."), //
				new Action("Swallow",
						"The behir makes one bite attack against a Medium or smaller target it is grappling. If the attack hits, the target is also swallowed, and the grapple ends. While swallowed, the target is blind ed and restrained, it has total cover against attacks and other effects outside the behir, and it takes 21 (6d6) acid damage at the start of each of the behir's turns. A behir can have only one creature swallowed at a time.\n    If the behir takes 30 damage or more on a single turn from the swallowed creature, the behir must succeed on a DC 14 Constitution saving throw at the end of that turn or regurgitate the creature, which fa lls prone in a space within 10 feet of the behir. If the behir dies, a swallowed creature is no lon ger restrained by it and can escape from the corpse by using 15 feet of movement, exiting prone."));

		peryton = new Creature();
		peryton.name = "Peryton";
		peryton.shortName = "The peryton";
		peryton.size = Size.MEDIUM;
		peryton.type = CreatureType.MONSTROSITY;
		peryton.alignment = Alignment.CHAOTIC_EVIL;
		peryton.ac = 13;
		peryton.armorNote = "natural armor";
		peryton.hitDice = new DiceRoll(6, 8, 6);
		peryton.speed = 20;
		peryton.speeds.put(MovementType.FLY, 60);
		peryton.abilityScores.put(Ability.STRENGTH, 16);
		peryton.abilityScores.put(Ability.DEXTERITY, 12);
		peryton.abilityScores.put(Ability.CONSTITUTION, 13);
		peryton.abilityScores.put(Ability.INTELLIGENCE, 9);
		peryton.abilityScores.put(Ability.WISDOM, 12);
		peryton.abilityScores.put(Ability.CHARISMA, 10);
		peryton.skills.put(Skill.PERCEPTION, 5);
		peryton.languages.add("understands Common and Elvish but can't speak");
		peryton.challengeRating = 2;
		peryton.features = Arrays.asList(new Feature("Dive Attack", "If the peryton is flying and dives at least 30 feet straight toward a target and then hits it with a melee weapon attack, the attack deals an extra 9 (2d8) damage to the target."), //
				new Feature("Flyby", "The peryton doesn't provoke an opportunity attack when it flies out of an enemy's reach."), //
				new Feature("Keen Sight and Smell", "The peryton has advantage on Wisdom (Perception) checks that rely on sight or smell."));
		peryton.actions = Arrays.asList(new Action("Multiattack", "The leucrotta makes one gore attack, and one talon attack."), //
				new Attack("Gore", Type.MELEE_WEAPON, 5, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(1, 8, 3), DamageType.PIERCING)), //
				new Attack("Talons", Type.MELEE_WEAPON, 5, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(2, 4, 3), DamageType.PIERCING)));

		gelatinousCube = new Creature();
		gelatinousCube.name = "Gelatinous Cube";
		gelatinousCube.shortName = "The cube";
		gelatinousCube.size = Size.LARGE;
		gelatinousCube.type = CreatureType.OOZE;
		gelatinousCube.alignment = Alignment.UNALIGNED;
		gelatinousCube.ac = 6;
		gelatinousCube.hitDice = new DiceRoll(8, 10, 40);
		gelatinousCube.speed = 15;
		gelatinousCube.abilityScores.put(Ability.STRENGTH, 14);
		gelatinousCube.abilityScores.put(Ability.DEXTERITY, 3);
		gelatinousCube.abilityScores.put(Ability.CONSTITUTION, 20);
		gelatinousCube.abilityScores.put(Ability.INTELLIGENCE, 1);
		gelatinousCube.abilityScores.put(Ability.WISDOM, 6);
		gelatinousCube.abilityScores.put(Ability.CHARISMA, 1);
		gelatinousCube.conditionImmunities.add(Condition.BLINDED);
		gelatinousCube.conditionImmunities.add(Condition.CHARMED);
		gelatinousCube.conditionImmunities.add(Condition.DEAFENED);
		gelatinousCube.conditionImmunities.add(Condition.EXHAUSTION);
		gelatinousCube.conditionImmunities.add(Condition.FRIGHTENED);
		gelatinousCube.conditionImmunities.add(Condition.PRONE);
		gelatinousCube.senses.put(VisionType.BLINDSIGHT, 60);
		gelatinousCube.challengeRating = 2;
		gelatinousCube.features = Arrays.asList(new Feature("Ooze Cube",
				"The cube takes up its entire space. Other creatures can enter the space, but a creature that does so is subjected to the cube's Engulf and has disadvantage on the saving throw.\n    Creatures inside the cube can be seen but have total cover.\n    A creature within 5 feet of the cube can take an action to pull a creature or object out of the cube. Doing so requires a successful DC 12 Strength check, and the creature making the attempt takes 10 (3d6) acid damage.\n    The cube can hold only one Large creature or up to four Medium or smaller creatures inside it at a time."), //
				new Feature("Transparent", "Even when the cube is in plain sight, it takes a successful DC 15 Wisdom (Perception) check to spot a cube that has neither moved nor attacked. A creature tbat tries to enter the cube's space while unaware of the cube is surprised by the cube."));
		gelatinousCube.actions = Arrays.asList(new Attack("Pseudopod", Type.MELEE_WEAPON, 4, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(3, 6), DamageType.ACID)), //
				new Action("Engulf",
						"The cube moves up to its speed. While doing so, it can enter Large or smaller creatures' spaces. Whenever the cube enters a creature's space, the creature must make a DC 12 Dexterity saving throw.\n    On a successful save, the creature can choose to be pushed 5 feet back or to the side of the cube. A creature that chooses not to be pushed suffers the consequences of a failed saving throw.\n    On a failed save, the cube enters the creature's space, and the creature takes 10 (3d6) acid damage and is engulfed. The engulfed creature can't breathe, is restrained, and takes 21 (6d6) acid damage at the start of each of the cube's turns. When the cube moves, the engulfed creature moves with it.\n    An engulfed creature can try to escape by taking an action to make a DC 12 Strength check. On a success, the creature escapes and enters a space of its choice within 5 feet of the cube."));

		efreeti = new Creature();
		efreeti.name = "Efreeti";
		efreeti.shortName = "The efreeti";
		efreeti.size = Size.MEDIUM;
		efreeti.type = CreatureType.UNDEAD;
		efreeti.alignment = Alignment.NEUTRAL_EVIL;
		efreeti.ac = 17;
		efreeti.armorNote = "natural armor";
		efreeti.hitDice = new DiceRoll(16, 10, 112);
		efreeti.speed = 40;
		efreeti.speeds.put(MovementType.FLY, 60);
		efreeti.abilityScores.put(Ability.STRENGTH, 22);
		efreeti.abilityScores.put(Ability.DEXTERITY, 12);
		efreeti.abilityScores.put(Ability.CONSTITUTION, 24);
		efreeti.abilityScores.put(Ability.INTELLIGENCE, 16);
		efreeti.abilityScores.put(Ability.WISDOM, 15);
		efreeti.abilityScores.put(Ability.CHARISMA, 16);
		efreeti.savingThrows.put(Ability.INTELLIGENCE, 7);
		efreeti.savingThrows.put(Ability.WISDOM, 6);
		efreeti.savingThrows.put(Ability.CHARISMA, 7);
		efreeti.immunities.put(null, new HashSet<>(Arrays.asList(DamageType.FIRE)));
		efreeti.languages.add("Ignan");
		efreeti.challengeRating = 11;

		final Map<String, Map<String, String>> efreetiSpells = new LinkedHashMap<>();

		final Map<String, String> efreetiAtWill = new HashMap<>();
		Stream.of("detect magic").forEach(s -> efreetiAtWill.put(s, ""));
		efreetiSpells.put("At will", efreetiAtWill);

		final Map<String, String> efreeti3Day = new HashMap<>();
		Stream.of("enlarge/reduce", "tongues").forEach(s -> efreeti3Day.put(s, ""));
		efreetiSpells.put("3/day each", efreeti3Day);

		final Map<String, String> efreeti1Day = new HashMap<>();
		Stream.of("gaseous form", "invisibility", "major image", "plane shift", "wall of fire").forEach(s -> efreeti1Day.put(s, ""));
		efreeti1Day.put("conjure elemental", "fire elemental only");
		efreetiSpells.put("1/day each", efreeti1Day);

		efreeti.features = Arrays.asList(new Feature("Elemental Demise", "If the efreeti dies, its body disintegrates in a flash of fire and puff of smoke, leaving behind only equipment the efreeti was wearing or carrying."), //
				new InnateSpellcasting(null, "the wight", Ability.CHARISMA, 15, 7, "it", null, efreetiSpells));
		efreeti.actions = Arrays.asList(new Action("Multiattack", "The efreeti makes two scimitar attacks or uses its Hurl Flame twice."), //
				new Attack("Scimitar", Type.MELEE_WEAPON, 10, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(2, 6, 6), DamageType.SLASHING), new Attack.Damage(new DiceRoll(2, 6), DamageType.FIRE)), //
				new Attack("Hurl Flame", Type.RANGED_SPELL, 7, "range 120 ft., one target", null, new Attack.Damage(new DiceRoll(5, 6), DamageType.FIRE)));
	}

}
