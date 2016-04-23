package org.zigi.evolution.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.zigi.evolution.EvolutionTool;
import org.zigi.evolution.model.AlgorithmModel;
import org.zigi.evolution.services.Services;
import org.zigi.evolution.util.Utils;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

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
	private BorderPane mainContent;

	@FXML
	private BorderPane algContent;

	@FXML
	private BorderPane fitnessChart;

	@FXML
	private BorderPane problem;

	@FXML
	private AnchorPane footBar;

	@FXML
	private AnchorPane controlPane;

	@FXML
	Parent root;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// menu
		applicationMenu.setText(Utils.getLabel("application_menu"));
		closeMenuItem.setText(Utils.getLabel("close_menu_item"));
		helpMenu.setText(Utils.getLabel("help_menu"));
		aboutMenuItem.setText(Utils.getLabel("about_menu_item"));

		algorithmTab.setText(Utils.getLabel("algorithm_tab"));
		summaryTab.setText(Utils.getLabel("summary_tab"));
		problemTab.setText(Utils.getLabel("problem_tab"));

		fitnessChart.setCenter(new FitnessChart());
		problem.setCenter(new ProblemProperty());
		mainContent.setBottom(new FootBar());

		// set inner algorithm
		algContent.setLeft(new AlgorithmProperty());
		algContent.setCenter(new ControlPanel());

	}

	@FXML
	public void closeWindowAction() {
		// stopping algorithm
		AlgorithmModel alg = Services.algorithmService().getSelected();
		alg.stop();

		EvolutionTool.stage.close();
	}
}
