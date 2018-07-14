package dmscreen.statblock;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import dmscreen.data.base.Ability;
import dmscreen.data.base.DamageType;
import dmscreen.data.base.Size;
import dmscreen.data.base.Skill;
import dmscreen.data.creature.Alignment;
import dmscreen.data.creature.Condition;
import dmscreen.data.creature.Creature;
import dmscreen.data.creature.CreatureType;
import dmscreen.data.creature.MovementType;
import dmscreen.data.creature.VisionType;
import dmscreen.data.creature.feature.Action;
import dmscreen.data.creature.feature.Feature;
import dmscreen.data.creature.feature.LegendaryAction;
import dmscreen.data.spell.Spell;
import dmscreen.data.spell.SpellType;
import dmscreen.statblock.editor.BooleanEditor;
import dmscreen.statblock.editor.ChallengeRatingEditor;
import dmscreen.statblock.editor.DiceRollEditor;
import dmscreen.statblock.editor.Editor;
import dmscreen.statblock.editor.EnumEditor;
import dmscreen.statblock.editor.IntegerEditor;
import dmscreen.statblock.editor.SpellDescriptionEditor;
import dmscreen.statblock.editor.StringEditor;
import dmscreen.statblock.editor.collection.BlockEntryCollectionEditor;
import dmscreen.statblock.editor.collection.CollectionEnumEditor;
import dmscreen.statblock.editor.collection.CollectionStringEditor;
import dmscreen.statblock.editor.map.EditableMapCollectionEditor;
import dmscreen.statblock.editor.map.EditableMapEnumIntegerEditor;
import dmscreen.statblock.editor.map.MapEnumIntegerEditor;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

public class StatBlockEditor<T> extends VBox {

	private T originalValue;
	private Supplier<T> newValueGetter;

	private StatBlockEditor(final T originalValue, final Supplier<T> newValueGetter) {
		super(2);

		this.originalValue = originalValue;
		this.newValueGetter = newValueGetter;

		setPadding(new Insets(8));
	}

	private StatBlockEditor(final T originalValue) {
		this(originalValue, () -> null);
	}

	private StatBlockEditor(final Supplier<T> newValueGetter) {
		this(null, newValueGetter);
	}

	private StatBlockEditor() {
		this(null, () -> null);
	}

	public T getOriginalValue() {
		return originalValue;
	}

	public T getNewValue() {
		return newValueGetter.get();
	}

	public static boolean isEditable(final Object obj) {
		return isEditable(obj.getClass());
	}

