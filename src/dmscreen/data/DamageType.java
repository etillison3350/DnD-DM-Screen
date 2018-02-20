package dmscreen.data;

public enum DamageType {

	ACID,
	BLUDGEONING,
	COLD,
	FIRE,
	FORCE,
	LIGHTNING,
	NECROTIC,
	PIERCING,
	POISON,
	PSYCHIC,
	RADIANT,
	SLASHING,
	THUNDER;

	@Override
	public String toString() {
		return name().toLowerCase();
	}

}
