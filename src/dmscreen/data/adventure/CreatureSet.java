package dmscreen.data.adventure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import dmscreen.data.base.DiceRoll;

public class CreatureSet {

	final Map<String, DiceRoll> amounts = new LinkedHashMap<>();
	final Map<String, String> names = new HashMap<>();

	public CreatureSet(final String creatureName) {
		this(new DiceRoll(1, 1), creatureName);
	}

	public CreatureSet(final String creatureName, final String displayName) {
		this(new DiceRoll(1, 1), creatureName, displayName);
	}

	public CreatureSet(final DiceRoll amount, final String creatureName) {
		this(amount, creatureName, null);
	}

	public CreatureSet(final DiceRoll amount, final String creatureName, final String displayName) {
		amounts.put(creatureName, amount);
		if (displayName != null) names.put(creatureName, displayName);
	}

	public CreatureSet(final DiceRoll amount, final Collection<String> creatureNames) {
		if (creatureNames.isEmpty()) throw new IllegalArgumentException("CreatureSets must contain at least one creature");

		creatureNames.forEach(c -> amounts.put(c, amount));
	}

	public CreatureSet(final DiceRoll amount, final Map<String, String> creatureNames) {
		if (creatureNames.isEmpty()) throw new IllegalArgumentException("CreatureSets must contain at least one creature");

		creatureNames.keySet().forEach(c -> amounts.put(c, amount));
		names.putAll(creatureNames);
	}

	public CreatureSet(final Map<String, DiceRoll> creatures, final Map<String, String> creatureNames) {
		if (creatures.isEmpty()) throw new IllegalArgumentException("CreatureSets must contain at least one creature");

		amounts.putAll(creatures);
		creatureNames.keySet().stream().filter(amounts::containsKey).forEach(key -> names.put(key, creatureNames.get(key)));
	}

	public boolean multipleOr() {
		if (amounts.isEmpty()) return false;

		final Iterator<DiceRoll> iter = amounts.values().iterator();
		final DiceRoll first = iter.next();

		while (iter.hasNext()) {
			if (!first.equals(iter.next())) return true;
		}

		return false;
	}

	public Collection<String> getCreatures() {
		return new ArrayList<>(amounts.keySet());
	}

	public DiceRoll getAmount(final String creature) {
		return amounts.get(creature);
	}

	public String getDisplayName(final String creature) {
		return names.get(creature);
	}

}
