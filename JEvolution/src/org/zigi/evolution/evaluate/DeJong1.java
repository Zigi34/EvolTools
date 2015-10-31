package org.zigi.evolution.evaluate;

import org.zigi.evolution.Population;
import org.zigi.evolution.solution.DoubleValue;
import org.zigi.evolution.solution.array.ArraySolution;

public class DeJong1 extends EvaluateFunction<ArraySolution<DoubleValue>, DoubleValue> {

	@Override
	public Double evaluate(ArraySolution<DoubleValue> solution) {
		Double result = 0.0;
		for (DoubleValue val : solution.getValues()) {
			result += Math.pow(val.getValue(), 2.0);
		}
		if (result == 0.0)
			result = 0.00000000000000000000001;
		solution.setFitness(1.0 / result);
		return solution.getFitness();
	}

	@Override
	public void evaluate(Population<ArraySolution<DoubleValue>, DoubleValue> population) {
		for (ArraySolution<DoubleValue> sol : population.getSolutions()) {
			Double result = 0.0;
			for (DoubleValue val : sol.getValues()) {
				result += Math.pow(val.getValue(), 2.0);
			}
			if (result == 0.0)
				result = 0.00000000000000000000001;
			sol.setFitness(1.0 / result);
		}
	}

	@Override
	public String toString() {
		return "PROBLEM[De Jong]\n";
	}
}
