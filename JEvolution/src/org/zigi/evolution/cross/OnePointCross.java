package org.zigi.evolution.cross;

import java.util.Random;

import org.apache.log4j.Logger;
import org.zigi.evolution.Population;
import org.zigi.evolution.exception.CrossException;
import org.zigi.evolution.solution.CloneableValue;
import org.zigi.evolution.solution.array.ArraySolution;

public class OnePointCross<U extends CloneableValue<U>> extends CrossFunction<ArraySolution<U>, U> {

	private Random rand = new Random();
	private Double crossProbability = 1.0;
	private static Logger log = Logger.getLogger(OnePointCross.class);

	@Override
	public void cross(Population<ArraySolution<U>, U> population) throws CrossException {
		if (population.size() % 2 != 0)
			throw new CrossException("Must be odd");

		for (int i = 0; i < population.size(); i += 2) {
			if (rand.nextDouble() >= crossProbability) {
				continue;
			}

			if (i + 1 >= population.size())
				continue;

			ArraySolution<U> solution1 = population.getSolution(i);
			ArraySolution<U> solution2 = population.getSolution(i + 1);

			int crossIndex = rand.nextInt(solution1.size() - 2) + 1;
			U tempValue = null;

			double randValue = rand.nextDouble();
			if (randValue <= 0.5) {
				for (int j = 0; j < crossIndex; j++) {
					tempValue = solution1.getChildNode(j);
					solution1.setChildNode(j, solution2.getChildNode(j));
					solution2.setChildNode(j, tempValue);
				}
			} else {
				for (int j = crossIndex; j < population.getSolutionSize(); j++) {
					tempValue = solution1.getChildNode(j);
					solution1.setChildNode(j, solution2.getChildNode(j));
					solution2.setChildNode(j, tempValue);
				}
			}
		}
	}

	public Double getCrossProbability() {
		return crossProbability;
	}

	public void setCrossProbability(Double crossProbability) {
		this.crossProbability = crossProbability;
	}

	@Override
	public String toString() {
		return String.format("CROSS[One Point Cross] (prob: %.3f)\n", getCrossProbability());
	}

}
