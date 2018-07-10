package dmscreen.data.creature.feature;

import java.util.Map;

import dmscreen.Screen;
import dmscreen.statblock.StatBlock;
import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Subfeatures extends Feature {

	private final Map<String, String> subfeatures;

	public Subfeatures(final String name, final String note, final String description, final Map<String, String> subfeatures) {
		super(name, note, description);

		this.subfeatures = subfeatures;
	}

	public Subfeatures(final String name, final String description, final Map<String, String> subfeatures) {
		this(name, "", description, subfeatures);
	}

	public Map<String, String> getSubfeatures() {
		return subfeatures;
	}

	@Override
	public Node getNode() {
		final TextFlow node = StatBlock.conditionAltDataLine(getTitle() + ".", getDescription(), true);

		subfeatures.forEach((name, desc) -> {
			final Text title = new Text("\n   " + name + ". ");
			title.setFont(Font.font(Screen.DEFAULT_FONT_NAME, FontPosture.ITALIC, Font.getDefault().getSize()));
			node.getChildren().add(title);

			node.getChildren().addAll(StatBlock.conditionTooltips(desc));
		});
		node.getChildren().add(new Text("\n"));

		return node;
	}

}
