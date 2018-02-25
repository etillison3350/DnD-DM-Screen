package dmscreen;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import dmscreen.data.creature.feature.Action;
import dmscreen.data.creature.feature.Attack;
import dmscreen.data.creature.feature.Feature;
import dmscreen.data.creature.feature.InnateSpellcasting;
import dmscreen.data.creature.feature.Spellcasting;
import dmscreen.data.spell.Bullet;
import dmscreen.data.spell.SpellFeature;
import dmscreen.data.spell.SpellParagraph;
import dmscreen.misc.TestCreatures;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Screen extends Application {

	public static final Gson GSON;

	static {
		final RuntimeTypeAdapterFactory<Feature> featureAdapter = RuntimeTypeAdapterFactory.of(Feature.class);
		featureAdapter.registerSubtype(Feature.class);
		featureAdapter.registerSubtype(InnateSpellcasting.class);
		featureAdapter.registerSubtype(Spellcasting.class);

		final RuntimeTypeAdapterFactory<Action> actionAdapter = RuntimeTypeAdapterFactory.of(Action.class, "class");
		actionAdapter.registerSubtype(Action.class);
		actionAdapter.registerSubtype(Attack.class);

		final RuntimeTypeAdapterFactory<SpellParagraph> spellAdapter = RuntimeTypeAdapterFactory.of(SpellParagraph.class);
		spellAdapter.registerSubtype(SpellParagraph.class, "Paragraph");
		spellAdapter.registerSubtype(Bullet.class);
		spellAdapter.registerSubtype(SpellFeature.class, "Feature");

		GSON = new GsonBuilder().registerTypeAdapterFactory(featureAdapter).registerTypeAdapterFactory(actionAdapter).registerTypeAdapterFactory(spellAdapter).create();

	}

	public static void main(final String[] args) {
		try {
			Font.loadFont(new FileInputStream(Paths.get("Cormorant_Garamond/CormorantGaramond-Medium.ttf").toFile()), 12);
			Font.loadFont(new FileInputStream(Paths.get("Cormorant_Garamond/CormorantGaramond-Bold.ttf").toFile()), 12);
		} catch (final FileNotFoundException e) {}
		Application.launch(args);
	}

	@Override
	public void start(final Stage stage) throws Exception {
		// final TreeView<Object> segments = new TreeView<>();

		final Node statBlock = StatBlockUtils.getStatBlock(TestCreatures.drow);
		final StackPane blockPane = new StackPane(statBlock);
		blockPane.setPadding(new Insets(8));
		final ScrollPane scroll = new ScrollPane(blockPane);
		scroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		scroll.setFitToWidth(true);
		final SplitPane root = new SplitPane(new StackPane(), scroll);

		final Scene scene = new Scene(root, 1280, 720);
		scene.getStylesheets().add("dmscreen/statBlock.css");

		stage.setScene(scene);
		stage.setTitle("Dungeon Master's Screen");
		stage.show();

		createPlayerStage();
	}

	private void createPlayerStage() {

	}

}
