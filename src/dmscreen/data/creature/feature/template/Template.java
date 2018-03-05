package dmscreen.data.creature.feature.template;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dmscreen.Util;
import dmscreen.data.base.Ability;
import dmscreen.data.base.DamageType;
import dmscreen.data.base.DiceRoll;
import dmscreen.data.base.Size;
import dmscreen.data.base.Skill;
import dmscreen.data.creature.Condition;
import dmscreen.data.creature.CreatureType;
import dmscreen.data.creature.SpeedType;
import dmscreen.data.creature.VisionType;

public abstract class Template<T> {

	public static final List<Class<?>> SIMPLE_CLASSES = Arrays.asList(int.class, boolean.class, String.class, DiceRoll.class, Ability.class, Skill.class, DamageType.class, Size.class, Condition.class, CreatureType.class, SpeedType.class, VisionType.class);

	public static final Pattern VARIABLE = Pattern.compile("\\$\\{(\\w+?)(?:\\:(\\w+?))?\\}");

	public final String name;
	protected final Map<String, Class<?>> fields = new LinkedHashMap<>();

	public Template(final String name) {
		this.name = name;
	}

	public abstract T make(final Map<String, Object> values);

	public static Class<?> getClass(final String className) {
		for (final Class<?> clazz : SIMPLE_CLASSES) {
			if (clazz.getSimpleName().equalsIgnoreCase(className)) return clazz;
		}
		return String.class;
	}

	public static String format(final String format, final Map<String, Object> values) {
		final StringBuffer ret = new StringBuffer(format.length() + 16);
		final Matcher m = Template.VARIABLE.matcher(format);

		int lastEnd = 0;
		while (m.find()) {
			ret.append(format.substring(lastEnd, m.start()));

			final String var = m.group(1);
			ret.append(Util.matchCase(var, values.get(var).toString()));

			lastEnd = m.end();
		}
		ret.append(format.substring(lastEnd));

		return ret.toString();
	}

}
