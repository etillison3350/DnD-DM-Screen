package dmscreen.statblock;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

import javafx.scene.layout.VBox;
import dmscreen.data.creature.Creature;
import dmscreen.data.spell.Spell;

public class StatBlockEditor<T> extends VBox {

	private T originalValue;
	private Supplier<T> newValueGetter;

	private StatBlockEditor(final T originalValue, final Supplier<T> newValueGetter) {
		this.originalValue = originalValue;
		this.newValueGetter = newValueGetter;
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
			if (obj == null || obj.getClass() == Object.class) return new StatBlockEditor<Object>(null);

			return (StatBlockEditor<?>) StatBlockEditor.class.getMethod("getEditor", obj.getClass()).invoke(null, obj);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return new StatBlockEditor<Object>(null);
		}
	}

	public static StatBlockEditor<Creature> getEditor(final Creature creature) {
		final StatBlockEditor<Creature> editor = new StatBlockEditor<Creature>(creature);

		editor.newValueGetter = () -> {
			final Creature newCreature = new Creature();

			return newCreature;
		};
		return editor;
	}

	public static StatBlockEditor<Spell> getEditor(final Spell spell) {
		final StatBlockEditor<Spell> editor = new StatBlockEditor<Spell>(spell);

		editor.newValueGetter = () -> {
			final Spell newSpell = new Spell();

			return newSpell;
		};
		return editor;
	}

}
