package dmscreen.data.adventure;

import dmscreen.data.creature.Creature;
import dmscreen.util.NameLookup;

/**
 * Used for the output of rolling a random encounter.
 */
public class CreatureRoll {

	public final CreatureSet source;
	public final String creatureName;
	public final int number;
	public final int[] hitPoints;

	public CreatureRoll(final CreatureSet source, final String creature, final int number) {
		this.source = source;
		creatureName = creature;
		this.number = number;

		int[] hitPoints;
		try {
			final Creature c = NameLookup.creatureFromName(creature);
			hitPoints = new int[number];
			for (int n = 0; n < number; n++)
				hitPoints[n] = c.hitDice.roll();
		} catch (final Exception e) {
			hitPoints = null;
		}
		this.hitPoints = hitPoints;
	}

}
