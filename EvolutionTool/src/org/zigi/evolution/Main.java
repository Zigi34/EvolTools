package org.zigi.evolution;

import org.apache.log4j.Logger;
import org.zigi.evolution.controller.MainController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

	private static final Logger log = Logger.getLogger(Main.class);

	private AnchorPane mainPane;
	private MainController mainController;

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("gui/main.fxml"));
		mainPane = (AnchorPane) loader.load();

		mainController = loader.getController();
		if (mainController != null) {
			mainController.setMainApp(this);
			log.info("Controller loaded");
		}

		Scene scene = new Scene(mainPane);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		Application.launch(args);

	}
}
