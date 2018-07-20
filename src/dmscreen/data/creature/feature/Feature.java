package dmscreen.data.creature.feature;

import dmscreen.data.base.BlockEntry;
import dmscreen.statblock.StatBlock;
import javafx.scene.Node;

public class Feature implements BlockEntry {

	private final String name, note, description;

	public Feature(final String name, final String description) {
		this(name, "", description);
	}

	public Feature(final String name, final String note, final String description) {
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
		return StatBlock.formattedDataLine(getTitle() + ".", description + "\n", true);
	}

}
