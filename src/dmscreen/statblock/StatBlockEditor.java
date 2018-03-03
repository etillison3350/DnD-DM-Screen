package dmscreen.statblock;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import dmscreen.data.base.Ability;
import dmscreen.data.base.Size;
import dmscreen.data.base.Skill;
import dmscreen.data.creature.Alignment;
import dmscreen.data.creature.Creature;
import dmscreen.data.creature.CreatureType;
import dmscreen.data.creature.SpeedType;
import dmscreen.data.spell.Spell;
import dmscreen.data.spell.SpellType;

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

	public static StatBlockEditor<? extends Object> getEditor(final Object obj) {
		try {
			if (obj == null || obj.getClass() == Object.class) return new StatBlockEditor<>(null);

			return (StatBlockEditor<?>) StatBlockEditor.class.getMethod("getEditor", obj.getClass()).invoke(null, obj);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return new StatBlockEditor<>(null);
		}
	}

	public static StatBlockEditor<Creature> getEditor(final Creature creature) {
		final StatBlockEditor<Creature> editor = new StatBlockEditor<>(creature);

		final StringPropertyEditor name = new StringPropertyEditor("Name", creature.name);
		final StringPropertyEditor shortName = new StringPropertyEditor("General Name", creature.shortName);
		final EnumPropertyEditor<Size> size = new EnumPropertyEditor<>("Size", creature.size);
		final EnumPropertyEditor<CreatureType> type = new EnumPropertyEditor<>("Type", creature.type);
		final StringPropertyEditor subtype = new StringPropertyEditor("Subtype", creature.subtype);
		final EnumPropertyEditor<Alignment> alignment = new EnumPropertyEditor<>("Alignment", creature.alignment);
		final IntegerPropertyEditor ac = new IntegerPropertyEditor("Armor Class", 0, 30, creature.ac);
		final StringPropertyEditor armorNote = new StringPropertyEditor("Armor Description", creature.armorNote);
		final DiceRollPropertyEditor hitDice = new DiceRollPropertyEditor("Hit Dice", creature.hitDice);
		final IntegerPropertyEditor speed = new IntegerPropertyEditor("Speed (ft.)", 0, 500, creature.speed, 5);
		final MapEnumIntegerPropertyEditor<SpeedType> speeds = new MapEnumIntegerPropertyEditor<>(SpeedType.class, "Other Speeds", "Type", "Speed (ft.)", 0, 500, 5, creature.speeds, true);
		final MapEnumIntegerPropertyEditor<Ability> abilityScores = new MapEnumIntegerPropertyEditor<Ability>(Ability.class, "Ability Scores", "Ability", "Score", 0, 40, creature.abilityScores, true);
		final MapEnumIntegerPropertyEditor<Ability> savingThrows = new MapEnumIntegerPropertyEditor<Ability>(Ability.class, "Saving Throws", "Ability", "Modifier", -10, 20, creature.savingThrows, false);
		final MapEnumIntegerPropertyEditor<Skill> skills = new MapEnumIntegerPropertyEditor<>(Skill.class, "Skills", "Skill", "Modifier", -10, 20, creature.skills, false);

		editor.getChildren().addAll(name, shortName, size, type, subtype, alignment, ac, armorNote, hitDice, speed, speeds, abilityScores, savingThrows, skills);

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

			return newCreature;
		};
		return editor;
	}

	public static StatBlockEditor<Spell> getEditor(final Spell spell) {
		final StatBlockEditor<Spell> editor = new StatBlockEditor<>(spell);

		final StringPropertyEditor name = new StringPropertyEditor("Name", spell.name);
		final IntegerPropertyEditor level = new IntegerPropertyEditor("Level", 0, 9, spell.level);
		final EnumPropertyEditor<SpellType> type = new EnumPropertyEditor<SpellType>("Type", spell.type);
		final BooleanPropertyEditor ritual = new BooleanPropertyEditor("Ritual", spell.ritual);
		final StringPropertyEditor castingTime = new StringPropertyEditor("Casting Time", spell.castingTime);
		final StringPropertyEditor range = new StringPropertyEditor("Range", spell.range);
		final BooleanPropertyEditor verbal = new BooleanPropertyEditor("Verbal Components", spell.verbal);
		final BooleanPropertyEditor somatic = new BooleanPropertyEditor("Somatic Components", spell.somatic);
		final StringPropertyEditor materialComponents = new StringPropertyEditor("Material Components", spell.materialComponents);
		final StringPropertyEditor duration = new StringPropertyEditor("Duration", spell.duration);
		final BooleanPropertyEditor concentration = new BooleanPropertyEditor("Concentration", spell.concentration);
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
