package dmscreen.data.base;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import dmscreen.data.creature.Condition;
import dmscreen.data.creature.Creature;
import dmscreen.data.creature.feature.template.Template;
import dmscreen.data.spell.Spell;

public class DataSet {

	public String name;

	public transient final Set<Condition> conditions = new TreeSet<>();
	public final Set<Spell> spells = new TreeSet<>(Comparator.comparing(spell -> spell.name));
	public final Set<Creature> creatures = new TreeSet<>(Comparator.comparing(creature -> creature.name));
	public final Set<Template<?>> features = new TreeSet<>(Comparator.comparing(feature -> feature.name));

}
