package dmscreen;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;

public class Util {

	public static final NumberFormat COMMA_SEPARATED = new DecimalFormat("#,###");

	private Util() {}

	public static String titleCase(final String string) {
		if (string == null || string.isEmpty()) return "";
		return Arrays.stream(string.split("[_ ]")).map(Util::sentenceCase).collect(Collectors.joining(" "));
	}

	public static String sentenceCase(final String string) {
		if (string == null || string.isEmpty()) return "";
		return Character.toUpperCase(string.charAt(0)) + string.substring(1).toLowerCase();
	}

	public static String titleCaseFromCamelCase(final String camelCase) {
		if (camelCase == null || camelCase.isEmpty()) return "";
		return Character.toUpperCase(camelCase.charAt(0)) + camelCase.substring(1).replaceAll("[A-Z]", " $0");
	}

	/**
	 * Applies a case rule (uppercase, lowercase, sentence case, or title case) on {@code string} based on {@code source}.<br>
	 * <ul>
	 * <li>If {@code source} is entirely uppercase, returns an uppercase string.</li>
	 * <li>If {@code source} is entirely lowercase, returns an lowercase string.</li>
	 * <li>If the first and second word of {@code source} begin with an uppercase letter (i.e. the first character and the
	 * character after the first underscore are both uppercase), returns a title case string (as by {@link #titleCase(String)})</li>
	 * <li>If the first character of {@code source} is uppercase, and {@code source} either has one word, or has a second word
	 * that begins with a lowercase letter, returns a sentence case string, as by {@link #sentenceCase(String)}</li>
	 * <li>If none of the above conditions are met, the original string is returned.
	 * </ul>
	 */
	public static String matchCase(final String source, final String string) {
		if (source.equals(source.toUpperCase())) {
			return string.toUpperCase();
		} else if (source.equals(source.toLowerCase())) {
			return string.toLowerCase();
		} else if (Character.isUpperCase(source.charAt(0))) {
			final int index = source.indexOf('_');
			if (index < 0 || index >= source.length() - 1 || !Character.isUpperCase(source.charAt(index + 1))) return sentenceCase(string);

			return titleCase(string);
		} else {
			return string;
		}
	}

	public static String andJoin(final Collection<String> strings) {
		return andJoin("and", strings);
	}

	public static String andJoin(final String and, final Collection<String> strings) {
		if (strings.isEmpty()) return "";
		if (strings.size() == 1) return strings.iterator().next();
		if (strings.size() == 2) return strings.stream().collect(Collectors.joining(String.format(" %s ", and)));

		final Iterator<String> iter = strings.iterator();
		final StringBuilder ret = new StringBuilder(iter.next());
		while (iter.hasNext()) {
			final String str = iter.next();
			ret.append(", ");
			if (!iter.hasNext()) ret.append(and + " ");
			ret.append(str);
		}
		return ret.toString();
	}

	public static String andJoin(final Object[] strings) {
		return andJoin("and", strings);
	}

	public static String andJoin(final String and, final Object[] strings) {
		if (strings.length == 0) return "";
		if (strings.length == 1) return strings[0].toString();
		if (strings.length == 2) return String.format("%s %s %s", strings[0], and, strings[1]);

		final StringBuilder ret = new StringBuilder(strings[0].toString());
		for (int i = 1; i < strings.length - 1; i++) {
			ret.append(", ");
			ret.append(strings[i]);
		}
		ret.append(", ");
		ret.append(and);
		ret.append(' ');
		ret.append(strings[strings.length - 1]);

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

	/**
	 * Attempts to cast <code>t</code> to <code>clazz</code>. If this is not possible, or if <code>t</code> is <code>null</code>
	 * , returns <code>def</code> instead.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T castOrDefault(final Class<T> clazz, final Object t, final T def) {
		if (t != null && clazz.isAssignableFrom(t.getClass())) return (T) t;
		return def;
	}

}
