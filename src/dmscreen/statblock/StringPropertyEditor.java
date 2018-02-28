package dmscreen.statblock;

import java.util.LinkedHashMap;
import java.util.Map;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class StringPropertyEditor extends PropertyEditor<String> {

	private final Map<Node, Constraints> nodes;
	private final TextField value;

	public StringPropertyEditor(final String name) {
		super(name);

		nodes = new LinkedHashMap<>();

		final TextFlow label = new TextFlow(new Text(name + ":"));
		value = new TextField();
		nodes.put(label, new Constraints(0, 0));
		nodes.put(value, new Constraints(1, 0));
	}

	@Override
	public String getValue() {
		return value.getText();
	}

	@Override
	public Map<Node, Constraints> getNodes() {
		return new LinkedHashMap<>(nodes);
	}

}