	public static boolean isEditable(final Class<?> c) {
		try {
			StatBlockEditor.class.getMethod("getEditor", c);
			return true;
		} catch (NoSuchMethodException | SecurityException e) {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> StatBlockEditor<? extends T> getEditor(final T obj) {
		try {
			if (obj == null || obj.getClass() == Object.class) return new StatBlockEditor<>(null);

			return (StatBlockEditor<? extends T>) StatBlockEditor.class.getMethod("getEditor", obj.getClass()).invoke(null, obj);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return new StatBlockEditor<>(null);
		}
	}

	public static StatBlockEditor<Creature> getEditor(final Creature creature) {
		final StatBlockEditor<Creature> editor = new StatBlockEditor<>(creature);

		final StringEditor name = new StringEditor("Name", creature.name);
		final StringEditor shortName = new StringEditor("General Name", creature.shortName);
		final EnumEditor<Size> size = new EnumEditor<>(Size.class, "Size", creature.size);
		final EnumEditor<CreatureType> type = new EnumEditor<>(CreatureType.class, "Type", creature.type);
		final StringEditor subtype = new StringEditor("Subtype", creature.subtype);
		final EnumEditor<Alignment> alignment = new EnumEditor<>(Alignment.class, "Alignment", creature.alignment);
		final IntegerEditor ac = new IntegerEditor("Armor Class", 0, 30, creature.ac);
		final StringEditor armorNote = new StringEditor("Armor Description", creature.armorNote);
		final DiceRollEditor hitDice = new DiceRollEditor("Hit Dice", creature.hitDice);
		final IntegerEditor speed = new IntegerEditor("Speed (ft.)", 0, 500, creature.speed, 5);
		final MapEnumIntegerEditor<MovementType> speeds = new MapEnumIntegerEditor<>(MovementType.class, "Other Speeds", "Type", "Speed (ft.)", 0, 500, 5, creature.speeds);
		final MapEnumIntegerEditor<Ability> abilityScores = new MapEnumIntegerEditor<Ability>(Ability.class, "Ability Scores", "Ability", "Score", 0, 40, creature.abilityScores);
		final EditableMapEnumIntegerEditor<Ability> savingThrows = new EditableMapEnumIntegerEditor<Ability>(Ability.class, "Saving Throws", "Ability", "Modifier", -10, 20, creature.savingThrows);
		final EditableMapEnumIntegerEditor<Skill> skills = new EditableMapEnumIntegerEditor<>(Skill.class, "Skills", "Skill", "Modifier", -10, 20, creature.skills);

		final BiFunction<String, String, Editor<String>> damageKeyEditor = (n, v) -> new StringEditor(n, v == null || v.equals("null") ? null : v, "any source");
		final BiFunction<String, Collection<DamageType>, Editor<Collection<DamageType>>> damageValueEditor = (s, c) -> new CollectionEnumEditor<>(DamageType.class, s, c);
		final EditableMapCollectionEditor<String, DamageType> vulnerabilities = new EditableMapCollectionEditor<String, DamageType>("Damage Vulnerabilities", "Sources", null, "Add Source", creature.vulnerabilities, damageKeyEditor, damageValueEditor);
		final EditableMapCollectionEditor<String, DamageType> resistances = new EditableMapCollectionEditor<String, DamageType>("Damage Resistances", "Sources", null, "Add Source", creature.resistances, damageKeyEditor, damageValueEditor);
		final EditableMapCollectionEditor<String, DamageType> immunities = new EditableMapCollectionEditor<String, DamageType>("Damage Immunities", "Sources", null, "Add Source", creature.immunities, damageKeyEditor, damageValueEditor);

		final CollectionEnumEditor<Condition> conditionImmunities = new CollectionEnumEditor<Condition>(Condition.class, "Condition Immunities", creature.conditionImmunities);
		final MapEnumIntegerEditor<VisionType> senses = new MapEnumIntegerEditor<VisionType>(VisionType.class, "Senses", "Type", "Range (ft.)", 0, 5000, 5, creature.senses);
		final CollectionStringEditor languages = new CollectionStringEditor("Languages", creature.languages);
		final ChallengeRatingEditor challengeRating = new ChallengeRatingEditor("Challenge Rating", creature.challengeRating);

		final BlockEntryCollectionEditor<Feature> features = new BlockEntryCollectionEditor<Feature>(Feature.class, "Features", creature.features);
		final BlockEntryCollectionEditor<Action> actions = new BlockEntryCollectionEditor<>(Action.class, "Actions", creature.actions);
		final BlockEntryCollectionEditor<Action> reactions = new BlockEntryCollectionEditor<>(Action.class, "Reactions", creature.reactions);
		final BlockEntryCollectionEditor<LegendaryAction> legendaryActions = new BlockEntryCollectionEditor<LegendaryAction>(LegendaryAction.class, "Legendary Actions", creature.legendaryActions);

		editor.getChildren().addAll(name, shortName, size, type, subtype, alignment, ac, armorNote, hitDice, speed, speeds, abilityScores, savingThrows, skills, vulnerabilities, resistances, immunities, conditionImmunities, senses, languages, challengeRating, features, actions, reactions, legendaryActions);

		editor.newValueGetter = () -> {
			final Creature newCreature = new Creature();

			newCreature.name = name.getValue();
			newCreature.shortName = shortName.getValue();
			newCreature.size = size.getValue();
			newCreature.type = type.getValue();
			newCreature.subtype = subtype.getValue();
			newCreature.alignment = alignment.getValue();
			newCreature.ac = ac.getValue();
			newCreature.armorNote = armorNote.getValue();
			newCreature.hitDice = hitDice.getValue();
			newCreature.speed = speed.getValue();
			newCreature.speeds = speeds.getValue();
			newCreature.abilityScores = abilityScores.getValue();
			newCreature.savingThrows = savingThrows.getValue();
			newCreature.skills = skills.getValue();

			newCreature.vulnerabilities.clear();
			vulnerabilities.getValue().forEach((s, c) -> {
				newCreature.vulnerabilities.put(s, new TreeSet<>(c));
			});

			newCreature.resistances.clear();
			resistances.getValue().forEach((s, c) -> {
				newCreature.resistances.put(s, new TreeSet<>(c));
			});

			newCreature.immunities.clear();
			immunities.getValue().forEach((s, c) -> {
				newCreature.immunities.put(s, new TreeSet<>(c));
			});

			newCreature.conditionImmunities = new TreeSet<>(conditionImmunities.getValue());
			newCreature.senses = senses.getValue();
			newCreature.languages = new TreeSet<>(languages.getValue());
			newCreature.challengeRating = challengeRating.getValue();
			newCreature.features = features.getValue();
			newCreature.actions = actions.getValue();
			newCreature.reactions = reactions.getValue();
			newCreature.legendaryActions = legendaryActions.getValue();

			return newCreature;
		};
		return editor;
	}

	public static StatBlockEditor<Spell> getEditor(final Spell spell) {
		final StatBlockEditor<Spell> editor = new StatBlockEditor<>(spell);

		final StringEditor name = new StringEditor("Name", spell.name);
		final IntegerEditor level = new IntegerEditor("Level", 0, 9, spell.level);
		final EnumEditor<SpellType> type = new EnumEditor<SpellType>(SpellType.class, "Type", spell.type);
		final BooleanEditor ritual = new BooleanEditor("Ritual", spell.ritual);
		final StringEditor castingTime = new StringEditor("Casting Time", spell.castingTime);
		final StringEditor range = new StringEditor("Range", spell.range);
		final BooleanEditor verbal = new BooleanEditor("Verbal Components", spell.verbal);
		final BooleanEditor somatic = new BooleanEditor("Somatic Components", spell.somatic);
		final StringEditor materialComponents = new StringEditor("Material Components", spell.materialComponents);
		final StringEditor duration = new StringEditor("Duration", spell.duration);
		final BooleanEditor concentration = new BooleanEditor("Concentration", spell.concentration);
		final SpellDescriptionEditor description = new SpellDescriptionEditor("Features", spell.description);

		editor.getChildren().addAll(name, level, type, ritual, castingTime, range, verbal, somatic, materialComponents, duration, concentration, description);

		editor.newValueGetter = () -> {
			final Spell newSpell = new Spell();

			newSpell.name = name.getValue();
			newSpell.level = level.getValue();
			newSpell.type = type.getValue();
			newSpell.ritual = ritual.getValue();
			newSpell.castingTime = castingTime.getValue();
			newSpell.range = range.getValue();
			newSpell.verbal = verbal.getValue();
			newSpell.somatic = somatic.getValue();
			newSpell.materialComponents = materialComponents.getValue();
			newSpell.duration = duration.getValue();
			newSpell.concentration = concentration.getValue();
			newSpell.description = description.getValue();

			return newSpell;
		};
		return editor;
	}
}
