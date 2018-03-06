package dmscreen.data.creature.feature.template_old;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TemplateField {

	public final String name;
	public final Class<?> type;
	public final List<Class<?>> typeParameters = new ArrayList<>();
	public final List<Object> parameters = new ArrayList<>();

	public TemplateField(final String name, final Class<?> type, final Collection<Class<?>> typeParameters, final Collection<Object> parameters) {
		this.name = name;
		this.type = type;
	}

}
