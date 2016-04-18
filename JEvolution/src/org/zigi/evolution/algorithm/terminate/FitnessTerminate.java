package org.zigi.evolution.algorithm.terminate;

import org.zigi.evolution.algorithm.EvolutionAlgorithm;
import org.zigi.evolution.solution.Solution;

/**
 * Define function for terminate evolution algorithm progress with define max
 * fitness value
 * 
 * @author zigi
 *
 */
public class FitnessTerminate extends TerminateFunction {

	private Double maxFitness;

	public FitnessTerminate() {
		this(Double.MAX_VALUE);
	}

	public FitnessTerminate(Double maxFitness) {
		this.maxFitness = maxFitness;
	}

	@Override
	public boolean isTerminate(EvolutionAlgorithm algorithm) {
		if (algorithm != null) {
			Solution solution = algorithm.getBestSolution();
			if (solution != null && solution.getFunctionValue() != null && solution.getFunctionValue() >= maxFitness)
				return true;
		}
		return false;
	}

}
