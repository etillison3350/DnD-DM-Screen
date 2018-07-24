package dmscreen.statblock.editor.encounter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import dmscreen.data.adventure.CreatureSet;
import dmscreen.data.base.DiceRoll;
import dmscreen.statblock.StatBlock;
import dmscreen.statblock.editor.DiceRollEditor;
import dmscreen.statblock.editor.Editor;
import dmscreen.statblock.editor.StringEditor;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.RowConstraints;

public class CreatureSetCollectionEditor extends Editor<Collection<CreatureSet>> {

	private static final Pattern SPLIT_PATTERN = Pattern.compile(",?\\s*(?:and|plus)\\s*|,\\s*(?=(?:.+?,\\s*)+and)");
	private static final Pattern COMMA_OR_PATTERN = Pattern.compile(",\\s+(?:or\\s+)?|\\s+or\\s+|$");
	private static final Pattern SINGLE_CREATURE_PATTERN = Pattern.compile("^(\\d+(?:d\\d+\\s*(?:[+\\-]\\s*\\d+)?)?\\s*)?(?:\\[([\\s\\S]+?)\\](?:\\(([\\s\\S]*?)\\))|([\\s\\S]+?))$");

	private DiceRollEditor diceRoll;
	private StringEditor name, lookup;
	private TextArea full;

	private final BooleanProperty simpleProperty = new SimpleBooleanProperty();

	public CreatureSetCollectionEditor(final String editorName, final Collection<CreatureSet> initialValues, final boolean simpleEditor) {
		super(editorName);

		setPadding(new Insets(8, 0, 8, 0));

		this.add(StatBlock.smallCaps(editorName, "header-small"), 0, 0, 2, 1);
		this.add(new Separator(), 0, 1, 2, 1);

		final RowConstraints header = new RowConstraints();
		header.setMinHeight(20);
		getRowConstraints().addAll(new RowConstraints(), new RowConstraints(), header);

		simpleProperty.set(!simpleEditor);
		simpleProperty.addListener((observable, oldValue, newValue) -> {
			if (newValue) {
				if (diceRoll == null) {
					DiceRoll diceRoll;
					String name, lookup;
					try {
						final CreatureSet value = (full == null ? initialValues : getValue(oldValue)).iterator().next();
						final String creature = value.getCreatures().iterator().next();

						diceRoll = value.getAmount(creature);
						name = value.getDisplayName(creature);
						lookup = creature;
					} catch (final Exception e) {
						diceRoll = null;
						name = lookup = null;
					}
					this.diceRoll = new DiceRollEditor("Amount", diceRoll);
					this.name = new StringEditor("Display Name", name);
					this.lookup = new StringEditor("Creature Name", lookup);
				}

				if (getChildren().contains(full)) getChildren().remove(full);
				add(diceRoll, 0, 2, 2, 1);
				add(name, 0, 3, 2, 1);
				add(lookup, 0, 4, 2, 1);
			} else {
				if (full == null) {
					full = new TextArea(getString(diceRoll == null ? initialValues : getValue(oldValue)));
					full.setPrefRowCount(4);
					full.setWrapText(true);
					full.textProperty().addListener((obs, o, n) -> {
						try {
							processFullText();
							full.setStyle("-fx-control-inner-background: white;");
						} catch (final Exception e) {
							e.printStackTrace();
							full.setStyle("-fx-control-inner-background: #FFCCCC;");
						}
					});
				}
				getChildren().removeAll(diceRoll, name, lookup);
				add(full, 0, 2, 2, 3);
			}

			try {
				setValue(getValue(oldValue));
			} catch (final Exception e) {}
		});
		simpleProperty.set(simpleEditor);
	}

	@Override
	public void setValue(final Collection<CreatureSet> value) {
		if (isSimple()) {
			final Collection<CreatureSet> current = getValue();
			if (current.isEmpty()) {
				diceRoll.setValue(null);
				name.setValue(null);
				lookup.setValue(null);
			} else {
				final CreatureSet set = current.iterator().next();
				final String creature = set.getCreatures().iterator().next();

				diceRoll.setValue(set.getAmount(creature));
				name.setValue(set.getDisplayName(creature));
				lookup.setValue(creature);
			}
		} else {
			full.setText(getString(value));
		}
	}

	@Override
	public Collection<CreatureSet> getValue() {
		return getValue(isSimple());
	}

	private Collection<CreatureSet> getValue(final boolean fromSimple) {
		if (fromSimple) {
			final DiceRoll roll = diceRoll.getValue();
			final String creature = lookup.getValue();
			final String creatureName = name.getValue();

			if (roll == null || creature == null) return new ArrayList<>();
			return Arrays.asList(new CreatureSet(roll, creature, creatureName));
		} else {
			return processFullText();
		}
	}

