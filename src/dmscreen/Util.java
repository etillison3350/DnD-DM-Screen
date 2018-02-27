package dmscreen;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;

public class Util {

	private Util() {}

	public static String titleCase(final String string) {
		return Arrays.stream(string.split("[_ ]")).map(Util::sentenceCase).collect(Collectors.joining(" "));
	}

	public static String sentenceCase(final String string) {
		return Character.toUpperCase(string.charAt(0)) + string.substring(1).toLowerCase();
	}

	public static String titleCaseFromCamelCase(final String camelCase) {
		return Character.toUpperCase(camelCase.charAt(0)) + camelCase.substring(1).replaceAll("[A-Z]", " $0");
	}

	public static String andJoin(final Collection<String> strings) {
		if (strings.isEmpty()) return "";
		if (strings.size() == 1) return strings.iterator().next();
		if (strings.size() == 2) return strings.stream().collect(Collectors.joining(" and "));

		final Iterator<String> iter = strings.iterator();
		final StringBuilder ret = new StringBuilder(iter.next());
		while (iter.hasNext()) {
			final String str = iter.next();
			ret.append(", ");
			if (!iter.hasNext()) ret.append("and ");
			ret.append(str);
		}
		return ret.toString();
	}

	public static String ordinal(final int number) {
		int last = Math.abs(number % 100);
		if (last > 10 && last < 20) return "th";

		last %= 10;
		switch (last) {
			case 1:
				return "st";
			case 2:
				return "nd";
			case 3:
				return "rd";
			default:
				return "th";
		}
	}

	public static String getName(final Object obj) {
		String name;
		try { // Find a field named "name"
			name = obj.getClass().getField("name").get(obj).toString();
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e1) {
			try { // Find a method named "name" (mostly for enums)
				name = obj.getClass().getMethod("name").invoke(obj).toString();
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e2) {
				try { // Find a method named "getName"
					name = obj.getClass().getMethod("getName").invoke(obj).toString();
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e3) {
					name = obj.toString(); // Give up and use toString()
				}
			}
		}
		return name;
	}

	public static String escapeUnicode(final String unicode) {
		final StringBuffer ret = new StringBuffer(unicode.length());
		unicode.chars().forEach(c -> {
			if (c > 0xff)
				ret.append(String.format("\\u%04x", c));
			else
				ret.append((char) c);
		});
		return ret.toString();
	}

}
