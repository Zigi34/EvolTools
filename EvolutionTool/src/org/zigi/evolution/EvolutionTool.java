package org.zigi.evolution;

import org.apache.log4j.Logger;
import org.zigi.evolution.util.Utils;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class EvolutionTool extends Application {

	private static final Logger LOG = Logger.getLogger(EvolutionTool.class);

	// window size
	private static final int WIDTH = 1000;
	private static final int HEIGHT = 600;

	private Stage stage;

	@FXML
	private MenuItem closeMenuItem;

	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		try {
			Parent panel = FXMLLoader.load(getClass().getResource("/gui/EvolutionTool.fxml"));
			Scene init = new Scene(panel, WIDTH, HEIGHT);

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
