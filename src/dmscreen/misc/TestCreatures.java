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
import java.util.Set;
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

			final Set<Object> toJson = new HashSet<>();
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

	public static final Creature vampire, drow, demilich, zombie, ogreZombie, otyugh, grick, grell, gibberingMouther, orc, gnoll, troll, wight, skeleton, behir, peryton, gelatinousCube, //
			efreeti, cockatrice, gorgon, helmedHorror, blackPudding, grayOoze, ochreJelly, specter, sahuagin, sahuaginBaron, sahuaginPriestess, hunterShark, commoner, ghoul, stoneGiant, //
			clayGolem, fleshGolem, stoneGolem, wraith, succubusIncubus, fireElemental, quasit, manes, hezrou, vrock, glabrezu, giantSpider, giantCentipede, remorhaz, basilisk, darkmantle, //
			ettercap, carrionCrawler, hookHorror, wyvern, blackDragonWyrmling, dragonTurtle, spiritNaga, kuoToa, merrow, seaHag, giantCrab, youngRemorhaz;

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
		vampire.legendaryActions = Arrays.asList(new LegendaryAction("Move", "The vampire moves up to half its speed without provoking opportunity attacks."), //
				new LegendaryAction("Unarmed Strike", "The vampire makes one unarmed strike."), //
				new LegendaryAction("Bite", 2, "The vampire makes one bite attack."));

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
				new LegendaryAction("Energy Drain", 2, "Each creature with in 30 feet of the demilich must make a DC 15 Constitution saving throw. On a failed save, the creature's hit point maximum is magically reduced by 10 (3d6). If a creature's hit point maximum is reduced to 0 by this effect, the creature dies. A creature's hit point maximum can be restored with the [greater restoration] spell or similar magic."), //
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
		grick.resistances.put("nonmagical weapons", new HashSet<>(Arrays.asList(DamageType.BLUDGEONING, DamageType.PIERCING, DamageType.SLASHING)));
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
		wight.resistances.put("nonmagical weapons that aren't silvered", new HashSet<>(Arrays.asList(DamageType.BLUDGEONING, DamageType.PIERCING, DamageType.SLASHING)));
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
		peryton.resistances.put("nonmagical weapons", new HashSet<>(Arrays.asList(DamageType.BLUDGEONING, DamageType.PIERCING, DamageType.SLASHING)));
		peryton.languages.add("understands Common and Elvish but can't speak");
		peryton.challengeRating = 2;
		peryton.features = Arrays.asList(new Feature("Dive Attack", "If the peryton is flying and dives at least 30 feet straight toward a target and then hits it with a melee weapon attack, the attack deals an extra 9 (2d8) damage to the target."), //
				new Feature("Flyby", "The peryton doesn't provoke an opportunity attack when it flies out of an enemy's reach."), //
				new Feature("Keen Sight and Smell", "The peryton has advantage on Wisdom (Perception) checks that rely on sight or smell."));
		peryton.actions = Arrays.asList(new Action("Multiattack", "The peryton makes one gore attack, and one talon attack."), //
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

		cockatrice = new Creature();
		cockatrice.name = "Cockatrice";
		cockatrice.shortName = "The cockatrice";
		cockatrice.size = Size.SMALL;
		cockatrice.type = CreatureType.MONSTROSITY;
		cockatrice.alignment = Alignment.UNALIGNED;
		cockatrice.ac = 11;
		cockatrice.hitDice = new DiceRoll(6, 6, 6);
		cockatrice.speed = 30;
		cockatrice.abilityScores.put(Ability.STRENGTH, 6);
		cockatrice.abilityScores.put(Ability.DEXTERITY, 12);
		cockatrice.abilityScores.put(Ability.CONSTITUTION, 12);
		cockatrice.abilityScores.put(Ability.INTELLIGENCE, 2);
		cockatrice.abilityScores.put(Ability.WISDOM, 13);
		cockatrice.abilityScores.put(Ability.CHARISMA, 5);
		cockatrice.senses.put(VisionType.DARKVISION, 60);
		cockatrice.challengeRating = -1;
		cockatrice.actions = Arrays.asList(new Attack("Bite", Type.MELEE_WEAPON, 3, "reach 5 ft., one creature", ", and the target must succeed on a DC 11 Constitution saving throw against being magically petrified. On a failed save, the creature begins to turn to stone and is restrained. It must repeat the saving throw at the end of its next turn. On a success, the effect ends. On a failure, the creature is petrified for 24 hours.", new Attack.Damage(new DiceRoll(1, 4, 1), DamageType.PIERCING)));

		gorgon = new Creature();
		gorgon.name = "Gorgon";
		gorgon.shortName = "The gorgon";
		gorgon.size = Size.LARGE;
		gorgon.type = CreatureType.MONSTROSITY;
		gorgon.alignment = Alignment.UNALIGNED;
		gorgon.ac = 19;
		gorgon.armorNote = "natural armor";
		gorgon.hitDice = new DiceRoll(12, 10, 48);
		gorgon.speed = 40;
		gorgon.abilityScores.put(Ability.STRENGTH, 20);
		gorgon.abilityScores.put(Ability.DEXTERITY, 11);
		gorgon.abilityScores.put(Ability.CONSTITUTION, 18);
		gorgon.abilityScores.put(Ability.INTELLIGENCE, 2);
		gorgon.abilityScores.put(Ability.WISDOM, 12);
		gorgon.abilityScores.put(Ability.CHARISMA, 7);
		gorgon.skills.put(Skill.PERCEPTION, 4);
		gorgon.conditionImmunities.add(Condition.PETRIFIED);
		gorgon.senses.put(VisionType.DARKVISION, 60);
		gorgon.challengeRating = 5;
		gorgon.features = Arrays.asList(new Feature("Trampling Charge", "If the gorgon moves at least 20 feet straight toward a creature and then hits it with a gore attack on the same turn, the target must succeed on a DC 16 Strength saving throw or be knocked prone. If the target is prone, the gorgon can make one attack with its hooves against it as a bonus action."));
		gorgon.actions = Arrays.asList(new Attack("Gore", Type.MELEE_WEAPON, 8, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(2, 12, 5), DamageType.PIERCING)), //
				new Attack("Hooves", Type.MELEE_WEAPON, 8, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(2, 10, 5), DamageType.BLUDGEONING)), //
				new Action("Petrifying Breath", "Recharge 5-6", "The gorgon exhales petrifying gas in a 30-foot cone. Each creature in that area must succeed on a DC 13 Constitu tion saving throw. On a failed save, a target begins to turn to stone and is restrained. The restrained target must repeat the saving throw at the end of its next turn. On a success, the effect ends on the target. On a failure, the target is petrified until freed by the [greater restoration] spell or other magic."));

		helmedHorror = new Creature();
		helmedHorror.name = "Helmed Horror";
		helmedHorror.shortName = "The helmed horror";
		helmedHorror.size = Size.MEDIUM;
		helmedHorror.type = CreatureType.CONSTRUCT;
		helmedHorror.alignment = Alignment.NEUTRAL;
		helmedHorror.ac = 20;
		helmedHorror.armorNote = "plate, shield";
		helmedHorror.hitDice = new DiceRoll(8, 8, 24);
		helmedHorror.speed = 30;
		helmedHorror.speeds.put(MovementType.FLY, 30);
		helmedHorror.abilityScores.put(Ability.STRENGTH, 18);
		helmedHorror.abilityScores.put(Ability.DEXTERITY, 13);
		helmedHorror.abilityScores.put(Ability.CONSTITUTION, 16);
		helmedHorror.abilityScores.put(Ability.INTELLIGENCE, 10);
		helmedHorror.abilityScores.put(Ability.WISDOM, 10);
		helmedHorror.abilityScores.put(Ability.CHARISMA, 10);
		helmedHorror.skills.put(Skill.PERCEPTION, 4);
		helmedHorror.resistances.put("nonmagical weapons that aren't adamantine", new HashSet<>(Arrays.asList(DamageType.BLUDGEONING, DamageType.PIERCING, DamageType.SLASHING)));
		helmedHorror.conditionImmunities.addAll(Arrays.asList(Condition.BLINDED, Condition.CHARMED, Condition.DEAFENED, Condition.EXHAUSTION, Condition.FRIGHTENED, Condition.PARALYZED, Condition.PETRIFIED, Condition.POISONED, Condition.STUNNED));
		helmedHorror.senses.put(VisionType.BLINDSIGHT, 60);
		helmedHorror.languages.add("understands the languages of its creator but can't speak");
		helmedHorror.challengeRating = 4;
		helmedHorror.features = Arrays.asList(new Feature("Magic Resistance", "The helmed horror has advantage on saving throws against spells and other magical effects."), //
				new Feature("Spell Immunity", "The helmed horror is immune to three spells chosen by its creator. Typical immunities include fireball, heat metal, and lightning bolt."));
		helmedHorror.actions = Arrays.asList(new Action("Multiattack", "The helmed horror makes two longsword attacks."), //
				new Attack("Longsword", Type.MELEE_WEAPON, 6, "reach 5 ft., one target", ", or 9 (1d10 + 4) slashing damage if used with two hands.", new Attack.Damage(new DiceRoll(1, 8, 4), DamageType.SLASHING)));

		blackPudding = new Creature();
		blackPudding.name = "Black Pudding";
		blackPudding.shortName = "The pudding";
		blackPudding.size = Size.LARGE;
		blackPudding.type = CreatureType.OOZE;
		blackPudding.alignment = Alignment.UNALIGNED;
		blackPudding.ac = 7;
		blackPudding.hitDice = new DiceRoll(10, 10, 30);
		blackPudding.speed = 20;
		blackPudding.speeds.put(MovementType.CLIMB, 20);
		blackPudding.abilityScores.put(Ability.STRENGTH, 16);
		blackPudding.abilityScores.put(Ability.DEXTERITY, 5);
		blackPudding.abilityScores.put(Ability.CONSTITUTION, 16);
		blackPudding.abilityScores.put(Ability.INTELLIGENCE, 1);
		blackPudding.abilityScores.put(Ability.WISDOM, 6);
		blackPudding.abilityScores.put(Ability.CHARISMA, 1);
		blackPudding.immunities.put(null, Stream.of(DamageType.ACID, DamageType.COLD, DamageType.LIGHTNING, DamageType.SLASHING).collect(Collectors.toSet()));
		blackPudding.conditionImmunities.addAll(Arrays.asList(Condition.BLINDED, Condition.CHARMED, Condition.DEAFENED, Condition.EXHAUSTION, Condition.FRIGHTENED, Condition.PRONE));
		blackPudding.senses.put(VisionType.BLINDSIGHT, 60);
		blackPudding.challengeRating = 4;
		blackPudding.features = Arrays.asList(new Feature("Amorphous", "The pudding can move through a space as narrow as 1 inch wide without squeezing."), //
				new Feature("Corrosive Form",
						"A creature that touches the pudding or hits it with a melee attack while within 5 feet of it takes 4 (1d8) acid damage. Any nonmagical weapon made of metal or wood that hits the pudding corrodes. After dealing damage, the weapon takes a permanent cumulative -1 penalty to damage rolls. If its penalty drops to -5, the weapon is destroyed. Nonmagical ammunition made of metal or wood that hits the pudding is destroyed after dealing damage.\n    The pudding can eat through 2-inch-thick, non magical wood or metal in 1 round."), //
				new Feature("Spider Climb", "The pudding can climb difficult surfaces, including upside down on ceilings, without needing to make an ability check."));
		blackPudding.actions = Arrays.asList(new Attack("Pseudopod", Type.MELEE_WEAPON, 5, "reach 5 ft., one target", ". In addition, nonmagical armor worn by the target is partly dissolved and takes a permanent and cumulative -1 penalty to the AC it offers. The armor is destroyed if the penalty reduces its AC to 10.", new Attack.Damage(new DiceRoll(1, 6, 3), DamageType.BLUDGEONING), new Attack.Damage(new DiceRoll(4, 8), DamageType.ACID)));
		blackPudding.reactions = Arrays.asList(new Action("Split", "When a pudding that is Medium or larger is subjected to lightning or slashing damage, it splits into two new puddings if it has at least 10 hit points. Each new pudding has hit points equal to half the original pudding's, rounded down. New puddings are one size smaller than the original pudding."));

		grayOoze = new Creature();
		grayOoze.name = "Gray Ooze";
		grayOoze.shortName = "The ooze";
		grayOoze.size = Size.MEDIUM;
		grayOoze.type = CreatureType.OOZE;
		grayOoze.alignment = Alignment.UNALIGNED;
		grayOoze.ac = 8;
		grayOoze.hitDice = new DiceRoll(3, 8, 9);
		grayOoze.speed = 10;
		grayOoze.speeds.put(MovementType.CLIMB, 10);
		grayOoze.abilityScores.put(Ability.STRENGTH, 12);
		grayOoze.abilityScores.put(Ability.DEXTERITY, 6);
		grayOoze.abilityScores.put(Ability.CONSTITUTION, 16);
		grayOoze.abilityScores.put(Ability.INTELLIGENCE, 1);
		grayOoze.abilityScores.put(Ability.WISDOM, 6);
		grayOoze.abilityScores.put(Ability.CHARISMA, 2);
		grayOoze.skills.put(Skill.STEALTH, 2);
		grayOoze.immunities.put(null, Stream.of(DamageType.ACID, DamageType.COLD, DamageType.FIRE).collect(Collectors.toSet()));
		grayOoze.conditionImmunities.addAll(Arrays.asList(Condition.BLINDED, Condition.CHARMED, Condition.DEAFENED, Condition.EXHAUSTION, Condition.FRIGHTENED, Condition.PRONE));
		grayOoze.senses.put(VisionType.BLINDSIGHT, 60);
		grayOoze.challengeRating = -1;
		grayOoze.features = Arrays.asList(new Feature("Amorphous", "The pudding can move through a space as narrow as 1 inch wide without squeezing."), //
				new Feature("Corrode Metal", "Any non magical weapon made of metal that hits the ooze corrodes. After dealing damage, the weapon takes a permanent and cumulative -1 penalty to damage rolls. If its penalty drops to -5, the weapon is destroyed. Nonmagical ammunition made of metal that hits the pudding is destroyed after dealing damage.\n    The pudding can eat through 2-inch-thick, nonmagical metal in 1 round."), //
				new Feature("False Appearance", "While the ooze remains motionless, it is indistinguishable from an oily pool or wet rock."));
		grayOoze.actions = Arrays.asList(new Attack("Pseudopod", Type.MELEE_WEAPON, 5, "reach 5 ft., one target", ", and if the target is wearing nonmagical metal armor, its armor is partly corroded and takes a permanent and cumulative -1 penalty to the AC it offers. The armor is destroyed if the penalty reduces its AC to 10.", new Attack.Damage(new DiceRoll(1, 6, 1), DamageType.BLUDGEONING), new Attack.Damage(new DiceRoll(2, 8), DamageType.ACID)));

		ochreJelly = new Creature();
		ochreJelly.name = "Ochre Jelly";
		ochreJelly.shortName = "The jelly";
		ochreJelly.size = Size.LARGE;
		ochreJelly.type = CreatureType.OOZE;
		ochreJelly.alignment = Alignment.UNALIGNED;
		ochreJelly.ac = 8;
		ochreJelly.hitDice = new DiceRoll(6, 10, 12);
		ochreJelly.speed = 10;
		ochreJelly.speeds.put(MovementType.CLIMB, 10);
		ochreJelly.abilityScores.put(Ability.STRENGTH, 15);
		ochreJelly.abilityScores.put(Ability.DEXTERITY, 6);
		ochreJelly.abilityScores.put(Ability.CONSTITUTION, 14);
		ochreJelly.abilityScores.put(Ability.INTELLIGENCE, 2);
		ochreJelly.abilityScores.put(Ability.WISDOM, 6);
		ochreJelly.abilityScores.put(Ability.CHARISMA, 1);
		ochreJelly.resistances.put(null, Stream.of(DamageType.ACID).collect(Collectors.toSet()));
		ochreJelly.immunities.put(null, Stream.of(DamageType.LIGHTNING, DamageType.SLASHING).collect(Collectors.toSet()));
		ochreJelly.conditionImmunities.addAll(Arrays.asList(Condition.BLINDED, Condition.CHARMED, Condition.DEAFENED, Condition.EXHAUSTION, Condition.FRIGHTENED, Condition.PRONE));
		ochreJelly.senses.put(VisionType.BLINDSIGHT, 60);
		ochreJelly.challengeRating = 2;
		ochreJelly.features = Arrays.asList(new Feature("Amorphous", "The jelly can move through a space as narrow as 1 inch wide without squeezing."), //
				new Feature("Spider Climb", "The jelly can climb difficult surfaces, including upside down on ceilings, without needing to make an ability check."));
		ochreJelly.actions = Arrays.asList(new Attack("Pseudopod", Type.MELEE_WEAPON, 5, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(2, 6, 2), DamageType.BLUDGEONING), new Attack.Damage(new DiceRoll(1, 6), DamageType.ACID)));
		ochreJelly.reactions = Arrays.asList(new Action("Split", "When a jelly that is Medium or larger is subjected to lightning or slashing damage, it splits into two new jellies if it has at least 10 hit points. Each new jelly has hit points equal to half the original jelly's, rounded down. New jellies are one size smaller than the original jelly."));

		specter = new Creature();
		specter.name = "Specter";
		specter.shortName = "The specter";
		specter.size = Size.MEDIUM;
		specter.type = CreatureType.UNDEAD;
		specter.alignment = Alignment.CHAOTIC_EVIL;
		specter.ac = 12;
		specter.hitDice = new DiceRoll(5, 8);
		specter.speed = 0;
		specter.speeds.put(MovementType.FLY, 50);
		specter.abilityScores.put(Ability.STRENGTH, 1);
		specter.abilityScores.put(Ability.DEXTERITY, 14);
		specter.abilityScores.put(Ability.CONSTITUTION, 11);
		specter.abilityScores.put(Ability.INTELLIGENCE, 10);
		specter.abilityScores.put(Ability.WISDOM, 10);
		specter.abilityScores.put(Ability.CHARISMA, 11);
		specter.resistances.put(null, Stream.of(DamageType.ACID, DamageType.COLD, DamageType.FIRE, DamageType.LIGHTNING).collect(Collectors.toSet()));
		specter.resistances.put("nonmagical weapons", new HashSet<>(Arrays.asList(DamageType.BLUDGEONING, DamageType.PIERCING, DamageType.SLASHING)));
		specter.immunities.put(null, Stream.of(DamageType.NECROTIC, DamageType.POISON).collect(Collectors.toSet()));
		specter.conditionImmunities.addAll(Arrays.asList(Condition.CHARMED, Condition.EXHAUSTION, Condition.GRAPPLED, Condition.PARALYZED, Condition.PETRIFIED, Condition.POISONED, Condition.PRONE, Condition.RESTRAINED, Condition.UNCONSCIOUS));
		specter.senses.put(VisionType.DARKVISION, 60);
		specter.languages.add("understands all languages it knew in life but can't speak");
		specter.challengeRating = 1;
		specter.features = Arrays.asList(new Feature("Incorporeal Movement", "The specter can move through objects and other creatures as if they were difficult terrain. It takes 5 (1d10) force damage if it ends its turn inside an object."), //
				new Feature("Sunlight Sensitivity", "While in sunlight, the specter has disadvantage on attack rolls, as well as Wisdom (Perception) checks that rely on sight."));
		specter.actions = Arrays.asList(new Attack("Life Drain", Type.MELEE_SPELL, 4, "reach 5 ft., one creature", ". The target must succeed on a DC 10 Constitution saving throw or its hit point maximum is reduced by an amount equal to the damage taken. This reduction lasts until the creature finishes a long rest. The target dies if this effect reduces its hit point maximum to 0.", new Attack.Damage(new DiceRoll(3, 6), DamageType.NECROTIC)));

		sahuagin = new Creature();
		sahuagin.name = "Sahuagin";
		sahuagin.shortName = "The sahuagin";
		sahuagin.size = Size.MEDIUM;
		sahuagin.type = CreatureType.HUMANOID;
		sahuagin.subtype = "sahuagin";
		sahuagin.alignment = Alignment.LAWFUL_EVIL;
		sahuagin.ac = 12;
		sahuagin.armorNote = "natural armor";
		sahuagin.hitDice = new DiceRoll(4, 8, 4);
		sahuagin.speed = 30;
		sahuagin.speeds.put(MovementType.SWIM, 40);
		sahuagin.abilityScores.put(Ability.STRENGTH, 13);
		sahuagin.abilityScores.put(Ability.DEXTERITY, 11);
		sahuagin.abilityScores.put(Ability.CONSTITUTION, 12);
		sahuagin.abilityScores.put(Ability.INTELLIGENCE, 12);
		sahuagin.abilityScores.put(Ability.WISDOM, 13);
		sahuagin.abilityScores.put(Ability.CHARISMA, 9);
		sahuagin.skills.put(Skill.PERCEPTION, 5);
		sahuagin.senses.put(VisionType.DARKVISION, 120);
		sahuagin.languages.add("Sahuagin");
		sahuagin.challengeRating = -1;
		sahuagin.features = Arrays.asList(new Feature("Blood Frenzy", "The sahuagin has advantage on melee attack rolls against any creature that doesn't have all its hit points."), //
				new Feature("Limited Amphibiousness", "The sahuagin can breathe air and water, but it needs to be submerged at least once every 4 hours to avoid suffocating."), //
				new Feature("Shark Telepathy", "The sahuagin can magically command any shark within 120 feet of it, using a limited telepathy."));
		sahuagin.actions = Arrays.asList(new Action("Multiattack", "The sahaugin makes two melee attacks: one with its bite and one with its claws or its spear."), //
				new Attack("Bite", Type.MELEE_WEAPON, 3, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(1, 4, 1), DamageType.PIERCING)), //
				new Attack("Claws", Type.MELEE_WEAPON, 3, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(1, 4, 1), DamageType.SLASHING)), //
				new Attack("Spear", Type.MELEE_OR_RANGED_WEAPON, 3, "reach 5 ft. or range 20/60 ft., one target", ", or 5 (1d8 + 1) piercing damage if used with two hands to make a melee attack.", new Attack.Damage(new DiceRoll(1, 6, 1), DamageType.PIERCING)));

		sahuaginBaron = new Creature();
		sahuaginBaron.name = "Sahuagin Baron";
		sahuaginBaron.shortName = "The sahuagin";
		sahuaginBaron.size = Size.LARGE;
		sahuaginBaron.type = CreatureType.HUMANOID;
		sahuaginBaron.subtype = "sahuagin";
		sahuaginBaron.alignment = Alignment.LAWFUL_EVIL;
		sahuaginBaron.ac = 16;
		sahuaginBaron.armorNote = "breastplate";
		sahuaginBaron.hitDice = new DiceRoll(9, 10, 27);
		sahuaginBaron.speed = 30;
		sahuaginBaron.speeds.put(MovementType.SWIM, 50);
		sahuaginBaron.abilityScores.put(Ability.STRENGTH, 19);
		sahuaginBaron.abilityScores.put(Ability.DEXTERITY, 15);
		sahuaginBaron.abilityScores.put(Ability.CONSTITUTION, 16);
		sahuaginBaron.abilityScores.put(Ability.INTELLIGENCE, 14);
		sahuaginBaron.abilityScores.put(Ability.WISDOM, 13);
		sahuaginBaron.abilityScores.put(Ability.CHARISMA, 17);
		sahuaginBaron.savingThrows.put(Ability.DEXTERITY, 5);
		sahuaginBaron.savingThrows.put(Ability.CONSTITUTION, 6);
		sahuaginBaron.savingThrows.put(Ability.INTELLIGENCE, 5);
		sahuaginBaron.savingThrows.put(Ability.WISDOM, 4);
		sahuaginBaron.skills.put(Skill.PERCEPTION, 7);
		sahuaginBaron.senses.put(VisionType.DARKVISION, 120);
		sahuaginBaron.languages.add("Sahuagin");
		sahuaginBaron.challengeRating = 5;
		sahuaginBaron.features = Arrays.asList(new Feature("Blood Frenzy", "The sahuagin has advantage on melee attack rolls against any creature that doesn't have all its hit points."), //
				new Feature("Limited Amphibiousness", "The sahuagin can breathe air and water, but he needs to be submerged at least once every 4 hours to avoid suffocating."), //
				new Feature("Shark Telepathy", "The sahuagin can magically command any shark within 120 feet of him, using a limited telepathy."));
		sahuaginBaron.actions = Arrays.asList(new Action("Multiattack", "The sahaugin makes three melee attacks: one with his bite and two with his claws or his trident."), //
				new Attack("Bite", Type.MELEE_WEAPON, 7, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(2, 4, 4), DamageType.PIERCING)), //
				new Attack("Claws", Type.MELEE_WEAPON, 7, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(2, 6, 4), DamageType.SLASHING)), //
				new Attack("Trident", Type.MELEE_OR_RANGED_WEAPON, 7, "reach 5 ft. or range 20/60 ft., one target", ", or 5 (2d8 + 4) piercing damage if used with two hands to make a melee attack.", new Attack.Damage(new DiceRoll(2, 6, 4), DamageType.PIERCING)));

		sahuaginPriestess = new Creature();
		sahuaginPriestess.name = "Sahuagin Priestess";
		sahuaginPriestess.shortName = "The sahuagin";
		sahuaginPriestess.size = Size.MEDIUM;
		sahuaginPriestess.type = CreatureType.HUMANOID;
		sahuaginPriestess.subtype = "sahuagin";
		sahuaginPriestess.alignment = Alignment.LAWFUL_EVIL;
		sahuaginPriestess.ac = 12;
		sahuaginPriestess.armorNote = "natural armor";
		sahuaginPriestess.hitDice = new DiceRoll(4, 8, 4);
		sahuaginPriestess.speed = 30;
		sahuaginPriestess.speeds.put(MovementType.SWIM, 40);
		sahuaginPriestess.abilityScores.put(Ability.STRENGTH, 13);
		sahuaginPriestess.abilityScores.put(Ability.DEXTERITY, 11);
		sahuaginPriestess.abilityScores.put(Ability.CONSTITUTION, 12);
		sahuaginPriestess.abilityScores.put(Ability.INTELLIGENCE, 12);
		sahuaginPriestess.abilityScores.put(Ability.WISDOM, 14);
		sahuaginPriestess.abilityScores.put(Ability.CHARISMA, 13);
		sahuaginPriestess.skills.put(Skill.PERCEPTION, 6);
		sahuaginPriestess.skills.put(Skill.RELIGION, 3);
		sahuaginPriestess.senses.put(VisionType.DARKVISION, 120);
		sahuaginPriestess.languages.add("Sahuagin");
		sahuaginPriestess.challengeRating = 2;

		final Map<String, Map<String, String>> sahuaginSpells = new LinkedHashMap<>();

		final Map<String, String> sahuaginCantrips = new HashMap<>();
		Stream.of("guidance, thaumaturgy").forEach(s -> sahuaginCantrips.put(s, ""));
		sahuaginSpells.put("Cantrips (at will)", sahuaginCantrips);

		final Map<String, String> sahuaginLevel1 = new HashMap<>();
		Stream.of("bless", "detect magic", "guiding bolt").forEach(s -> sahuaginLevel1.put(s, ""));
		sahuaginSpells.put("1st level (4 slots)", sahuaginLevel1);

		final Map<String, String> sahuaginLevel2 = new HashMap<>();
		sahuaginLevel2.put("hold person", "");
		sahuaginLevel2.put("spiritual weapon", " (trident)");
		sahuaginSpells.put("2nd level (3 slots)", sahuaginLevel2);

		final Map<String, String> sahuaginLevel3 = new HashMap<>();
		Stream.of("mass healing word", "tongues").forEach(s -> sahuaginLevel3.put(s, ""));
		sahuaginSpells.put("3rd level (3 slots)", sahuaginLevel3);

		sahuaginPriestess.features = Arrays.asList(new Feature("Blood Frenzy", "The sahuagin has advantage on melee attack rolls against any creature that doesn't have all its hit points."), //
				new Feature("Limited Amphibiousness", "The sahuagin can breathe air and water, but she needs to be submerged at least once every 4 hours to avoid suffocating."), //
				new Feature("Shark Telepathy", "The sahuagin can magically command any shark within 120 feet of her, using a limited telepathy."), //
				new Spellcasting(null, "the sahuagin", 6, Ability.WISDOM, 12, "her", 4, null, "cleric", null, sahuaginSpells));
		sahuaginPriestess.actions = Arrays.asList(new Action("Multiattack", "The sahaugin makes two melee attacks: one with her bite and one with her claws."), //
				new Attack("Bite", Type.MELEE_WEAPON, 3, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(1, 4, 1), DamageType.PIERCING)), //
				new Attack("Claws", Type.MELEE_WEAPON, 3, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(1, 4, 1), DamageType.SLASHING)));

		hunterShark = new Creature();
		hunterShark.name = "Hunter Shark";
		hunterShark.shortName = "The shark";
		hunterShark.size = Size.LARGE;
		hunterShark.type = CreatureType.BEAST;
		hunterShark.alignment = Alignment.UNALIGNED;
		hunterShark.ac = 12;
		hunterShark.armorNote = "natural armor";
		hunterShark.hitDice = new DiceRoll(6, 10, 12);
		hunterShark.speed = 0;
		hunterShark.speeds.put(MovementType.SWIM, 40);
		hunterShark.abilityScores.put(Ability.STRENGTH, 18);
		hunterShark.abilityScores.put(Ability.DEXTERITY, 13);
		hunterShark.abilityScores.put(Ability.CONSTITUTION, 15);
		hunterShark.abilityScores.put(Ability.INTELLIGENCE, 1);
		hunterShark.abilityScores.put(Ability.WISDOM, 10);
		hunterShark.abilityScores.put(Ability.CHARISMA, 4);
		hunterShark.skills.put(Skill.PERCEPTION, 2);
		hunterShark.senses.put(VisionType.BLINDSIGHT, 30);
		hunterShark.challengeRating = 2;
		hunterShark.features = Arrays.asList(new Feature("Blood Frenzy", "The sahuagin has advantage on melee attack rolls against any creature that doesn't have all its hit points."), //
				new Feature("Water Breathing", "The shark can breathe only underwater."));
		hunterShark.actions = Arrays.asList(new Attack("Bite", Type.MELEE_WEAPON, 6, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(2, 8, 4), DamageType.PIERCING)));

		commoner = new Creature();
		commoner.name = "Commoner";
		commoner.shortName = "The commoner";
		commoner.size = Size.MEDIUM;
		commoner.type = CreatureType.HUMANOID;
		commoner.subtype = "any race";
		commoner.alignment = Alignment.ANY_ALIGNMENT;
		commoner.ac = 10;
		commoner.hitDice = new DiceRoll(1, 8);
		commoner.speed = 30;
		for (final Ability a : Ability.values())
			commoner.abilityScores.put(a, 10);
		commoner.languages.add("any one language (usually Common)");
		commoner.challengeRating = -4;
		commoner.actions = Arrays.asList(new Attack("Club", Type.MELEE_WEAPON, 2, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(1, 4), DamageType.BLUDGEONING)));

		ghoul = new Creature();
		ghoul.name = "Ghoul";
		ghoul.shortName = "The ghoul";
		ghoul.size = Size.MEDIUM;
		ghoul.type = CreatureType.UNDEAD;
		ghoul.alignment = Alignment.CHAOTIC_EVIL;
		ghoul.ac = 12;
		ghoul.hitDice = new DiceRoll(5, 8);
		ghoul.speed = 30;
		ghoul.abilityScores.put(Ability.STRENGTH, 13);
		ghoul.abilityScores.put(Ability.DEXTERITY, 15);
		ghoul.abilityScores.put(Ability.CONSTITUTION, 10);
		ghoul.abilityScores.put(Ability.INTELLIGENCE, 7);
		ghoul.abilityScores.put(Ability.WISDOM, 10);
		ghoul.abilityScores.put(Ability.CHARISMA, 6);
		ghoul.immunities.put(null, Stream.of(DamageType.POISON).collect(Collectors.toSet()));
		ghoul.conditionImmunities.addAll(Arrays.asList(Condition.CHARMED, Condition.EXHAUSTION, Condition.POISONED));
		ghoul.senses.put(VisionType.DARKVISION, 60);
		ghoul.languages.add("Common");
		ghoul.challengeRating = 1;
		ghoul.actions = Arrays.asList(new Attack("Bite", Type.MELEE_WEAPON, 2, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(2, 6, 2), DamageType.PIERCING)), //
				new Attack("Claws", Type.MELEE_WEAPON, 4, "reach 5 ft., one target", ". If the target is a creature other than an elf or undead, it must succeed on a DC 10 Constitution saving throw or be paralyzed for 1 minute. The target can repeat the saving throw at the end of each of its turns, ending the effect on itself on a success.", new Attack.Damage(new DiceRoll(2, 4, 2), DamageType.SLASHING)));

		stoneGiant = new Creature();
		stoneGiant.name = "Stone Giant";
		stoneGiant.shortName = "The giant";
		stoneGiant.size = Size.HUGE;
		stoneGiant.type = CreatureType.GIANT;
		stoneGiant.alignment = Alignment.CHAOTIC_GOOD;
		stoneGiant.ac = 17;
		stoneGiant.armorNote = "natural armor";
		stoneGiant.hitDice = new DiceRoll(11, 12, 55);
		stoneGiant.speed = 40;
		stoneGiant.abilityScores.put(Ability.STRENGTH, 23);
		stoneGiant.abilityScores.put(Ability.DEXTERITY, 15);
		stoneGiant.abilityScores.put(Ability.CONSTITUTION, 20);
		stoneGiant.abilityScores.put(Ability.INTELLIGENCE, 10);
		stoneGiant.abilityScores.put(Ability.WISDOM, 12);
		stoneGiant.abilityScores.put(Ability.CHARISMA, 9);
		stoneGiant.savingThrows.put(Ability.DEXTERITY, 5);
		stoneGiant.savingThrows.put(Ability.CONSTITUTION, 8);
		stoneGiant.savingThrows.put(Ability.WISDOM, 4);
		stoneGiant.skills.put(Skill.ATHLETICS, 12);
		stoneGiant.skills.put(Skill.PERCEPTION, 4);
		stoneGiant.senses.put(VisionType.DARKVISION, 60);
		stoneGiant.languages.add("Giant");
		stoneGiant.challengeRating = 7;
		stoneGiant.actions = Arrays.asList(new Action("Multiattack", "The giant makes two greatclub attacks"), //
				new Attack("Greatclub", Type.MELEE_WEAPON, 9, "reach 15 ft., one target", null, new Attack.Damage(new DiceRoll(3, 6, 6), DamageType.BLUDGEONING)), //
				new Attack("Claws", Type.RANGED_WEAPON, 9, "range 60/240 ft., one target", ". If the target is a creature, it must succeed on a DC 17 Strength saving throw or be knocked prone.", new Attack.Damage(new DiceRoll(4, 10, 6), DamageType.BLUDGEONING)));

		clayGolem = new Creature();
		clayGolem.name = "Clay Golem";
		clayGolem.shortName = "The golem";
		clayGolem.size = Size.LARGE;
		clayGolem.type = CreatureType.CONSTRUCT;
		clayGolem.alignment = Alignment.UNALIGNED;
		clayGolem.ac = 14;
		clayGolem.armorNote = "natural armor";
		clayGolem.hitDice = new DiceRoll(14, 10, 56);
		clayGolem.speed = 20;
		clayGolem.abilityScores.put(Ability.STRENGTH, 20);
		clayGolem.abilityScores.put(Ability.DEXTERITY, 9);
		clayGolem.abilityScores.put(Ability.CONSTITUTION, 18);
		clayGolem.abilityScores.put(Ability.INTELLIGENCE, 3);
		clayGolem.abilityScores.put(Ability.WISDOM, 8);
		clayGolem.abilityScores.put(Ability.CHARISMA, 1);
		clayGolem.immunities.put("nonmagical weapons that aren't adamantine", new HashSet<>(Arrays.asList(DamageType.BLUDGEONING, DamageType.PIERCING, DamageType.SLASHING)));
		clayGolem.immunities.put(null, new HashSet<>(Arrays.asList(DamageType.ACID, DamageType.POISON, DamageType.PSYCHIC)));
		clayGolem.conditionImmunities.addAll(Arrays.asList(Condition.CHARMED, Condition.EXHAUSTION, Condition.FRIGHTENED, Condition.PARALYZED, Condition.PETRIFIED, Condition.POISONED));
		clayGolem.senses.put(VisionType.BLINDSIGHT, 60);
		clayGolem.languages.add("understands the languages of its creator but can't speak");
		clayGolem.challengeRating = 9;
		clayGolem.features = Arrays.asList(new Feature("Acid Absorbtion", "Whenever the golem is subjected to acid damage, it takes no damage and instead regains a number of hit points equal to the acid damage dealt."), //
				new Feature("Berserk", "Whenever the golem starts its turn with 60 hit points or fewer, roll a d6. On a 6, the golem goes berserk. On each of its turns while berserk, the golem attacks the nearest creature it can see. If no creature is near enough to move to and attack, the golem attacks an object, with a preference for an object smaller than itself. Once the golem goes berserk, it continues to do so until it is dedstroyed or regains all its hit points."), //
				new Feature("Immutable Form", "The golem is immune to any spell or effect that would alter its form."), //
				new Feature("Magic Resistance", "The golem has advantage on saving throws against spells and other magical effects."), //
				new Feature("Magic Weapons", "The golem's attacks are magical."));
		clayGolem.actions = Arrays.asList(new Action("Multiattack", "The golem makes two slam attacks."), //
				new Attack("Slam", Type.MELEE_WEAPON, 8, "reach 5 ft., one target", ". If the target is a creature, it must succeed on a DC 15 Constitution saving throw or have its hit point maximum reduced by an amount equal to the damage taken. The target dies if this attack reduces its hit point maximum to 0. The reduction lasts until removed by the [greater restoration] spell or other magic.", new Attack.Damage(new DiceRoll(2, 10, 5), DamageType.BLUDGEONING)), //
				new Action("Haste", "Recharge 5-6", "Until the end of its next turn, the golem magically gains a +2 bonus to its AC, has advantage on Dexterity saving throws, and can use its slam attack as a bonus action."));

		fleshGolem = new Creature();
		fleshGolem.name = "Flesh Golem";
		fleshGolem.shortName = "The golem";
		fleshGolem.size = Size.MEDIUM;
		fleshGolem.type = CreatureType.CONSTRUCT;
		fleshGolem.alignment = Alignment.UNALIGNED;
		fleshGolem.ac = 9;
		fleshGolem.hitDice = new DiceRoll(11, 8, 44);
		fleshGolem.speed = 30;
		fleshGolem.abilityScores.put(Ability.STRENGTH, 19);
		fleshGolem.abilityScores.put(Ability.DEXTERITY, 9);
		fleshGolem.abilityScores.put(Ability.CONSTITUTION, 18);
		fleshGolem.abilityScores.put(Ability.INTELLIGENCE, 6);
		fleshGolem.abilityScores.put(Ability.WISDOM, 10);
		fleshGolem.abilityScores.put(Ability.CHARISMA, 5);
		fleshGolem.immunities.put("nonmagical weapons that aren't adamantine", new HashSet<>(Arrays.asList(DamageType.BLUDGEONING, DamageType.PIERCING, DamageType.SLASHING)));
		fleshGolem.immunities.put(null, new HashSet<>(Arrays.asList(DamageType.LIGHTNING, DamageType.POISON)));
		fleshGolem.conditionImmunities.addAll(Arrays.asList(Condition.CHARMED, Condition.EXHAUSTION, Condition.FRIGHTENED, Condition.PARALYZED, Condition.PETRIFIED, Condition.POISONED));
		fleshGolem.senses.put(VisionType.BLINDSIGHT, 60);
		fleshGolem.languages.add("understands the languages of its creator but can't speak");
		fleshGolem.challengeRating = 5;
		fleshGolem.features = Arrays.asList(new Feature("Berserk", "Whenever the golem starts its turn with 40 hit points or fewer, roll a d6. On a 6, the golem goes berserk. On each of its turns while berserk, the golem attacks the nearest creature it can see. If no creature is near enough to move to and attack, the golem attacks an object, with a preference for an object smaller than itself. Once the golem goes berserk, it continues to do so until it is destroyed or regains all its hit points."), //
				new Feature("Aversion of Fire", "Whenever the golem takes fire damage, it has disadvantage on attack rolls and ability checks until the end of its next turn."), //
				new Feature("Immutable Form", "The golem is immune to any spell or effect that would alter its form."), //
				new Feature("Lightning Absorbtion", "Whenever the golem is subjected to lightning damage, it takes no damage and instead regains a number of hit points equal to the lightning damage dealt."), //
				new Feature("Magic Resistance", "The golem has advantage on saving throws against spells and other magical effects."), //
				new Feature("Magic Weapons", "The golem's attacks are magical."));
		fleshGolem.actions = Arrays.asList(new Action("Multiattack", "The golem makes two slam attacks."), //
				new Attack("Slam", Type.MELEE_WEAPON, 7, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(2, 8, 4), DamageType.BLUDGEONING)));

		stoneGolem = new Creature();
		stoneGolem.name = "Stone Golem";
		stoneGolem.shortName = "The golem";
		stoneGolem.size = Size.LARGE;
		stoneGolem.type = CreatureType.CONSTRUCT;
		stoneGolem.alignment = Alignment.UNALIGNED;
		stoneGolem.ac = 17;
		stoneGolem.armorNote = "natural armor";
		stoneGolem.hitDice = new DiceRoll(17, 10, 85);
		stoneGolem.speed = 30;
		stoneGolem.abilityScores.put(Ability.STRENGTH, 22);
		stoneGolem.abilityScores.put(Ability.DEXTERITY, 9);
		stoneGolem.abilityScores.put(Ability.CONSTITUTION, 20);
		stoneGolem.abilityScores.put(Ability.INTELLIGENCE, 3);
		stoneGolem.abilityScores.put(Ability.WISDOM, 11);
		stoneGolem.abilityScores.put(Ability.CHARISMA, 1);
		stoneGolem.immunities.put("nonmagical weapons that aren't adamantine", new HashSet<>(Arrays.asList(DamageType.BLUDGEONING, DamageType.PIERCING, DamageType.SLASHING)));
		stoneGolem.immunities.put(null, new HashSet<>(Arrays.asList(DamageType.POISON, DamageType.PSYCHIC)));
		stoneGolem.conditionImmunities.addAll(Arrays.asList(Condition.CHARMED, Condition.EXHAUSTION, Condition.FRIGHTENED, Condition.PARALYZED, Condition.PETRIFIED, Condition.POISONED));
		stoneGolem.senses.put(VisionType.BLINDSIGHT, 60);
		stoneGolem.languages.add("understands the languages of its creator but can't speak");
		stoneGolem.challengeRating = 10;
		stoneGolem.features = Arrays.asList(new Feature("Immutable Form", "The golem is immune to any spell or effect that would alter its form."), //
				new Feature("Magic Resistance", "The golem has advantage on saving throws against spells and other magical effects."), //
				new Feature("Magic Weapons", "The golem's attacks are magical."));
		stoneGolem.actions = Arrays.asList(new Action("Multiattack", "The golem makes two slam attacks."), //
				new Attack("Slam", Type.MELEE_WEAPON, 10, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(3, 8, 6), DamageType.BLUDGEONING)), //
				new Action("Slow", "Recharge 5-6",
						"The golem targets one or more creatures that it can see within 10 feet of it. Each target must make a DC 17 Wisdom saving throw against this magic. On a failed save, a target can't use reactions, its speed is halved, and it can't make more than one attack on its turn. In addition, the target can take either an action or a bonus action on its turn, not both. These effects last for 1 minute. A target can repeat the saving throw at the end of each of its turns, ending the effect on itself on a success."));

		wraith = new Creature();
		wraith.name = "Wraith";
		wraith.shortName = "The wraith";
		wraith.size = Size.MEDIUM;
		wraith.type = CreatureType.UNDEAD;
		wraith.alignment = Alignment.NEUTRAL_EVIL;
		wraith.ac = 13;
		wraith.hitDice = new DiceRoll(9, 8, 27);
		wraith.speed = 0;
		wraith.speeds.put(MovementType.FLY, 60);
		wraith.abilityScores.put(Ability.STRENGTH, 6);
		wraith.abilityScores.put(Ability.DEXTERITY, 16);
		wraith.abilityScores.put(Ability.CONSTITUTION, 16);
		wraith.abilityScores.put(Ability.INTELLIGENCE, 12);
		wraith.abilityScores.put(Ability.WISDOM, 14);
		wraith.abilityScores.put(Ability.CHARISMA, 15);
		wraith.resistances.put(null, Stream.of(DamageType.ACID, DamageType.COLD, DamageType.FIRE, DamageType.LIGHTNING, DamageType.THUNDER).collect(Collectors.toSet()));
		wraith.resistances.put("nonmagical weapons that aren't silvered", new HashSet<>(Arrays.asList(DamageType.BLUDGEONING, DamageType.PIERCING, DamageType.SLASHING)));
		wraith.immunities.put(null, Stream.of(DamageType.NECROTIC, DamageType.POISON).collect(Collectors.toSet()));
		wraith.conditionImmunities.addAll(Arrays.asList(Condition.CHARMED, Condition.EXHAUSTION, Condition.GRAPPLED, Condition.PARALYZED, Condition.PETRIFIED, Condition.POISONED, Condition.PRONE, Condition.RESTRAINED));
		wraith.senses.put(VisionType.DARKVISION, 60);
		wraith.languages.add("the languages it knew in life");
		wraith.challengeRating = 5;
		wraith.features = Arrays.asList(new Feature("Incorporeal Movement", "The wraith can move through objects and other creatures as if they were difficult terrain. It takes 5 (1d10) force damage if it ends its turn inside an object."), //
				new Feature("Sunlight Sensitivity", "While in sunlight, the wraith has disadvantage on attack rolls, as well as Wisdom (Perception) checks that rely on sight."));
		wraith.actions = Arrays.asList(new Attack("Life Drain", Type.MELEE_SPELL, 4, "reach 5 ft., one creature", ". The target must succeed on a DC 14 Constitution saving throw or its hit point maximum is reduced by an amount equal to the damage taken. This reduction lasts until the creature finishes a long rest. The target dies if this effect reduces its hit point maximum to 0.", new Attack.Damage(new DiceRoll(4, 8, 3), DamageType.NECROTIC)), //
				new Action("Create Specter", "The wraith targets a humanoid within 10 feet of it that has been dead no longer than 1 minute and died violently. The target's spirit rises as a [specter] in the space of its corpse or in the nearest unoccupied space. The specter is under the wraith's control. The wraith can have no more than seven specters under its control at one time."));

		succubusIncubus = new Creature();
		succubusIncubus.name = "Succubus/Incubus";
		succubusIncubus.shortName = "The fiend";
		succubusIncubus.size = Size.MEDIUM;
		succubusIncubus.type = CreatureType.FIEND;
		succubusIncubus.subtype = "shapechanger";
		succubusIncubus.alignment = Alignment.NEUTRAL_EVIL;
		succubusIncubus.ac = 15;
		succubusIncubus.armorNote = "natural armor";
		succubusIncubus.hitDice = new DiceRoll(9, 8, 27);
		succubusIncubus.speed = 30;
		succubusIncubus.speeds.put(MovementType.FLY, 60);
		succubusIncubus.abilityScores.put(Ability.STRENGTH, 8);
		succubusIncubus.abilityScores.put(Ability.DEXTERITY, 17);
		succubusIncubus.abilityScores.put(Ability.CONSTITUTION, 13);
		succubusIncubus.abilityScores.put(Ability.INTELLIGENCE, 15);
		succubusIncubus.abilityScores.put(Ability.WISDOM, 12);
		succubusIncubus.abilityScores.put(Ability.CHARISMA, 20);
		succubusIncubus.skills.put(Skill.DECEPTION, 9);
		succubusIncubus.skills.put(Skill.INSIGHT, 5);
		succubusIncubus.skills.put(Skill.PERCEPTION, 5);
		succubusIncubus.skills.put(Skill.PERSUASION, 9);
		succubusIncubus.skills.put(Skill.STEALTH, 7);
		succubusIncubus.resistances.put(null, Stream.of(DamageType.COLD, DamageType.FIRE, DamageType.LIGHTNING, DamageType.POISON).collect(Collectors.toSet()));
		succubusIncubus.resistances.put("nonmagical weapons", new HashSet<>(Arrays.asList(DamageType.BLUDGEONING, DamageType.PIERCING, DamageType.SLASHING)));
		succubusIncubus.senses.put(VisionType.DARKVISION, 60);
		succubusIncubus.languages.addAll(Arrays.asList("Abyssal", "Common", "Infernal", "telepathy 60 ft."));
		succubusIncubus.challengeRating = 4;
		succubusIncubus.features = Arrays.asList(new Feature("Telepathic Bond", "The fiend ignores the range restriction on its telepathy when communicating with a creature it has charmed. The two don't even need to be on the same plane of existence."), //
				new Feature("Shapechanger", "The fiend can use its action to polymorph into a Small or Medium humanoid, or back into its true form. Without wings, the fiend loses its flying speed. Other than its size and speed, its statistics are the same in each form. Any equipment it is wearing or carrying isn't transformed. It reverts to its true form if it dies."));
		succubusIncubus.actions = Arrays.asList(new Attack("Claw", "Fiend Form Only", Type.MELEE_WEAPON, 5, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(1, 6, 3), DamageType.SLASHING)), //
				new Action("Charm",
						"One humanoid the fiend can see within 30 feet of it must succeed on a DC 15 Wisdom saving throw or be magically charmed for 1 day. The charmed target obeys the fiend's verbal or telepathic commands. If the target suffe rs any harm or receives a suicidal command, it can repeat the saving throw, ending the effect on a success. If the target successfully saves against the effect, or if the effect on it ends, the target is immune to this fiend's Charm for the next 24 hours.\n    The fiend can have only one target charmed at a time. If it charms another, the effect on the previous target ends."), //
				new Action("Draining Kiss", "The fiend kisses a creature charmed by it or a willing creature. The target must make a DC 15 Constitution saving throw against this magic, taking 32 (5d10 + 5) psychic damage on a failed save, or half as much damage on a successful one. The target's hit point maximum is reduced by an amount equal to the damage taken. This reduction lasts until the target finishes a long rest. The target dies if this effect reduces its hit point maximum to 0."), //
				new Action("Etherealness", "The fiend magically enters the Ethereal Plane from the Material Plane, or vice versa."));

		fireElemental = new Creature();
		fireElemental.name = "Fire Elemental";
		fireElemental.shortName = "The elemental";
		fireElemental.size = Size.LARGE;
		fireElemental.type = CreatureType.ELEMENTAL;
		fireElemental.alignment = Alignment.NEUTRAL;
		fireElemental.ac = 13;
		fireElemental.hitDice = new DiceRoll(12, 10, 36);
		fireElemental.speed = 50;
		fireElemental.abilityScores.put(Ability.STRENGTH, 10);
		fireElemental.abilityScores.put(Ability.DEXTERITY, 17);
		fireElemental.abilityScores.put(Ability.CONSTITUTION, 16);
		fireElemental.abilityScores.put(Ability.INTELLIGENCE, 6);
		fireElemental.abilityScores.put(Ability.WISDOM, 10);
		fireElemental.abilityScores.put(Ability.CHARISMA, 7);
		fireElemental.resistances.put("nonmagical weapons", new HashSet<>(Arrays.asList(DamageType.BLUDGEONING, DamageType.PIERCING, DamageType.SLASHING)));
		fireElemental.immunities.put(null, Stream.of(DamageType.FIRE, DamageType.POISON).collect(Collectors.toSet()));
		fireElemental.conditionImmunities.addAll(Arrays.asList(Condition.EXHAUSTION, Condition.GRAPPLED, Condition.PARALYZED, Condition.PETRIFIED, Condition.POISONED, Condition.PRONE, Condition.RESTRAINED, Condition.UNCONSCIOUS));
		fireElemental.senses.put(VisionType.DARKVISION, 60);
		fireElemental.languages.add("Ignan");
		fireElemental.challengeRating = 5;
		fireElemental.features = Arrays.asList(new Feature("Fire Form",
				"The elemental can move through a space as narrow as 1 inch wide without squeezing. A creature that touches the elemental or hits it with a melee attack while within 5 feet of it takes 5 (1d10) fire damage. In addition, the elemental can enter a hostile creature's space and stop there. The first time it enters a creature's space on a turn, that creature takes 5 (1d10) fire damage and catches fire; until someone takes an action to douse the fire, the creature takes 5 (1d10) fire damage at the start of each of its turns."), //
				new Feature("Illumination", "The elemental sheds bright light in a 30-foot radius and dim light in an additional 30 feet."), //
				new Feature("Water Susceptibility", "For every 5 feet the elemental moves in water, or for every ga llon of water splashed on it, it takes 1 cold damage."));
		fireElemental.actions = Arrays.asList(new Action("Multiattack", "The elemental makes two touch attacks."), //
				new Attack("Touch", Type.MELEE_WEAPON, 6, "reach 5 ft., one target", ". If the target is a creature or a flammable object, it ignites. Until a creature takes an action to douse the fire, the target takes 5 (1d10) fire damage at the start of each of its turns.", new Attack.Damage(new DiceRoll(2, 6, 3), DamageType.FIRE)));

		quasit = new Creature();
		quasit.name = "Quasit";
		quasit.shortName = "The quasit";
		quasit.size = Size.TINY;
		quasit.type = CreatureType.FIEND;
		quasit.subtype = "demon, shapechanger";
		quasit.alignment = Alignment.CHAOTIC_EVIL;
		quasit.ac = 13;
		quasit.hitDice = new DiceRoll(3, 4);
		quasit.speed = 40;
		quasit.abilityScores.put(Ability.STRENGTH, 5);
		quasit.abilityScores.put(Ability.DEXTERITY, 17);
		quasit.abilityScores.put(Ability.CONSTITUTION, 10);
		quasit.abilityScores.put(Ability.INTELLIGENCE, 7);
		quasit.abilityScores.put(Ability.WISDOM, 10);
		quasit.abilityScores.put(Ability.CHARISMA, 10);
		quasit.resistances.put(null, Stream.of(DamageType.COLD, DamageType.FIRE, DamageType.LIGHTNING).collect(Collectors.toSet()));
		quasit.resistances.put("nonmagical weapons", new HashSet<>(Arrays.asList(DamageType.BLUDGEONING, DamageType.PIERCING, DamageType.SLASHING)));
		quasit.immunities.put(null, Stream.of(DamageType.POISON).collect(Collectors.toSet()));
		quasit.conditionImmunities.add(Condition.POISONED);
		quasit.senses.put(VisionType.DARKVISION, 120);
		quasit.languages.add("Abyssal");
		quasit.languages.add("Common");
		quasit.challengeRating = 1;
		quasit.features = Arrays.asList(new Feature("Shapechanger", "The quasit can use its action to polymorph into a beast form that resembles a bat (speed 10 ft., fly 40 ft.), a centipede (40 ft., climb 40 ft.), or a toad (40 ft., swim 40 ft.), or back into its true form. Its statistics are the same in each form, except for the speed changes noted. Any equipment it is wearing or carrying isn't transformed. It reverts to its true form if it dies."), //
				new Feature("Magic Resistance", "The quasit has advantage on saving throws against spells and other magical effects."));
		quasit.actions = Arrays.asList(new Attack("Claws", "Bite in Beast Form", Type.MELEE_WEAPON, 4, "reach 5 ft., one target", ", and the target must succeed on a DC 10 Constittion saving throw or take 5 (2d4) poison damage and become poisoned for 1 minute. The target can repeat the saving throw at the end of each of its turns, with disadvantage if the quasit is within line of sight, ending the effect on itself on a success.", new Attack.Damage(new DiceRoll(1, 4, 3), DamageType.PIERCING)), //
				new Action("Scare", "1/Day", "One creature of the quasit's choice within 20 feet of it must succeed on a DC 10 Wisdom saving throw or be frightened for 1 minute. The target can repeat the saving throw at the end of each of its turns, with disadvantage if the quasit is within line of sight, ending the effect on itself on a success."), //
				new Action("Invisibility", "The quasit magically turns invisible until it attacks or uses Scare, or until its concentration ends (as if concentrating on a spell). Any equipment the quasit wears or carries is invisible with it."));

		manes = new Creature();
		manes.name = "Manes";
		manes.shortName = "The manes";
		manes.size = Size.SMALL;
		manes.type = CreatureType.FIEND;
		manes.subtype = "demon";
		manes.alignment = Alignment.CHAOTIC_EVIL;
		manes.ac = 9;
		manes.hitDice = new DiceRoll(2, 6, 2);
		manes.speed = 20;
		manes.abilityScores.put(Ability.STRENGTH, 10);
		manes.abilityScores.put(Ability.DEXTERITY, 9);
		manes.abilityScores.put(Ability.CONSTITUTION, 13);
		manes.abilityScores.put(Ability.INTELLIGENCE, 3);
		manes.abilityScores.put(Ability.WISDOM, 8);
		manes.abilityScores.put(Ability.CHARISMA, 4);
		manes.resistances.put(null, Stream.of(DamageType.COLD, DamageType.FIRE, DamageType.LIGHTNING).collect(Collectors.toSet()));
		manes.immunities.put(null, Stream.of(DamageType.POISON).collect(Collectors.toSet()));
		manes.conditionImmunities.add(Condition.CHARMED);
		manes.conditionImmunities.add(Condition.FRIGHTENED);
		manes.conditionImmunities.add(Condition.POISONED);
		manes.senses.put(VisionType.DARKVISION, 60);
		manes.languages.add("understands Abyssal but can't speak");
		manes.challengeRating = -3;
		manes.actions = Arrays.asList(new Attack("Claws", Type.MELEE_WEAPON, 2, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(2, 4), DamageType.SLASHING)));

		hezrou = new Creature();
		hezrou.name = "Hezrou";
		hezrou.shortName = "The hezrou";
		hezrou.size = Size.LARGE;
		hezrou.type = CreatureType.FIEND;
		hezrou.subtype = "demon";
		hezrou.alignment = Alignment.CHAOTIC_EVIL;
		hezrou.ac = 16;
		hezrou.armorNote = "natural armor";
		hezrou.hitDice = new DiceRoll(13, 10, 65);
		hezrou.speed = 30;
		hezrou.abilityScores.put(Ability.STRENGTH, 19);
		hezrou.abilityScores.put(Ability.DEXTERITY, 17);
		hezrou.abilityScores.put(Ability.CONSTITUTION, 20);
		hezrou.abilityScores.put(Ability.INTELLIGENCE, 5);
		hezrou.abilityScores.put(Ability.WISDOM, 12);
		hezrou.abilityScores.put(Ability.CHARISMA, 13);
		hezrou.savingThrows.put(Ability.STRENGTH, 7);
		hezrou.savingThrows.put(Ability.CONSTITUTION, 8);
		hezrou.savingThrows.put(Ability.WISDOM, 4);
		hezrou.resistances.put(null, Stream.of(DamageType.COLD, DamageType.FIRE, DamageType.LIGHTNING).collect(Collectors.toSet()));
		hezrou.resistances.put("nonmagical weapons", Stream.of(DamageType.BLUDGEONING, DamageType.PIERCING, DamageType.SLASHING).collect(Collectors.toSet()));
		hezrou.immunities.put(null, Stream.of(DamageType.POISON).collect(Collectors.toSet()));
		hezrou.conditionImmunities.add(Condition.POISONED);
		hezrou.senses.put(VisionType.DARKVISION, 120);
		hezrou.languages.add("Abyssal");
		hezrou.languages.add("telepathy 120 ft.");
		hezrou.challengeRating = 8;
		hezrou.features = Arrays.asList(new Feature("Magic Resistance", "The hezrou has advantage on saving throws against spells and other magical effects."), //
				new Feature("Stench", "Any creature that starts its turn within 10 feet of the hezrou must succeed on a DC 14 Constitution saving throw or be poisoned until the start of its next turn. On a successful saving throw, the creature is immune to the hezrou's stench for 24 hours."));
		hezrou.actions = Arrays.asList(new Action("Multiattack", "The hezrou makes three attacks: one with its bite, and two with its claws."), //
				new Attack("Bite", Type.MELEE_WEAPON, 7, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(2, 10, 4), DamageType.PIERCING)), //
				new Attack("Claw", Type.MELEE_WEAPON, 7, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(2, 6, 4), DamageType.SLASHING)));

		vrock = new Creature();
		vrock.name = "Vrock";
		vrock.shortName = "The vrock";
		vrock.size = Size.LARGE;
		vrock.type = CreatureType.FIEND;
		vrock.subtype = "demon";
		vrock.alignment = Alignment.CHAOTIC_EVIL;
		vrock.ac = 15;
		vrock.armorNote = "natural armor";
		vrock.hitDice = new DiceRoll(11, 10, 44);
		vrock.speed = 40;
		vrock.speeds.put(MovementType.FLY, 60);
		vrock.abilityScores.put(Ability.STRENGTH, 17);
		vrock.abilityScores.put(Ability.DEXTERITY, 15);
		vrock.abilityScores.put(Ability.CONSTITUTION, 18);
		vrock.abilityScores.put(Ability.INTELLIGENCE, 8);
		vrock.abilityScores.put(Ability.WISDOM, 13);
		vrock.abilityScores.put(Ability.CHARISMA, 8);
		vrock.savingThrows.put(Ability.DEXTERITY, 5);
		vrock.savingThrows.put(Ability.WISDOM, 4);
		vrock.savingThrows.put(Ability.CHARISMA, 2);
		vrock.resistances.put(null, Stream.of(DamageType.COLD, DamageType.FIRE, DamageType.LIGHTNING).collect(Collectors.toSet()));
		vrock.resistances.put("nonmagical weapons", Stream.of(DamageType.BLUDGEONING, DamageType.PIERCING, DamageType.SLASHING).collect(Collectors.toSet()));
		vrock.immunities.put(null, Stream.of(DamageType.POISON).collect(Collectors.toSet()));
		vrock.conditionImmunities.add(Condition.POISONED);
		vrock.senses.put(VisionType.DARKVISION, 120);
		vrock.languages.add("Abyssal");
		vrock.languages.add("telepathy 120 ft.");
		vrock.challengeRating = 6;
		vrock.features = Arrays.asList(new Feature("Magic Resistance", "The vrock has advantage on saving throws against spells and other magical effects."));
		vrock.actions = Arrays.asList(new Action("Multiattack", "The vrock makes two attacks: one with its beak, and one with its talons."), //
				new Attack("Beak", Type.MELEE_WEAPON, 7, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(2, 6, 3), DamageType.PIERCING)), //
				new Attack("Claw", Type.MELEE_WEAPON, 7, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(2, 10, 3), DamageType.SLASHING)), //
				new Action("Spores", "Recharge 6",
						"A 15-foot-radius cloud of toxic spores extends out from the vrock. The spores spread around corners. Each creature in that area must succeed on a DC 14 Constitution saving throw or become poisoned. While poisoned in this way, a target takes 5 (1d10) poison damage at the start of each of its turns. A target can repeat the saving throw at the end of each of its turns, ending the effect on itself on a success. Emptying a vial of holy water on the target also ends the effect on it."), //
				new Action("Stunning Screech", "1/Day", "The vrock emits a horrific screech. Each creature within 20 feet of it that can hear it and that isn't a demon must succeed on a DC 14 Constitution saving throw or be stunned until the end of the vrock's next turn."));

		glabrezu = new Creature();
		glabrezu.name = "Glabrezu";
		glabrezu.shortName = "The glabrezu";
		glabrezu.size = Size.LARGE;
		glabrezu.type = CreatureType.FIEND;
		glabrezu.subtype = "demon";
		glabrezu.alignment = Alignment.CHAOTIC_EVIL;
		glabrezu.ac = 17;
		glabrezu.armorNote = "natural armor";
		glabrezu.hitDice = new DiceRoll(15, 10, 75);
		glabrezu.speed = 40;
		glabrezu.abilityScores.put(Ability.STRENGTH, 20);
		glabrezu.abilityScores.put(Ability.DEXTERITY, 15);
		glabrezu.abilityScores.put(Ability.CONSTITUTION, 21);
		glabrezu.abilityScores.put(Ability.INTELLIGENCE, 19);
		glabrezu.abilityScores.put(Ability.WISDOM, 17);
		glabrezu.abilityScores.put(Ability.CHARISMA, 16);
		glabrezu.savingThrows.put(Ability.STRENGTH, 9);
		glabrezu.savingThrows.put(Ability.CONSTITUTION, 9);
		glabrezu.savingThrows.put(Ability.WISDOM, 7);
		glabrezu.savingThrows.put(Ability.CHARISMA, 7);
		glabrezu.resistances.put(null, Stream.of(DamageType.COLD, DamageType.FIRE, DamageType.LIGHTNING).collect(Collectors.toSet()));
		glabrezu.resistances.put("nonmagical weapons", Stream.of(DamageType.BLUDGEONING, DamageType.PIERCING, DamageType.SLASHING).collect(Collectors.toSet()));
		glabrezu.immunities.put(null, Stream.of(DamageType.POISON).collect(Collectors.toSet()));
		glabrezu.conditionImmunities.add(Condition.POISONED);
		glabrezu.senses.put(VisionType.DARKVISION, 120);
		glabrezu.languages.add("Abyssal");
		glabrezu.languages.add("telepathy 120 ft.");
		glabrezu.challengeRating = 9;

		final Map<String, Map<String, String>> glabrezuSpells = new LinkedHashMap<>();

		final Map<String, String> glabrezuAtWill = new HashMap<>();
		Stream.of("darkness", "detect magic", "dispel magic").forEach(s -> glabrezuAtWill.put(s, ""));
		glabrezuSpells.put("At will", glabrezuAtWill);

		final Map<String, String> glabrezu1Day = new HashMap<>();
		Stream.of("confusion", "fly", "power word stun").forEach(s -> glabrezu1Day.put(s, ""));
		glabrezuSpells.put("1/day each", glabrezu1Day);

		glabrezu.features = Arrays.asList(new Feature("Magic Resistance", "The glabrezu has advantage on saving throws against spells and other magical effects."), //
				new InnateSpellcasting(null, "The glabrezu", Ability.CHARISMA, 16, Integer.MIN_VALUE, "it", null, glabrezuSpells));
		glabrezu.actions = Arrays.asList(new Action("Multiattack", "The glabrezu makes four attacks: two with its pincers and two with its fist s. Alternatively, it makes two attacks with its pincers and casts one spell."), //
				new Attack("Pincer", Type.MELEE_WEAPON, 9, "reach 10 ft., one target", ". If the target is a Medium or smaller creature, it is grappled (escape DC 15). The glabrezu has two pincers, each of which can grapple only one target.", new Attack.Damage(new DiceRoll(2, 10, 5), DamageType.BLUDGEONING)), //
				new Attack("Fist", Type.MELEE_WEAPON, 9, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(2, 4, 2), DamageType.BLUDGEONING)));

		giantSpider = new Creature();
		giantSpider.name = "Giant Spider";
		giantSpider.shortName = "The spider";
		giantSpider.size = Size.LARGE;
		giantSpider.type = CreatureType.BEAST;
		giantSpider.alignment = Alignment.UNALIGNED;
		giantSpider.ac = 14;
		giantSpider.armorNote = "natural armor";
		giantSpider.hitDice = new DiceRoll(4, 10, 4);
		giantSpider.speed = 30;
		giantSpider.speeds.put(MovementType.CLIMB, 30);
		giantSpider.abilityScores.put(Ability.STRENGTH, 14);
		giantSpider.abilityScores.put(Ability.DEXTERITY, 16);
		giantSpider.abilityScores.put(Ability.CONSTITUTION, 12);
		giantSpider.abilityScores.put(Ability.INTELLIGENCE, 2);
		giantSpider.abilityScores.put(Ability.WISDOM, 11);
		giantSpider.abilityScores.put(Ability.CHARISMA, 4);
		giantSpider.skills.put(Skill.STEALTH, 7);
		giantSpider.senses.put(VisionType.BLINDSIGHT, 10);
		giantSpider.senses.put(VisionType.DARKVISION, 60);
		giantSpider.challengeRating = 1;
		giantSpider.features = Arrays.asList(new Feature("Spider Climb", "The spider can climb difficult surfaces, including upside-down on ceilings, without needing to make an ability check."), //
				new Feature("Web Sense", "While in contact with a web, the spider knows the exact location of any other creature in contact with the same web."), //
				new Feature("Web Walker", "The spider ignores movement restrictions caused by webbing."));
		giantSpider.actions = Arrays.asList(new Attack("Bite", Type.MELEE_WEAPON, 5, "reach 5 ft., one creature", ", and the target must make a DC 11 Constitution saving throw, taking 9 (2d8) poison damage on a failed save, or half as much damage on a successful one. If the poison damage reduces the target to 0 hit points, the target is stable but poisoned for 1 hour, even after regaining hit points, and is paralyzed while poisoned this way.", new Attack.Damage(new DiceRoll(1, 8, 3), DamageType.PIERCING)), //
				new Attack("Web", "Recharge 5-6", Type.RANGED_WEAPON, 5, "range 30/60 ft., one creature", "The target is restrained by webbing. As an action, the target can make a DC 12 Strength check, bursting the webbing on a success. The webbing can also be destroyed (AC 10; hp 5; vulnerability to fire damage; immunity to bludgeoning, poison, and psychic damage)."));

		giantCentipede = new Creature();
		giantCentipede.name = "Giant Centipede";
		giantCentipede.shortName = "The centipede";
		giantCentipede.size = Size.SMALL;
		giantCentipede.type = CreatureType.BEAST;
		giantCentipede.alignment = Alignment.UNALIGNED;
		giantCentipede.ac = 13;
		giantCentipede.armorNote = "natural armor";
		giantCentipede.hitDice = new DiceRoll(1, 6, 1);
		giantCentipede.speed = 30;
		giantCentipede.speeds.put(MovementType.CLIMB, 30);
		giantCentipede.abilityScores.put(Ability.STRENGTH, 5);
		giantCentipede.abilityScores.put(Ability.DEXTERITY, 14);
		giantCentipede.abilityScores.put(Ability.CONSTITUTION, 12);
		giantCentipede.abilityScores.put(Ability.INTELLIGENCE, 1);
		giantCentipede.abilityScores.put(Ability.WISDOM, 7);
		giantCentipede.abilityScores.put(Ability.CHARISMA, 3);
		giantCentipede.senses.put(VisionType.BLINDSIGHT, 30);
		giantCentipede.challengeRating = -2;
		giantCentipede.actions = Arrays.asList(new Attack("Bite", Type.MELEE_WEAPON, 4, "reach 5 ft., one creature", ", and the target must succeed on a DC 11 Constitution saving throw or taking 9 (2d8) poison damage. If the poison damage reduces the target to 0 hit points, the target is stable but poisoned for 1 hour, even after regaining hit points, and is paralyzed while poisoned this way.", new Attack.Damage(new DiceRoll(1, 8, 3), DamageType.PIERCING)));

		remorhaz = new Creature();
		remorhaz.name = "Remorhaz";
		remorhaz.shortName = "The remorhaz";
		remorhaz.size = Size.HUGE;
		remorhaz.type = CreatureType.MONSTROSITY;
		remorhaz.alignment = Alignment.UNALIGNED;
		remorhaz.ac = 17;
		remorhaz.armorNote = "natural armor";
		remorhaz.hitDice = new DiceRoll(17, 12, 85);
		remorhaz.speed = 30;
		remorhaz.speeds.put(MovementType.BURROW, 20);
		remorhaz.abilityScores.put(Ability.STRENGTH, 24);
		remorhaz.abilityScores.put(Ability.DEXTERITY, 13);
		remorhaz.abilityScores.put(Ability.CONSTITUTION, 21);
		remorhaz.abilityScores.put(Ability.INTELLIGENCE, 4);
		remorhaz.abilityScores.put(Ability.WISDOM, 10);
		remorhaz.abilityScores.put(Ability.CHARISMA, 5);
		remorhaz.immunities.put(null, Stream.of(DamageType.COLD, DamageType.FIRE).collect(Collectors.toSet()));
		remorhaz.senses.put(VisionType.DARKVISION, 60);
		remorhaz.senses.put(VisionType.TREMORSENSE, 60);
		remorhaz.challengeRating = 11;
		remorhaz.features = Arrays.asList(new Feature("Heated Body", "A creature that touches the remorhaz or hits it with a melee attack while within 5 feet of it takes 10 (3d6) fire damage."));
		remorhaz.actions = Arrays.asList(new Attack("Bite", Type.MELEE_WEAPON, 11, "reach 10 ft., one target", ". If the target is a creature, it is grappled (escape DC 17). Until this grapple ends, the target is restrained, and the remorhaz can't bite another target.", new Attack.Damage(new DiceRoll(6, 10, 7), DamageType.PIERCING), new Attack.Damage(new DiceRoll(3, 6), DamageType.FIRE)), //
				new Action("Swallow",
						"The remorhaz makes one bite attack against a Medium or smaller creature it is grappling. If the attack hits, that creature takes the bite's damage and is swallowed, and the grapple ends. While swallowed, the creature is blinded and restrained, it has total cover against attacks and other effects outside the remorhaz, and it takes 21 (6d6) acid damage at the start of each of the remorhaz's turns.\n    If the remorhaz takes 30 damage or more on a single turn from a creature inside it, the remorhaz must succeed on a DC 15 Constitution saving throw at the end ofthat turn or regurgitate all swallowed creatures, which fall prone in a space within 10 feet oft he remorhaz. If the remorhaz dies, a swallowed creature is no longer restrained by it and can escape from the corpse using 15 feet of movement, exiting prone."));

		youngRemorhaz = new Creature();
		youngRemorhaz.name = "Young Remorhaz";
		youngRemorhaz.shortName = "The remorhaz";
		youngRemorhaz.size = Size.LARGE;
		youngRemorhaz.type = CreatureType.MONSTROSITY;
		youngRemorhaz.alignment = Alignment.UNALIGNED;
		youngRemorhaz.ac = 14;
		youngRemorhaz.armorNote = "natural armor";
		youngRemorhaz.hitDice = new DiceRoll(11, 10, 33);
		youngRemorhaz.speed = 30;
		youngRemorhaz.speeds.put(MovementType.BURROW, 20);
		youngRemorhaz.abilityScores.put(Ability.STRENGTH, 18);
		youngRemorhaz.abilityScores.put(Ability.DEXTERITY, 13);
		youngRemorhaz.abilityScores.put(Ability.CONSTITUTION, 17);
		youngRemorhaz.abilityScores.put(Ability.INTELLIGENCE, 3);
		youngRemorhaz.abilityScores.put(Ability.WISDOM, 10);
		youngRemorhaz.abilityScores.put(Ability.CHARISMA, 4);
		youngRemorhaz.immunities.put(null, Stream.of(DamageType.COLD, DamageType.FIRE).collect(Collectors.toSet()));
		youngRemorhaz.senses.put(VisionType.DARKVISION, 60);
		youngRemorhaz.senses.put(VisionType.TREMORSENSE, 60);
		youngRemorhaz.challengeRating = 5;
		youngRemorhaz.features = Arrays.asList(new Feature("Heated Body", "A creature that touches the remorhaz or hits it with a melee attack while within 5 feet of it takes 7 (2d6) fire damage."));
		youngRemorhaz.actions = Arrays.asList(new Attack("Bite", Type.MELEE_WEAPON, 6, "reach 10 ft., one target", null, new Attack.Damage(new DiceRoll(3, 10, 4), DamageType.PIERCING), new Attack.Damage(new DiceRoll(2, 6), DamageType.FIRE)));

		basilisk = new Creature();
		basilisk.name = "Basilisk";
		basilisk.shortName = "The basilisk";
		basilisk.size = Size.MEDIUM;
		basilisk.type = CreatureType.MONSTROSITY;
		basilisk.alignment = Alignment.UNALIGNED;
		basilisk.ac = 15;
		basilisk.armorNote = "natural armor";
		basilisk.hitDice = new DiceRoll(8, 8, 16);
		basilisk.speed = 20;
		basilisk.abilityScores.put(Ability.STRENGTH, 16);
		basilisk.abilityScores.put(Ability.DEXTERITY, 8);
		basilisk.abilityScores.put(Ability.CONSTITUTION, 15);
		basilisk.abilityScores.put(Ability.INTELLIGENCE, 2);
		basilisk.abilityScores.put(Ability.WISDOM, 8);
		basilisk.abilityScores.put(Ability.CHARISMA, 7);
		basilisk.senses.put(VisionType.DARKVISION, 60);
		basilisk.challengeRating = 3;
		basilisk.features = Arrays.asList(new Feature("Petrifying Gaze",
				"If a creature starts its turn within 30 feet of the basilisk and the two of them can see each other, the basilisk can force the creature to make a DC 12 Constitution saving throw if the basilisk isn't incapacitated. On a failed save, the creature magically begins to turn to stone and is restrained. It must repeat the saving throw at the end of its next turn. On a success, the effect ends. On a failure, the creature is petrified until freed by the [greater restoration] spell or other magic.\n    A creature that isn't surprised can avert its eyes to avoid the saving throw at the start of its turn. If it does so, it can't see the basilisk until the start of its next turn, when it can avert its eyes again. If it looks at the basilisk in the meantime, it must immediately make the save.\n    If the basilisk sees its reflection within 30 feet of it in bright light, it mistakes itself for a rival and targets itself with its gaze."));
		basilisk.actions = Arrays.asList(new Attack("Bite", Type.MELEE_WEAPON, 5, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(2, 6, 3), DamageType.PIERCING), new Attack.Damage(new DiceRoll(2, 6), DamageType.POISON)));

		darkmantle = new Creature();
		darkmantle.name = "Darkmantle";
		darkmantle.shortName = "The darkmantle";
		darkmantle.size = Size.SMALL;
		darkmantle.type = CreatureType.MONSTROSITY;
		darkmantle.alignment = Alignment.UNALIGNED;
		darkmantle.ac = 11;
		darkmantle.hitDice = new DiceRoll(5, 6, 5);
		darkmantle.speed = 10;
		darkmantle.speeds.put(MovementType.FLY, 30);
		darkmantle.abilityScores.put(Ability.STRENGTH, 16);
		darkmantle.abilityScores.put(Ability.DEXTERITY, 12);
		darkmantle.abilityScores.put(Ability.CONSTITUTION, 13);
		darkmantle.abilityScores.put(Ability.INTELLIGENCE, 2);
		darkmantle.abilityScores.put(Ability.WISDOM, 10);
		darkmantle.abilityScores.put(Ability.CHARISMA, 5);
		darkmantle.skills.put(Skill.STEALTH, 3);
		darkmantle.senses.put(VisionType.BLINDSIGHT, 60);
		darkmantle.challengeRating = -1;
		darkmantle.features = Arrays.asList(new Feature("Echolocation", "The darkmantle can't use its blindsight while deafened."), //
				new Feature("False Appearance", "While the darkmantle remains motionless, it is indistingushable from a cave formation such as a stalactite or stalagmite."));
		darkmantle.actions = Arrays.asList(new Attack("Crush", Type.MELEE_WEAPON, 5, "reach 5 ft., one creature",
				", and the darkmantle attaches to the target. If the target is Medium or smaller and the darkmantle has advantage on the attack roll, it attaches by engulfing the target's head, and the target is also blinded and unable to breathe while the darkmantle is attached in this way.\n    While attached to the target, the darkmantle can attack no other creature except the target but has advantage on its attack rolls. The darkmantle's speed also becomes 0, it can't benefit from any bonus to its speed, and it moves with the target.\n    A creature can detach the darkmantle by making a successful DC l3 Strength check as an action. On its turn, the darkmantle can detach itself from the target by using 5 feet of movement.",
				new Attack.Damage(new DiceRoll(1, 6, 3), DamageType.BLUDGEONING)),
				new Action("Darkness Aura", "1/Day",
						"A 15-foot radius of magical darkness extends out from the darkmantle, moves with it, and spreads around corners. The darkness lasts as long as the darkmantle mai ntains concentration, up to 10 min utes (as if concentrating on a spell). Darkvision can't penetrate this darkness , and no natural light can illuminate it. If any of the darkness overlaps with an area of light created by a spell of 2nd level or lower, the spell creating the light is dispelled."));

		ettercap = new Creature();
		ettercap.name = "Ettercap";
		ettercap.shortName = "The ettercap";
		ettercap.size = Size.MEDIUM;
		ettercap.type = CreatureType.MONSTROSITY;
		ettercap.alignment = Alignment.NEUTRAL_EVIL;
		ettercap.ac = 13;
		ettercap.armorNote = "natural armor";
		ettercap.hitDice = new DiceRoll(8, 8, 8);
		ettercap.speed = 30;
		ettercap.speeds.put(MovementType.FLY, 30);
		ettercap.abilityScores.put(Ability.STRENGTH, 14);
		ettercap.abilityScores.put(Ability.DEXTERITY, 15);
		ettercap.abilityScores.put(Ability.CONSTITUTION, 13);
		ettercap.abilityScores.put(Ability.INTELLIGENCE, 7);
		ettercap.abilityScores.put(Ability.WISDOM, 12);
		ettercap.abilityScores.put(Ability.CHARISMA, 8);
		ettercap.skills.put(Skill.PERCEPTION, 3);
		ettercap.skills.put(Skill.STEALTH, 4);
		ettercap.skills.put(Skill.SURVIVAL, 3);
		ettercap.senses.put(VisionType.DARKVISION, 60);
		ettercap.challengeRating = 2;
		ettercap.features = Arrays.asList(new Feature("Spider Climb", "The ettercap can climb difficult surfaces, including upside down on ceilings, without needing to make an ability check."), //
				new Feature("Web Sense", "While in contact with a web, the ettercap knows the exact location of any other creature in contact with the same web."), //
				new Feature("Web Walker", "The ettercap ignores movement restrictions caused by webbing."));
		ettercap.actions = Arrays.asList(new Action("Multiattack", "The ettercap makes two attacks: one with its bite, and one with its claws."), //
				new Attack("Bite", Type.MELEE_WEAPON, 4, "reach 5 ft., one creature", ". The target must succeed on a DC 11 Constitution saving throw or become poisoned for 1 minute. The creature can repeat the saving throw at the end of each of its turns, ending the effect on itself on a success.", new Attack.Damage(new DiceRoll(1, 8, 3), DamageType.PIERCING), new Attack.Damage(new DiceRoll(1, 8), DamageType.POISON)), //
				new Attack("Claws", Type.MELEE_WEAPON, 4, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(2, 4, 2), DamageType.SLASHING)), //
				new Attack("Web", "Recharge 5-6", Type.RANGED_WEAPON, 5, "range 30/60 ft., one Large or smaller creature", "The target is restrained by webbing. As an action, the target can make a DC 11 Strength check, escaping from the webbing on a success. The effect ends if the webbing is destroyed. The webbing has AC 10, 5 hit points, resistance to bludgeoning damage, and immunity to poison, and psychic damage)."));

		carrionCrawler = new Creature();
		carrionCrawler.name = "Carrion Crawler";
		carrionCrawler.shortName = "The carrion crawler";
		carrionCrawler.size = Size.LARGE;
		carrionCrawler.type = CreatureType.MONSTROSITY;
		carrionCrawler.alignment = Alignment.UNALIGNED;
		carrionCrawler.ac = 13;
		carrionCrawler.armorNote = "natural armor";
		carrionCrawler.hitDice = new DiceRoll(6, 10, 18);
		carrionCrawler.speed = 30;
		carrionCrawler.speeds.put(MovementType.CLIMB, 30);
		carrionCrawler.abilityScores.put(Ability.STRENGTH, 14);
		carrionCrawler.abilityScores.put(Ability.DEXTERITY, 13);
		carrionCrawler.abilityScores.put(Ability.CONSTITUTION, 16);
		carrionCrawler.abilityScores.put(Ability.INTELLIGENCE, 1);
		carrionCrawler.abilityScores.put(Ability.WISDOM, 12);
		carrionCrawler.abilityScores.put(Ability.CHARISMA, 5);
		carrionCrawler.skills.put(Skill.PERCEPTION, 3);
		carrionCrawler.senses.put(VisionType.DARKVISION, 60);
		carrionCrawler.challengeRating = 2;
		carrionCrawler.features = Arrays.asList(new Feature("Keen Smell", "The carrion crawler has advantage on Wisdom (Perception) checks that rely on smell."), //
				new Feature("Spider Climb", "The carrion crawler can climb difficult surfaces, including upside down on ceilings, without needing to make an ability check."));
		carrionCrawler.actions = Arrays.asList(new Action("Multiattack", "The carrion crawler makes two attacks: one with its tentacles and one with its bite."), //
				new Attack("Tentacles", Type.MELEE_WEAPON, 8, "reach 10 ft., one creature", ", and the target must succeed on a DC 13 Constitution saving throw or become poisoned for 1 minute. Until this poison ends, the target is paralyzed. The creature can repeat the saving throw at the end of each of its turns, ending the effect on itself on a success.", new Attack.Damage(new DiceRoll(1, 4, 2), DamageType.PIERCING)), //
				new Attack("Bite", Type.MELEE_WEAPON, 4, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(2, 4, 2), DamageType.PIERCING)));

		hookHorror = new Creature();
		hookHorror.name = "Hook Horror";
		hookHorror.shortName = "The hook horror";
		hookHorror.size = Size.LARGE;
		hookHorror.type = CreatureType.MONSTROSITY;
		hookHorror.alignment = Alignment.NEUTRAL;
		hookHorror.ac = 15;
		hookHorror.armorNote = "natural armor";
		hookHorror.hitDice = new DiceRoll(10, 10, 20);
		hookHorror.speed = 30;
		hookHorror.speeds.put(MovementType.CLIMB, 30);
		hookHorror.abilityScores.put(Ability.STRENGTH, 18);
		hookHorror.abilityScores.put(Ability.DEXTERITY, 10);
		hookHorror.abilityScores.put(Ability.CONSTITUTION, 15);
		hookHorror.abilityScores.put(Ability.INTELLIGENCE, 6);
		hookHorror.abilityScores.put(Ability.WISDOM, 12);
		hookHorror.abilityScores.put(Ability.CHARISMA, 7);
		hookHorror.skills.put(Skill.PERCEPTION, 3);
		hookHorror.senses.put(VisionType.BLINDSIGHT, 60);
		hookHorror.senses.put(VisionType.DARKVISION, 10);
		hookHorror.languages.add("Hook Horror");
		hookHorror.challengeRating = 3;
		hookHorror.features = Arrays.asList(new Feature("Echolocation", "The hook horror can't use its blindsight while deafened."), //
				new Feature("Keen Hearing", "The hook horror has advantage on Wisdom (Perception) checks that rely on hearing."));
		hookHorror.actions = Arrays.asList(new Action("Multiattack", "The hook horror makes two hook attacks."), //
				new Attack("Hook", Type.MELEE_WEAPON, 6, "reach 10 ft., one target", null, new Attack.Damage(new DiceRoll(2, 6, 4), DamageType.PIERCING)));

		wyvern = new Creature();
		wyvern.name = "Wyvern";
		wyvern.shortName = "The wyvern";
		wyvern.size = Size.LARGE;
		wyvern.type = CreatureType.DRAGON;
		wyvern.alignment = Alignment.UNALIGNED;
		wyvern.ac = 13;
		wyvern.armorNote = "natural armor";
		wyvern.hitDice = new DiceRoll(13, 10, 39);
		wyvern.speed = 20;
		wyvern.speeds.put(MovementType.FLY, 80);
		wyvern.abilityScores.put(Ability.STRENGTH, 19);
		wyvern.abilityScores.put(Ability.DEXTERITY, 10);
		wyvern.abilityScores.put(Ability.CONSTITUTION, 16);
		wyvern.abilityScores.put(Ability.INTELLIGENCE, 5);
		wyvern.abilityScores.put(Ability.WISDOM, 12);
		wyvern.abilityScores.put(Ability.CHARISMA, 6);
		wyvern.skills.put(Skill.PERCEPTION, 4);
		wyvern.senses.put(VisionType.DARKVISION, 60);
		wyvern.challengeRating = 6;
		wyvern.actions = Arrays.asList(new Action("Multiattack", "The wyvern makes two attacks: one with its bite and one with its stinger. While flying, it can use its claws in place of one other attack."), //
				new Attack("Bite", Type.MELEE_WEAPON, 7, "reach 10 ft., one target", null, new Attack.Damage(new DiceRoll(2, 6, 4), DamageType.PIERCING)), //
				new Attack("Claws", Type.MELEE_WEAPON, 7, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(2, 8, 4), DamageType.SLASHING)), //
				new Attack("Stinger", Type.MELEE_WEAPON, 7, "reach 10 ft., one target", ". The target mest make a DC 15 Constitution saving throw, taking 24 (7d6) poison damage on a failed save, or half as much damage on a successful one.", new Attack.Damage(new DiceRoll(2, 6, 4), DamageType.PIERCING)));

		blackDragonWyrmling = new Creature();
		blackDragonWyrmling.name = "Black Dragon Wyrmling";
		blackDragonWyrmling.shortName = "The dragon";
		blackDragonWyrmling.size = Size.MEDIUM;
		blackDragonWyrmling.type = CreatureType.DRAGON;
		blackDragonWyrmling.alignment = Alignment.UNALIGNED;
		blackDragonWyrmling.ac = 17;
		blackDragonWyrmling.armorNote = "natural armor";
		blackDragonWyrmling.hitDice = new DiceRoll(6, 8, 6);
		blackDragonWyrmling.speed = 30;
		blackDragonWyrmling.speeds.put(MovementType.FLY, 60);
		blackDragonWyrmling.speeds.put(MovementType.SWIM, 30);
		blackDragonWyrmling.abilityScores.put(Ability.STRENGTH, 15);
		blackDragonWyrmling.abilityScores.put(Ability.DEXTERITY, 14);
		blackDragonWyrmling.abilityScores.put(Ability.CONSTITUTION, 13);
		blackDragonWyrmling.abilityScores.put(Ability.INTELLIGENCE, 10);
		blackDragonWyrmling.abilityScores.put(Ability.WISDOM, 11);
		blackDragonWyrmling.abilityScores.put(Ability.CHARISMA, 13);
		blackDragonWyrmling.savingThrows.put(Ability.DEXTERITY, 4);
		blackDragonWyrmling.savingThrows.put(Ability.CONSTITUTION, 3);
		blackDragonWyrmling.savingThrows.put(Ability.WISDOM, 2);
		blackDragonWyrmling.savingThrows.put(Ability.CHARISMA, 3);
		blackDragonWyrmling.skills.put(Skill.PERCEPTION, 4);
		blackDragonWyrmling.skills.put(Skill.STEALTH, 4);
		blackDragonWyrmling.immunities.put(null, Stream.of(DamageType.ACID).collect(Collectors.toSet()));
		blackDragonWyrmling.senses.put(VisionType.BLINDSIGHT, 10);
		blackDragonWyrmling.senses.put(VisionType.DARKVISION, 60);
		blackDragonWyrmling.languages.add("Draconic");
		blackDragonWyrmling.challengeRating = 2;
		blackDragonWyrmling.features = Arrays.asList(new Feature("Amphibious", "The dragon can breathe air and water."));
		blackDragonWyrmling.actions = Arrays.asList(new Attack("Bite", Type.MELEE_WEAPON, 4, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(1, 10, 2), DamageType.PIERCING), new Attack.Damage(new DiceRoll(1, 4), DamageType.ACID)), //
				new Action("Acid Breath", "Recharge 5-6", "The dragon exhales acid in a 15-foot line that is 5 feet wide. Each creature in that line must make a DC 11 Dexterity saving throw, taking 22 (5d8) acid damage on a failed save, or half as much damage on a successful one."));

		dragonTurtle = new Creature();
		dragonTurtle.name = "Dragon Turtle";
		dragonTurtle.shortName = "The dragon turtle";
		dragonTurtle.size = Size.GARGANTUAN;
		dragonTurtle.type = CreatureType.DRAGON;
		dragonTurtle.alignment = Alignment.NEUTRAL;
		dragonTurtle.ac = 20;
		dragonTurtle.armorNote = "natural armor";
		dragonTurtle.hitDice = new DiceRoll(22, 20, 110);
		dragonTurtle.speed = 20;
		dragonTurtle.speeds.put(MovementType.SWIM, 40);
		dragonTurtle.abilityScores.put(Ability.STRENGTH, 25);
		dragonTurtle.abilityScores.put(Ability.DEXTERITY, 10);
		dragonTurtle.abilityScores.put(Ability.CONSTITUTION, 20);
		dragonTurtle.abilityScores.put(Ability.INTELLIGENCE, 10);
		dragonTurtle.abilityScores.put(Ability.WISDOM, 12);
		dragonTurtle.abilityScores.put(Ability.CHARISMA, 12);
		dragonTurtle.savingThrows.put(Ability.DEXTERITY, 5);
		dragonTurtle.savingThrows.put(Ability.CONSTITUTION, 10);
		dragonTurtle.savingThrows.put(Ability.WISDOM, 6);
		dragonTurtle.resistances.put(null, Stream.of(DamageType.FIRE).collect(Collectors.toSet()));
		dragonTurtle.senses.put(VisionType.DARKVISION, 120);
		dragonTurtle.languages.add("Aquan");
		dragonTurtle.languages.add("Draconic");
		dragonTurtle.challengeRating = 17;
		dragonTurtle.features = Arrays.asList(new Feature("Amphibious", "The dragon turtle can breathe air and water."));
		dragonTurtle.actions = Arrays.asList(new Action("Multiattack", "The dragon turtle makes three attacks: one with its bite, and two with its claws. It can make one tail attack in place of its two claw attacks."), //
				new Attack("Bite", Type.MELEE_WEAPON, 12, "reach 15 ft., one target", null, new Attack.Damage(new DiceRoll(3, 12, 7), DamageType.PIERCING)), //
				new Attack("Claw", Type.MELEE_WEAPON, 12, "reach 10 ft., one target", null, new Attack.Damage(new DiceRoll(2, 8, 7), DamageType.SLASHING)), //
				new Attack("Tail", Type.MELEE_WEAPON, 12, "reach 15 ft., one target", ". If the target is a creature, it must succeed on a DC 20 Strength saving throw or be pushed up to 10 feet away from the dragon turtle and be knocked prone.", new Attack.Damage(new DiceRoll(3, 12, 7), DamageType.BLUDGEONING)), //
				new Action("Steam Breath", "Recharge 5-6", "The dragon exhales scalding steam in a 60-foot cone. Each creature in that area must make a DC 18 Constitution saving throw, taking 52 (15d6) fire damage on a failed save, or half as much damage on a successful one. Being underwater doesn't grant resistance against this damage."));

		spiritNaga = new Creature();
		spiritNaga.name = "Spirit Naga";
		spiritNaga.shortName = "The naga";
		spiritNaga.size = Size.LARGE;
		spiritNaga.type = CreatureType.MONSTROSITY;
		spiritNaga.alignment = Alignment.CHAOTIC_EVIL;
		spiritNaga.ac = 15;
		spiritNaga.armorNote = "natural armor";
		spiritNaga.hitDice = new DiceRoll(10, 10, 20);
		spiritNaga.speed = 40;
		spiritNaga.abilityScores.put(Ability.STRENGTH, 18);
		spiritNaga.abilityScores.put(Ability.DEXTERITY, 17);
		spiritNaga.abilityScores.put(Ability.CONSTITUTION, 14);
		spiritNaga.abilityScores.put(Ability.INTELLIGENCE, 16);
		spiritNaga.abilityScores.put(Ability.WISDOM, 15);
		spiritNaga.abilityScores.put(Ability.CHARISMA, 16);
		spiritNaga.savingThrows.put(Ability.DEXTERITY, 6);
		spiritNaga.savingThrows.put(Ability.CONSTITUTION, 5);
		spiritNaga.savingThrows.put(Ability.WISDOM, 5);
		spiritNaga.savingThrows.put(Ability.CHARISMA, 6);
		spiritNaga.immunities.put(null, Stream.of(DamageType.POISON).collect(Collectors.toSet()));
		spiritNaga.conditionImmunities.add(Condition.CHARMED);
		spiritNaga.conditionImmunities.add(Condition.POISONED);
		spiritNaga.senses.put(VisionType.DARKVISION, 60);
		spiritNaga.languages.add("Abyssal");
		spiritNaga.languages.add("Common");
		spiritNaga.challengeRating = 8;

		final Map<String, Map<String, String>> nagaSpells = new LinkedHashMap<>();

		final Map<String, String> nagaCantrips = new HashMap<>();
		Stream.of("mage hand", "minor illusion", "ray of frost").forEach(s -> nagaCantrips.put(s, ""));
		nagaSpells.put("Cantrips (at will)", nagaCantrips);

		final Map<String, String> nagaLevel1 = new HashMap<>();
		Stream.of("charm person", "detect magic", "sleep").forEach(s -> nagaLevel1.put(s, ""));
		nagaSpells.put("1st level (4 slots)", nagaLevel1);

		final Map<String, String> nagaLevel2 = new HashMap<>();
		Stream.of("detect thoughts", "hold person").forEach(s -> nagaLevel2.put(s, ""));
		nagaSpells.put("2nd level (3 slots)", nagaLevel2);

		final Map<String, String> nagaLevel3 = new HashMap<>();
		Stream.of("lightning bolt", "water breathing").forEach(s -> nagaLevel3.put(s, ""));
		nagaSpells.put("3rd level (3 slots)", nagaLevel3);

		final Map<String, String> nagaLevel4 = new HashMap<>();
		Stream.of("blight", "dimension door").forEach(s -> nagaLevel4.put(s, ""));
		nagaSpells.put("4th level (3 slots)", nagaLevel4);

		final Map<String, String> nagaLevel5 = new HashMap<>();
		Stream.of("dominate person").forEach(s -> nagaLevel5.put(s, ""));
		nagaSpells.put("5th level (2 slots)", nagaLevel5);

		spiritNaga.features = Arrays.asList(new Feature("Rejuvenation", "If it dies, the naga returns to life in 1d6 days and regains all its hit points. Only a [wish] spell can prevent this trait from functioning."), //
				new Spellcasting(null, "the naga", 10, Ability.INTELLIGENCE, 14, "it", 6, ", and it needs only verbal components to cast its spells", "wizard", null, nagaSpells));
		spiritNaga.actions = Arrays.asList(new Attack("Bite", Type.MELEE_WEAPON, 7, "reach 10 ft., one creature", ", and the target must make a DC 13 Constitution saving throw, taking 31 (7d8) poison damage on a failed save, or half as much damage on a successful one.", new Attack.Damage(new DiceRoll(1, 6, 4), DamageType.PIERCING)));

		kuoToa = new Creature();
		kuoToa.name = "Kuo-toa";
		kuoToa.shortName = "The kuo-toa";
		kuoToa.size = Size.MEDIUM;
		kuoToa.type = CreatureType.HUMANOID;
		kuoToa.subtype = "kuo-toa";
		kuoToa.alignment = Alignment.NEUTRAL_EVIL;
		kuoToa.ac = 13;
		kuoToa.armorNote = "natural armor, shield";
		kuoToa.hitDice = new DiceRoll(4, 8);
		kuoToa.speed = 30;
		kuoToa.speeds.put(MovementType.SWIM, 30);
		kuoToa.abilityScores.put(Ability.STRENGTH, 23);
		kuoToa.abilityScores.put(Ability.DEXTERITY, 10);
		kuoToa.abilityScores.put(Ability.CONSTITUTION, 11);
		kuoToa.abilityScores.put(Ability.INTELLIGENCE, 11);
		kuoToa.abilityScores.put(Ability.WISDOM, 10);
		kuoToa.abilityScores.put(Ability.CHARISMA, 8);
		kuoToa.skills.put(Skill.PERCEPTION, 4);
		kuoToa.senses.put(VisionType.DARKVISION, 120);
		kuoToa.languages.add("Undercommon");
		kuoToa.challengeRating = 17;
		kuoToa.features = Arrays.asList(new Feature("Amphibious", "The kuo-toa can breathe air and water."), //
				new Feature("Otherworldly Perception", "The kuo-toa can sense the presence of any creature within 30 feet of it that is invisible or on the Ethereal Plane. It can pinpoint such a creature that is moving."), //
				new Feature("Slippery", "The kuo-toa has advantage on ability checks and saving throws made to escape a grapple."), //
				new Feature("Sunlight Sensitivity", "While in sunlight, the kuo-toa has disadvantage on attack rolls, as well as on Wisdom (Perception) checks that rely on sight."));
		kuoToa.actions = Arrays.asList(new Attack("Bite", Type.MELEE_WEAPON, 3, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(1, 4, 1), DamageType.PIERCING)), //
				new Attack("Spear", Type.MELEE_OR_RANGED_WEAPON, 3, "reach 5 ft. or range 20/60 ft., one target", ", or 5 (1d8 + 1) piercing damage if used with two hands to make a melee attack.", new Attack.Damage(new DiceRoll(1, 6, 1), DamageType.PIERCING)), //
				new Attack("Net", Type.RANGED_WEAPON, 3, "range 5/15 ft., one Large or smaller creature", "The target is restrained. A creature can use its action to make a DC 10 Strength check to free itself or another creature in a net, ending the effect on a success. Dealing 5 slashing damage to the net (AC 10) frees the target without harming it and destroys the net."));
		kuoToa.reactions = Arrays.asList(new Action("Sticky Shield",
				"When a creature misses the kuo-toa with a melee weapon attack, the kuo-toa uses its sticky shield to catch the weapon. The attacker must succeed on a DC 11 Strength saving throw, or the weapon becomes stuck to the kuo-toa's shield. If the weapon's wielder can't or won't let go of the weapon, the wielder is grappled while the weapon is stuck. While stuck, the weapon can't be used. A creature can pull the weapon free by taking an action to make a DC 11 Strength check and succeeding."));

		merrow = new Creature();

		seaHag = new Creature();

		giantCrab = new Creature();

	}

}
