package dmscreen;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;

public class Util {

	private Util() {}

	public static String titleCase(final String string) {
		return Arrays.stream(string.split("[_ ]")).map(str -> Character.toUpperCase(str.charAt(0)) + str.substring(1).toLowerCase()).collect(Collectors.joining(" "));
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

}
