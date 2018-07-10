package dmscreen.statblock;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dmscreen.Screen;
import dmscreen.data.adventure.RandomEncounter;
import dmscreen.data.base.Ability;
import dmscreen.data.base.DamageType;
import dmscreen.data.base.Skill;
import dmscreen.data.creature.Condition;
import dmscreen.data.creature.Creature;
import dmscreen.data.creature.MovementType;
import dmscreen.data.creature.feature.template.Template;
import dmscreen.data.spell.Bullet;
import dmscreen.data.spell.Spell;
import dmscreen.data.spell.SpellParagraph;
import dmscreen.util.PopupManager;
import dmscreen.util.Util;
import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Separator;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Popup;
import javafx.util.Duration;

public class StatBlock {

	public static final Pattern SMALL_CAPS = Pattern.compile("([^a-z]+)|(?:[^A-Z]+)");
	public static final String TITLE_STYLE_CLASS = "block-title";
	public static final String HEADER_STYLE_CLASS = "header";

	private StatBlock() {}

	public static Pane getStatBlock(final Object obj) {
		if (obj == null || obj.getClass() == Object.class) return new VBox(2);

		for (final Method m : StatBlock.class.getMethods()) {
			if (!m.getName().equals("getStatBlock") || m.getParameterCount() != 1) continue;

			if (m.getParameterTypes()[0] != Object.class && m.getParameterTypes()[0].isAssignableFrom(obj.getClass())) {
				try {
					return (Pane) m.invoke(null, obj);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}

		try {
			final VBox statBlock = new VBox(2);
			final ObservableList<Node> children = statBlock.getChildren();

			final String name = Util.getName(obj);

			children.add(smallCaps(Util.titleCase(name), TITLE_STYLE_CLASS));

			for (final Field field : obj.getClass().getFields()) {
				try {
					if (Modifier.isStatic(field.getModifiers())) continue;

					Object value = field.get(obj);
					if (value.getClass().isArray()) {
						if (value.getClass().getComponentType().isPrimitive()) {
							value = Arrays.class.getMethod("toString", value.getClass()).invoke(null, value);
						} else if (value.getClass().getComponentType().isArray()) {
							value = Arrays.class.getMethod("deepToString", Object[].class).invoke(null, value);
						} else {
							value = Arrays.class.getMethod("toString", Object[].class).invoke(null, value);
						}
					}

					children.add(dataLine(Util.titleCase(field.getName()), value.toString()));
				} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e1) {}
			}

			return statBlock;
		} catch (final Exception e1) {
			return new VBox(2);
		}
	}

	public static Pane getStatBlock(final Condition condition) {
		final VBox statBlock = new VBox(2);
		final ObservableList<Node> children = statBlock.getChildren();

		children.add(smallCaps(Util.titleCase(condition.name()), TITLE_STYLE_CLASS));

		for (final String d : condition.descriptions) {
			children.add(dataLine("", d));
		}

		return statBlock;
	}

	public static Pane getStatBlock(final Creature creature) {
		final VBox statBlock = new VBox(2);
		final ObservableList<Node> children = statBlock.getChildren();

		children.add(smallCaps(creature.name, TITLE_STYLE_CLASS));

		final Text subtitle = new Text(String.format("%s %s, %s", Util.titleCase(creature.size.name()), creature.type.name().toLowerCase() + (creature.subtype == null || creature.subtype.isEmpty() ? "" : " (" + creature.subtype + ")"), creature.alignment.name().toLowerCase().replace('_', ' ')));
		subtitle.getStyleClass().add("subtitle");
		children.add(subtitle);

		children.add(separator());

		children.add(dataLine("Armor Class", String.format("%d%s", creature.ac, creature.armorNote == null || creature.armorNote.isEmpty() ? "" : " (" + creature.armorNote + ")")));

		children.add(dataLine("Hit Points", String.format("%d (%s)", (int) Math.floor(creature.hitDice.expectedValue()), creature.hitDice)));

		final StringBuffer speed = new StringBuffer(Integer.toString(creature.speed));
		speed.append(" ft.");
		for (final MovementType s : creature.speeds.keySet()) {
			final Integer sp = creature.speeds.get(s);
			if (sp > 0) speed.append(String.format(", %s %d ft.", s.name().toLowerCase(), sp));
		}
		children.add(dataLine("Speed", speed.toString()));

		children.add(separator());

		final GridPane abilityScores = new GridPane();
		abilityScores.setHgap(4);
		abilityScores.setVgap(4);
		int c = 0;
		for (final Ability ability : Ability.values()) {
			final int score = creature.abilityScores.get(ability);
			abilityScores.addColumn(c++, new Text(ability.abbr()), new Text(String.format("%d (%+d)", score, (int) Math.floor(score * 0.5 - 5))));
			final ColumnConstraints cc = new ColumnConstraints();
			cc.setHalignment(HPos.CENTER);
			cc.setPercentWidth(100D / 6);
			abilityScores.getColumnConstraints().add(cc);
		}
		children.add(abilityScores);

		children.add(separator());

		if (!creature.savingThrows.isEmpty()) children.add(dataLine("Saving Throws", creature.savingThrows.keySet().stream().sorted().map(a -> String.format("%s %+d", Util.titleCase(a.abbr()), creature.savingThrows.get(a))).collect(Collectors.joining(", "))));
		if (!creature.skills.isEmpty()) children.add(dataLine("Skills", creature.skills.keySet().stream().sorted().map(s -> String.format("%s %+d", s == Skill.SLEIGHT_OF_HAND ? "Sleight of Hand" : Util.titleCase(s.name()), creature.skills.get(s))).collect(Collectors.joining(", "))));

		if (!creature.vulnerabilities.isEmpty() && !creature.vulnerabilities.values().stream().allMatch(Set::isEmpty)) children.add(dataLine("Damage Vulnerabilities", creatureDamageTypes(creature.vulnerabilities)));
		if (!creature.resistances.isEmpty() && !creature.resistances.values().stream().allMatch(Set::isEmpty)) children.add(dataLine("Damage Resistances", creatureDamageTypes(creature.resistances)));
		if (!creature.immunities.isEmpty() && !creature.immunities.values().stream().allMatch(Set::isEmpty)) children.add(dataLine("Damage Immunities", creatureDamageTypes(creature.immunities)));

		if (creature.conditionImmunities != null && !creature.conditionImmunities.isEmpty()) children.add(dataLine("Condition Immunities", creature.conditionImmunities.stream().sorted().map(condition -> condition.name().toLowerCase()).collect(Collectors.joining(", "))));

		// if (creature.conditionImmunities != null && !creature.conditionImmunities.isEmpty()) {
		// final TextFlow conditionImmunities = dataLine("Condition Immunities", null);
		// creature.conditionImmunities.stream().sorted().forEach(condition -> {
		// final Text text = new Text(condition.name().toLowerCase());
		// addTooltip(text, condition);
		// conditionImmunities.getChildren().addAll(text, new Text(", "));
		// });
		// conditionImmunities.getChildren().remove(conditionImmunities.getChildren().size() - 1);
		// children.add(conditionImmunities);
		// }

		Integer passivePerception = creature.skills.get(Skill.PERCEPTION);
		if (passivePerception == null)
			passivePerception = creature.abilityScores.get(Ability.WISDOM) / 2 + 5;
		else
			passivePerception += 10;

		children.add(dataLine("Senses", creature.senses.keySet().stream().filter(v -> creature.senses.get(v) > 0).map(v -> String.format("%s %d ft., ", v.name().toLowerCase(), creature.senses.get(v))).collect(Collectors.joining()) + String.format("passive Perception %d", passivePerception)));

		children.add(dataLine("Languages", creature.languages.isEmpty() ? "\u2014" : creature.languages.stream().collect(Collectors.joining(", "))));

		children.add(dataLine("Challenge", String.format("%s (%s XP)", challenge(creature.challengeRating), Util.COMMA_SEPARATED.format(Creature.XP[creature.challengeRating + 5]))));

		children.add(separator());

		children.add(dataLine("", ""));

		creature.features.forEach(f -> children.add(f.getNode()));

		if (!creature.actions.isEmpty()) {
			children.add(smallCaps("Actions", HEADER_STYLE_CLASS));
			children.add(separator());
			creature.actions.forEach(a -> children.add(a.getNode()));
		}

		if (!creature.reactions.isEmpty()) {
			children.add(smallCaps("Reactions", HEADER_STYLE_CLASS));
			children.add(separator());
			creature.reactions.forEach(a -> children.add(a.getNode()));
		}

		if (!creature.legendaryActions.isEmpty()) {
			children.add(smallCaps("Legendary Actions", HEADER_STYLE_CLASS));
			children.add(separator());
			children.add(dataLine("", String.format("%1$s can take 3 legendary actions, choosing from the options below. Only one legendary action option can be used at a time and only at the end of another creature's turn. %1$s regains spent legendary actions at the start of its turn.%n", Character.toUpperCase(creature.shortName.charAt(0)) + creature.shortName.substring(1).toLowerCase())));
			creature.legendaryActions.forEach(l -> children.add(l.getNode()));
		}

		return statBlock;
	}

	public static Pane getStatBlock(final Spell spell) {
		final VBox statBlock = new VBox(2);
		final ObservableList<Node> children = statBlock.getChildren();

		children.add(smallCaps(spell.name, TITLE_STYLE_CLASS));

		final Text subtitle = new Text(spell.level <= 0 ? String.format("%s cantrip\n", Util.titleCase(spell.type.name())) : String.format("%d%s-level %s%s\n", spell.level, Util.ordinal(spell.level), spell.type.name().toLowerCase(), spell.ritual ? " (ritual)" : ""));
		subtitle.getStyleClass().add("subtitle");
		children.add(subtitle);

		children.add(dataLine("Casting Time:", spell.castingTime));
		children.add(dataLine("Range:", spell.range));
		children.add(dataLine("Components:", componentsString(spell.verbal, spell.somatic, spell.materialComponents)));
		children.add(dataLine("Duration:", (spell.concentration ? "Concentration, up to " : "") + spell.duration + "\n"));

		Class<? extends SpellParagraph> last = null;
		for (final SpellParagraph p : spell.description) {
			if (p.getClass() != Bullet.class && last == Bullet.class) children.add(new Text(""));
			last = p.getClass();

			children.add(p.getNode());
		}

		return statBlock;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static Pane getStatBlock(final Template template) {
		final VBox statBlock = new VBox(2);
		final ObservableList<Node> children = statBlock.getChildren();

		children.add(smallCaps(template.name, TITLE_STYLE_CLASS));

		String typeName = "";
		try {
			typeName = template.getClass().getMethod("make", Map.class).getReturnType().getSimpleName();
		} catch (NoSuchMethodException | SecurityException e) {}
		final Text subtitle = new Text(Util.sentenceCase(Util.titleCaseFromCamelCase(typeName)) + " template");
		subtitle.getStyleClass().add("subtitle");
		children.add(subtitle);

		children.add(new Text(" "));

		children.add(new TemplateEditor(template, true));

		return statBlock;
	}

	public static Pane getStatBlock(final RandomEncounter encounter) {
		final VBox statBlock = new VBox(2);
		final ObservableList<Node> children = statBlock.getChildren();

		children.add(smallCaps(encounter.name, TITLE_STYLE_CLASS));

		final Text subtitle = new Text("Random encounter");
		subtitle.getStyleClass().add("subtitle");
		children.add(subtitle);

		final GridPane table = new GridPane();
		table.setPadding(new Insets(10, 0, 10, 0));
		table.setHgap(12);
		table.setVgap(2);
		final ColumnConstraints cc = new ColumnConstraints();
		cc.setFillWidth(true);
		cc.setHalignment(HPos.CENTER);
		table.getColumnConstraints().add(cc);
		table.setAlignment(Pos.TOP_LEFT);

		final Text diceRollText = new Text(encounter.getDiceRoll().toString().replaceAll("^1(?=d)", ""));
		final Text encounterHeader = new Text("Encounter");
		diceRollText.setFont(Font.font(Screen.DEFAULT_FONT_NAME, FontWeight.BOLD, 12));
		encounterHeader.setFont(Font.font(Screen.DEFAULT_FONT_NAME, FontWeight.BOLD, 12));
		table.addRow(0, diceRollText, encounterHeader);

		final RowConstraints rc = new RowConstraints();
		rc.setValignment(VPos.TOP);
		table.getRowConstraints().add(rc);

		final int[] ranges = encounter.getEncounterRanges();
		for (int i = 0, row = 1; i < ranges.length - 1; i++) {
			String rangeStr;
			if (ranges[i] < ranges[i + 1] - 1) {
				rangeStr = String.format("%d - %d", ranges[i], ranges[i + 1] - 1);
			} else if (ranges[i] == ranges[i + 1] - 1) {
				rangeStr = Integer.toString(ranges[i]);
			} else {
				continue;
			}
			final Text rangeText = new Text(rangeStr);
			rangeText.setTextAlignment(TextAlignment.CENTER);
			table.add(rangeText, 0, row);
			table.add(encounter.encounters.get(i).getNode(), 1, row++);

			table.getRowConstraints().add(rc);
		}
		children.add(table);

		children.add(smallCaps("Roll", HEADER_STYLE_CLASS));
		children.add(separator());
		final BorderPane rollPane = new BorderPane(encounter.roll());
		children.add(rollPane);
		final Hyperlink reroll = new Hyperlink("Re-roll");
		reroll.setOnAction(event -> {
			rollPane.setCenter(encounter.roll());
		});
		children.add(reroll);

		return statBlock;
	}

	public static void addTooltip(final Node node, final Object info) {
		final PauseTransition pt = new PauseTransition(Duration.seconds(1));
		pt.setOnFinished(event -> {
			final Bounds screenBounds = node.localToScreen(node.getBoundsInLocal());

			try {
				final Popup popup = PopupManager.getPopup();
				popup.setAnchorX(screenBounds.getMinX());
				popup.setAnchorY(screenBounds.getMinY());
				final BorderPane content = new BorderPane(StatBlock.getStatBlock(info));
				content.setTop(new Pane());
				content.setPadding(new Insets(8));
				content.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(4), Insets.EMPTY)));
				content.setOnMouseExited(e -> {
					PopupManager.releasePopup();
				});
				content.setMaxWidth(384);
				popup.getContent().add(content);
				popup.show(node.getScene().getWindow());
			} catch (final IllegalStateException e) {}
		});

