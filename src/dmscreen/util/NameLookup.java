package dmscreen.util;

import java.util.HashMap;
import java.util.Map;

import dmscreen.data.Data;
import dmscreen.data.base.DataSet;
import dmscreen.data.creature.Creature;
import dmscreen.data.spell.Spell;

public final class NameLookup {

	private NameLookup() {}

	private static final Map<String, Creature> creatures = new HashMap<>();
	private static final Map<String, Spell> spells = new HashMap<>();

	public static Creature creatureFromName(final String name) {
		final Creature c = creatures.get(name);
		if (c != null) return c;

		for (final DataSet dataSet : Data.getData().values()) {
			for (final Creature cr : dataSet.creatures) {
				if (cr.name.equalsIgnoreCase(name)) {
					creatures.put(name, cr);
					return cr;
				}
			}
		}

		throw new IllegalArgumentException("No creature with the name \"" + name + "\" could be found.");
	}

	public static Spell spellFromName(final String name) {
		final Spell s = spells.get(name);
		if (s != null) return s;

		for (final DataSet dataSet : Data.getData().values()) {
			for (final Spell sp : dataSet.spells) {
				if (sp.name.equalsIgnoreCase(name.replaceAll("[^A-Za-z\\s\'\\/]", ""))) {
					spells.put(name, sp);
					return sp;
				}
			}
		}
		throw new IllegalArgumentException("No spell with the name \"" + name + "\" could be found.");
	}

}
