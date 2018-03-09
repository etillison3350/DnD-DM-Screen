package dmscreen.data.base;

import java.util.Random;

public class DiceRoll {

	public static final Random rand = new Random();

	public final int number;
	public final int die;
	public final int modifier;
	public final Double expected;

	public DiceRoll(final int number, final int die) {
		this(number, die, 0);
	}

	public DiceRoll(final int number, final int die, final int modifier) {
		this.number = number;
		this.die = die;
		this.modifier = modifier;
		expected = null;
	}

	public DiceRoll(final int number, final int die, final int modifier, final double overrideExpectedValue) {
		this.number = number;
		this.die = die;
		this.modifier = modifier;
		expected = overrideExpectedValue;
	}

	public double expectedValue() {
		return expected == null ? (die + 1) * number * 0.5 + modifier : expected;
	}

	public boolean overridesExpectedValue() {
		return expected != null;
	}

	public int roll() {
		int ret = 0;
		for (int i = 0; i < number; i++) {
			ret += rand.nextInt(die) + 1;
		}
		return ret + modifier;
	}

	@Override
	public String toString() {
		return String.format("%dd%d%s", number, die, modifier == 0 ? "" : modifier > 0 ? String.format(" + %d", modifier) : String.format(" - %d", -modifier));
	}

}
