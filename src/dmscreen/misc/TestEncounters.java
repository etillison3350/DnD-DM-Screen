package dmscreen.misc;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dmscreen.Screen;
import dmscreen.data.Data;
import dmscreen.data.adventure.CreatureSet;
import dmscreen.data.adventure.Encounter;
import dmscreen.data.adventure.RandomEncounter;
import dmscreen.data.base.DiceRoll;

public class TestEncounters {

	public static void main(final String[] args) {
		try {
			Files.createDirectories(Paths.get("resources/source/"));

			final List<Object> toJson = new ArrayList<>();
			for (final Field field : TestEncounters.class.getDeclaredFields()) {
				if (Modifier.isStatic(field.getModifiers()) && !Modifier.isTransient(field.getModifiers())) {
					try {
						toJson.add(field.get(null));
					} catch (IllegalArgumentException | IllegalAccessException e) {}
				}
			}

			Files.write(Paths.get("resources/source/encounters.json"), Data.GSON.toJson(toJson).getBytes());
		} catch (final IOException e) {
			e.printStackTrace();
		}

		Screen.main(args);
	}

	public static final RandomEncounter test, test2;

	// 1 zombie
	// 2 zombies
	// 1d4 zombies
	// 1d4 skeletons or zombies
	// 1d4 skeletons or 1d6 zombies
	// 1d4 skeletons, zombies, or gnolls
	// 1d4 skeletons, 1d4 zombies, or 1d6 gnolls
	// 1d4 skeletons and 1d4 zombies
	// 1d4 skeletons and 1d4 zombies or gnolls
	// 1d4 skeletons or zombies and 1d6 gnolls
	// 1d4 skeletons or zombies and 1d6 gnolls or orcs
	// 1d4 skeletons, 1d4 zombies, and 1d6 gnolls
	// 1d4 skeletons, plus 1d4 zombies or 1d6 gnolls
	// 1d4 skeletons or 1d6 zombies, plus 1d6 gnolls
	// 1d4 skeletons and 1d4 zombies, plus 1d6 gnolls or 1d8 orcs
	// 1d4 skeletons, plus 1d4 zombies or 1d6 gnolls, plus 1d8 orcs

	private static transient final List<String> creatureTypes = Arrays.asList("skeleton", "zombie", "gnoll", "orc");

	static {
		final Encounter o1 = new Encounter(1, new CreatureSet("zombie"));
		final Encounter o2 = new Encounter(1, new CreatureSet(new DiceRoll(2, 1), "zombie", "zombies"));
		final Encounter o3 = new Encounter(1, new CreatureSet(new DiceRoll(1, 4), "zombie", "zombies"));
		final Map<String, String> skZ = new LinkedHashMap<>();
		skZ.put("skeleton", "skeletons");
		skZ.put("zombie", "zombies");
		final Encounter o4 = new Encounter(2, new CreatureSet(new DiceRoll(1, 4), skZ));
		final Encounter o5 = new Encounter(2, new CreatureSet(creaturesMap(4, 6), skZ));
		final Map<String, String> skZG = new LinkedHashMap<>(skZ);
		skZG.put("gnoll", "gnolls");
		final Encounter o6 = new Encounter(2, new CreatureSet(creaturesMap(4, 4, 4), skZG));
		final Encounter o7 = new Encounter(2, new CreatureSet(creaturesMap(4, 4, 6), skZG));
		final Encounter o8 = new Encounter(3, new CreatureSet(new DiceRoll(1, 4), "skeleton", "skeletons"), new CreatureSet(new DiceRoll(1, 4), "zombie", "zombies"));
		final Encounter o9 = new Encounter(3, new CreatureSet(new DiceRoll(1, 4), "skeleton", "skeletons"), new CreatureSet(creaturesMap(0, 4, 4), skZG));
		final Encounter oA = new Encounter(3, new CreatureSet(creaturesMap(4, 4), skZ), new CreatureSet(new DiceRoll(1, 6), "gnoll", "gnolls"));
		final Map<String, String> skZGO = new LinkedHashMap<>(skZG);
		skZGO.put("orc", "orcs");
		final Encounter oB = new Encounter(3, new CreatureSet(creaturesMap(4, 4), skZ), new CreatureSet(creaturesMap(0, 0, 6, 6), skZGO));
		final Encounter oC = new Encounter(4, new CreatureSet(new DiceRoll(1, 4), "skeleton", "skeletons"), new CreatureSet(new DiceRoll(1, 4), "zombie", "zombies"), new CreatureSet(new DiceRoll(1, 6), "gnoll", "gnolls"));
		final Encounter oD = new Encounter(4, new CreatureSet(new DiceRoll(1, 4), "skeleton", "skeletons"), new CreatureSet(creaturesMap(0, 4, 6), skZG));
		final Encounter oE = new Encounter(4, new CreatureSet(creaturesMap(4, 6), skZ), new CreatureSet(new DiceRoll(1, 6), "gnoll", "gnolls"));
		final Encounter oF = new Encounter(4, new CreatureSet(new DiceRoll(1, 4), "skeleton", "skeletons"), new CreatureSet(new DiceRoll(1, 4), "zombie", "zombies"), new CreatureSet(creaturesMap(0, 0, 6, 8), skZGO));
		final Encounter oG = new Encounter(5, new CreatureSet(new DiceRoll(1, 4), "skeleton", "skeletons"), new CreatureSet(creaturesMap(0, 4, 6), skZG), new CreatureSet(new DiceRoll(1, 8), "orc", "orcs"));
		final Encounter oH = new Encounter(6, new CreatureSet(creaturesMap(4, 6), skZ), new CreatureSet(creaturesMap(0, 0, 6, 8), skZGO));
		test = new RandomEncounter("Test Cases", o1, o2, o3, o4, o5, o6, o7, o8, o9, oA, oB, oC, oD, oE, oF, oG, oH);
		test.diceRoll = new DiceRoll(9, 12);

		test2 = new RandomEncounter("Dice Test");
		for (int i = 0; i < 34; i++)
			test2.encounters.add(new Encounter(1, Integer.toString(test2.encounters.size())));
		test2.diceRoll = new DiceRoll(1, 50);
	}

	private static Map<String, DiceRoll> creaturesMap(final int... amounts) {
		final Map<String, DiceRoll> ret = new LinkedHashMap<>();
		for (int i = 0; i < amounts.length; i++) {
			if (amounts[i] > 0) {
				ret.put(creatureTypes.get(i), new DiceRoll(1, amounts[i]));
			}
		}
		return ret;
	}

}
