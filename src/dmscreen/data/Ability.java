package dmscreen.data;

public enum Ability {

	STRENGTH,
	DEXTERITY,
	CONSTITUTION,
	INTELLIGENCE,
	WISDOM,
	CHARISMA;

	public String abbr() {
		return name().substring(0, 3);
	}

}
