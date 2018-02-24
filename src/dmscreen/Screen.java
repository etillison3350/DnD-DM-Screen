package dmscreen;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import dmscreen.data.Ability;
import dmscreen.data.DamageType;
import dmscreen.data.DiceRoll;
import dmscreen.data.Size;
import dmscreen.data.Skill;
import dmscreen.data.creature.Action;
import dmscreen.data.creature.Alignment;
import dmscreen.data.creature.Attack;
import dmscreen.data.creature.Creature;
import dmscreen.data.creature.CreatureType;
import dmscreen.data.creature.Feature;
import dmscreen.data.creature.VisionType;
import dmscreen.data.spell.Bullet;
import dmscreen.data.spell.SpellFeature;
import dmscreen.data.spell.SpellParagraph;
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

	private static final Gson GSON;

	static {
		final RuntimeTypeAdapterFactory<Action> actionAdapter = RuntimeTypeAdapterFactory.of(Action.class, "class");
		actionAdapter.registerSubtype(Action.class);
		actionAdapter.registerSubtype(Attack.class);

		final RuntimeTypeAdapterFactory<SpellParagraph> spellAdapter = RuntimeTypeAdapterFactory.of(SpellParagraph.class);
		spellAdapter.registerSubtype(SpellParagraph.class, "Paragraph");
		spellAdapter.registerSubtype(Bullet.class);
		spellAdapter.registerSubtype(SpellFeature.class, "Feature");

		GSON = new GsonBuilder().registerTypeAdapterFactory(actionAdapter).registerTypeAdapterFactory(spellAdapter).create();

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

		Creature creature = new Creature();
		creature.name = "Vampire";
		creature.shortName = "the vampire";
		creature.size = Size.MEDIUM;
		creature.type = CreatureType.UNDEAD;
		creature.subtype = "shapechanger";
		creature.alignment = Alignment.LAWFUL_EVIL;
		creature.ac = 16;
		creature.armorNote = "natural armor";
		creature.hitDice = new DiceRoll(17, 8, 68);
		creature.speed = 30;
		creature.abilityScores.put(Ability.STRENGTH, 18);
		creature.abilityScores.put(Ability.DEXTERITY, 18);
		creature.abilityScores.put(Ability.CONSTITUTION, 18);
		creature.abilityScores.put(Ability.INTELLIGENCE, 17);
		creature.abilityScores.put(Ability.WISDOM, 15);
		creature.abilityScores.put(Ability.CHARISMA, 18);
		creature.savingThrows.put(Ability.DEXTERITY, 9);
		creature.savingThrows.put(Ability.WISDOM, 7);
		creature.savingThrows.put(Ability.CHARISMA, 9);
		creature.skills.put(Skill.PERCEPTION, 7);
		creature.skills.put(Skill.STEALTH, 9);
		creature.resistances.get(null).add(DamageType.NECROTIC);
		creature.resistances.put("nonmagical weapons", Stream.of(DamageType.BLUDGEONING, DamageType.PIERCING, DamageType.SLASHING).collect(Collectors.toCollection(HashSet::new)));
		creature.senses.put(VisionType.DARKVISION, 120);
		creature.languages.add("the languages it knew in life");
		creature.challengeRating = 13;
		creature.features = Arrays.asList(new Feature("Legendary Resistance", "3/Day", "If the vampire fails a saving throw, it can choose to succeed instead."), new Feature("Regeneration", "The vampire regains 20 hit points at the start of its turn if it has at least 1 hit point and isn't in sunlight or running water. If the vampire takes radiant damage or damage from holy water, this trait doesn't function at the start of the vampire's next turn."), new Feature("Spell Storing",
				"A spellcaster who wears the shield guardian's amulet can cause the guardian to store one spell of 4th level or lower. To do so, the wearer must cast the spell on the guardian. The spell has no effect but is stored within the guardian. When commanded to do so by the wearer or when a situation arises that was predefined by the spellcaster, the guardian casts the stored spell with any parameters set by the original caster, requiring no components. When the spell is cast or a new spell is stored, any previously stored spell is lost."));
		creature.actions = Arrays.asList(new Action("Multiattack", "Vampire Form Only", "The vampire makes two attacks, only one of which can be a bite attack."),
				new Attack("Bite", "Bat or Vampire Form Only", Attack.Type.MELEE_WEAPON, 9, "range 5 ft., one willing creature, or a creature that is grappled by the vampire, incapcitated, or restrained",
						". The target's hit point maximum is reduced by an amount equal to the necrotic damage taken, and the vampire regains hit points equal to that amount. The reduction lasts until the target finishes a long rest. The target dies if this effect reduces its hit point maximum to 0. A humanoid slain in this way and then buried in the ground rises the following night as a vampire spawn under the vampire's control.", new Attack.Damage(new DiceRoll(1, 6, 4), DamageType.BLUDGEONING),
						new Attack.Damage(new DiceRoll(3, 6), DamageType.NECROTIC)));
		creature.legendaryActions = Arrays.asList(new LegendaryAction("Move", "The vampire moves up to half its speed without provoking opportunity attacks."), new LegendaryAction("Unarmed Strike", "The vampire makes one unarmed strike."), new LegendaryAction("Bite", 2, "The vampire makes one bite attack."));

		System.out.println(GSON.toJson(creature));
		creature = GSON.fromJson(GSON.toJson(creature), Creature.class);

		final Node statBlock = StatBlockUtils.getStatBlock(creature);
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