	protected Collection<CreatureSet> processFullText() {
		return SPLIT_PATTERN.splitAsStream(full.getText()).map(str -> {
			final Matcher m = COMMA_OR_PATTERN.matcher(str);

			final Map<String, DiceRoll> creatures = new LinkedHashMap<>();
			final Map<String, String> creatureNames = new HashMap<>();

			DiceRoll initial = null;
			boolean useInitial = true;

			int bracket = 0, parens = 0;
			int endLast = 0, endLastAdded = 0;
			while (m.find()) {
				final String pre = str.substring(endLast, m.start());
				endLast = m.end();

				bracket += pre.chars().map(c -> c == '[' ? 1 : c == ']' ? -1 : 0).sum();
				parens += pre.chars().map(c -> c == '(' ? 1 : c == ')' ? -1 : 0).sum();

				if (bracket == 0 && parens == 0) {
					final String creature = str.substring(endLastAdded, m.start());
					endLastAdded = m.end();

					final Matcher c = SINGLE_CREATURE_PATTERN.matcher(creature);
					c.find();

					final String rollStr = c.group(1);
					final DiceRoll roll = rollStr == null ? !useInitial ? null : initial : DiceRoll.fromString(rollStr);
					if (roll == null) throw new NullPointerException("Must specify an amount");
					if (initial == null)
						initial = roll;
					else if (rollStr != null) //
						useInitial = false;

					if (c.group(4) != null) {
						creatures.put(c.group(4), roll);
					} else {
						creatures.put(c.group(3), roll);
						creatureNames.put(c.group(3), c.group(2));
					}
				}
			}

			return new CreatureSet(creatures, creatureNames);
		}).collect(Collectors.toList());
	}

	public BooleanProperty simpleProperty() {
		return simpleProperty;
	}

	public boolean isSimple() {
		return simpleProperty.get();
	}

	public void setSimple(final boolean simple) {
		simpleProperty.set(simple);
	}

	private static String creatureText(final String creatureName, final CreatureSet creatures, final boolean includeDice) {
		final String displayName = creatures.getDisplayName(creatureName);
		return (includeDice ? creatures.getAmount(creatureName) + " " : "") + (displayName != null ? String.format("[%s](%s)", displayName, creatureName) : creatureName);
	}

	private static String getString(final Collection<CreatureSet> creatureCollection) {
		final List<CreatureSet> creatures = creatureCollection instanceof List<?> ? (List<CreatureSet>) creatureCollection : new ArrayList<>(creatureCollection);
		final StringBuffer text = new StringBuffer();

		int last = 0;
		for (int i = 0; i <= creatures.size(); i++) {
			if (i == creatures.size() || creatures.get(i).multipleOr()) {
				for (int n = last; n < i; n++) {
					if (n > last) {
						if (n == i - 1) {
							if (i - last == 2) {
								text.append(" and ");
							} else {
								text.append(", and ");
							}
						} else {
							text.append(", ");
						}
					}
					final CreatureSet c = creatures.get(n);
					final Collection<String> creatureList = c.getCreatures();

					if (creatureList.size() == 1) {
						text.append(CreatureSetCollectionEditor.creatureText(creatureList.iterator().next(), c, true));
					} else {
						final Iterator<String> iter = creatureList.iterator();
						text.append(CreatureSetCollectionEditor.creatureText(iter.next(), c, true));
						if (creatureList.size() == 2) {
							text.append(" or ");
							text.append(CreatureSetCollectionEditor.creatureText(iter.next(), c, false));
						} else {
							while (iter.hasNext()) {
								final String creature = iter.next();
								text.append(iter.hasNext() ? ", " : ", or ");
								text.append(CreatureSetCollectionEditor.creatureText(creature, c, false));
							}
						}
					}
				}

				if (i != creatures.size()) {
					if (i > last) text.append(", plus ");
					final CreatureSet c = creatures.get(i);
					final Collection<String> creatureList = c.getCreatures();

					if (creatureList.size() == 1) {
						text.append(CreatureSetCollectionEditor.creatureText(creatureList.iterator().next(), c, true));
					} else {
						final Iterator<String> iter = creatureList.iterator();
						text.append(CreatureSetCollectionEditor.creatureText(iter.next(), c, true));
						if (creatureList.size() == 2) {
							text.append(" or ");
							text.append(CreatureSetCollectionEditor.creatureText(iter.next(), c, true));
						} else {
							while (iter.hasNext()) {
								final String creature = iter.next();
								text.append(iter.hasNext() ? ", " : ", or ");
								text.append(CreatureSetCollectionEditor.creatureText(creature, c, true));
							}
						}
					}
					if (i != creatures.size() - 1) {
						text.append(", plus ");
					}
				}

				last = i + 1;
			}
		}

		return text.toString();
	}

}
