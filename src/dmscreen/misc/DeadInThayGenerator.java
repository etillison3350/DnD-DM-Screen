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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dmscreen.Screen;
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

public class DeadInThayGenerator {

	private DeadInThayGenerator() {}

	public static void main(final String[] args) {
		try {
			Files.createDirectories(Paths.get("resources/dead_in_thay/"));

			final List<Object> toJson = new ArrayList<>();
			for (final Field field : DeadInThayGenerator.class.getDeclaredFields()) {
				if (Modifier.isStatic(field.getModifiers())) {
					try {
						toJson.add(field.get(null));
					} catch (IllegalArgumentException | IllegalAccessException e) {}
				}
			}
			toJson.add(TestCreatures.zombie);
			toJson.add(TestCreatures.ogreZombie);
			toJson.add(TestCreatures.otyugh);
			toJson.add(TestCreatures.grick);
			toJson.add(TestCreatures.grell);
			toJson.add(TestCreatures.gibberingMouther);
			toJson.add(TestCreatures.orc);
			toJson.add(TestCreatures.gnoll);
			toJson.add(TestCreatures.troll);
			toJson.add(TestCreatures.wight);
			toJson.add(TestCreatures.skeleton);
			toJson.add(TestCreatures.behir);
			toJson.add(TestCreatures.peryton);
			toJson.add(TestCreatures.gelatinousCube);
			toJson.add(TestCreatures.efreeti);

			Files.write(Paths.get("resources/dead_in_thay/creatures.json"), Data.GSON.toJson(toJson).getBytes());
		} catch (final IOException e) {
			e.printStackTrace();
		}

		Screen.main(args);
	}

	private static final Creature thalaxia, aboleth, unarmedSkeleton, youngOtyugh, thayanWarrior, thayanApprentice, evoker, dreadWarrior, necromancer, deathlockWight, wight31, choker, leucrotta, perytonYoung, transmuter, //
			vampiricMist, fourArmedGargoyle, conjurer;

	static {
		thalaxia = new Creature();
		thalaxia.name = "Thalaxia, the Beholder";
		thalaxia.shortName = "Thalaxia";
		thalaxia.size = Size.LARGE;
		thalaxia.type = CreatureType.ABERRATION;
		thalaxia.alignment = Alignment.LAWFUL_EVIL;
		thalaxia.ac = 18;
		thalaxia.armorNote = "natural armor";
		thalaxia.hitDice = new DiceRoll(19, 10, 76);
		thalaxia.speed = 0;
		thalaxia.speeds.put(MovementType.FLY, 20);
		thalaxia.abilityScores.put(Ability.STRENGTH, 10);
		thalaxia.abilityScores.put(Ability.DEXTERITY, 14);
		thalaxia.abilityScores.put(Ability.CONSTITUTION, 18);
		thalaxia.abilityScores.put(Ability.INTELLIGENCE, 17);
		thalaxia.abilityScores.put(Ability.WISDOM, 15);
		thalaxia.abilityScores.put(Ability.CHARISMA, 17);
		thalaxia.savingThrows.put(Ability.INTELLIGENCE, 8);
		thalaxia.savingThrows.put(Ability.WISDOM, 7);
		thalaxia.savingThrows.put(Ability.CHARISMA, 8);
		thalaxia.skills.put(Skill.PERCEPTION, 12);
		thalaxia.conditionImmunities.add(Condition.PRONE);
		thalaxia.senses.put(VisionType.DARKVISION, 120);
		thalaxia.languages.add("Deep Speech");
		thalaxia.languages.add("Undercommon");
		thalaxia.challengeRating = 13;
		thalaxia.features = Arrays.asList(new Feature("Antimagic Cone", "Thalaxia's central eye creates an area of antimagic, as in the antimagic field spell, in a 150 foot cone. At the start of each of its turns, Thalaxia decides which way the cone faces and whether the cone is active. The cone works against Thalaxia's own eye rays."));
		thalaxia.actions = Arrays.asList(new Attack("Bite", Type.MELEE_WEAPON, 5, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(4, 6), DamageType.PIERCING)), //
				new Action("Eye Rays",
						"Thalaxia shoots three of the following magical eye rays at random (reroll duplicates), choosing one to three targets it can see within 120 feet of it:\n\n    1. Charm Ray. The targeted creature must succeed on a DC 16 Wisdom saving throw or be charmed by Thalaxia for 1 hour, or until Thalaxia harms the creature.\n    2. Paralyzing Ray. The target must succeed on a DC 16 Constitution saving throw or be paralyzed for 1 minute. The target can repeat the saving throw at the end of each of its turns, ending the effect on itself on a success.\n    3. Fear Ray. The targeted creature must succeed on a DC 16 Wisdom saving throw or be frightened for 1 minute. The target can repeat the saving throw at the end of each of its turns, ending the effect on itself on a success.\n    4. Slowing Ray. The targeted creature must make a DC 16 Dexterity saving throw. On a failed save, the target's speed is halved for 1 minute. In addition, the creature can't take reactions, and it can take either an action or a bonus action on its turn, but not both. The creature can repeat the saving throw at the end of each of its turns, ending the effect on itself on the success.\n    5. Enervation Ray. The targeted creature must make a DC 16 Constitution saving throw, taking 36 (8d8) necrotic damage on a failed save, or half as much damage on a sucessful one.\n    6. Telekinetic Ray. If the target is a creature, it must succeed on a DC 16 Strength saving throw, or the beholder moves it up to 30 feet in any direction. It is restrained by the beholder's telekinetic grip until the start of the beholder's next turn or until the beholder is incapactiated.\nIf the target is an object weighting less than 300 pounds or less that isn't being worn or carried, it is moved up to 30 feet in any direction. The beholder can also exert fine control over objects, such as manipulating a simple tool or opening a door or a container.\n    7. Sleep Ray. The targeted creature must succeed on a DC 16 Wisdom saving throw or fall asleep and remain unconscious for 1 minute. The target awakens if it takes any damage or another creature takes an action to wake it. This ray has no effect on constructs and undead.\n    8. Petrification Ray. The targeted creature must make a DC 16 Dexterity saving throw. On a failed save, the creature begins to turn to stone, and is restrained. It must make the saving throw at the end of its next turn. On a success, the effect ends. On a failure, the creature is petrified until freed by the greater restoration spell or other magic."));
		thalaxia.legendaryActions = Arrays.asList(new LegendaryAction("Eye Ray", "Thalaxia uses one random eye ray."));

