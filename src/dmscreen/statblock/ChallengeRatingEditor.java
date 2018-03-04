package dmscreen.statblock;

import javafx.scene.control.Spinner;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import dmscreen.Util;
import dmscreen.data.creature.Creature;

public class ChallengeRatingEditor extends Editor<Integer> {

	private final Spinner<Integer> value;

	public ChallengeRatingEditor(final String name, final int initialValue) {
		super(name);

		value = new Spinner<>(-4, 30, initialValue == 0 ? -4 : initialValue < 0 ? initialValue + 1 : initialValue);
		value.getValueFactory().setConverter(new StringConverter<Integer>() {
			{
				for (int i = -4; i <= 30; i++) {
					System.out.println(toString(i) + ", " + fromString(toString(i)));
				}
			}

			@Override
			public String toString(final Integer i) {
				final int cr = i < 1 ? i - 1 : i;
				System.out.println(i + ", " + cr);
				return String.format("%s (%s XP)", StatBlock.challenge(cr), Util.COMMA_SEPARATED.format(Creature.XP[cr + 5]));
			}

			@Override
			public Integer fromString(final String str) {
				// Replace all spaces
				final String noSp = str.replaceAll("\\s+", "");

				int cr = Integer.MIN_VALUE;
				int xp = Integer.MIN_VALUE;
				if (noSp.matches("[\\d\\/\\,]+\\(.+?\\)")) { // If the value has numbers and parentheses
					final String crStr = noSp.substring(0, noSp.indexOf('('));
					try {
						// Find the value of the string before the parentheses
						final int i = Integer.parseUnsignedInt(crStr);
						if (i <= 30) { // If the value is in CR range
							if (i == 0) { // If the CR is 0, it could either be 0 (0 XP) or 0 (10 XP)
								if (noSp.contains("(10"))
									cr = -3;
								else
									cr = -4;
							} else {
								cr = i;
							}
						} else { // Otherwise, assume the value is an XP amount
							xp = i;
						}
					} catch (final NumberFormatException e) { // If the string is not a number...
						if (noSp.startsWith("1/")) { // It could be a fractional CR
							// Find the denominator
							final int recip = Integer.parseUnsignedInt(crStr.substring(2));
							switch (recip) {
								case 2:
									cr = 0;
									break;
								case 4:
									cr = -1;
									break;
								case 8:
									cr = -2;
									break;
								default: // If the denominator is not 2, 4, or 8, the string is invalid.
									throw e;
							}
						} else { // Or it could have commas in it, in which case it's definitely XP
							xp = Integer.parseInt(crStr.replace(",", ""));
						}
					}
				} else if (noSp.endsWith("XP") || noSp.contains(",")) {
					// If it ends with 'XP' or has commas, it's XP
					xp = Integer.parseUnsignedInt(noSp.replaceAll("[,XP]+", ""));
				} else if (noSp.startsWith("1/")) { // If it's a standalone fraction, parse it as above
					final int recip = Integer.parseUnsignedInt(noSp.substring(2));
					switch (recip) {
						case 2:
							cr = -0;
							break;
						case 4:
							cr = -1;
							break;
						case 8:
							cr = -2;
							break;
						default:
							throw new NumberFormatException();
					}
				} else { // Assume it's a standalone number
					final int i = Integer.parseUnsignedInt(noSp);
					if (i <= 30) { // If it's in CR range...
						if (i == 0) { // If it's 0, assume it's the 0 XP variant
							cr = -4;
						} else {
							cr = i;
						}
					} else { // Otherwise, assume it's XP
						xp = i;
					}
				}

				// If the value of cr has been set, return it.
				if (cr > Integer.MIN_VALUE) return cr;

				// If xp hasn't been set either, the input is invalid.
				if (xp < 0) throw new NumberFormatException();

				// Iterate through Creature#XP
				for (int r = 0; r < 36; r++) {
					if (r == 5) r++; // Skip index 5 (0)

					final int xpr = Creature.XP[r];

					// If the XP matches exactly, return the corresponding CR
					if (xp == xpr) return r < 5 ? r - 4 : r - 5;

					// If we've passed the value of xp...
					if (xp < xpr) {
						// If xp is closer to the higher value, return the corresponding CR
						if (2 * xp - xpr - Creature.XP[r - 1] > 0) {
							return r < 5 ? r - 4 : r - 5;
						} else { // Otherwise return one less
							return r < 5 ? r - 5 : r - 6;
						}
					}
				}

				// If the XP value given is greater than CR 30, return 30
				return 30;
			}
		});
		value.setEditable(true);
		value.focusedProperty().addListener((observable, oldValue, newValue) -> {
			try {
				if (!newValue) value.increment(0);
			} catch (final NumberFormatException e) {
				value.getEditor().setText(value.getValueFactory().getConverter().toString(value.getValue()));
			}
		});
		value.getEditor().setText(value.getValueFactory().getConverter().toString(value.getValue()));

		addRow(0, new Text(name + ":"), value);
	}

	@Override
	public Integer getValue() {
		final int v = value.getValue();
		return v < 1 ? v - 1 : v;
	}

}
