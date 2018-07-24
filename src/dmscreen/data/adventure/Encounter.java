package dmscreen.data.adventure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import dmscreen.statblock.StatBlock;
import dmscreen.util.NameLookup;
import dmscreen.util.Util;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Encounter {

	public final int weight;
	final List<CreatureSet> creatures = new ArrayList<>();
	public final String description;

	public Node getNode() {
		final TextFlow text = new TextFlow();

		int last = 0;
		for (int i = 0; i <= creatures.size(); i++) {
			if (i == creatures.size() || creatures.get(i).multipleOr()) {
				for (int n = last; n < i; n++) {
					if (n > last) {
						if (n == i - 1) {
							if (i - last == 2) {
								text.getChildren().add(new Text(" and "));
							} else {
								text.getChildren().add(new Text(", and "));
							}
						} else {
							text.getChildren().add(new Text(", "));
						}
					}
					final CreatureSet c = creatures.get(n);

					if (c.amounts.size() == 1) {
						addCreatureText(text, c.amounts.keySet().iterator().next(), c, true);
					} else {
						final Iterator<String> iter = c.amounts.keySet().iterator();
						addCreatureText(text, iter.next(), c, true);
						if (c.amounts.size() == 2) {
							text.getChildren().add(new Text(" or "));
							addCreatureText(text, iter.next(), c, false);
						} else {
							while (iter.hasNext()) {
								final String creature = iter.next();
								text.getChildren().add(new Text(iter.hasNext() ? ", " : ", or "));
								addCreatureText(text, creature, c, false);
							}
						}
					}
				}

				if (i != creatures.size()) {
					if (i > last) text.getChildren().add(new Text(", plus "));
					final CreatureSet c = creatures.get(i);

					if (c.amounts.size() == 1) {
						addCreatureText(text, c.amounts.keySet().iterator().next(), c, true);
					} else {
						final Iterator<String> iter = c.amounts.keySet().iterator();
						addCreatureText(text, iter.next(), c, true);
						if (c.amounts.size() == 2) {
							text.getChildren().add(new Text(" or "));
							addCreatureText(text, iter.next(), c, true);
						} else {
							while (iter.hasNext()) {
								final String creature = iter.next();
								text.getChildren().add(new Text(iter.hasNext() ? ", " : ", or "));
								addCreatureText(text, creature, c, true);
							}
						}
					}
					if (i != creatures.size() - 1) {
						text.getChildren().add(new Text(", plus "));
					}
				}

				last = i + 1;
			}
		}

		text.getChildren().add(new Text(description));

		return text;
	}

	public Node getNodeFromRoll(final Collection<? extends CreatureRoll> rolls) {
		final VBox ret = new VBox(2);

		final TextFlow text = new TextFlow();
		final Iterator<? extends CreatureRoll> iter = rolls.iterator();
		if (iter.hasNext()) {
			addCreatureText(text, iter.next());
			while (iter.hasNext()) {
				final CreatureRoll roll = iter.next();
				text.getChildren().add(new Text(rolls.size() <= 2 ? " and " : iter.hasNext() ? ", " : ", and "));
				addCreatureText(text, roll);
			}
		}
		text.getChildren().add(new Text(description));
		text.setPadding(new Insets(0, 0, 8, 0));
		ret.getChildren().add(text);

		for (final CreatureRoll cr : rolls) {
			for (int n = 0; n < cr.number; n++) {
				TextFlow hitPoints;
				if (cr.hitPoints == null) {
					hitPoints = new TextFlow(new Text(String.format(cr.number > 1 ? "%s %d: ? HP" : "%s: ? HP", Util.sentenceCase(cr.creatureName), n + 1)));
				} else {
					hitPoints = new TextFlow(new Text(String.format(cr.number > 1 ? "%s %d: %d HP" : "%s: %3$d HP", Util.sentenceCase(cr.creatureName), n + 1, cr.hitPoints[n])));
					StatBlock.addTooltip(hitPoints, NameLookup.creatureFromName(cr.creatureName));
				}
				ret.getChildren().add(hitPoints);
			}
		}

		return ret;
	}

	private static void addCreatureText(final TextFlow addTo, final String creatureName, final CreatureSet creatures, final boolean includeDice) {
		final Text text = new Text((includeDice ? creatures.amounts.get(creatureName) + " " : "") + (creatures.names.containsKey(creatureName) ? creatures.names.get(creatureName) : creatureName));
		addTo.getChildren().add(text);

		try {
			StatBlock.addTooltip(text, NameLookup.creatureFromName(creatureName));
		} catch (final IllegalArgumentException e) {}
	}

	private static void addCreatureText(final TextFlow addTo, final CreatureRoll roll) {
		final Text text = new Text(String.format("%d %s", roll.number, roll.source.names.containsKey(roll.creatureName) ? roll.source.names.get(roll.creatureName) : roll.creatureName));
		addTo.getChildren().add(text);

		try {
			StatBlock.addTooltip(text, NameLookup.creatureFromName(roll.creatureName));
		} catch (final IllegalArgumentException e) {}
	}

	public Encounter(final int weight, final String text) {
		this.weight = weight;
		description = text;
	}

	public Encounter(final int weight, final CreatureSet creatures) {
		this.weight = weight;
		this.creatures.add(creatures);
		description = null;
	}

	public Encounter(final int weight, final CreatureSet creatures, final String description) {
		this.weight = weight;
		this.creatures.add(creatures);
		this.description = description;
	}

	public Encounter(final int weight, final CreatureSet... creatures) {
		this.weight = weight;
		for (final CreatureSet c : creatures) {
			this.creatures.add(c);
		}
		description = null;
	}

	public Encounter(final int weight, final Collection<CreatureSet> creatures, final String description) {
		this.weight = weight;
		this.creatures.addAll(creatures);
		this.description = description;
	}

	public List<CreatureSet> getCreatures() {
		return new ArrayList<>(creatures);
	}

}
