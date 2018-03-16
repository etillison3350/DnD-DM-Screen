package dmscreen.statblock;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import dmscreen.data.base.BlockEntry;
import dmscreen.data.creature.feature.template.Template;
import dmscreen.data.creature.feature.template.TemplateField;
import dmscreen.statblock.editor.Editor;

public class TemplateEditor<T> extends VBox {

	private final Template<T> template;
	private final Map<String, Editor<?>> editors = new LinkedHashMap<>();

	public TemplateEditor(final Template<T> template, final boolean includePreview) {
		super(2);
		this.template = template;

		final ObservableList<Node> children = getChildren();

		template.getFields().forEach(f -> {
			final Editor<?> editor = TemplateField.getEditor(f);
			editors.put(f.name, editor);
		});

		if (includePreview) {
			children.add(StatBlock.smallCaps("Preview", "header-small"));
			children.add(new Separator());

			final StackPane preview = new StackPane();
			preview.setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
			preview.setPadding(new Insets(4));
			children.add(preview);

			final Button update = new Button("Update");
			update.setOnAction(event -> {
				preview.getChildren().clear();

				try {
					final Object result = getValue();
					if (result instanceof BlockEntry) {
						preview.getChildren().add(((BlockEntry) result).getNode());
					} else {
						preview.getChildren().add(StatBlock.getStatBlock(result));
					}
				} catch (final Exception e) {
					e.printStackTrace();
					preview.getChildren().add(StatBlock.dataLine("[ERROR]", null));
				}
			});
			children.add(update);
			update.fire();
		}

		editors.forEach((s, editor) -> {
			children.add(editor);
		});
	}

	public T getValue() {
		final Map<String, Object> values = new HashMap<>();
		editors.forEach((name, editor) -> {
			values.put(name, editor.getValue());
		});
		return template.make(values);
	}
}
