package dmscreen.data.spell;

import dmscreen.statblock.StatBlock;
import javafx.scene.Node;

public class Bullet extends SpellParagraph {

	public Bullet(final String text) {
		super(text);
	}

	@Override
	public Node getNode() {
		return StatBlock.formattedDataLine("", " \u2022 " + getText());
	}

}
