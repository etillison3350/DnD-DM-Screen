package dmscreen.data.creature.feature.template;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import dmscreen.Util;

public abstract class Template<T> {

	public final String name;
	protected final List<TemplateField> fields = new ArrayList<>();

	public Template(final String name, final Collection<TemplateField> fields) {
		this.name = name;
		this.fields.addAll(fields);
	}

	public abstract T make(final Map<String, Object> values);

	public static String format(final String format, final Map<String, Object> values) {
		ScriptEngine engine = null;

		final StringBuffer ret = new StringBuffer(format.length() + 16);
		StringBuffer script = null;
		int bracket = 0;

		for (final char c : format.toCharArray()) {
			if (script == null) {
				if (c == '$')
					script = new StringBuffer();
				else
					ret.append(c);
			} else {
				if (bracket == 0 && c != '{') {
					ret.append('$');
					ret.append(c);
				} else if (c == '{') {
					if (bracket++ > 0) script.append(c);
				} else if (c == '}') {
					if (--bracket <= 0) {
						if (values.containsKey(script.toString())) {
							ret.append(getString(values.get(script.toString()).toString()));
						} else {
							if (engine == null) engine = JavaScript.getEngine(values);
							try {
								ret.append(engine.eval(script.toString()));
							} catch (final ScriptException e) {
								ret.append("[ERROR]");
							}
						}
						script = null;
					} else {
						script.append(c);
					}
				} else {
					script.append(c);
				}
			}
		}

		return ret.toString();
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static String getString(final Object o) {
		if (o instanceof Collection) {
			return ((Collection) o).stream().collect(Collectors.joining(", ")).toString();
		} else if (o instanceof Map) {
			return ((Map) o).keySet().stream().map(k -> String.format("%s: %s", k, ((Map) o).get(k))).collect(Collectors.joining(", ")).toString();
		} else if (o instanceof Enum) {
			return Util.titleCase(((Enum) o).name());
		}

		return o.toString();
	}

}
