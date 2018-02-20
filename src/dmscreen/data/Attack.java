package dmscreen.data;

public class Attack extends Action {

	private final Damage[] damages;

	public Attack(final String name, final String note, final String description, final Damage... damages) {
		super(name, note, description);

		this.damages = damages;
	}

	public Attack(final String name, final String description, final Damage... damages) {
		this(name, "", description, damages);
	}

	public Damage[] getDamages() {
		return damages;
	}

	public String getOnHit() {
		final StringBuffer onHit = new StringBuffer();
		for (int i = 0; i < damages.length; i++) {
			if (i > 0) onHit.append(" plus ");
			onHit.append(damages[i].diceRoll.expectedValue());
			onHit.append(" (");
			onHit.append(damages[i].diceRoll);
			onHit.append(") ");
			onHit.append(damages[i].type);
			onHit.append(" damage");
		}
		if (getDescription() == null || getDescription().isEmpty())
			onHit.append('.');
		else
			onHit.append(getDescription());

		return onHit.toString();
	}

	public static enum Type {
		MELEE_WEAPON,
		MELEE_SPELL,
		RANGED_WEAPON,
		RANGED_SPELL;
	}

	public static class Damage {
		public final DiceRoll diceRoll;
		public final DamageType type;

		public Damage(final DiceRoll diceRoll, final DamageType type) {
			this.diceRoll = diceRoll;
			this.type = type;
		}

	}

}
