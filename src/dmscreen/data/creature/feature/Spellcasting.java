package dmscreen.data.creature.feature;

import java.util.Map;

import dmscreen.data.base.Ability;

public class Spellcasting extends InnateSpellcasting {

	private final int level;
	private final int attackModifier;
	private final String spellcastingClass;
	private final String extra;

	public Spellcasting(final String note, final String shortName, final int level, final Ability ability, final int saveDC, final String pronoun, final int attackModifier, final String extra2, final String spellcastingClass, final String extra, final Map<String, Map<String, String>> spells) {
		super(note, shortName, ability, saveDC, pronoun, extra, spells);
		this.level = level;
		this.attackModifier = attackModifier;
		this.spellcastingClass = spellcastingClass;
		this.extra = extra2;
	}

	public Spellcasting(final String note, final String shortName, final int level, final Ability ability, final int saveDC, final int attackModifier, final String extra2, final String spellcastingClass, final String extra, final Map<String, Map<String, String>> spells) {
		this(note, shortName, level, ability, saveDC, "it", attackModifier, extra2, spellcastingClass, extra, spells);
	}

	public int getLevel() {
		return level;
	}

	public int getAttackModifier() {
		return attackModifier;
	}

	public String getSpellcastingClass() {
		return spellcastingClass;
	}

	public String getExtra() {
		return extra;
	}

}
