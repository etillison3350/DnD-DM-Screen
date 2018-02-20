package dmscreen.data;

import java.util.Random;

public class DiceRoll {

	public static final Random rand = new Random();

	public final int number;
	public final int die;
	public final int modifier;

	public DiceRoll(final int number, final int die) {
		this(number, die, 0);
	}

	public DiceRoll(final int number, final int die, final int modifier) {
		this.number = number;
		this.die = die;
		this.modifier = modifier;
	}

	public double expectedValue() {
		return (die + 1) * number * 0.5 + modifier;
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
		return String.format("%dd%d%s", number, die, modifier == 0 ? "" : modifier > 0 ? String.format("+ %d", modifier) : String.format("- %d", -modifier));
	}

}
