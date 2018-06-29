package dmscreen.data.base;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiceRoll {

	public static final Random rand = new Random();

	public final int number;
	public final int die;
	public final int modifier;
	public final Double expected;

	public static final Pattern VALUE_DIE_ROLL = Pattern.compile("^(\\d+)\\((\\d+)d(\\d+)([+\\-]\\d+)?\\)$");

	public static final Pattern DIE_ROLL = Pattern.compile("^(\\d+)d(\\d+)([+\\-]\\d+)?$");

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
		if (Math.abs(die) <= 1) {
			return String.format("%d%s", die == 0 ? modifier : number * die, die == 0 || modifier == 0 ? "" : modifier > 0 ? String.format(" + %d", modifier) : String.format(" - %d", -modifier));
		}
		return String.format("%dd%d%s", number, die, modifier == 0 ? "" : modifier > 0 ? String.format(" + %d", modifier) : String.format(" - %d", -modifier));
	}

	public static DiceRoll fromString(final String value) {
		final String formatted = value.toLowerCase().replaceAll("[^\\dd+\\-\\(\\)]+", "");
		Matcher matcher = DIE_ROLL.matcher(formatted);
		if (matcher.find()) {
			return new DiceRoll(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), matcher.group(3) == null ? 0 : Integer.parseInt(matcher.group(3)));
		}
		matcher = VALUE_DIE_ROLL.matcher(formatted);
		if (matcher.find()) {
			return new DiceRoll(Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)), matcher.group(4) == null ? 0 : Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(1)));
		}

		return null;
	}

	public static boolean isValidDiceRoll(final String diceRoll) {
		final String formatted = diceRoll.toLowerCase().replaceAll("[^\\dd+\\-\\(\\)]+", "");
		return DiceRoll.DIE_ROLL.matcher(formatted).find() || DiceRoll.VALUE_DIE_ROLL.matcher(formatted).find();
	}

}
