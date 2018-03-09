package dmscreen.data;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import dmscreen.Util;
import dmscreen.data.base.DataSet;
import dmscreen.data.creature.Condition;
import dmscreen.data.creature.feature.Action;
import dmscreen.data.creature.feature.Attack;
import dmscreen.data.creature.feature.Feature;
import dmscreen.data.creature.feature.InnateSpellcasting;
import dmscreen.data.creature.feature.Spellcasting;
import dmscreen.data.creature.feature.Subfeatures;
import dmscreen.data.creature.feature.template.InnateSpellcastingTemplate;
import dmscreen.data.creature.feature.template.SimpleActionTemplate;
import dmscreen.data.creature.feature.template.SimpleFeatureTemplate;
import dmscreen.data.creature.feature.template.SpellcastingTemplate;
import dmscreen.data.creature.feature.template.Template;
import dmscreen.data.spell.Bullet;
import dmscreen.data.spell.SpellFeature;
import dmscreen.data.spell.SpellParagraph;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Data {

	public static final Gson GSON;

	static {
		final RuntimeTypeAdapterFactory<Feature> featureAdapter = RuntimeTypeAdapterFactory.of(Feature.class);
		featureAdapter.registerSubtype(Feature.class);
		featureAdapter.registerSubtype(InnateSpellcasting.class);
		featureAdapter.registerSubtype(Spellcasting.class);
		featureAdapter.registerSubtype(Subfeatures.class);

		final RuntimeTypeAdapterFactory<Action> actionAdapter = RuntimeTypeAdapterFactory.of(Action.class, "class");
		actionAdapter.registerSubtype(Action.class);
		actionAdapter.registerSubtype(Attack.class);

		final RuntimeTypeAdapterFactory<SpellParagraph> spellAdapter = RuntimeTypeAdapterFactory.of(SpellParagraph.class);
		spellAdapter.registerSubtype(SpellParagraph.class, "Paragraph");
		spellAdapter.registerSubtype(Bullet.class);
		spellAdapter.registerSubtype(SpellFeature.class, "Feature");

		@SuppressWarnings("rawtypes")
		final RuntimeTypeAdapterFactory<Template> templateAdapter = RuntimeTypeAdapterFactory.of(Template.class);
		templateAdapter.registerSubtype(SimpleFeatureTemplate.class, "Feature");
		templateAdapter.registerSubtype(SimpleActionTemplate.class, "Action");
		templateAdapter.registerSubtype(InnateSpellcastingTemplate.class, "InnateSpellcasting");
		templateAdapter.registerSubtype(SpellcastingTemplate.class, "Spellcasting");

		GSON = new GsonBuilder().registerTypeAdapterFactory(featureAdapter).registerTypeAdapterFactory(actionAdapter).registerTypeAdapterFactory(spellAdapter).registerTypeAdapterFactory(templateAdapter).create();
	}

	private final Map<String, DataSet> data = new TreeMap<>((o1, o2) -> {
		if (o1.equalsIgnoreCase("source")) {
			if (o2.equalsIgnoreCase("source")) return 0;
			return -1;
		} else if (o2.equalsIgnoreCase("source")) {
			return 1;
		}
		return o1.compareToIgnoreCase(o2);
	});

	public Data(final Path root) throws IOException {
		final Map<String, Exception> errors = new HashMap<>();
		Files.walk(root).filter(path -> !Files.isDirectory(path) && path.getFileName().toString().endsWith(".json")).forEach(path -> {
			final String setName = path.getParent().getFileName().toString();

			DataSet set = data.get(setName);
			if (set == null) {
				set = new DataSet();
				set.name = Util.titleCase(setName);
				data.put(setName, set);

				if (setName.equalsIgnoreCase("source")) {
					Arrays.stream(Condition.values()).forEach(set.conditions::add);
				}
			}

			final String name = path.getFileName().toString().replace(".json", "").toLowerCase();
			try {
				if (name.equals("data")) {
					final DataSet newSet = GSON.fromJson(new String(Files.readAllBytes(path)), DataSet.class);
					for (final Field field : DataSet.class.getFields()) {
						field.getType().getMethod("addAll", Collection.class).invoke(field.get(set), field.get(newSet));
					}
				} else {
					final Field field = DataSet.class.getField(name);
					if (Modifier.isTransient(field.getModifiers())) return;

					final Object newSet = GSON.fromJson(new String(Files.readAllBytes(path)), TypeToken.getParameterized(field.getType(), ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]).getType());
					field.getType().getMethod("addAll", Collection.class).invoke(field.get(set), newSet);
				}
			} catch (JsonSyntaxException | IOException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
				System.err.println("Error reading " + path.toString() + ": " + e.toString());
				errors.put(setName + "/" + name, e);
			}
		});

		if (!errors.isEmpty()) {
			final Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Error while loading JSON:\n\n" + errors.keySet().stream().map(s -> String.format(" \u2022  %s.json: %s", s, errors.get(s))).collect(Collectors.joining("\n")) + "\n\nThese files will not be loaded.");
			alert.showAndWait();
		}
	}

	public Map<String, DataSet> getData() {
		return data;
	}

}
