package dmscreen.statblock;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

public class Accordion extends VBox {

	private final ObservableList<TitledPane> panes = FXCollections.observableArrayList();

	private final ChangeListener<Boolean> expandedListener = (observable, oldValue, newValue) -> {
		if (newValue) {
			panes.forEach(p -> {
				if (p.expandedProperty() != observable) p.setExpanded(false);
			});
		}
	};

	public Accordion() {
		this((TitledPane[]) null);
	}

	public Accordion(final TitledPane... panes) {
		this.panes.addListener((ListChangeListener<TitledPane>) c -> {
			while (c.next()) {
				c.getAddedSubList().forEach(p -> {
					p.setExpanded(false);
					p.expandedProperty().addListener(expandedListener);
				});
				c.getRemoved().forEach(p -> p.expandedProperty().removeListener(expandedListener));
			}
			getChildren().setAll(this.panes);
		});

		if (panes != null) {
			for (final TitledPane p : panes) {
				this.panes.add(p);
			}
		}
	}

	public ObservableList<TitledPane> getPanes() {
		return panes;
	}

}
