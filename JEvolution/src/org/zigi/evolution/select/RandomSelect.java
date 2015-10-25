package org.zigi.evolution.select;

import java.util.Random;

import org.zigi.evolution.Population;
import org.zigi.evolution.solution.CloneableValue;
import org.zigi.evolution.solution.Solution;

public class RandomSelect<T extends CloneableValue<T>> extends SelectFunction<Solution<T>, T> {

	private Random rand = new Random();
	private Double part = 0.7;

	public Population<Solution<T>, T> select(Population<Solution<T>, T> population) {
		Population<Solution<T>, T> pop = new Population<Solution<T>, T>();
		pop.setMaxSolutions(population.getMaxSolutions());
		pop.setSolutionSize(population.getSolutionSize());
		for (Solution<T> solution : population.getSolutions()) {
			if (rand.nextDouble() > part)
				continue;
			pop.add(solution);
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
