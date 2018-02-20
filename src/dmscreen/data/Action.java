package dmscreen.data;

public class Action {

	private final String name, note, description;

	public Action(final String name, final String description) {
		this(name, "", description);
	}

	public Action(final String name, final String note, final String description) {
		this.name = name;
		this.note = note;
		this.description = description;
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

}
