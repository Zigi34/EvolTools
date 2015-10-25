package org.zigi.evolution.select;

import java.util.Random;

import org.zigi.evolution.Population;
import org.zigi.evolution.solution.CloneableValue;
import org.zigi.evolution.solution.array.ArraySolution;

public class RouleteWheelSelect<T extends CloneableValue<T>> extends SelectFunction<ArraySolution<T>, T> {

	private Random rand = new Random();
	private Double part = 0.7;

	public Population<ArraySolution<T>, T> select(Population<ArraySolution<T>, T> population) {
		Population<ArraySolution<T>, T> pop = new Population<ArraySolution<T>, T>();
		pop.setMaxSolutions(population.getMaxSolutions());
		pop.setSolutionSize(population.getSolutionSize());

		Double max = 0.0;
		for (ArraySolution<T> sol : population.getSolutions()) {
			max += sol.getFitness();
		}

		int count = (int) (population.size() * part);
		for (int i = 0; i < count; i++) {
			Double randVal = rand.nextDouble() * max;
			Double val = 0.0;
			ArraySolution<T> selected = null;
			for (ArraySolution<T> sol : population.getSolutions()) {
				if (val < randVal)
					val += sol.getFitness();
				else {
					selected = sol;
					break;
				}
			}

			pop.add(selected);
		}
		return pop;
	}

	public Double getPart() {
		return part;
	}

	public void setPart(Double part) {
		this.part = part;
	}
}
