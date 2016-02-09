package org.zigi.evolution.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.zigi.evolution.util.Utils;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.stage.Stage;

public class EvolutionToolController implements Initializable {

	private static final Logger LOG = Logger.getLogger(EvolutionToolController.class);

	@FXML
	private Menu applicationMenu;

	@FXML
	private MenuItem closeMenuItem;

	@FXML
	private Menu helpMenu;

	@FXML
	private Tab algorithmTab;

	@FXML
	private Tab summaryTab;

	@FXML
	private Tab problemTab;

	@FXML
	private MenuItem aboutMenuItem;

	@FXML
	Parent root;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// menu
		applicationMenu.setText(Utils.getLabel("application_menu"));
		closeMenuItem.setText(Utils.getLabel("close_menu_item"));
		helpMenu.setText(Utils.getLabel("help_menu"));
		aboutMenuItem.setText(Utils.getLabel("about_menu_item"));

		// tabs
		algorithmTab.setText(Utils.getLabel("algorithm_tab"));
		summaryTab.setText(Utils.getLabel("summary_tab"));
		problemTab.setText(Utils.getLabel("problem_tab"));
	}

	@FXML
	public void closeWindowAction() {
		Stage stage = (Stage) root.getScene().getWindow();
		stage.close();
	}
}
