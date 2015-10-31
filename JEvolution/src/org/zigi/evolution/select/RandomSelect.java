package org.zigi.evolution.select;

import java.util.Random;

import org.zigi.evolution.Population;
import org.zigi.evolution.solution.CloneableValue;
import org.zigi.evolution.solution.Solution;

public class RandomSelect<T extends CloneableValue<T>> extends SelectFunction<Solution<T>, T> {

	private Random rand = new Random();
	private Double prob = 0.7;

	public Population<Solution<T>, T> select(Population<Solution<T>, T> population) {
		Population<Solution<T>, T> pop = new Population<Solution<T>, T>(population);
		pop.setSolutionSize(population.getSolutionSize());
		for (Solution<T> solution : population.getSolutions()) {
			if (rand.nextDouble() > prob)
				continue;
			pop.add(solution);
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
		return String.format("SELECT [Random Select] (prob: %.3f)", prob);
	}
}
