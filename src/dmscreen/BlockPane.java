package dmscreen;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import dmscreen.data.base.DataSet;
import dmscreen.statblock.StatBlock;
import dmscreen.statblock.StatBlockEditor;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class BlockPane<T> extends StackPane {

	private T info;
	public final DataSet source;

	private final StackPane blockPane;
	private final Button editButton;
	private final BorderPane editPane;
	private final ScrollPane editScroll;
	private boolean isEditing = false;
	private final ScrollPane scroll;
	private StatBlockEditor<? extends T> currentEditor;

	private final ObjectProperty<ChangeListener<T>> onEditSavedProperty = new SimpleObjectProperty<>(null);

	private final DoubleProperty animationProgress = new SimpleDoubleProperty(0);

	public BlockPane(final T info, final DataSet source) {
		this.info = info;
		this.source = source;

		blockPane = new StackPane();
		blockPane.setPadding(new Insets(8));
		scroll = new ScrollPane(blockPane);
		scroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		scroll.setFitToWidth(true);

		scroll.translateXProperty().bind(widthProperty().multiply(animationProgress.negate()));

		getChildren().add(scroll);

		if (StatBlockEditor.isEditable(info)) {
			editButton = new Button("Edit");
			editButton.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
			editButton.setOnAction(event -> setEditing(true));

			editScroll = new ScrollPane();
			editScroll.setHbarPolicy(ScrollBarPolicy.NEVER);
			editScroll.setFitToWidth(true);
			editPane = new BorderPane(editScroll);
			editPane.setBottom(createEditButtonsPane());

			editPane.translateXProperty().bind(widthProperty().multiply(animationProgress.negate().add(1)));

			getChildren().addAll(editPane);
		} else {
			editButton = null;
			editScroll = null;
			editPane = null;
		}

		update();
	}

	private HBox createEditButtonsPane() {
		final HBox buttonPane = new HBox(8);
		buttonPane.setPadding(new Insets(8));

		final Button save = new Button("Save");
		save.setOnAction(saveEvent -> {
			try {
				for (final Field field : DataSet.class.getFields()) {
					System.out.println(field.getName());
					if (field.getGenericType() instanceof ParameterizedType) {
						Type t = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
						if (t instanceof ParameterizedType) t = ((ParameterizedType) t).getRawType();

						System.out.println(t + ", " + info.getClass());

						if (((Class<?>) t).isAssignableFrom(info.getClass())) {
							final T originalValue = currentEditor.getOriginalValue();
							field.getType().getMethod("remove", Object.class).invoke(field.get(source), originalValue);
							info = currentEditor.getNewValue();
							field.getType().getMethod("add", Object.class).invoke(field.get(source), info);
							blockPane.getChildren().set(0, StatBlock.getStatBlock(info));

							onEditSavedProperty.get().changed(null, originalValue, info);
						}
					}
				}

			} catch (final NullPointerException | ClassCastException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			setEditing(false);
		});
		buttonPane.getChildren().add(save);

		final Button cancel = new Button("Cancel");
		cancel.setOnAction(cancelEvent -> {
			setEditing(false);
		});
		buttonPane.getChildren().add(cancel);

		return buttonPane;
	}

	private boolean setEditing(final boolean editing) {
		if (this.isEditing != editing) {
			this.isEditing = editing;
			if (this.isEditing) {
				if (!StatBlockEditor.isEditable(info)) {
					this.isEditing = false;
					return false;
				}

				final Transition openPane = new Transition() {
					{
						setCycleDuration(Duration.millis(300));
						setInterpolator(Interpolator.EASE_BOTH);
					}

					@Override
					protected void interpolate(final double pct) {
						animationProgress.set(pct);
					}
				};
				openPane.play();

				currentEditor = StatBlockEditor.getEditor(info);
				editScroll.setContent(currentEditor);
			} else {
				final Transition closePane = new Transition() {
					{
						setCycleDuration(Duration.millis(300));
						setInterpolator(Interpolator.EASE_BOTH);
						setOnFinished(event -> {
							editScroll.setContent(null);
						});
					}

					@Override
					protected void interpolate(final double pct) {
						animationProgress.set(1 - pct);
					}
				};
				closePane.play();
			}
		}

		return this.isEditing;
	}

	private void update() {
		blockPane.getChildren().clear();
		final Pane statBlock = StatBlock.getStatBlock(info);
		if (StatBlockEditor.isEditable(info)) {
			final Node topNode = statBlock.getChildren().isEmpty() ? new Pane() : statBlock.getChildren().remove(0);
			final HBox top = new HBox(8, topNode, editButton);
			HBox.setHgrow(topNode, Priority.ALWAYS);
			statBlock.getChildren().add(0, top);
		}
		blockPane.getChildren().add(statBlock);
	}

	public T getInfo() {
		return info;
	}

	public ObjectProperty<ChangeListener<T>> onEditSavedProperty() {
		return onEditSavedProperty;
	}

	public void setOnEditSaved(final ChangeListener<T> onEditSaved) {
		this.onEditSavedProperty.set(onEditSaved);
	}

	public ChangeListener<T> getOnEditSaved() {
		return onEditSavedProperty.get();
	}

}
