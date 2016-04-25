package org.evolution.select;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.evolution.solution.Solution;
import org.evolution.util.Population;

public class SpravedlivyBestElitism extends ElitismFunction {

	private static final Logger LOG = Logger.getLogger(SpravedlivyBestElitism.class);

	private double bestPercentage = 0.5;
	private static final Random RAND = new Random();

	@Override
	public List<Solution> select(Population population, Integer max) {

		Population pop = new Population();
		pop.setMaxSolutions(population.getMaxSolutions());
		for (Solution sol : population.getSolutions())
			if (!pop.getSolutions().contains(sol))
				pop.add(sol);
		pop.sort();

		List<Solution> list = new LinkedList<Solution>();
		long count = Math.round(bestPercentage * max);
		while (list.size() < count) {
			list.add(pop.getSolutions().remove(pop.size() - 1).cloneMe());
		}

		while (list.size() < max) {
			int rnd = RAND.nextInt(pop.size());
			list.add(pop.getSolutions().remove(rnd).cloneMe());
		}
		return list;
	}

}
