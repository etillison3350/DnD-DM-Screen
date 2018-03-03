package dmscreen.statblock;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import dmscreen.Util;
import dmscreen.data.base.Ability;
import dmscreen.data.base.DamageType;
import dmscreen.data.base.Skill;
import dmscreen.data.creature.Condition;
import dmscreen.data.creature.Creature;
import dmscreen.data.creature.SpeedType;
import dmscreen.data.spell.Bullet;
import dmscreen.data.spell.Spell;
import dmscreen.data.spell.SpellParagraph;

public class StatBlock {

	public static final NumberFormat COMMA_SEPARATED = new DecimalFormat("#,###");
	public static final Pattern SMALL_CAPS = Pattern.compile("([^a-z]+)|(?:[^A-Z]+)");

	private StatBlock() {}

	public static Pane getStatBlock(final Object obj) {
		try {
			if (obj == null || obj.getClass() == Object.class) return new VBox(2);

			return (Pane) StatBlock.class.getMethod("getStatBlock", obj.getClass()).invoke(null, obj);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();

			try {
				final VBox statBlock = new VBox(2);
				final ObservableList<Node> children = statBlock.getChildren();

				final String name = Util.getName(obj);

				children.add(smallCaps(Util.titleCase(name), "title"));

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
	}

	public static Pane getStatBlock(final Condition condition) {
		final VBox statBlock = new VBox(2);
		final ObservableList<Node> children = statBlock.getChildren();

		children.add(smallCaps(Util.titleCase(condition.name()), "title"));

		for (final String d : condition.descriptions) {
			children.add(dataLine("", d));
		}

		return statBlock;
	}

	public static Pane getStatBlock(final Creature creature) {
		final VBox statBlock = new VBox(2);
		final ObservableList<Node> children = statBlock.getChildren();

		children.add(smallCaps(creature.name, "title"));

		final Text subtitle = new Text(String.format("%s %s, %s", Util.titleCase(creature.size.name()), creature.type.name().toLowerCase() + (creature.subtype == null || creature.subtype.isEmpty() ? "" : " (" + creature.subtype + ")"), creature.alignment.name().toLowerCase().replace('_', ' ')));
		subtitle.getStyleClass().add("subtitle");
		children.add(subtitle);

		children.add(separator());

		children.add(dataLine("Armor Class", String.format("%d%s", creature.ac, creature.armorNote == null || creature.armorNote.isEmpty() ? "" : " (" + creature.armorNote + ")")));

		children.add(dataLine("Hit Points", String.format("%d (%s)", (int) Math.floor(creature.hitDice.expectedValue()), creature.hitDice)));

		final StringBuffer speed = new StringBuffer(Integer.toString(creature.speed));
		speed.append(" ft.");
		for (final SpeedType s : creature.speeds.keySet()) {
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
		if (!creature.skills.isEmpty()) children.add(dataLine("Skills", creature.skills.keySet().stream().sorted().map(s -> String.format("%s %+d", Util.titleCase(s.name()), creature.skills.get(s))).collect(Collectors.joining(", "))));

		if (!creature.vulnerabilities.isEmpty() && !creature.vulnerabilities.values().stream().allMatch(Set::isEmpty)) children.add(dataLine("Damage Vulnerabilities", creatureDamageTypes(creature.vulnerabilities)));
		if (!creature.resistances.isEmpty() && !creature.resistances.values().stream().allMatch(Set::isEmpty)) children.add(dataLine("Damage Resistances", creatureDamageTypes(creature.resistances)));
		if (!creature.immunities.isEmpty() && !creature.immunities.values().stream().allMatch(Set::isEmpty)) children.add(dataLine("Damage Immunities", creatureDamageTypes(creature.immunities)));

		if (creature.conditionImmunities != null && !creature.conditionImmunities.isEmpty()) children.add(dataLine("Condition Immunities", creature.conditionImmunities.stream().sorted().map(condition -> condition.name().toLowerCase()).collect(Collectors.joining(", "))));

		Integer passivePerception = creature.skills.get(Skill.PERCEPTION);
		if (passivePerception == null)
			passivePerception = creature.abilityScores.get(Ability.WISDOM) / 2 + 5;
		else
			passivePerception += 10;

		children.add(dataLine("Senses", creature.senses.keySet().stream().map(v -> String.format("%s %d ft., ", v.name().toLowerCase(), creature.senses.get(v))).collect(Collectors.joining()) + String.format("passive Perception %d", passivePerception)));

		children.add(dataLine("Languages", creature.languages.isEmpty() ? "\u2014" : creature.languages.stream().collect(Collectors.joining(", "))));

		children.add(dataLine("Challenge", String.format("%s (%s XP)", challenge(creature.challengeRating), COMMA_SEPARATED.format(Creature.XP[creature.challengeRating + 5]))));

		children.add(separator());

		children.add(dataLine("", ""));

		creature.features.forEach(f -> children.add(f.getNode()));

		if (!creature.actions.isEmpty()) {
			children.add(smallCaps("Actions", "header"));
			children.add(separator());
			creature.actions.forEach(a -> children.add(a.getNode()));
		}

		if (!creature.reactions.isEmpty()) {
			children.add(smallCaps("Reactions", "header"));
			children.add(separator());
			creature.reactions.forEach(a -> children.add(a.getNode()));
		}

		if (!creature.legendaryActions.isEmpty()) {
			children.add(smallCaps("Legendary Actions", "header"));
			children.add(separator());
			children.add(dataLine("", String.format("%1$s can take 3 legendary actions, choosing from the options below. Only one legendary action option can be used at a time and only at the end of another creature's turn. %1$s regains spent legendary actions at the start of its turn.%n", Character.toUpperCase(creature.shortName.charAt(0)) + creature.shortName.substring(1).toLowerCase())));
			creature.legendaryActions.forEach(l -> children.add(l.getNode()));
		}

		return statBlock;
	}

	public static Pane getStatBlock(final Spell spell) {
		final VBox statBlock = new VBox(2);
		final ObservableList<Node> children = statBlock.getChildren();

		children.add(smallCaps(spell.name, "title"));

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
			titleText.setFont(Font.font("System", FontWeight.BOLD, feature ? FontPosture.ITALIC : FontPosture.REGULAR, Font.getDefault().getSize()));
			line.getChildren().add(titleText);
		}

		if (text != null && !text.isEmpty()) {
			final Text descriptionText = new Text(text);
			line.getChildren().add(descriptionText);
		}

		return line;
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
