package dmscreen.statblock.editor.encounter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import dmscreen.data.adventure.CreatureSet;
import dmscreen.data.adventure.Encounter;
import dmscreen.statblock.StatBlock;
import dmscreen.statblock.editor.Editor;
import dmscreen.statblock.editor.IntegerEditor;
import dmscreen.statblock.editor.StringEditor;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;

public class EncounterCollectionEditor extends Editor<Collection<Encounter>> {

	private final List<EncounterRow> rows = new ArrayList<>();
	private int minRow = 2;

	private final CheckBox simple;
	private final Hyperlink add;

	public EncounterCollectionEditor(final String name, final Collection<Encounter> initialValue) {
		super(name);

		setPadding(new Insets(8, 0, 8, 0));

		this.add(StatBlock.smallCaps(name, "header-small"), 0, 0, 2, 1);
		this.add(new Separator(), 0, 1, 2, 1);

		final RowConstraints header = new RowConstraints();
		header.setMinHeight(20);
		getRowConstraints().addAll(new RowConstraints(), new RowConstraints(), header);

		simple = new CheckBox("Use simple editor");
		add(simple, 0, minRow++, 2, 1);
		simple.setSelected(!initialValue.stream().anyMatch(e -> e.getCreatures().size() > 1));
		simple.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue && rows.stream().anyMatch(r -> r.creatures.getValue().size() > 1)) {
				final Alert alert = new Alert(AlertType.WARNING, "You have data entered in the full editor which is not supported in the simple editor. Changing to the simple editor may result in loss of information which cannot be undone. Proceed anyway?", ButtonType.YES, ButtonType.NO);
				alert.setTitle("Simple Editor");
				if (alert.showAndWait().orElse(null) != ButtonType.YES) {
					simple.setSelected(false);
					return;
				}
			}
			rows.forEach(r -> r.creatures.setSimple(newValue));
		});

		add = new Hyperlink("Add Encounter");
		add(add, 0, minRow++, 2, 1);
		add.setOnAction(event -> {
			add.setVisited(false);
			addEncounter(null);
		});

		if (initialValue != null) initialValue.forEach(this::addEncounter);
	}

	protected void addEncounter(final Encounter encounter) {
		final String title = String.format("Encounter %d", rows.size() + 1);
		final EncounterRow row = encounter == null ? new EncounterRow(title, simple.isSelected()) : new EncounterRow(title, encounter, simple.isSelected());
		rows.add(row);
		add(row.creatures, 0, minRow++, 2, 1);
		add(row.weight, 0, minRow++, 2, 1);
		add(row.description, 0, minRow++, 2, 1);
		add(new Text(), 0, minRow++, 2, 1);

		GridPane.setRowIndex(add, minRow + 1);
	}

	@Override
	public Collection<Encounter> getValue() {
		return rows.stream().map(EncounterRow::getValue).collect(Collectors.toList());
	}

	@Override
	public void setValue(final Collection<Encounter> value) {
		throw new IllegalStateException("No.");
	}

	protected static class EncounterRow {
		private final IntegerEditor weight;
		private final CreatureSetCollectionEditor creatures;
		private final StringEditor description;

		protected EncounterRow(final String name, final Collection<CreatureSet> creatures, final int weight, final String description, final boolean simple) {
			this.creatures = new CreatureSetCollectionEditor(name, creatures, false);
			this.weight = new IntegerEditor("Weight", 1, Short.MAX_VALUE, weight);
			this.description = new StringEditor("Details", description);
		}

		protected EncounterRow(final String name, final Encounter encounter, final boolean simple) {
			this(name, encounter.getCreatures(), encounter.weight, encounter.description, simple);
		}

		protected EncounterRow(final String name, final boolean simple) {
			this(name, new ArrayList<>(), 1, "", simple);
		}

		protected EncounterRow(final Collection<CreatureSet> creatures, final int weight, final String description, final boolean simple) {
			this("Creatures", creatures, weight, description, simple);
		}

		protected EncounterRow(final Encounter encounter, final boolean simple) {
			this("Creatures", encounter, simple);
		}

		protected EncounterRow(final boolean simple) {
			this("Creatures", simple);
		}

		protected Encounter getValue() {
			return new Encounter(weight.getValue(), creatures.getValue(), description.getValue());
		}

	}

}
