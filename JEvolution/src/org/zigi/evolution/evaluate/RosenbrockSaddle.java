package org.zigi.evolution.evaluate;

import org.zigi.evolution.Population;
import org.zigi.evolution.solution.DoubleValue;
import org.zigi.evolution.solution.array.ArraySolution;

public class RosenbrockSaddle extends EvaluateFunction<ArraySolution<DoubleValue>, DoubleValue> {

	@Override
	public Double evaluate(ArraySolution<DoubleValue> solution) {
		Double result = 0.0;
		for (int i = 0; i < solution.size() - 1; i++) {
			result += 100 * Math
					.pow(Math.pow(solution.getValue(i).getValue(), 2.0) - solution.getValue(i + 1).getValue(), 2.0)
					+ Math.pow(1 - solution.getValue(i).getValue(), 2.0);
		}
		if (result == 0.0)
			result = 0.00000000000000000000000000001;
		solution.setFitness(1.0 / result);
		return solution.getFitness();
	}

	@Override
	public void evaluate(Population<ArraySolution<DoubleValue>, DoubleValue> population) {
		for (ArraySolution<DoubleValue> item : population.getSolutions()) {
			Double result = 0.0;
			for (int i = 0; i < item.size() - 1; i++) {
				result += 100
						* Math.pow(Math.pow(item.getValue(i).getValue(), 2.0) - item.getValue(i + 1).getValue(), 2.0)
						+ Math.pow(1 - item.getValue(i).getValue(), 2.0);
			}
			if (result == 0.0)
				result = 0.00000000000000000000000000001;
			item.setFitness(1.0 / result);
		}
	}

	@Override
	public String toString() {
		return "PROBLEM[Rosenbrack Saddle]\n";
	}

}
