package org.zigi.evolution.model;

import org.apache.log4j.Logger;
import org.evolution.algorithm.EvolutionAlgorithm;

public class AlgorithmModel {

	private static final Logger LOG = Logger.getLogger(AlgorithmModel.class);
	private EvolutionAlgorithm algorithm;

	public AlgorithmModel(EvolutionAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	public EvolutionAlgorithm getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(EvolutionAlgorithm alg) {
		this.algorithm = alg;
	}

	public void start() {
		try {
			algorithm.start();
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e);

		}
	}

	public void stop() {
		algorithm.stop();
	}

	public void reset() {
		algorithm.stop();

	}
}
