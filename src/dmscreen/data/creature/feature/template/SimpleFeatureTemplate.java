package dmscreen.data.creature.feature.template;

import java.util.Map;
import java.util.regex.Matcher;

import dmscreen.data.creature.feature.Feature;

public class SimpleFeatureTemplate extends Template<Feature> {

	private final String nameFormat, noteFormat, descFormat;

	public SimpleFeatureTemplate(final String name, final String nameFormat, final String noteFormat, final String descFormat) {
		super(name);

		this.nameFormat = nameFormat;
		this.noteFormat = noteFormat;
		this.descFormat = descFormat;

		Matcher m = Template.VARIABLE.matcher(nameFormat);
		while (m.find()) {
			fields.put(m.group(1), getClass(m.group(2)));
		}

		m = Template.VARIABLE.matcher(noteFormat);
		while (m.find()) {
			fields.put(m.group(1), getClass(m.group(2)));
		}

		m = Template.VARIABLE.matcher(descFormat);
		while (m.find()) {
			fields.put(m.group(1), getClass(m.group(2)));
		}
	}

	@Override
	public Feature make(final Map<String, Object> values) {
		return new Feature(Template.format(nameFormat, values), Template.format(noteFormat, values), Template.format(descFormat, values));
	}

}
