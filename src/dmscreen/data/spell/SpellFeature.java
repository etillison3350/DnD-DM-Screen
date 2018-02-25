package dmscreen.data.spell;

public class SpellFeature extends SpellParagraph {

	private final String title;

	public SpellFeature(final String title, final String text) {
		super(text);

		this.title = title;
	}

	public String getTitle() {
		return title;
	}

}
