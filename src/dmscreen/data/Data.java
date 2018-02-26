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
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import dmscreen.data.base.DataSet;
import dmscreen.data.creature.Condition;
import dmscreen.data.creature.feature.Action;
import dmscreen.data.creature.feature.Attack;
import dmscreen.data.creature.feature.Feature;
import dmscreen.data.creature.feature.InnateSpellcasting;
import dmscreen.data.creature.feature.Spellcasting;
import dmscreen.data.creature.feature.Subfeatures;
import dmscreen.data.spell.Bullet;
import dmscreen.data.spell.SpellFeature;
import dmscreen.data.spell.SpellParagraph;

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

		GSON = new GsonBuilder().registerTypeAdapterFactory(featureAdapter).registerTypeAdapterFactory(actionAdapter).registerTypeAdapterFactory(spellAdapter).create();
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
		Files.walk(root).filter(path -> !Files.isDirectory(path) && path.getFileName().toString().endsWith(".json")).forEach(path -> {
			final String setName = path.getParent().getFileName().toString();

			DataSet set = data.get(setName);
			if (set == null) {
				set = new DataSet();
				data.put(setName, set);

				if (setName.equalsIgnoreCase("source")) {
					Arrays.stream(Condition.values()).forEach(set.conditions::add);
				}
			}

			try {
				final String name = path.getFileName().toString().replace(".json", "").toLowerCase();
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
				e.printStackTrace();
			}
		});

		System.out.println(GSON.toJson(data));
	}

	public Map<String, DataSet> getData() {
		return data;
	}

}
