package dmscreen.statblock;

import java.util.Collection;

import javafx.scene.control.ComboBox;

public class CollectionEnumEditor<T extends Enum<?>> extends CollectionEditor<T> {

	private final Class<T> clazz;

	public CollectionEnumEditor(final Class<T> clazz, final String name, final Collection<T> initialValue) {
		super(name);

		this.clazz = clazz;

		initialValue.forEach(t -> {
			addListItem(makeItem(clazz, t));
		});
	}

	private static <T extends Enum<?>> ListItem<T> makeItem(final Class<T> clazz, final T value) {
		final ComboBox<T> node = EnumEditor.createEditorComboBox(clazz, value);
		return new ListItem<>(node, node::getValue);
	}

	@Override
	protected ListItem<T> makeItem() {
		return makeItem(clazz, null);
	}

}
