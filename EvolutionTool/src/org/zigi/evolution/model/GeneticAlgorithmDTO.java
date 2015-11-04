package org.zigi.evolution.model;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.zigi.evolution.Algorithm;
import org.zigi.evolution.algorithm.EA;
import org.zigi.evolution.controller.AlgorithmController;
import org.zigi.evolution.controller.GeneticAlgorithmController;
import org.zigi.evolution.solution.DoubleValue;
import org.zigi.evolution.solution.array.ArraySolution;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class GeneticAlgorithmDTO extends AlgorithmDTO<ArraySolution<DoubleValue>, DoubleValue> {

	private static Logger log = Logger.getLogger(GeneticAlgorithmDTO.class);

	private GeneticAlgorithmController controller;

	public GeneticAlgorithmDTO(AlgorithmController controller, EA<ArraySolution<DoubleValue>, DoubleValue> algorithm,
			String guiLocation) {
		super(algorithm, controller, guiLocation);
	}

	public GeneticAlgorithmController getGAController() {
		return controller;
	}

	@Override
	public void initSettingPane(AnchorPane pane) {
		try {
			FXMLLoader loader = Algorithm.createLoader("panel/" + getGuiLocation() + ".fxml");
			AnchorPane ap = (AnchorPane) loader.load();

			controller = loader.getController();
			controller.setWindow(getController().getWindow());
			controller.setAlgorithmDTO(this);
			controller.init();

			AnchorPane p = getController().getSettingPane();
			p.getChildren().add(ap);

		} catch (IOException e) {
			log.error(e);
		}
	}

	@Override
	public String toString() {
		return "GA";
	}

}
