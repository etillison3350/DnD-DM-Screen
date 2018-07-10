package dmscreen.misc;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import dmscreen.data.Data;
import dmscreen.data.base.Ability;
import dmscreen.data.base.DamageType;
import dmscreen.data.base.DiceRoll;
import dmscreen.data.base.Size;
import dmscreen.data.creature.Alignment;
import dmscreen.data.creature.Condition;
import dmscreen.data.creature.Creature;
import dmscreen.data.creature.CreatureType;
import dmscreen.data.creature.VisionType;
import dmscreen.data.creature.feature.Action;
import dmscreen.data.creature.feature.Attack;
import dmscreen.data.creature.feature.Attack.Type;
import dmscreen.data.creature.feature.Feature;

public class MiscCreatures {

	private MiscCreatures() {}

	public static void main(final String[] args) {
		try {
			Files.createDirectories(Paths.get("resources/misc/"));

			final List<Object> toJson = new ArrayList<>();
			for (final Field field : MiscCreatures.class.getDeclaredFields()) {
				if (Modifier.isStatic(field.getModifiers())) {
					try {
						toJson.add(field.get(null));
					} catch (IllegalArgumentException | IllegalAccessException e) {}
				}
			}

			Files.write(Paths.get("resources/misc/creatures.json"), Data.GSON.toJson(toJson).getBytes());
		} catch (final IOException e) {
			e.printStackTrace();
		}

	}

	public static final Creature lodestoneGolem;

	static {
		lodestoneGolem = new Creature();
		lodestoneGolem.name = "Lodestone Golem";
		lodestoneGolem.shortName = "The golem";
		lodestoneGolem.size = Size.LARGE;
		lodestoneGolem.type = CreatureType.CONSTRUCT;
		lodestoneGolem.alignment = Alignment.UNALIGNED;
		lodestoneGolem.ac = 17;
		lodestoneGolem.armorNote = "natural armor";
		lodestoneGolem.hitDice = new DiceRoll(17, 10, 85);
		lodestoneGolem.speed = 30;
		lodestoneGolem.abilityScores.put(Ability.STRENGTH, 22);
		lodestoneGolem.abilityScores.put(Ability.DEXTERITY, 8);
		lodestoneGolem.abilityScores.put(Ability.CONSTITUTION, 20);
		lodestoneGolem.abilityScores.put(Ability.INTELLIGENCE, 3);
		lodestoneGolem.abilityScores.put(Ability.WISDOM, 11);
		lodestoneGolem.abilityScores.put(Ability.CHARISMA, 1);
		lodestoneGolem.immunities.put(null, new HashSet<>(Arrays.asList(DamageType.POISON, DamageType.PSYCHIC)));
		lodestoneGolem.immunities.put("from nonmagical weapons that aren't adamantine", new HashSet<>(Arrays.asList(DamageType.BLUDGEONING, DamageType.PIERCING, DamageType.SLASHING)));
		lodestoneGolem.conditionImmunities.addAll(Arrays.asList(Condition.CHARMED, Condition.EXHAUSTION, Condition.FRIGHTENED, Condition.PARALYZED, Condition.PETRIFIED, Condition.POISONED, Condition.STUNNED));
		lodestoneGolem.senses.put(VisionType.DARKVISION, 60);
		lodestoneGolem.languages.add("understands the languages of its creator but can't speak");
		lodestoneGolem.challengeRating = 13;
		lodestoneGolem.features = Arrays.asList(new Feature("Immutable Form", "The golem is immune to any spell or effect that would alter its form."), //
				new Feature("Magic Resistance", "The golem has advantage on saving throws against spells and other magical effects."), //
				new Feature("Magic Weapons", "The golem's attacks are magical."), //
				new Feature("Magnetic", "Whenever a creature makes an attack against the golem with a weapon made of metal or a spell that would deal lightning damage, or when the golem makes an attack against a creature wearing armor made of metal, the attack hits regardless of the attack roll. If the attack would miss, it instead deals half the damage it would normally deal."));
		lodestoneGolem.actions = Arrays.asList(new Action("Multiattack", "The golem makes two slam attacks."), //
				new Attack("Slam", Type.MELEE_WEAPON, 10, "reach 5 ft., one target", null, new Attack.Damage(new DiceRoll(3, 8, 6), DamageType.BLUDGEONING)), //
				new Action("Magnetize", "Recharge 6",
						"The golem targets one or more creatures it can see within 10 feet of it. Each target must succeed on a DC 17 Dexterity saving throw against this magic. On a failed save, a target is treated as though it were wearing metal armor for the purpose of the golem's Magnetic feature. In addition, the target's attacks ignore the golem's Magnetic feature, and the target's melee attacks or ranged attacks that would deal lightning damage are made with a -5 penalty to the attack roll, as they are repelled from the golem. These effects last for 1 minute. A target can repeat the saving throw at the end of each of its turns, ending the effect on itself on a success."));

	}
}
