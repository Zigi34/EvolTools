package org.zigi.evolution.controller;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.zigi.evolution.algorithm.GeneticProgramming;
import org.zigi.evolution.cross.TreeCross;
import org.zigi.evolution.model.AlgorithmModel;
import org.zigi.evolution.model.ProblemModel;
import org.zigi.evolution.model.SelectFunctionModel;
import org.zigi.evolution.mutate.TreeMutate;
import org.zigi.evolution.services.Services;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class ControlPanel extends AnchorPane {

	private static final Logger LOG = Logger.getLogger(ControlPanel.class);

	@FXML
	private Button stopButton;

	@FXML
	private Button resetButton;

	@FXML
	private Button startButton;

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
	}

	@FXML
	private void startAction(ActionEvent event) {
		LOG.info("Akce spusteni algoritmu");
		try {
			AlgorithmModel alg = Services.algorithmService().getSelected();
			if (alg != null) {
				if (alg.getAlgorithm() instanceof GeneticProgramming) {
					GeneticProgramming gp = (GeneticProgramming) alg.getAlgorithm();
					gp.setPopulationSize(400);
					gp.setGeneration(2000);

					ProblemModel problem = Services.problemService().getSelected();
					if (problem != null)
						gp.setProblem(problem.getProblem());
					else
						LOG.info("Problem nebyl vybrán");

					gp.setCross(new TreeCross());
					gp.setMutate(new TreeMutate());

					SelectFunctionModel select = Services.selectFunctionService().getSelected();
					if (select != null)
						gp.setSelect(select.getFunction());
					else
						LOG.info("Selekce nebyla vybrana");
				}
				alg.start();
			} else {
				LOG.info("Algoritmus nebyl vybran");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e);
		}
	}

	@FXML
	private void stopAction() {
		// stop
	}

	@FXML
	private void resetAction() {
		// reset
	}
}
