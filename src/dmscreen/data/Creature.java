package dmscreen.data;

import java.util.HashMap;

public class Creature {

	public static final int[] XP = {0, 10, 25, 50, 100, 200, 450, 700, 1100, 1800, 2300, 2900, 3900, 5000, 5900, 7200, 8400, 10000, 11500, 13000, 15000, 18000, 20000, 22000, 25000, 33000, 41000, 50000, 62000, 75000, 90000, 105000, 120000, 135000, 155000};

	public String name;
	public SizeClass size;
	public CreatureType type;
	public String subtype;
	public Alignment alignment;
	public int ac;
	public DiceRoll hitDice;
	public HashMap<SpeedType, Integer> speeds;
	public int[] abilityScores;
	public int[] savingThrows;
	public int[] skills;
	public DamageType[] vulnerabilities;
	public DamageType[] resistances;
	public DamageType[] immunities;
	public Condition[] conditionImmunities;
	public HashMap<VisionType, Integer> senses;
	public String[] languages;

	/**
	 * Values of -1, -2, -3, -4, and -5 correspond to 1/2, 1/4, 1/8, 0 (10XP) and 0 (0XP)
	 * respectively
	 */
	public int challengeRating;

	public Feature[] features;
	public Action[] actions;
	public Action[] reactions;

	public String getHTML() {
		final StringBuffer html = new StringBuffer("<html><head><style type=\"text/css\"></style><body contentEditable=\"false\">");
		html.append(String.format("<h1>%s</h1>", name));

		return html.toString();
	}

}