		node.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
			if (newValue) {
				pt.play();
			} else {
				pt.stop();
			}
		});
	}

	public static String componentsString(final boolean verbal, final boolean somatic, final String materialComponents) {
		final StringBuffer ret = new StringBuffer();
		if (verbal) ret.append("V");
		if (somatic) {
			if (ret.length() > 0) ret.append(", ");
			ret.append("S");
		}

		if (materialComponents != null && !materialComponents.isEmpty()) {
			if (ret.length() > 0) ret.append(", ");
			ret.append("M (");
			ret.append(materialComponents);
			ret.append(")");
		}

		return ret.toString();
	}

	public static String challenge(final int saved) {
		if (saved >= 0) return Integer.toString(saved);

		if (saved < -3) return "0";

		return String.format("1/%.0f", Math.pow(0.5, saved));
	}

	public static String creatureDamageTypes(final Map<String, Set<DamageType>> damages) {
		final TreeSet<DamageType> base = new TreeSet<>();
		Stream.of(null, "null", "").map(damages::get).filter(s -> s != null).forEach(base::addAll);

		final StringBuilder ret = new StringBuilder(base.stream().sorted().map(d -> d.name().toLowerCase()).collect(Collectors.joining(", ")));

		damages.keySet().stream().filter(s -> s != null && !s.isEmpty() && !s.equals("null")).map(s -> Util.andJoin(damages.get(s).stream().sorted().map(d -> d.name().toLowerCase()).collect(Collectors.toCollection(TreeSet::new))) + " from " + s).forEach(s -> {
			if (ret.length() > 0) ret.append("; ");
			ret.append(s);
		});

		return ret.toString();

	}

	public static Separator separator() {
		final Separator sep = new Separator();
		return sep;
	}

	public static TextFlow dataLine(final String title, final String text) {
		return dataLine(title, text, false);
	}

	public static TextFlow dataLine(final String title, final String text, final boolean feature) {
		final TextFlow line = new TextFlow();

		if (title != null && !title.isEmpty()) {
			final Text titleText = new Text(title + " ");
			titleText.setFont(Font.font(Screen.DEFAULT_FONT_NAME, FontWeight.BOLD, feature ? FontPosture.ITALIC : FontPosture.REGULAR, Font.getDefault().getSize()));
			line.getChildren().add(titleText);
		}

		if (text != null && !text.isEmpty()) {
			final Text descriptionText = new Text(text);
			line.getChildren().add(descriptionText);
		}

		return line;
	}

	private static final Pattern CONDITION_PATTERN = Pattern.compile(String.format("\\b(?:%s)\\b", Arrays.stream(Condition.values()).map(Condition::name).collect(Collectors.joining("|"))), Pattern.CASE_INSENSITIVE);

	/**
	 * @return a {@link TextFlow} of a stat block data line, with the given title and text.
	 *         {@link Condition}s in the text will display a tooltip with that condition's
	 *         description on hover.
	 */
	public static TextFlow conditionAltDataLine(final String title, final String text) {
		return conditionAltDataLine(title, text, false);
	}

	/**
	 * @return a {@link TextFlow} of a stat block data line, with the given title and text.
	 *         {@link Condition}s in the text will display a tooltip with that condition's
	 *         description on hover.
	 */
	public static TextFlow conditionAltDataLine(final String title, final String text, final boolean feature) {
		final TextFlow line = new TextFlow();

		if (title != null && !title.isEmpty()) {
			final Text titleText = new Text(title + " ");
			titleText.setFont(Font.font(Screen.DEFAULT_FONT_NAME, FontWeight.BOLD, feature ? FontPosture.ITALIC : FontPosture.REGULAR, Font.getDefault().getSize()));
			line.getChildren().add(titleText);
		}

		line.getChildren().addAll(conditionTooltips(text));

		return line;
	}

	public static Collection<Text> conditionTooltips(final String text) {
		final List<Text> ret = new ArrayList<>();

		if (text != null && !text.isEmpty()) {
			final Matcher m = CONDITION_PATTERN.matcher(text);

			int endLast = 0;
			while (m.find()) {
				ret.add(new Text(text.substring(endLast, m.start())));
				endLast = m.end();
				final Text condition = new Text(m.group());
				StatBlock.addTooltip(condition, Condition.valueOf(m.group().toUpperCase()));
				ret.add(condition);
			}

			ret.add(new Text(text.substring(endLast)));
		}

		return ret;
	}

	public static TextFlow smallCaps(final String text, final String styleClass) {
		final TextFlow ret = new TextFlow();

		final Matcher m = SMALL_CAPS.matcher(text);
		while (m.find()) {
			final Text t = new Text(m.group().toUpperCase());
			t.getStyleClass().add(m.group(1) == null || m.group(1).isEmpty() ? styleClass + "-small" : styleClass);
			ret.getChildren().add(t);
		}

		return ret;
	}

	public static TextFlow smallCaps(final String text, final Font cap, final Font small) {
		final TextFlow ret = new TextFlow();

		final Matcher m = SMALL_CAPS.matcher(text);
		while (m.find()) {
			final Text t = new Text(m.group().toUpperCase());
			t.setFont(m.group(1) == null || m.group(1).isEmpty() ? small : cap);
			ret.getChildren().add(t);
		}

		return ret;
	}

}
