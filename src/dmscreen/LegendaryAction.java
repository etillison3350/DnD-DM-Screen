package dmscreen;

import dmscreen.data.creature.feature.Action;
import dmscreen.statblock.StatBlock;
import javafx.scene.Node;

public class LegendaryAction extends Action {

	private final int cost;

	public LegendaryAction(final String name, final int cost, final String description) {
		super(name, cost == 1 ? "" : String.format("Costs %d Actions", cost), description);

		this.cost = cost;
	}

	public LegendaryAction(final String name, final String description) {
		this(name, 1, description);
	}

	public int getCost() {
		return cost;
	}

	@Override
	public Node getNode() {
		return StatBlock.dataLine(getTitle() + ".", getDescription());
	}

}
