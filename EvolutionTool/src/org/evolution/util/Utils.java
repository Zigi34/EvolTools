package org.evolution.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class Utils {

	private static final Logger LOG = Logger.getLogger(Utils.class);
	private static Map<Locale, ResourceBundle> RB = new HashMap<Locale, ResourceBundle>();

	public static String getLabel(String key) {
		if (!RB.containsKey(Locale.getDefault())) {
			RB.put(Locale.getDefault(), ResourceBundle.getBundle("labels", Locale.getDefault()));
		}

		return RB.get(Locale.getDefault()).getString(key);
	}

	public static Parent loadView(Class<?> clazz, String path) {
		try {
			return FXMLLoader.load(clazz.getResource(path));
		} catch (IOException e) {
			LOG.error(e);
		}
		return null;
	}

	/**
	 * Nacitani controlleru
	 * 
	 * @param fxml
	 * @return
	 */
	public static Object loadController(Class<?> clazz, String fxml, Object root) {
		FXMLLoader fxmlLoader = new FXMLLoader(clazz.getResource(fxml));
		fxmlLoader.setRoot(root);
		fxmlLoader.setController(root);

		try {
			return fxmlLoader.load();
		} catch (IOException exception) {
			LOG.error(exception);
		}

		return fxmlLoader.getController();
	}

	public static Object getInstanceOfClass(Class<?> clazz, List<?> items) {
		for (Object item : items) {
			if (item.getClass().equals(clazz))
				return item;
		}
		return null;
	}
}