		aboleth = new Creature();
		aboleth.name = "Aboleth (Reduced Threat)";
		aboleth.shortName = "The aboleth";
		aboleth.size = Size.LARGE;
		aboleth.type = CreatureType.ABERRATION;
		aboleth.alignment = Alignment.LAWFUL_EVIL;
		aboleth.ac = 17;
		aboleth.armorNote = "natural armor";
		aboleth.hitDice = new DiceRoll(16, 10, 16);
		aboleth.speed = 10;
		aboleth.speeds.put(MovementType.SWIM, 40);
		aboleth.abilityScores.put(Ability.STRENGTH, 19);
		aboleth.abilityScores.put(Ability.DEXTERITY, 7);
		aboleth.abilityScores.put(Ability.CONSTITUTION, 13);
		aboleth.abilityScores.put(Ability.INTELLIGENCE, 16);
		aboleth.abilityScores.put(Ability.WISDOM, 13);
		aboleth.abilityScores.put(Ability.CHARISMA, 16);
		aboleth.savingThrows.put(Ability.CONSTITUTION, 4);
		aboleth.savingThrows.put(Ability.INTELLIGENCE, 6);
		aboleth.savingThrows.put(Ability.WISDOM, 4);
		aboleth.skills.put(Skill.HISTORY, 9);
		aboleth.skills.put(Skill.PERCEPTION, 7);
		aboleth.senses.put(VisionType.DARKVISION, 120);
		aboleth.languages.add("Deep Speech");
		aboleth.languages.add("telepathy 120 ft.");
		aboleth.challengeRating = 8;
		aboleth.features = Arrays.asList(new Feature("Amphibious", "The aboleth can breathe both air and water."), //
				new Feature("Mucous Cloud", "While underwater, the aboleth is surrounded by transformative mucus. A creature that touches the aboleth or that hits it with a melee attack while withi n 5 feet of it must make a DC 14 Constitution saving throw. On a failure, the creature is diseased for 1d4 hours. The diseased creature can breathe only underwater."), //
				new Feature("Probing Telepathy", "If a creature communicates telepathically with the aboleth, the aboleth learns the creature's greatest desires if the aboleth can see the creature."));
		aboleth.actions = Arrays.asList(new Action("Multiattack", "The aboleth makes three tentacle attacks."), //
				new Attack("Tentacle", Type.MELEE_WEAPON, 7, "reach 10 ft., one target",
						". If the target is a creature, it must succeed on a DC 14 Constitution saving throw or become diseased. The disease has no effect for 1 minute and can be removed by any effect that cures disease. After 1 minute, the creature's skin becomes translucent and slimy, the creature can't regain hit points unless it is underwater, and the disease can only be removed by heal or another disease-curing spell of 6th level or higher. When the creature is outside a body of water, it takes 6 (1d12) acid damage every 10 minutes unless moisture is applied to the skin before 10 minutes have passed.",
						new Attack.Damage(new DiceRoll(2, 6, 4), DamageType.BLUDGEONING)), //
				new Attack("Tail", Type.MELEE_WEAPON, 7, "reach 10 ft., one target", null, new Attack.Damage(new DiceRoll(3, 6, 4), DamageType.BLUDGEONING)), //
				new Action("Enslave", "3/Day",
						"The aboleth targets one creature it can see within 30 feet of it. The target must succeed on a DC 14 Wisdom saving throw or be magically charmed by the aboleth until the aboleth dies or until it is on a different plane of existence from the target. The charmed target is under the aboleth's control and can't take reactions, and the aboleth and the target can communicate telepathically with each other over any distance.\n    Whenever the charmed target takes damage, the target can repeat the saving throw. On a success, the effect ends. No more than once every 24 hours, the target can also repeat the saving throw when it is at least 1 mile away from the aboleth."));
		aboleth.legendaryActions = Arrays.asList(new LegendaryAction("Detect", "The aboleth makes a Wisdom (Perception) check."), //
				new LegendaryAction("Tail Swipe", "The aboleth makes one tail attack."), //
				new LegendaryAction("Psychic Drain", "One creature charmed by the aboleth takes 10 (3d6) psychic damage, and the aboleth regains hit points equal to the damage the creature takes."));

