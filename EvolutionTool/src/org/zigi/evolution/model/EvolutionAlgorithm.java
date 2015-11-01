package org.zigi.evolution.model;

import org.zigi.evolution.algorithm.EA;
import org.zigi.evolution.controller.MainController;
import org.zigi.evolution.solution.CloneableValue;
import org.zigi.evolution.solution.Solution;

public abstract class EvolutionAlgorithm<T extends Solution<U>, U extends CloneableValue<U>> {
	private MainController controller;
	private EA<T, U> algorithm;

	public EvolutionAlgorithm(MainController controller, EA<T, U> algorithm) {
		this.controller = controller;
		this.algorithm = algorithm;
	}

	public MainController getController() {
		return controller;
	}

	public EA<T, U> getAlgorithm() {
		return algorithm;
	}

	public abstract void initPane();
}
