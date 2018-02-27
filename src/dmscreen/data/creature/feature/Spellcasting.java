package dmscreen.data.creature.feature;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import dmscreen.Util;
import dmscreen.data.base.Ability;
import dmscreen.statblock.StatBlock;
import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Spellcasting extends InnateSpellcasting {

	private final int level;
	private final String spellcastingClass;
	private final String extra;

	public Spellcasting(final String note, final String shortName, final int level, final Ability ability, final int saveDC, final String pronoun, final int attackModifier, final String extra2, final String spellcastingClass, final String extra, final Map<String, Map<String, String>> spells) {
		super("Spellcasting", note, shortName, ability, saveDC, attackModifier, pronoun, extra, spells);
		this.level = level;
		this.spellcastingClass = spellcastingClass;
		this.extra = extra2;
	}

	public Spellcasting(final String note, final String shortName, final int level, final Ability ability, final int saveDC, final int attackModifier, final String extra2, final String spellcastingClass, final String extra, final Map<String, Map<String, String>> spells) {
		this(note, shortName, level, ability, saveDC, "it", attackModifier, extra2, spellcastingClass, extra, spells);
	}

	public int getLevel() {
		return level;
	}

	public String getSpellcastingClass() {
		return spellcastingClass;
	}

	public String getExtra() {
		return extra;
	}

	@Override
	public Node getNode() {
		final boolean noDescription = getDescription() == null || getDescription().isEmpty();
		final TextFlow line = StatBlock.dataLine(getTitle() + ".", String.format("%s is a %d%s level spellcaster. %s spellcasting ability is %s (save DC %d, %+d to hit with spell attacks)%s. %1$s has %s %s spells prepared%s\n", Util.sentenceCase(getShortName()), level, Util.ordinal(level), Util.sentenceCase(getPronoun()), Util.sentenceCase(getAbility().name()), getSaveDC(), getAttackModifier(), extra == null || extra.isEmpty() ? "" : " " + extra, noDescription ? "the following" : "a number of",
				spellcastingClass, noDescription ? ":" : ". " + getDescription()), true);

		getSpells().forEach((s, map) -> {
			line.getChildren().add(new Text("\n" + s + ": "));
			final TreeSet<String> spells = new TreeSet<>(map.keySet());
			final Iterator<String> iter = spells.iterator();

			while (iter.hasNext()) {
				final String spell = iter.next();
				final Text spellText = new Text(spell);
				spellText.setFont(Font.font("System", FontPosture.ITALIC, Font.getDefault().getSize()));
				line.getChildren().add(spellText);

				final String note = map.get(spell);
				if (iter.hasNext() || note != null && !note.isEmpty()) line.getChildren().add(new Text(note == null || note.isEmpty() ? ", " : String.format(" (%s)%s", note, iter.hasNext() ? ", " : "")));
			}
		});
		line.getChildren().add(new Text("\n"));

		return line;
	}

}
