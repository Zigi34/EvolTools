package org.zigi.evolution;

import org.apache.log4j.Logger;
import org.zigi.evolution.controller.AlgorithmController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Algorithm extends Application {

	private static final Logger log = Logger.getLogger(Algorithm.class);

	private AnchorPane pane;
	private AlgorithmController controller;

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("gui/algorithm.fxml"));
		pane = (AnchorPane) loader.load();

		controller = loader.getController();
		if (controller != null) {
			controller.setWindow(this);
		}

		Scene scene = new Scene(pane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		Application.launch(args);

	}

	public static FXMLLoader createLoader(String path) {
		return new FXMLLoader(Algorithm.class.getResource(path));
	}
}
