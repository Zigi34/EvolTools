package org.zigi.evolution.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.zigi.evolution.algorithm.EvolutionAlgorithm;
import org.zigi.evolution.algorithm.GeneticProgramming;
import org.zigi.evolution.model.AlgorithmModel;
import org.zigi.evolution.model.ProblemModel;
import org.zigi.evolution.model.SelectFunctionModel;
import org.zigi.evolution.services.AlgorithmService;
import org.zigi.evolution.services.PopulationService;
import org.zigi.evolution.services.ProblemService;
import org.zigi.evolution.services.SelectFunctionService;
import org.zigi.evolution.solution.Solution;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

/**
 * Algorithm control manager
 * 
 * @author zigi
 *
 */
public class ControlPanel extends AnchorPane {

	private static final Logger LOG = Logger.getLogger(ControlPanel.class);

	@FXML
	private Button stopButton;

	@FXML
	private Button resetButton;

	@FXML
	private Button startButton;

	@FXML
	private TextArea reportArea;

	public ControlPanel() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/ControlPanel.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}

		initialize();
	}

	private void initialize() {
		stopButton.setText("Stop");
		resetButton.setText("Reset");
		startButton.setText("Start");

		AlgorithmModel model = AlgorithmService.getSelected();
		if (model != null) {
			GeneticProgramming alg = (GeneticProgramming) model.getAlgorithm();
			alg.addChangeListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					if (evt.getNewValue().equals(EvolutionAlgorithm.ALGORITHM_STARTED)) {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								reportArea.clear();
								reportArea.appendText("START");
							}
						});
					} else if (evt.getNewValue().equals(EvolutionAlgorithm.ALGORITHM_TERMINATED)) {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								reportArea.appendText("END");
							}
						});
					} else if (evt.getNewValue().equals(GeneticProgramming.NEW_POPULATION) && alg.getActualGeneration() % 25 == 0) {
						int generation = alg.getActualGeneration();
						Solution best = alg.getBestSolution();
						Double fitness = alg.getProblem().getNormalizedFitness(best);
						String text = String.format("GENERATION: %s, FITNESS[%.3f], BEST SOLUTION: %s", generation, fitness, best);
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								reportArea.appendText(text);
							}
						});
					}
				}
			});
		}
	}

	@FXML
	private void startAction(ActionEvent event) {
		AlgorithmModel alg = AlgorithmService.getSelected();
		initializeAlgorithm(alg);
		if (alg != null) {
			alg.start();
		}
	}

	@FXML
	private void stopAction() {
		// pause algorithm
		AlgorithmModel alg = AlgorithmService.getSelected();
		if (alg != null) {
			alg.stop();
		}
	}

	@FXML
	private void resetAction() {
		AlgorithmModel alg = AlgorithmService.getSelected();
		if (alg != null) {
			alg.reset();
		}
	}

	private void initializeAlgorithm(AlgorithmModel alg) {
		if (alg.getAlgorithm() instanceof GeneticProgramming) {
			GeneticProgramming gp = (GeneticProgramming) alg.getAlgorithm();

			ProblemModel problem = ProblemService.getSelected();
			if (problem != null)
				gp.setProblem(problem.getProblem());
			else
				LOG.info("Problem nebyl vybrán");

			SelectFunctionModel select = SelectFunctionService.getSelected();
			if (select != null)
				gp.setSelect(select.getFunction());
			else
				LOG.info("Selekce nebyla vybrana");
		}

		// set population
		alg.getAlgorithm().setPopulation(PopulationService.getSelected().getPopulation());
	}
}
