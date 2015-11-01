package org.zigi.evolution.util;

import java.util.LinkedList;
import java.util.List;

import org.zigi.evolution.Population;
import org.zigi.evolution.algorithm.Algorithm;
import org.zigi.evolution.algorithm.ga.GA;
import org.zigi.evolution.cross.OnePointCross;
import org.zigi.evolution.mutate.SimpleMutate;
import org.zigi.evolution.select.RouleteWheelSelect;
import org.zigi.evolution.solution.DoubleValue;
import org.zigi.evolution.solution.array.ArraySolution;
import org.zigi.evolution.space.DoubleValueSpace;

public class Utils {
	public static Integer populationSize = 20;
	public static Integer solutionSize = 5;
	public static Integer generation = 100;
	public static Double mutateProbability = 0.1;
	public static Double crossProbability = 0.75;

	public static List<Algorithm> createAlgorithmList() {
		List<Algorithm> list = new LinkedList<Algorithm>();

		// GA
		GA<DoubleValue> ga = new GA<DoubleValue>();

		DoubleValueSpace space = new DoubleValueSpace();
		ga.setSpace(space);
		ga.setSelect(new RouleteWheelSelect<DoubleValue>());

		SimpleMutate<DoubleValue> mutate = new SimpleMutate<DoubleValue>();
		mutate.setMutateProbability(mutateProbability);
		ga.setMutate(mutate);

		OnePointCross<DoubleValue> cross = new OnePointCross<DoubleValue>();
		cross.setCrossProbability(crossProbability);
		ga.setCross(cross);

		Population<ArraySolution<DoubleValue>, DoubleValue> population = new Population<ArraySolution<DoubleValue>, DoubleValue>(
				populationSize, solutionSize);
		for (int i = 0; i < populationSize; i++)
			population.add(space.randomSolution(population.getSolutionSize()));

		ga.setPopulation(population);
		ga.setGeneration(generation);

		list.add(ga);
		return list;
	}
}
