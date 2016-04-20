package org.zigi.evolution.problem;

import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.solution.value.GPFenotype;
import org.zigi.evolution.util.Population;

public abstract class Problem {
	public boolean isMinProblem = true;
	private double minFunctionValue = 0.0;
	private double maxFunctionValue = 2.0;
	private double epsilon = 0.00001;

	public abstract Solution randomSolution();

	public abstract GPFenotype randomGenotype();

	public abstract void evaluate(Population pop);

	public boolean isMinProblem() {
		return isMinProblem;
	}

	public double getMinFunctionValue() {
		return minFunctionValue;
	}

	public void setMinFunctionValue(double minFunctionValue) {
		this.minFunctionValue = minFunctionValue;
	}

	public double getMaxFunctionValue() {
		return maxFunctionValue;
	}

	public void setMaxFunctionValue(double maxFunctionValue) {
		this.maxFunctionValue = maxFunctionValue;
	}

	public double getNormalizedFitness(Solution bestSolution) {
		double functionValue = bestSolution.getFunctionValue();
		if (isMinProblem()) {
			return (1.0 / (minFunctionValue - maxFunctionValue))
					* ((1.0 - epsilon) * functionValue + minFunctionValue * epsilon - maxFunctionValue);
		} else {
			return 1.0 - (1.0 / (minFunctionValue - maxFunctionValue))
					* ((1.0 - epsilon) * functionValue + minFunctionValue * epsilon - maxFunctionValue);
		}
	}
}
