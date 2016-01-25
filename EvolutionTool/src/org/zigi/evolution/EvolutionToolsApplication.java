package org.zigi.evolution;

import org.apache.log4j.Logger;
import org.zigi.evolution.util.Utils;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class EvolutionToolsApplication extends Application {

	private static final Logger LOG = Logger.getLogger(EvolutionToolsApplication.class);

	// window size
	private static final int WIDTH = 1000;
	private static final int HEIGHT = 600;

	@Override
	public void start(Stage stage) throws Exception {
		try {
			Parent pane = Utils.loadView(EvolutionToolsApplication.class, "/gui/evolution-tools.fxml");
			Scene init = new Scene(pane, WIDTH, HEIGHT);

			stage.setScene(init);
			stage.getIcons().add(new Image("file:/resource/img/ico.ico"));
			stage.setTitle(Utils.getLabel("window_title"));
			stage.show();
		} catch (Exception e) {
			LOG.error(e);
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
