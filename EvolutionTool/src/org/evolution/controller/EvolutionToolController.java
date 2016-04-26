package org.evolution.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.evolution.EvolutionTool;
import org.evolution.algorithm.EvolutionAlgorithm;
import org.evolution.algorithm.GeneticProgramming;
import org.evolution.model.AlgorithmModel;
import org.evolution.problem.ArtificialAnt;
import org.evolution.problem.RegressionProblem;
import org.evolution.services.Services;
import org.evolution.util.Utils;

import javafx.application.Platform;
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
	private Tab resultTab;

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
	private BorderPane result;

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
		resultTab.setText("Výsledek");

		fitnessChart.setCenter(new FitnessChart());
		problem.setCenter(new ProblemProperty());
		mainContent.setBottom(new FootBar());

		GeneticProgramming alg = (GeneticProgramming) Services.algorithmService().getSelected().getAlgorithm();
		alg.addChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				Object value = evt.getNewValue();
				if (value != null && value.equals(EvolutionAlgorithm.PROBLEM_CHANGED)) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							if (alg.getProblem() instanceof ArtificialAnt) {
								result.setCenter(new ArtificialAntProgress());
							} else if (alg.getProblem() instanceof RegressionProblem) {
								result.setCenter(new SymbolicRegressionProgress());
							}
						}
					});
				}
			}
		});

		// result.setCenter(new ResultChart());
		result.setCenter(new ArtificialAntProgress());

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
