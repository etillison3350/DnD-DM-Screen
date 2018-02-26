package dmscreen.data.creature.feature;

import dmscreen.StatBlock;
import dmscreen.data.base.BlockEntry;
import javafx.scene.Node;

public class Action implements BlockEntry {

	private final String name, note, description;

	public Action(final String name, final String description) {
		this(name, "", description);
	}

	public Action(final String name, final String note, final String description) {
		this.name = name;
		this.note = note;
		this.description = description;
	}

	public String getTitle() {
		return note == null || note.isEmpty() ? name : String.format("%s (%s)", name, note);
	}

	public String getName() {
		return name;
	}

	public String getNote() {
		return note;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public Node getNode() {
		return StatBlock.dataLine(getTitle() + ".", description + "\n", true);
	}

}
