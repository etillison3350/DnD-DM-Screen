package dmscreen.data.creature.feature.template;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import dmscreen.Util;
import dmscreen.data.base.Ability;
import dmscreen.data.base.DamageType;
import dmscreen.data.base.Size;
import dmscreen.data.base.Skill;
import dmscreen.data.creature.Alignment;
import dmscreen.data.creature.Condition;
import dmscreen.data.creature.CreatureType;
import dmscreen.data.creature.SpeedType;
import dmscreen.data.creature.VisionType;
import dmscreen.data.spell.SpellType;

public class JavaScript {

	private static final List<Class<?>> LOADED_CLASSES = Arrays.asList(Util.class, Ability.class, DamageType.class, Size.class, Skill.class, Alignment.class, Condition.class, CreatureType.class, SpeedType.class, VisionType.class, SpellType.class);

	private JavaScript() {}

	public static ScriptEngine getEngine() {
		final ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
		LOADED_CLASSES.forEach(c -> {
			try {
				engine.eval(String.format("var %s = Java.type('%s');", c.getSimpleName(), c.getName()));
			} catch (final ScriptException e) {
				e.printStackTrace();
			}
		});

		return engine;
	}

	public static ScriptEngine getEngine(final Map<String, Object> variables) {
		final ScriptEngine engine = getEngine();
		variables.forEach(engine::put);
		return engine;
	}

}
