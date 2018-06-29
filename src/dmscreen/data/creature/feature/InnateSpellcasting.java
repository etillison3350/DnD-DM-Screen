package dmscreen.data.creature.feature;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import dmscreen.Screen;
import dmscreen.Util;
import dmscreen.data.Data;
import dmscreen.data.base.Ability;
import dmscreen.data.base.DataSet;
import dmscreen.data.spell.Spell;
import dmscreen.statblock.StatBlock;
import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Popup;
import javafx.util.Duration;

public class InnateSpellcasting extends Feature {

	private final String shortName;
	private final Ability ability;
	private final int attackModifier;
	private final int saveDC;
	private final String pronoun;
	private final Map<String, Map<String, String>> spells;

	protected InnateSpellcasting(final String name, final String note, final String shortName, final Ability ability, final int saveDC, final int attackModifier, final String pronoun, final String extra, final Map<String, Map<String, String>> spells) {
		super(name, note, extra);

		this.shortName = shortName;
		this.ability = ability;
		this.saveDC = saveDC;
		this.attackModifier = attackModifier;
		this.pronoun = pronoun;
		this.spells = spells;
	}

	protected InnateSpellcasting(final String name, final String note, final String shortName, final Ability ability, final int saveDC, final int attackModifier, final String extra, final Map<String, Map<String, String>> spells) {
		this(name, note, shortName, ability, saveDC, attackModifier, "it", extra, spells);
	}

	public InnateSpellcasting(final String note, final String shortName, final Ability ability, final int saveDC, final int attackModifier, final String pronoun, final String extra, final Map<String, Map<String, String>> spells) {
		this("Innate Spellcasting", note, shortName, ability, saveDC, attackModifier, pronoun, extra, spells);
	}

	public InnateSpellcasting(final String note, final String shortName, final Ability ability, final int saveDC, final int attackModifier, final String extra, final Map<String, Map<String, String>> spells) {
		this(note, shortName, ability, saveDC, attackModifier, "it", extra, spells);
	}

	public String getShortName() {
		return shortName;
	}

	public Ability getAbility() {
		return ability;
	}

	public int getSaveDC() {
		return saveDC;
	}

	public int getAttackModifier() {
		return attackModifier;
	}

	public String getPronoun() {
		return pronoun;
	}

	public Map<String, Map<String, String>> getSpells() {
		return spells;
	}

	@Override
	public Node getNode() {
		final boolean noDescription = getDescription() == null || getDescription().isEmpty();
		final TextFlow line = StatBlock.dataLine(getTitle() + ".",
				String.format("%s's innate spellcasting ability is %s (spell save DC %d%s). %s can innately cast %s spells, requiring no material components%s\n", Util.sentenceCase(shortName), Util.sentenceCase(ability.name()), saveDC, attackModifier > Integer.MIN_VALUE ? String.format(", %+d to hit with spell attacks", attackModifier) : "", Util.sentenceCase(pronoun), noDescription ? "the following" : "a number of", noDescription ? ":" : ". " + getDescription()), true);

		spells.forEach((s, map) -> {
			line.getChildren().add(new Text("\n" + s + ": "));
			final TreeSet<String> spells = new TreeSet<>(map.keySet());
			final Iterator<String> iter = spells.iterator();

			while (iter.hasNext()) {
				final String spellName = iter.next();

				final Text spellText = new Text(spellName);
				spellText.setFont(Font.font(Screen.DEFAULT_FONT_NAME, FontPosture.ITALIC, Font.getDefault().getSize()));

				try {
					InnateSpellcasting.addPopup(spellText, getSpellFromName(spellName));
				} catch (final IllegalArgumentException e) {}

				line.getChildren().add(spellText);

				final String note = map.get(spellName);
				if (iter.hasNext() || note != null && !note.isEmpty()) line.getChildren().add(new Text(note == null || note.isEmpty() ? ", " : String.format(" (%s)%s", note, iter.hasNext() ? ", " : "")));
			}
		});
		line.getChildren().add(new Text("\n"));

		return line;
	}

	protected static Spell getSpellFromName(final String spellName) {
		for (final DataSet dataSet : Data.getData().values()) {
			for (final Spell sp : dataSet.spells) {
				if (sp.name.equalsIgnoreCase(spellName.replaceAll("[^A-Za-z\\s\'\\/]", ""))) {
					return sp;
				}
			}
		}
		throw new IllegalArgumentException("No spell with the name \"" + spellName + "\" could be found.");
	}

	protected static void addPopup(final Text spellText, final Spell spell) {
		final Popup popup = new Popup();
		final PauseTransition pt = new PauseTransition(Duration.seconds(1));
		pt.setOnFinished(event -> {
			final Bounds screenBounds = spellText.localToScreen(spellText.getBoundsInLocal());

			popup.setAnchorX(screenBounds.getMinX());
			popup.setAnchorY(screenBounds.getMinY());
			popup.getContent().clear();
			final BorderPane content = new BorderPane(StatBlock.getStatBlock(spell));
			content.setTop(new Pane());
			content.setPadding(new Insets(8));
			content.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(4), Insets.EMPTY)));
			content.setOnMouseExited(e -> {
				popup.hide();
			});
			content.setMaxWidth(384);
			popup.getContent().add(content);
			popup.show(spellText.getScene().getWindow());
		});

		spellText.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
			if (newValue) {
				pt.play();
			} else {
				pt.stop();
			}
		});
	}

}
