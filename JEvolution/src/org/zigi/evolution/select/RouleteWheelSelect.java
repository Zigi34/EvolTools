package org.zigi.evolution.select;

import java.security.SecureRandom;

import org.apache.log4j.Logger;
import org.zigi.evolution.Population;
import org.zigi.evolution.exception.SelectException;
import org.zigi.evolution.solution.CloneableValue;
import org.zigi.evolution.solution.array.ArraySolution;

public class RouleteWheelSelect<T extends CloneableValue<T>> extends SelectFunction<ArraySolution<T>, T> {

	private SecureRandom rand = new SecureRandom();
	private Double prob = 0.7;
	private static Logger log = Logger.getLogger(RouleteWheelSelect.class);

	public Population<ArraySolution<T>, T> select(Population<ArraySolution<T>, T> population) throws SelectException {
		if (population.size() < 2)
			throw new SelectException("Population must be equal or greater than 2");

		Population<ArraySolution<T>, T> pop = new Population<ArraySolution<T>, T>(population);
		pop.setSolutionSize(population.getSolutionSize());

		Double max = 0.0;
		for (ArraySolution<T> sol : population.getSolutions()) {
			max += sol.getFitness();
		}

		int count = (int) (population.size() * prob);
		if (count % 2 != 0) {
			if (count < population.size())
				count++;
			else
				count--;
		}

		for (int i = 0; i < count; i++) {
			Double randVal = rand.nextDouble() * max;

			Double val = 0.0;
			ArraySolution<T> selected = null;
			for (ArraySolution<T> sol : population.getSolutions()) {
				val += sol.getFitness();
				if (val > randVal) {
					selected = sol;
					break;
				}
			}
			pop.add(selected.clone());
		}
		return pop;
	}

	public Double getProbability() {
		return prob;
	}

	public void setProbability(Double probability) {
		this.prob = probability;
	}

	@Override
	public String toString() {
		return String.format("SELECT [Roulete Wheel] (prob: %.3f)\n", getProbability());
	}
}
