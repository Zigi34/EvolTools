package org.zigi.evolution.model;

import org.zigi.evolution.algorithm.EA;
import org.zigi.evolution.controller.AlgorithmController;
import org.zigi.evolution.solution.CloneableValue;
import org.zigi.evolution.solution.Solution;

import javafx.scene.layout.AnchorPane;

public abstract class AlgorithmDTO<T extends Solution<U>, U extends CloneableValue<U>> {
	private EA<T, U> algorithm;
	private String guiLocation;
	private AlgorithmController controller;

	public AlgorithmDTO(EA<T, U> algorithm, AlgorithmController controller, String guiName) {
		this.algorithm = algorithm;
		this.guiLocation = guiName;
		this.controller = controller;
	}

	public AlgorithmController getController() {
		return this.controller;
	}

	public String getGuiLocation() {
		return guiLocation;
	}

	public EA<T, U> getAlgorithm() {
		return algorithm;
	}

	public abstract void initSettingPane(AnchorPane pane);
}
