package dmscreen.statblock;

import java.lang.reflect.InvocationTargetException;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import dmscreen.data.creature.Creature;

public class StatBlockEditor {

	private StatBlockEditor() {}

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

	public static Node getEditor(final Object obj) {
		try {
			if (obj == null || obj.getClass() == Object.class) return new VBox(2);

			return (Node) StatBlockEditor.class.getMethod("getEditor", obj.getClass()).invoke(null, obj);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return new VBox(2);
		}
	}

	public static Node getEditor(final Creature creature) {
		final VBox editor = new VBox(2);

		return editor;
	}

}