		unarmedSkeleton = new Creature();
		unarmedSkeleton.name = "Skeleton (Unarmed)";
		unarmedSkeleton.shortName = "The skeleton";
		unarmedSkeleton.size = Size.MEDIUM;
		unarmedSkeleton.type = CreatureType.UNDEAD;
		unarmedSkeleton.alignment = Alignment.LAWFUL_EVIL;
		unarmedSkeleton.ac = 13;
		unarmedSkeleton.armorNote = "armor scraps";
		unarmedSkeleton.hitDice = new DiceRoll(2, 8, 4);
		unarmedSkeleton.speed = 30;
		unarmedSkeleton.abilityScores.put(Ability.STRENGTH, 10);
		unarmedSkeleton.abilityScores.put(Ability.DEXTERITY, 14);
		unarmedSkeleton.abilityScores.put(Ability.CONSTITUTION, 15);
		unarmedSkeleton.abilityScores.put(Ability.INTELLIGENCE, 6);
		unarmedSkeleton.abilityScores.put(Ability.WISDOM, 8);
		unarmedSkeleton.abilityScores.put(Ability.CHARISMA, 5);
		unarmedSkeleton.vulnerabilities.put(null, new HashSet<>(Arrays.asList(DamageType.BLUDGEONING)));
		unarmedSkeleton.immunities.put(null, new HashSet<>(Arrays.asList(DamageType.POISON)));
		unarmedSkeleton.conditionImmunities.add(Condition.POISONED);
		unarmedSkeleton.conditionImmunities.add(Condition.EXHAUSTION);
		unarmedSkeleton.senses.put(VisionType.DARKVISION, 60);
		unarmedSkeleton.languages.add("understands all languages it knew in life but can't speak");
		unarmedSkeleton.challengeRating = -2;
		unarmedSkeleton.features = Arrays.asList(new Feature("Reassembly", "If damage of any type besides radiant damage reduces the skeleton to 0 hit points, it reassembles in 1d6 + 4 rounds, returning to life at its hit point maximum. It has no memory of its past fate."));
		unarmedSkeleton.actions = Arrays.asList(new Attack("Unarmed Strike", Type.MELEE_WEAPON, 2, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(1, 1, 0), DamageType.BLUDGEONING)));

		youngOtyugh = new Creature();
		youngOtyugh.name = "Otyugh (Young)";
		youngOtyugh.shortName = "The otyugh";
		youngOtyugh.size = Size.MEDIUM;
		youngOtyugh.type = CreatureType.ABERRATION;
		youngOtyugh.alignment = Alignment.NEUTRAL;
		youngOtyugh.ac = 14;
		youngOtyugh.armorNote = "natural armor";
		youngOtyugh.hitDice = new DiceRoll(6, 10, 18);
		youngOtyugh.speed = 30;
		youngOtyugh.abilityScores.put(Ability.STRENGTH, 14);
		youngOtyugh.abilityScores.put(Ability.DEXTERITY, 9);
		youngOtyugh.abilityScores.put(Ability.CONSTITUTION, 17);
		youngOtyugh.abilityScores.put(Ability.INTELLIGENCE, 4);
		youngOtyugh.abilityScores.put(Ability.WISDOM, 11);
		youngOtyugh.abilityScores.put(Ability.CHARISMA, 4);
		youngOtyugh.savingThrows.put(Ability.CONSTITUTION, 5);
		youngOtyugh.senses.put(VisionType.DARKVISION, 120);
		youngOtyugh.languages.add("Otyugh");
		youngOtyugh.challengeRating = 3;
		youngOtyugh.features = Arrays.asList(new Feature("Limited Telepathy", "The otyugh can magically transmit simple messages and images to any creature within 120 feet of it that can understand a language. This form of telepathy doesn't allow the receiving creature to telepathically respond."));
		youngOtyugh.actions = Arrays.asList(new Action("Multiattack", "The otyugh makes three attacks: one with its bite, and two with its tentacles"), //
				new Attack("Bite", Type.MELEE_WEAPON, 4, "reach 5 ft., one target",
						". If the target is a creature, it must succeed on a DC 13 Constitution saving throw against disease or become poisoned until the disease is cured. Every 24 hours that elapse, the target must repeat the saving throw, reducing its hit point maximum by 5 (1d10) on a failure. The disease is cured on a success. The target dies if the disease reduces its hit point maximum to 0. This reduction to the target's hit point maximum lasts until the disease is cured.",
						new Attack.Damage(new DiceRoll(2, 8, 2), DamageType.PIERCING)), //
				new Attack("Tentacle", Type.MELEE_WEAPON, 4, "reach 10 ft., one target", null, new Attack.Damage(new DiceRoll(1, 8, 2), DamageType.BLUDGEONING), new Attack.Damage(new DiceRoll(1, 8), DamageType.PIERCING)));

		thayanWarrior = new Creature();
		thayanWarrior.name = "Thayan Warrior";
		thayanWarrior.shortName = "The warrior";
		thayanWarrior.size = Size.MEDIUM;
		thayanWarrior.type = CreatureType.HUMANOID;
		thayanWarrior.subtype = "human";
		thayanWarrior.alignment = Alignment.LAWFUL_EVIL;
		thayanWarrior.ac = 16;
		thayanWarrior.armorNote = "chain shirt, shield";
		thayanWarrior.hitDice = new DiceRoll(8, 8, 16);
		thayanWarrior.speed = 30;
		thayanWarrior.abilityScores.put(Ability.STRENGTH, 16);
		thayanWarrior.abilityScores.put(Ability.DEXTERITY, 13);
		thayanWarrior.abilityScores.put(Ability.CONSTITUTION, 14);
		thayanWarrior.abilityScores.put(Ability.INTELLIGENCE, 10);
		thayanWarrior.abilityScores.put(Ability.WISDOM, 11);
		thayanWarrior.abilityScores.put(Ability.CHARISMA, 11);
		thayanWarrior.skills.put(Skill.PERCEPTION, 2);
		thayanWarrior.languages.add("Common");
		thayanWarrior.languages.add("Thayan");
		thayanWarrior.challengeRating = 2;
		thayanWarrior.features = Arrays.asList(new Feature("Doomvault Devotion", "Within the Doomvault, the warrior has advantage on saving throws against being charmed or frightened."), //
				new Feature("Pack Tactics", "The warrior has advantage on an attack roll if at least one of the warrior's allies is within 5 feet of the creature and the ally isn't incapacitated."));
		thayanWarrior.actions = Arrays.asList(new Action("Multiattack", "The warrior makes two melee attacks"), //
				new Attack("Longsword", Type.MELEE_WEAPON, 5, "reach 5 ft., one target", ", or 8 (1d10 + 3) slashing damage if used with two hands.", new Attack.Damage(new DiceRoll(1, 8, 3), DamageType.SLASHING)), //
				new Attack("Javelin", Type.MELEE_OR_RANGED_WEAPON, 5, "reach 5 ft. or range 30/120 ft., one target", null, new Attack.Damage(new DiceRoll(1, 6, 3), DamageType.PIERCING)));

		thayanApprentice = new Creature();
		thayanApprentice.name = "Thayan Apprentice";
		thayanApprentice.shortName = "The apprentice";
		thayanApprentice.size = Size.MEDIUM;
		thayanApprentice.type = CreatureType.HUMANOID;
		thayanApprentice.subtype = "human";
		thayanApprentice.alignment = Alignment.LAWFUL_EVIL;
		thayanApprentice.ac = 12;
		thayanApprentice.armorNote = "15 with mage armor";
		thayanApprentice.hitDice = new DiceRoll(5, 8, 5);
		thayanApprentice.speed = 30;
		thayanApprentice.abilityScores.put(Ability.STRENGTH, 10);
		thayanApprentice.abilityScores.put(Ability.DEXTERITY, 14);
		thayanApprentice.abilityScores.put(Ability.CONSTITUTION, 12);
		thayanApprentice.abilityScores.put(Ability.INTELLIGENCE, 15);
		thayanApprentice.abilityScores.put(Ability.WISDOM, 13);
		thayanApprentice.abilityScores.put(Ability.CHARISMA, 11);
		thayanApprentice.skills.put(Skill.ARCANA, 4);
		thayanApprentice.languages.add("Common");
		thayanApprentice.languages.add("Thayan");
		thayanApprentice.challengeRating = 2;

		final Map<String, Map<String, String>> apprenticeSpells = new LinkedHashMap<>();

		final Map<String, String> apprenticeCantrips = new HashMap<>();
		Stream.of("fire bolt", "mage hand", "prestidigitation", "shocking grasp").forEach(s -> apprenticeCantrips.put(s, ""));
		apprenticeSpells.put("Cantrips (at will)", apprenticeCantrips);

		final Map<String, String> apprenticeLevel1 = new HashMap<>();
		Stream.of("burning hands", "detect magic", "mage armor", "shield").forEach(s -> apprenticeLevel1.put(s, ""));
		apprenticeSpells.put("1st level (4 slots)", apprenticeLevel1);

		final Map<String, String> apprenticeLevel2 = new HashMap<>();
		Stream.of("blur", "scorching ray").forEach(s -> apprenticeLevel2.put(s, ""));
		apprenticeSpells.put("2nd level (3 slots)", apprenticeLevel2);

		thayanApprentice.features = Arrays.asList(new Feature("Doomvault Devotion", "Within the Doomvault, the apprentice has advantage on saving throws against being charmed or frightened."), //
				new Spellcasting(null, "the apprentice", 4, Ability.INTELLIGENCE, 12, "its", 4, null, "wizard", null, apprenticeSpells));
		thayanApprentice.actions = Arrays.asList(new Attack("Dagger", Type.MELEE_OR_RANGED_WEAPON, 4, "reach 5 ft. or range 20/60 ft., one target", null, new Attack.Damage(new DiceRoll(1, 4, 2), DamageType.PIERCING)));

		evoker = new Creature();
		evoker.name = "Evoker";
		evoker.shortName = "The evoker";
		evoker.size = Size.MEDIUM;
		evoker.type = CreatureType.HUMANOID;
		evoker.subtype = "any race";
		evoker.alignment = Alignment.LAWFUL_EVIL;
		evoker.ac = 12;
		evoker.armorNote = "15 with mage armor";
		evoker.hitDice = new DiceRoll(12, 8, 12);
		evoker.speed = 30;
		evoker.abilityScores.put(Ability.STRENGTH, 9);
		evoker.abilityScores.put(Ability.DEXTERITY, 14);
		evoker.abilityScores.put(Ability.CONSTITUTION, 12);
		evoker.abilityScores.put(Ability.INTELLIGENCE, 17);
		evoker.abilityScores.put(Ability.WISDOM, 12);
		evoker.abilityScores.put(Ability.CHARISMA, 11);
		evoker.savingThrows.put(Ability.INTELLIGENCE, 7);
		evoker.savingThrows.put(Ability.WISDOM, 5);
		evoker.skills.put(Skill.ARCANA, 7);
		evoker.skills.put(Skill.HISTORY, 7);
		evoker.languages.add("Common");
		evoker.languages.add("Thayan");
		evoker.challengeRating = 9;

		final Map<String, Map<String, String>> evokerSpells = new LinkedHashMap<>();

		final Map<String, String> evokerCantrips = new HashMap<>();
		Stream.of("fire bolt*", "light*", "prestidigitation", "shocking grasp*").forEach(s -> evokerCantrips.put(s, ""));
		evokerSpells.put("Cantrips (at will)", evokerCantrips);

		final Map<String, String> evokerLevel1 = new HashMap<>();
		Stream.of("burning hands*", "mage armor", "magic missile*").forEach(s -> evokerLevel1.put(s, ""));
		evokerSpells.put("1st level (4 slots)", evokerLevel1);

		final Map<String, String> evokerLevel2 = new HashMap<>();
		Stream.of("mirror image", "misty step", "shatter*").forEach(s -> evokerLevel2.put(s, ""));
		evokerSpells.put("2nd level (3 slots)", evokerLevel2);

		final Map<String, String> evokerLevel3 = new HashMap<>();
		Stream.of("counterspell", "fireball*", "lightning bolt*").forEach(s -> evokerLevel3.put(s, ""));
		evokerSpells.put("3rd level (3 slots)", evokerLevel3);

		final Map<String, String> evokerLevel4 = new HashMap<>();
		Stream.of("ice storm*", "stoneskin").forEach(s -> evokerLevel4.put(s, ""));
		evokerSpells.put("4th level (3 slots)", evokerLevel4);

		final Map<String, String> evokerLevel5 = new HashMap<>();
		Stream.of("Bigby's hand*", "cone of cold*").forEach(s -> evokerLevel5.put(s, ""));
		evokerSpells.put("5th level (2 slots)", evokerLevel5);

		final Map<String, String> evokerLevel6 = new HashMap<>();
		Stream.of("chain lightning*", "wall of ice*").forEach(s -> evokerLevel6.put(s, ""));
		evokerSpells.put("6th level (1 slot)", evokerLevel6);

		evoker.features = Arrays.asList(new Spellcasting(null, "the evoker", 12, Ability.INTELLIGENCE, 15, "its", 7, null, "wizard", null, evokerSpells), //
				new Feature("Sculpt Spells", "When the evoker casts an evocation spell (marked above with *) that forces other creatures it can see to make a saving throw, it can choose a number of them equal to 1 + the spell's level. These creatures automatically succeed on their saving throws against the spell. If a successful save means a chosen creature would take half damage from the spell, it instead takes no damage from it."));
		evoker.actions = Arrays.asList(new Attack("Dagger", Type.MELEE_OR_RANGED_WEAPON, 4, "reach 5 ft. or range 20/60 ft., one target", null, new Attack.Damage(new DiceRoll(1, 4, 2), DamageType.PIERCING)));

		dreadWarrior = new Creature();
		dreadWarrior.name = "Dread Warrior";
		dreadWarrior.shortName = "The dread warrior";
		dreadWarrior.size = Size.MEDIUM;
		dreadWarrior.type = CreatureType.UNDEAD;
		dreadWarrior.alignment = Alignment.NEUTRAL_EVIL;
		dreadWarrior.ac = 18;
		dreadWarrior.armorNote = "chain mail, shield";
		dreadWarrior.hitDice = new DiceRoll(5, 8, 15);
		dreadWarrior.speed = 30;
		dreadWarrior.abilityScores.put(Ability.STRENGTH, 15);
		dreadWarrior.abilityScores.put(Ability.DEXTERITY, 11);
		dreadWarrior.abilityScores.put(Ability.CONSTITUTION, 16);
		dreadWarrior.abilityScores.put(Ability.INTELLIGENCE, 10);
		dreadWarrior.abilityScores.put(Ability.WISDOM, 12);
		dreadWarrior.abilityScores.put(Ability.CHARISMA, 10);
		dreadWarrior.savingThrows.put(Ability.WISDOM, 3);
		dreadWarrior.skills.put(Skill.ATHLETICS, 4);
		dreadWarrior.skills.put(Skill.PERCEPTION, 3);
		dreadWarrior.immunities.put(null, new HashSet<>(Arrays.asList(DamageType.POISON)));
		dreadWarrior.conditionImmunities.add(Condition.POISONED);
		dreadWarrior.conditionImmunities.add(Condition.EXHAUSTION);
		dreadWarrior.senses.put(VisionType.DARKVISION, 60);
		dreadWarrior.languages.add("Common");
		dreadWarrior.challengeRating = 1;
		dreadWarrior.features = Arrays.asList(new Feature("Undead Fortitude", "If damage reduces the dread warrior to 0 hit points, it must make a Constitution saving throw with a DC of 5 + the damage taken, unless the damage is radiant or from a critcal hit. On a success, the dread warrior drops to 1 hit point instead."));
		dreadWarrior.actions = Arrays.asList(new Action("Multiattack", "The dread warrior makes two melee attacks."), //
				new Attack("Battleaxe", Type.MELEE_WEAPON, 4, "reach 5 ft., one target", ", or 7 (1d10 + 2) slashing damage if wielded with two hands.", new Attack.Damage(new DiceRoll(1, 8, 2), DamageType.SLASHING)), //
				new Attack("Javelin", Type.MELEE_OR_RANGED_WEAPON, 4, "reach 5 ft. or range 30/120 ft., one target", null, new Attack.Damage(new DiceRoll(1, 6, 2), DamageType.PIERCING)));

		necromancer = new Creature();
		necromancer.name = "Necromancer";
		necromancer.shortName = "The necromancer";
		necromancer.size = Size.MEDIUM;
		necromancer.type = CreatureType.HUMANOID;
		necromancer.subtype = "any race";
		necromancer.alignment = Alignment.ANY_ALIGNMENT;
		necromancer.ac = 12;
		necromancer.armorNote = "15 with mage armor";
		necromancer.hitDice = new DiceRoll(12, 8, 12);
		necromancer.speed = 30;
		necromancer.abilityScores.put(Ability.STRENGTH, 9);
		necromancer.abilityScores.put(Ability.DEXTERITY, 14);
		necromancer.abilityScores.put(Ability.CONSTITUTION, 12);
		necromancer.abilityScores.put(Ability.INTELLIGENCE, 17);
		necromancer.abilityScores.put(Ability.WISDOM, 12);
		necromancer.abilityScores.put(Ability.CHARISMA, 11);
		necromancer.savingThrows.put(Ability.INTELLIGENCE, 7);
		necromancer.savingThrows.put(Ability.WISDOM, 5);
		necromancer.skills.put(Skill.ARCANA, 7);
		necromancer.skills.put(Skill.HISTORY, 7);
		necromancer.resistances.put(null, new HashSet<>(Arrays.asList(DamageType.NECROTIC)));
		necromancer.languages.add("any four languages");
		necromancer.challengeRating = 9;

		final Map<String, Map<String, String>> necromancerSpells = new LinkedHashMap<>();

		final Map<String, String> necromancerCantrips = new HashMap<>();
		Stream.of("chill touch", "dancing lights", "mage hand", "mending").forEach(s -> necromancerCantrips.put(s, ""));
		necromancerSpells.put("Cantrips (at will)", necromancerCantrips);

		final Map<String, String> necromancerLevel1 = new HashMap<>();
		Stream.of("false life*", "mage armor", "ray of sickness*").forEach(s -> necromancerLevel1.put(s, ""));
		necromancerSpells.put("1st level (4 slots)", necromancerLevel1);

		final Map<String, String> necromancerLevel2 = new HashMap<>();
		Stream.of("blindness/deafness*", "ray of enfeeblement*", "web").forEach(s -> necromancerLevel2.put(s, ""));
		necromancerSpells.put("2nd level (3 slots)", necromancerLevel2);

		final Map<String, String> necromancerLevel3 = new HashMap<>();
		Stream.of("animate dead*", "bestow curse*", "vampiric touch*").forEach(s -> necromancerLevel3.put(s, ""));
		necromancerSpells.put("3rd level (3 slots)", necromancerLevel3);

		final Map<String, String> necromancerLevel4 = new HashMap<>();
		Stream.of("blight*", "dimension door", "stoneskin").forEach(s -> necromancerLevel4.put(s, ""));
		necromancerSpells.put("4th level (3 slots)", necromancerLevel4);

		final Map<String, String> necromancerLevel5 = new HashMap<>();
		Stream.of("Bigby's hand", "cloudkill").forEach(s -> necromancerLevel5.put(s, ""));
		necromancerSpells.put("5th level (2 slots)", necromancerLevel5);

		final Map<String, String> necromancerLevel6 = new HashMap<>();
		Stream.of("circle of death*").forEach(s -> necromancerLevel6.put(s, ""));
		necromancerSpells.put("6th level (1 slot)", necromancerLevel6);

		necromancer.features = Arrays.asList(new Spellcasting(null, "the necromancer", 12, Ability.INTELLIGENCE, 15, "its", 7, null, "wizard", null, necromancerSpells), //
				new Feature("Grim Harvest", "1/Turn", "When the necromancer kills a creature that is neither a construct nor undead with a spell of 1st level or higher, the necromancer regains hit points equal to twice the spell's level, or three times if it is a necromancy spell (marked above with *)."));
		necromancer.actions = Arrays.asList(new Attack("Withering Touch", Type.MELEE_SPELL, 7, "reach 5 ft., one creature", null, new Attack.Damage(new DiceRoll(2, 4, 0), DamageType.NECROTIC)));

		deathlockWight = new Creature();
		deathlockWight.name = "Deathlock Wight";
		deathlockWight.shortName = "The wight";
		deathlockWight.size = Size.MEDIUM;
		deathlockWight.type = CreatureType.UNDEAD;
		deathlockWight.alignment = Alignment.NEUTRAL_EVIL;
		deathlockWight.ac = 12;
		deathlockWight.armorNote = "15 with mage armor";
		deathlockWight.hitDice = new DiceRoll(5, 8, 15);
		deathlockWight.speed = 30;
		deathlockWight.abilityScores.put(Ability.STRENGTH, 11);
		deathlockWight.abilityScores.put(Ability.DEXTERITY, 14);
		deathlockWight.abilityScores.put(Ability.CONSTITUTION, 16);
		deathlockWight.abilityScores.put(Ability.INTELLIGENCE, 12);
		deathlockWight.abilityScores.put(Ability.WISDOM, 14);
		deathlockWight.abilityScores.put(Ability.CHARISMA, 16);
		deathlockWight.savingThrows.put(Ability.WISDOM, 4);
		deathlockWight.skills.put(Skill.ARCANA, 3);
		deathlockWight.skills.put(Skill.PERCEPTION, 4);
		deathlockWight.resistances.put(null, new HashSet<>(Arrays.asList(DamageType.NECROTIC)));
		deathlockWight.resistances.put("from nonmagical weapons", new HashSet<>(Arrays.asList(DamageType.BLUDGEONING, DamageType.PIERCING, DamageType.SLASHING)));
		deathlockWight.immunities.put(null, new HashSet<>(Arrays.asList(DamageType.POISON)));
		deathlockWight.conditionImmunities.add(Condition.POISONED);
		deathlockWight.conditionImmunities.add(Condition.EXHAUSTION);
		deathlockWight.languages.add("the languages it knew in life");
		deathlockWight.challengeRating = 3;

		final Map<String, Map<String, String>> deathlockSpells = new LinkedHashMap<>();

		final Map<String, String> deathlockAtWill = new HashMap<>();
		Stream.of("detect magic", "disguise self", "mage armor").forEach(s -> deathlockAtWill.put(s, ""));
		deathlockSpells.put("At will", deathlockAtWill);

		final Map<String, String> deathlock1Day = new HashMap<>();
		Stream.of("fear", "hold person", "misty step").forEach(s -> deathlock1Day.put(s, ""));
		deathlockSpells.put("1/day each", deathlock1Day);

		deathlockWight.features = Arrays.asList(new InnateSpellcasting(null, "the wight", Ability.CHARISMA, 13, Integer.MIN_VALUE, "it", null, deathlockSpells), //
				new Feature("Sunlight Sensitivity", "While in sunlight, the wight has disadvantage on attack rolls, as well as on Wisdom (Perception) checks that rely on sight."));
		deathlockWight.actions = Arrays.asList(new Action("Multiattack", "The wight attacks twice with grave bolt."), //
				new Attack("Grave Bolt", Type.RANGED_SPELL, 5, "range 120 ft., one target", null, new Attack.Damage(new DiceRoll(1, 8, 3), DamageType.NECROTIC)), //
				new Attack("Life Drain", Type.MELEE_WEAPON, 4, "reach 5 ft., one creature",
						". The target must succeed on a DC 13 Constitution saving throw or its hit point maximum is reduced by an amount equal to the damage taken . This reduction lasts until the target finishes a long rest. The target dies if this effect reduces its hit point maximum to 0.\n    A humanoid slain by this attack rises 24 hours later as a zombie under the wight's control, unless the humanoid is restored to life or its body is destroyed. The wight can have no more than twelve zombies under its control at one time.",
						new Attack.Damage(new DiceRoll(2, 6, 2), DamageType.NECROTIC)));

		wight31 = new Creature();
		wight31.name = "Wight (Undying Laboratory)";
		wight31.shortName = "The wight";
		wight31.size = Size.MEDIUM;
		wight31.type = CreatureType.UNDEAD;
		wight31.alignment = Alignment.NEUTRAL_EVIL;
		wight31.ac = 14;
		wight31.armorNote = "studded leather";
		wight31.hitDice = new DiceRoll(3, 8, 6);
		wight31.speed = 30;
		wight31.abilityScores.put(Ability.STRENGTH, 13);
		wight31.abilityScores.put(Ability.DEXTERITY, 12);
		wight31.abilityScores.put(Ability.CONSTITUTION, 14);
		wight31.abilityScores.put(Ability.INTELLIGENCE, 8);
		wight31.abilityScores.put(Ability.WISDOM, 11);
		wight31.abilityScores.put(Ability.CHARISMA, 13);
		wight31.skills.put(Skill.STEALTH, 3);
		wight31.skills.put(Skill.PERCEPTION, 2);
		wight31.resistances.put(null, new HashSet<>(Arrays.asList(DamageType.NECROTIC)));
		wight31.resistances.put("from nonmagical weapons that aren't silvered", new HashSet<>(Arrays.asList(DamageType.BLUDGEONING, DamageType.PIERCING, DamageType.SLASHING)));
		wight31.immunities.put(null, new HashSet<>(Arrays.asList(DamageType.POISON)));
		wight31.conditionImmunities.add(Condition.POISONED);
		wight31.conditionImmunities.add(Condition.EXHAUSTION);
		wight31.languages.add("the languages it knew in life");
		wight31.challengeRating = 3;
		wight31.features = Arrays.asList(new Feature("Sunlight Sensitivity", "While in sunlight, the wight has disadvantage on attack rolls, as well as on Wisdom (Perception) checks that rely on sight."));
		wight31.actions = Arrays.asList(new Action("Multiattack", "The wight makes two longsword attacks or two longbow attacks. It can use its life drain im place of one longsword attack."), //
				new Attack("Life Drain", Type.MELEE_WEAPON, 3, "reach 5 ft., one creature",
						". The target must succeed on a DC 12 Constitution saving throw or its hit point maximum is reduced by an amount equal to the damage taken . This reduction lasts until the target finishes a long rest. The target dies if this effect reduces its hit point maximum to 0.\n    A humanoid slain by this attack rises 24 hours later as a zombie under the wight's control, unless the humanoid is restored to life or its body is destroyed. The wight can have no more than twelve zombies under its control at one time.",
						new Attack.Damage(new DiceRoll(1, 6, 1), DamageType.NECROTIC)), //
				new Attack("Longsword", Type.MELEE_WEAPON, 3, "reach 5 ft., one target", ", or 6 (1d10 + 1) slashing damage if used with two hands.", new Attack.Damage(new DiceRoll(1, 8, 1), DamageType.SLASHING)), //
				new Attack("Longbow", Type.RANGED_WEAPON, 3, "range 150/600 ft., one target", null, new Attack.Damage(new DiceRoll(1, 8, 1), DamageType.PIERCING)), //
				new Attack("Urn", Type.RANGED_WEAPON, 3, "range 10/20 ft., one target", ". Unless the target is undead, it must suceed on a DC 17 Consitution saving throw or take 10 (3d6) necrotic damage and be paralyzed for 1 minute. If this damage reduces the creature to 0 hit points, the target dies and turns to dust. The target can repeat the saving throw at the end of each of its turns, ending the effect on itself on a success.", new Attack.Damage(new DiceRoll(1, 6, 1), DamageType.BLUDGEONING)));

		choker = new Creature();
		choker.name = "Choker";
		choker.shortName = "The choker";
		choker.size = Size.SMALL;
		choker.type = CreatureType.ABERRATION;
		choker.alignment = Alignment.CHAOTIC_EVIL;
		choker.ac = 16;
		choker.armorNote = "natural armor";
		choker.hitDice = new DiceRoll(3, 6, 3);
		choker.speed = 30;
		choker.abilityScores.put(Ability.STRENGTH, 16);
		choker.abilityScores.put(Ability.DEXTERITY, 14);
		choker.abilityScores.put(Ability.CONSTITUTION, 13);
		choker.abilityScores.put(Ability.INTELLIGENCE, 4);
		choker.abilityScores.put(Ability.WISDOM, 12);
		choker.abilityScores.put(Ability.CHARISMA, 7);
		choker.skills.put(Skill.STEALTH, 6);
		choker.senses.put(VisionType.DARKVISION, 60);
		choker.languages.add("Deep Speech");
		choker.challengeRating = 1;
		choker.features = Arrays.asList(new Feature("Aberrant Quickness", "Recharges after a Short or Long Rest", "The choker can take an extra action on its turn."), //
				new Feature("Spider Climb", "The choker can climb difficult surfaces, including upside down on ceilings, without needing to make an ability check."));
		choker.actions = Arrays.asList(new Action("Multiattack", "The choker makes two tentacle attacks"), //
				new Attack("Tentacle", Type.MELEE_WEAPON, 5, "reach 10 ft., one target", ". If the target is a large or smaller creature, it is grappled (escape DC 15). Until this grapple ends, the target is restrained, and the choker can't use this tentacle on another target (the choker has two tentacles). If this attack is a critical hit, the target also cannot breathe or speak until the grapple ends.", new Attack.Damage(new DiceRoll(1, 4, 3), DamageType.BLUDGEONING),
						new Attack.Damage(new DiceRoll(1, 6), DamageType.PIERCING)));

		leucrotta = new Creature();
		leucrotta.name = "Leucrotta";
		leucrotta.shortName = "The leucrotta";
		leucrotta.size = Size.LARGE;
		leucrotta.type = CreatureType.MONSTROSITY;
		leucrotta.alignment = Alignment.CHAOTIC_EVIL;
		leucrotta.ac = 14;
		leucrotta.armorNote = "natural armor";
		leucrotta.hitDice = new DiceRoll(9, 10, 18);
		leucrotta.speed = 50;
		leucrotta.abilityScores.put(Ability.STRENGTH, 18);
		leucrotta.abilityScores.put(Ability.DEXTERITY, 14);
		leucrotta.abilityScores.put(Ability.CONSTITUTION, 15);
		leucrotta.abilityScores.put(Ability.INTELLIGENCE, 9);
		leucrotta.abilityScores.put(Ability.WISDOM, 12);
		leucrotta.abilityScores.put(Ability.CHARISMA, 6);
		leucrotta.skills.put(Skill.DECEPTION, 2);
		leucrotta.skills.put(Skill.PERCEPTION, 3);
		leucrotta.senses.put(VisionType.DARKVISION, 60);
		leucrotta.languages.add("Abyssal");
		leucrotta.languages.add("Gnoll");
		leucrotta.challengeRating = 3;
		leucrotta.features = Arrays.asList(new Feature("Keen Smell", "The leucrotta has advantage on Wisdom (Perception) checks that rely on smell."), //
				new Feature("Kicking Retreat", "If the leucrotta attacks with its hooves, it can take the Disengage action as a bonus action."), //
				new Feature("Mimicry", "The leucrotta can mimic animal sounds and homainoid voices. A creature that hears the sounds can tell they are imitations with a successful DC 14 Wisdom (Insight) check."), //
				new Feature("Rampage", "When the leucrotta reduces a creature t 0 hit points with a melee attack on its turn, it can take a bonus action to move up to half its speed and make an attack with its hooves."));
		leucrotta.actions = Arrays.asList(new Action("Multiattack", "The leucrotta makes two attacks: one with its bite, and one with its hooves."), //
				new Attack("Bite", Type.MELEE_WEAPON, 6, "reach 5 ft., one target", ". If the leucrotta scores a critical hit, it rolls the damage dice three times, instead of twice.", new Attack.Damage(new DiceRoll(1, 8, 4), DamageType.PIERCING)), //
				new Attack("Hooves", Type.MELEE_WEAPON, 6, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(2, 6, 4), DamageType.BLUDGEONING)));

		perytonYoung = new Creature();
		perytonYoung.name = "Peryton Young";
		perytonYoung.shortName = "The peryton";
		perytonYoung.size = Size.MEDIUM;
		perytonYoung.type = CreatureType.MONSTROSITY;
		perytonYoung.alignment = Alignment.CHAOTIC_EVIL;
		perytonYoung.ac = 13;
		perytonYoung.armorNote = "natural armor";
		perytonYoung.hitDice = new DiceRoll(3, 8, 0);
		perytonYoung.speed = 20;
		perytonYoung.speeds.put(MovementType.FLY, 60);
		perytonYoung.abilityScores.put(Ability.STRENGTH, 14);
		perytonYoung.abilityScores.put(Ability.DEXTERITY, 10);
		perytonYoung.abilityScores.put(Ability.CONSTITUTION, 11);
		perytonYoung.abilityScores.put(Ability.INTELLIGENCE, 7);
		perytonYoung.abilityScores.put(Ability.WISDOM, 10);
		perytonYoung.abilityScores.put(Ability.CHARISMA, 8);
		perytonYoung.skills.put(Skill.PERCEPTION, 4);
		perytonYoung.languages.add("understands Common and Elvish but can't speak");
		perytonYoung.challengeRating = 0;
		perytonYoung.features = Arrays.asList(new Feature("Dive Attack", "If the peryton is flying and dives at least 30 feet straight toward a target and then hits it with a melee weapon attack, the attack deals an extra 9 (2d8) damage to the target."), //
				new Feature("Flyby", "The peryton doesn't provoke an opportunity attack when it flies out of an enemy's reach."), //
				new Feature("Keen Sight and Smell", "The peryton has advantage on Wisdom (Perception) checks that rely on sight or smell."));
		perytonYoung.actions = Arrays.asList(new Action("Multiattack", "The leucrotta makes one gore attack, and one talon attack."), //
				new Attack("Gore", Type.MELEE_WEAPON, 4, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(1, 8, 2), DamageType.PIERCING)), //
				new Attack("Talons", Type.MELEE_WEAPON, 4, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(2, 4, 2), DamageType.PIERCING)));

		transmuter = new Creature();
		transmuter.name = "Transmuter";
		transmuter.shortName = "The transmuter";
		transmuter.size = Size.MEDIUM;
		transmuter.type = CreatureType.HUMANOID;
		transmuter.subtype = "any race";
		transmuter.alignment = Alignment.ANY_ALIGNMENT;
		transmuter.ac = 12;
		transmuter.armorNote = "15 with mage armor";
		transmuter.hitDice = new DiceRoll(9, 8);
		transmuter.speed = 30;
		transmuter.abilityScores.put(Ability.STRENGTH, 9);
		transmuter.abilityScores.put(Ability.DEXTERITY, 14);
		transmuter.abilityScores.put(Ability.CONSTITUTION, 11);
		transmuter.abilityScores.put(Ability.INTELLIGENCE, 17);
		transmuter.abilityScores.put(Ability.WISDOM, 12);
		transmuter.abilityScores.put(Ability.CHARISMA, 11);
		transmuter.savingThrows.put(Ability.INTELLIGENCE, 6);
		transmuter.savingThrows.put(Ability.WISDOM, 4);
		transmuter.skills.put(Skill.ARCANA, 6);
		transmuter.skills.put(Skill.HISTORY, 6);
		transmuter.languages.add("any four languages");
		transmuter.challengeRating = 5;

		final Map<String, Map<String, String>> transmuterSpells = new LinkedHashMap<>();

		final Map<String, String> transmuterCantrips = new HashMap<>();
		Stream.of("light", "mending", "prestidigitation", "ray of frost").forEach(s -> transmuterCantrips.put(s, ""));
		transmuterSpells.put("Cantrips (at will)", transmuterCantrips);

		final Map<String, String> transmuterLevel1 = new HashMap<>();
		Stream.of("chromatic orb", "disguise self*", "mage armor").forEach(s -> transmuterLevel1.put(s, ""));
		transmuterSpells.put("1st level (4 slots)", transmuterLevel1);

		final Map<String, String> transmuterLevel2 = new HashMap<>();
		Stream.of("alter self*", "hold person", "knock*").forEach(s -> transmuterLevel2.put(s, ""));
		transmuterSpells.put("2nd level (3 slots)", transmuterLevel2);

		final Map<String, String> transmuterLevel3 = new HashMap<>();
		Stream.of("blink*", "fireball", "slow*").forEach(s -> transmuterLevel3.put(s, ""));
		transmuterSpells.put("3rd level (3 slots)", transmuterLevel3);

		final Map<String, String> transmuterLevel4 = new HashMap<>();
		Stream.of("polymorph*", "stoneskin*").forEach(s -> transmuterLevel4.put(s, ""));
		transmuterSpells.put("4th level (3 slots)", transmuterLevel4);

		final Map<String, String> transmuterLevel5 = new HashMap<>();
		Stream.of("telekinesis").forEach(s -> transmuterLevel5.put(s, ""));
		transmuterSpells.put("5th level (1 slot)", transmuterLevel5);

		transmuter.features = Arrays.asList(new Spellcasting(null, "the transmuter", 9, Ability.INTELLIGENCE, 14, "its", 6, null, "wizard", null, transmuterSpells), //
				new Feature("Transmuter's Stone",
						"The transmuter carries a magic stone it crafted that grants its bearer one of the following effects:\n\n \u2022  Darkvision out to a range of 60 feet\n \u2022  An extra 10 feet of speed while the bearer is unencumbered\n \u2022  Proficiency with Constitution saving throws\n \u2022  Resistance to acid, cold, fire, lightning, or thunder damage (transmuter's choice whenever the transmuter chooses this benefit)\n\n If the transmuter has the stone and casts a transmutation spell of lst level or higher (marked above with *), it can change the effect of the stone."));
		transmuter.actions = Arrays.asList(new Attack("Quarterstaff", Type.MELEE_WEAPON, 2, "reach 5 ft., one target", ", or 3 (1d8 - 1) bludgeoning damage if used with two hands.", new Attack.Damage(new DiceRoll(1, 6, -1), DamageType.BLUDGEONING)));

		vampiricMist = new Creature();
		vampiricMist.name = "Vampiric Mist";
		vampiricMist.shortName = "The vampiric mist";
		vampiricMist.size = Size.MEDIUM;
		vampiricMist.type = CreatureType.UNDEAD;
		vampiricMist.alignment = Alignment.CHAOTIC_EVIL;
		vampiricMist.ac = 13;
		vampiricMist.hitDice = new DiceRoll(6, 8, 18);
		vampiricMist.speed = 0;
		vampiricMist.speeds.put(MovementType.FLY, 30);
		vampiricMist.abilityScores.put(Ability.STRENGTH, 6);
		vampiricMist.abilityScores.put(Ability.DEXTERITY, 16);
		vampiricMist.abilityScores.put(Ability.CONSTITUTION, 16);
		vampiricMist.abilityScores.put(Ability.INTELLIGENCE, 6);
		vampiricMist.abilityScores.put(Ability.WISDOM, 12);
		vampiricMist.abilityScores.put(Ability.CHARISMA, 7);
		vampiricMist.savingThrows.put(Ability.WISDOM, 3);
		vampiricMist.resistances.put(null, Stream.of(DamageType.ACID, DamageType.LIGHTNING, DamageType.COLD, DamageType.NECROTIC).collect(Collectors.toSet()));
		vampiricMist.resistances.put("from nonmagical weapons", new HashSet<>(Arrays.asList(DamageType.BLUDGEONING, DamageType.PIERCING, DamageType.SLASHING)));
		vampiricMist.immunities.put(null, new HashSet<>(Arrays.asList(DamageType.POISON)));
		Stream.of(Condition.CHARMED, Condition.EXHAUSTION, Condition.GRAPPLED, Condition.PARALYZED, Condition.PETRIFIED, Condition.POISONED, Condition.PRONE, Condition.RESTRAINED).forEach(vampiricMist.conditionImmunities::add);
		vampiricMist.senses.put(VisionType.DARKVISION, 60);
		vampiricMist.challengeRating = 3;
		vampiricMist.features = Arrays.asList(new Feature("Blood Sense", "The vampiric mist can sense living creatures that have blood or similar vital fluids in a radius of 60 feet."), //
				new Feature("Forbiddance", "The vampiric mist can't enter a residence eithout an invitation from one of the occupants."), //
				new Feature("Misty Form", "The vampiric mist can occupy another creature's space and vice versa. In addition, if air can pass through a space, the mist can pass through it without squeezing. Each foot of movement in water costs it 2 extra feet, rather than l extra foot. The mist can't manipulate objects in any way that requires hands; it can apply simple force only."), //
				new Feature("Sunlight Hypersensitivity", "The vampiric mist takes 20 radiant damage when it starts its turn in sunlight. While in sunlight, the mist has disadvantage on attack rolls and ability checks."));
		vampiricMist.actions = Arrays.asList(new Action("Blood Drain",
				"One creature in the vampiric mist's space must make a DC 13 Constitution saving throw (undead and constructs automatically succeed). On a failed save, the target takes 10 (2d6 + 3) necrotic damage, its hit point maximum is reduced by an amount equal to the necrotic damage taken, and the mist regains hit points equal to that amount. This reduction to the target's hit point maximum lasts until the target finishes a long rest. It dies if this effect reduces its hit point maximum to 0."));

		fourArmedGargoyle = new Creature();
		fourArmedGargoyle.name = "Gargoyle (Four-armed)";
		fourArmedGargoyle.shortName = "The gargoyle";
		fourArmedGargoyle.size = Size.MEDIUM;
		fourArmedGargoyle.type = CreatureType.ELEMENTAL;
		fourArmedGargoyle.alignment = Alignment.CHAOTIC_EVIL;
		fourArmedGargoyle.ac = 15;
		fourArmedGargoyle.armorNote = "natural armor";
		fourArmedGargoyle.hitDice = new DiceRoll(9, 8, 27);
		fourArmedGargoyle.speed = 30;
		fourArmedGargoyle.speeds.put(MovementType.FLY, 60);
		fourArmedGargoyle.abilityScores.put(Ability.STRENGTH, 15);
		fourArmedGargoyle.abilityScores.put(Ability.DEXTERITY, 11);
		fourArmedGargoyle.abilityScores.put(Ability.CONSTITUTION, 16);
		fourArmedGargoyle.abilityScores.put(Ability.INTELLIGENCE, 6);
		fourArmedGargoyle.abilityScores.put(Ability.WISDOM, 11);
		fourArmedGargoyle.abilityScores.put(Ability.CHARISMA, 7);
		fourArmedGargoyle.resistances.put("from nonmagical weapons that aren't adamantine", new HashSet<>(Arrays.asList(DamageType.BLUDGEONING, DamageType.PIERCING, DamageType.SLASHING)));
		fourArmedGargoyle.immunities.put(null, new HashSet<>(Arrays.asList(DamageType.POISON)));
		fourArmedGargoyle.conditionImmunities.add(Condition.EXHAUSTION);
		fourArmedGargoyle.conditionImmunities.add(Condition.PETRIFIED);
		fourArmedGargoyle.conditionImmunities.add(Condition.POISONED);
		fourArmedGargoyle.senses.put(VisionType.DARKVISION, 60);
		fourArmedGargoyle.languages.add("Terran");
		fourArmedGargoyle.challengeRating = 2;
		fourArmedGargoyle.features = Arrays.asList(new Feature("False Appearance", "While the gargoyle is motionless, it is indistinguishable from an inanimate statue."));
		fourArmedGargoyle.actions = Arrays.asList(new Action("Multiattack", "The gargoyle makes three attacks: one with its bite, and two with its claws."), //
				new Attack("Bite", Type.MELEE_WEAPON, 4, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(1, 6, 2), DamageType.PIERCING)), //
				new Attack("Claws", Type.MELEE_WEAPON, 4, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(1, 6, 2), DamageType.SLASHING)));

		conjurer = new Creature();
		conjurer.name = "Conjurer";
		conjurer.shortName = "The conjurer";
		conjurer.size = Size.MEDIUM;
		conjurer.type = CreatureType.HUMANOID;
		conjurer.subtype = "any race";
		conjurer.alignment = Alignment.ANY_ALIGNMENT;
		conjurer.ac = 12;
		conjurer.armorNote = "15 with mage armor";
		conjurer.hitDice = new DiceRoll(9, 8);
		conjurer.speed = 30;
		conjurer.abilityScores.put(Ability.STRENGTH, 9);
		conjurer.abilityScores.put(Ability.DEXTERITY, 14);
		conjurer.abilityScores.put(Ability.CONSTITUTION, 11);
		conjurer.abilityScores.put(Ability.INTELLIGENCE, 17);
		conjurer.abilityScores.put(Ability.WISDOM, 12);
		conjurer.abilityScores.put(Ability.CHARISMA, 11);
		conjurer.savingThrows.put(Ability.INTELLIGENCE, 6);
		conjurer.savingThrows.put(Ability.WISDOM, 4);
		conjurer.skills.put(Skill.ARCANA, 6);
		conjurer.skills.put(Skill.HISTORY, 6);
		conjurer.languages.add("any four languages");
		conjurer.challengeRating = 6;

		final Map<String, Map<String, String>> conjurerSpells = new LinkedHashMap<>();

		final Map<String, String> conjurerCantrips = new HashMap<>();
		Stream.of("acid splash", "mage hand", "poison spray", "prestidigitation").forEach(s -> conjurerCantrips.put(s, ""));
		conjurerSpells.put("Cantrips (at will)", conjurerCantrips);

		final Map<String, String> conjurerLevel1 = new HashMap<>();
		Stream.of("mage armor", "magic missile", "unseen servant*").forEach(s -> conjurerLevel1.put(s, ""));
		conjurerSpells.put("1st level (4 slots)", conjurerLevel1);

		final Map<String, String> conjurerLevel2 = new HashMap<>();
		Stream.of("cloud of daggers*", "misty step*", "web*").forEach(s -> conjurerLevel2.put(s, ""));
		conjurerSpells.put("2nd level (3 slots)", conjurerLevel2);

		final Map<String, String> conjurerLevel3 = new HashMap<>();
		Stream.of("fireball", "stinking cloud*").forEach(s -> conjurerLevel3.put(s, ""));
		conjurerSpells.put("3rd level (3 slots)", conjurerLevel3);

		final Map<String, String> conjurerLevel4 = new HashMap<>();
		Stream.of("Evard's black tentacles*", "stoneskin").forEach(s -> conjurerLevel4.put(s, ""));
		conjurerSpells.put("4th level (3 slots)", conjurerLevel4);

		final Map<String, String> conjurerLevel5 = new HashMap<>();
		Stream.of("cloudkill*", "conjure elemental*").forEach(s -> conjurerLevel5.put(s, ""));
		conjurerSpells.put("5th level (2 slots)", conjurerLevel5);

		conjurer.features = Arrays.asList(new Spellcasting(null, "the conjurer", 9, Ability.INTELLIGENCE, 14, "its", 6, null, "wizard", null, conjurerSpells), //
				new Feature("Benign Transposition", "Recharges after the Conjurer Casts a Conjuration Spell of 1st Level or Higher", "As a bonus action, the conjurer teleports up to 30 feet to an unoccupied space that it can see. If it instead chooses a space within range that is occupied by a willing Small or Medium creature, they both teleport, swapping places."));
		conjurer.actions = Arrays.asList(new Attack("Dagger", Type.MELEE_OR_RANGED_WEAPON, 4, "reach 5 ft. or range 20/60 ft., one target", null, new Attack.Damage(new DiceRoll(1, 4, 2), DamageType.PIERCING)));

	}

}
