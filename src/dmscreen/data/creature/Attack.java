package dmscreen.data.creature;

import dmscreen.Util;
import dmscreen.data.DamageType;
import dmscreen.data.DiceRoll;
import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Attack extends Action {

	private final Damage[] damages;
	private final Type type;
	private final int mod;
	private final String params;

	/**
	 * Output format is <b>{Name} ({note}).</b> <i>{Type} Attack:</i> +{Mod} to hit, {params}.
	 * <i>Hit:</i> {damages} {description} <br>
	 * Example: <b>Unarmed Strike (Vampire Form Only).</b> <i>Melee Weapon Attack:</i> +9 to hit,
	 * reach 5 ft., one target. <i>Hit:</i> 8 (1d8 + 4) bludgeoning damage. Instead of dealing
	 * damage, the vampire can grapple the target (escape DC 18).
	 */
	public Attack(final String name, final String note, final Type type, final int mod, final String params, final String description, final Damage... damages) {
		super(name, note, description);

		this.type = type;
		this.mod = mod;
		this.params = params;
		this.damages = damages;
	}

	public Attack(final String name, final Type type, final int mod, final String params, final String description, final Damage... damages) {
		this(name, "", type, mod, params, description, damages);
	}

	public Damage[] getDamages() {
		return damages;
	}

	public Type getType() {
		return type;
	}

	public int getMod() {
		return mod;
	}

	public String getOnHit() {
		final StringBuffer onHit = new StringBuffer();
		for (int i = 0; i < damages.length; i++) {
			if (i > 0) onHit.append(" plus ");
			onHit.append((int) Math.floor(damages[i].diceRoll.expectedValue()));
			onHit.append(" (");
			onHit.append(damages[i].diceRoll);
			onHit.append(") ");
			onHit.append(damages[i].type.name().toLowerCase());
			onHit.append(" damage");
		}
		if (getDescription() == null || getDescription().isEmpty())
			onHit.append('.');
		else
			onHit.append(getDescription());

		return onHit.toString();
	}

	@Override
	public Node getNode() {
		final TextFlow line = new TextFlow();

		final Text titleText = new Text(getTitle() + ". ");
		titleText.setFont(Font.font("System", FontWeight.BOLD, FontPosture.ITALIC, Font.getDefault().getSize()));
		line.getChildren().add(titleText);

		final Text attackType = new Text(Util.titleCase(type.name()) + " Attack: ");
		attackType.setFont(Font.font("System", FontWeight.NORMAL, FontPosture.ITALIC, Font.getDefault().getSize()));
		line.getChildren().add(attackType);

		final Text attackParams = new Text(String.format("+%d to hit, %s. ", mod, params));
		line.getChildren().add(attackParams);

		final Text hit = new Text("Hit: ");
		hit.setFont(Font.font("System", FontWeight.NORMAL, FontPosture.ITALIC, Font.getDefault().getSize()));
		line.getChildren().add(hit);

		final Text onHit = new Text(getOnHit() + "\n");
		line.getChildren().add(onHit);

		return line;
	}

	public static enum Type {
		MELEE_WEAPON,
		MELEE_SPELL,
		RANGED_WEAPON,
		RANGED_SPELL;
	}

	public static class Damage {
		public final DiceRoll diceRoll;
		public final DamageType type;

		public Damage(final DiceRoll diceRoll, final DamageType type) {
			this.diceRoll = diceRoll;
			this.type = type;
		}

	}

}
