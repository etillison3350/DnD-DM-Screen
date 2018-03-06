package dmscreen.data.creature.feature.template;

import java.util.Collection;
import java.util.stream.Collectors;

public class TemplateUtil {

	private TemplateUtil() {}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static String format(final Object obj, final String... parameters) {
		if (obj instanceof Collection<?>) {
			final Collection collection = (Collection) obj;
			if (parameters.length == 0) {
				return collection.stream().map(o -> TemplateUtil.format(o)).collect(Collectors.joining(", ")).toString();
			} else if (parameters.length == 1 || parameters.length > 2) {
				return collection.stream().map(o -> TemplateUtil.format(o)).collect(Collectors.joining(parameters[0])).toString();
			} else if (parameters.length == 2) {
				final StringBuffer ret = new StringBuffer();

				return collection.stream().limit(collection.size() - 1).map(o -> TemplateUtil.format(o)).collect(Collectors.joining(parameters[0])).toString() + parameters[1];
			}
		}

		return obj.toString();
	}
}
