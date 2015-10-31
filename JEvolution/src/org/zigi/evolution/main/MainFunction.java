package org.zigi.evolution.main;

import org.apache.log4j.Logger;
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

	public static Logger log = Logger.getLogger(MainFunction.class);

	public static void main(String[] args) {
		int populationSize = 20;
		int solutionSize = 5;
		int generation = 100;
		double mutateProbability = 0.1;
		double crossProbability = 0.75;

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
		mutate.setMutateProbability(mutateProbability);
		alg.setMutate(mutate);

		// set cross operation
		OnePointCross<DoubleValue> cross = new OnePointCross<DoubleValue>();
		cross.setCrossProbability(crossProbability);
		alg.setCross(cross);

		// init population
		Population<ArraySolution<DoubleValue>, DoubleValue> population = new Population<ArraySolution<DoubleValue>, DoubleValue>(
				populationSize, solutionSize);
		for (int i = 0; i < populationSize; i++)
			population.add(space.randomSolution(population.getSolutionSize()));

		// set population
		alg.setPopulation(population);

		// generation
		alg.setGeneration(generation);

		log.debug(alg);
		// set cross operation
		alg.start();
	}

}
