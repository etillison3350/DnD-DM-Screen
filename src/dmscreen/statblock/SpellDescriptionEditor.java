package dmscreen.statblock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import dmscreen.data.spell.Bullet;
import dmscreen.data.spell.SpellFeature;
import dmscreen.data.spell.SpellParagraph;

public class SpellDescriptionEditor extends Editor<List<? extends SpellParagraph>> {

	private static final Pattern PARAGRAPH_BREAK = Pattern.compile("\n\\s*(?=[\n\\-\\#\\\\])");
	private final TextArea value;

	public SpellDescriptionEditor(final String name, final Collection<? extends SpellParagraph> initialValue) {
		super(name);

		setPadding(new Insets(8, 0, 8, 0));

		value = new TextArea(initialValue.stream().map(sp -> {
			if (sp instanceof Bullet) {
				return " - " + sp.getText();
			} else if (sp instanceof SpellFeature) {
				return " # " + ((SpellFeature) sp).getTitle() + ". " + sp.getText();
			} else {
				return sp.getText();
			}
		}).collect(Collectors.joining("\n\n")));
		value.setWrapText(true);

		final Button help = new Button("?");
		help.setOnAction(helpEvent -> {
			// TODO provide help
		});

		getColumnConstraints().get(1).setHalignment(HPos.RIGHT);
		addRow(0, StatBlock.smallCaps(name, "header-small"), help);
		this.add(value, 0, 1, 2, 1);
	}

	@Override
	public List<? extends SpellParagraph> getValue() {
		final String[] paragraphs = PARAGRAPH_BREAK.split(value.getText());
		final List<SpellParagraph> ret = new ArrayList<>();

		for (final String p : paragraphs) {
			if (p.startsWith("-")) {
				ret.add(new Bullet(p.substring(1).trim()));
			} else if (p.startsWith("#")) {
				final int l = p.indexOf('.');
				if (l < 0)
					ret.add(new SpellParagraph(p.substring(1).trim()));
				else
					ret.add(new SpellFeature(p.substring(1, l).trim(), p.substring(l + 1).trim()));
			} else {
				ret.add(new SpellParagraph(p.replaceFirst("^\\\\", "").trim()));
			}
		}

		return ret;
	}
}
