package org.zigi.evolution.cross;

import java.util.Random;

import org.zigi.evolution.Population;
import org.zigi.evolution.solution.CloneableValue;
import org.zigi.evolution.solution.array.ArraySolution;

public class OnePointCross<U extends CloneableValue<U>> extends CrossFunction<ArraySolution<U>, U> {

	private Random rand = new Random();
	private Double crossProbability = 0.75;

	@Override
	public void cross(Population<ArraySolution<U>, U> population) {
		for (int i = 0; i < population.size(); i += 2) {
			if (rand.nextDouble() >= crossProbability) {
				continue;
			}

			if (i + 1 >= population.size())
				continue;

			ArraySolution<U> solution1 = population.getSolution(i);
			ArraySolution<U> solution2 = population.getSolution(i + 1);

			int crossIndex = rand.nextInt(solution1.size());
			U tempValue = null;

			for (int j = 0; j < crossIndex; j++) {
				tempValue = solution1.getValue(j);
				solution1.setValue(j, solution2.getValue(j));
				solution2.setValue(j, tempValue);
			}

			for (int j = crossIndex; j < population.getSolutionSize(); j++) {
				tempValue = solution1.getValue(j);
				solution1.setValue(j, solution2.getValue(j));
				solution2.setValue(j, tempValue);
			}

		}
	}
}
