package dmscreen.util;

import javafx.stage.Popup;

public class PopupManager {

	private PopupManager() {}

	private static final Popup popup = new Popup() {

		@Override
		public void hide() {
			super.hide();
			getContent().clear();
			locked = false;
		}

	};
	private static boolean locked = false;

	public static synchronized Popup getPopup() throws IllegalStateException {
		if (locked) throw new IllegalStateException();
		locked = true;
		return popup;
	}

	public static synchronized void releasePopup() {
		popup.hide();
	}

}
