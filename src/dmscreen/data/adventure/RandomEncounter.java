package dmscreen.data.adventure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import dmscreen.data.base.DiceRoll;
import javafx.scene.Node;
import javafx.scene.text.TextFlow;

public class RandomEncounter {

	public String name;
	public final List<Encounter> encounters = new ArrayList<>();
	public DiceRoll diceRoll = null;

	public RandomEncounter() {
		name = "";
	}

	public RandomEncounter(final String name, final Encounter... encounters) {
		this.name = name;
		Arrays.stream(encounters).forEach(this.encounters::add);
	}

	public RandomEncounter(final String name, final Collection<Encounter> encounters) {
		this.name = name;
		this.encounters.addAll(encounters);
	}

	public Node roll() {
		final int total = getTotalWeight();
		final int roll = diceRoll == null ? DiceRoll.rand.nextInt(total) : diceRoll.roll() - diceRoll.min();

		double current = 0;
		for (final Encounter enc : encounters) {
			current += enc.weight * (diceRoll == null ? 1 : (diceRoll.max() - diceRoll.min() + 1) / total);
			if (current > roll) {
				final List<CreatureRoll> ret = new ArrayList<>();
				for (final CreatureSet set : enc.creatures) {
					final String[] creatures = set.amounts.keySet().toArray(new String[set.amounts.size()]);
					final String name = creatures[DiceRoll.rand.nextInt(creatures.length)];
					ret.add(new CreatureRoll(set, name, set.amounts.get(name).roll()));
				}
				return enc.getNodeFromRoll(ret);
			}
		}

		return new TextFlow();
	}

	public int[] getEncounterRanges() {
		final DiceRoll diceRoll = getDiceRoll();
		final int min = diceRoll.min();
		final int max = diceRoll.max();

		final int total = getTotalWeight();

		final int[] ret = new int[encounters.size() + 1];
		int n = 0;
		ret[0] = min;
		for (int i = 1; i < ret.length; i++) {
			n += encounters.get(i - 1).weight;
			ret[i] = min + n * (max - min + 1) / total;
		}

		return ret;
	}

	private static final int[] standardDice = {4, 6, 8, 10, 12, 20, 100};

	public DiceRoll getDiceRoll() {
		if (diceRoll != null) return diceRoll;
		final int total = getTotalWeight();
		for (final int d : standardDice) {
			if (d % total == 0) {
				return new DiceRoll(1, d);
			}
		}
		return new DiceRoll(1, total);
	}

	protected int getTotalWeight() {
		return encounters.stream().mapToInt(e -> e.weight).sum();
	}

}
