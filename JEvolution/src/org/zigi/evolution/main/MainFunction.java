package org.zigi.evolution.main;

import org.zigi.evolution.Population;
import org.zigi.evolution.algorithm.ga.GA;
import org.zigi.evolution.cross.OnePointCross;
import org.zigi.evolution.evaluate.DeJong1;
import org.zigi.evolution.mutate.SimpleMutate;
import org.zigi.evolution.select.RouleteWheelSelect;
import org.zigi.evolution.solution.DoubleValue;
import org.zigi.evolution.solution.array.ArraySolution;
import org.zigi.evolution.space.DoubleValueSpace;

public class MainFunction {

	public static void main(String[] args) {
		GA<DoubleValue> alg = new GA<DoubleValue>();
		// Define problem
		alg.setFunction(new DeJong1());

		// set space
		DoubleValueSpace space = new DoubleValueSpace();
		alg.setSpace(space);

		// set select
		alg.setSelect(new RouleteWheelSelect<DoubleValue>());

		// set mutate operation
		SimpleMutate<DoubleValue> mutate = new SimpleMutate<DoubleValue>();
		mutate.setMutateProbability(0.1);
		alg.setMutate(mutate);

		// set cross operation
		alg.setCross(new OnePointCross<DoubleValue>());

		// init population
		Population<ArraySolution<DoubleValue>, DoubleValue> population = new Population<ArraySolution<DoubleValue>, DoubleValue>();
		population.setSolutionSize(3);
		population.setMaxSolutions(4);
		for (int i = 0; i < 6; i++)
			population.add(space.randomSolution(population.getSolutionSize()));

		// set population
		alg.setPopulation(population);

		// set cross operation
		alg.start();
	}

}
