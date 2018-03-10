package dmscreen.data.creature.feature.template;

import java.util.Collection;
import java.util.Map;

import dmscreen.data.creature.feature.Action;

public class ActionTemplate extends Template<Action> {

	public final String nameFormat, noteFormat, descFormat;

	public ActionTemplate(final String name, final Collection<TemplateField> fields, final String nameFormat, final String noteFormat, final String descFormat) {
		super(name, fields);
		this.nameFormat = nameFormat;
		this.noteFormat = noteFormat;
		this.descFormat = descFormat;
	}

	@Override
	public Action make(final Map<String, Object> values) {
		return new Action(format(nameFormat, values), format(noteFormat, values), format(descFormat, values));
	}

}
