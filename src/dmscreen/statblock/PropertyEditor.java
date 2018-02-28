package dmscreen.statblock;

import java.util.Map;

import javafx.scene.Node;

public abstract class PropertyEditor<T> {

	private final String name;

	public PropertyEditor(final String name) {
		this.name = name;
	}

	public abstract T getValue();

	public abstract Map<Node, Constraints> getNodes();

	public final String getName() {
		return name;
	}

	public final int getWidth() {
		return getNodes().isEmpty() ? 0 : getNodes().values().stream().mapToInt(c -> c.x + c.width).max().getAsInt();
	}

	public final int getHeight() {
		return getNodes().isEmpty() ? 0 : getNodes().values().stream().mapToInt(c -> c.y + c.height).max().getAsInt();
	}

	public static class Constraints {
		public final int x, y, width, height;

		public Constraints(final int x, final int y, final int width, final int height) {
			if (x < 0 || y < 0 || width < 1 || height < 1) throw new IllegalArgumentException();

			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}

		public Constraints(final int x, final int y) {
			this(x, y, 1, 1);
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}

	}

}
