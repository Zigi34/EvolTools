package org.evolution.select;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.evolution.problem.Problem;
import org.evolution.solution.Solution;
import org.evolution.solution.SolutionComparator;
import org.evolution.util.Population;

/**
 * Selekce hodnot na základě pořadí fitness hodnot.
 * 
 * @author Zdeněk Gold
 *
 */
public class RankSelect extends SelectFunction {

	private static final Random RAND = new Random();

	@Override
	public Population select(Population pop, Problem problem, int count) {
		int[] ranks = new int[pop.size()];

		List<Solution> list = new LinkedList<Solution>();

		List<Solution> solutions = new LinkedList<Solution>();
		solutions.addAll(pop.getSolutions());

		Collections.sort(solutions, new SolutionComparator(problem.isMinProblem()));

		int sum = 0;
		int rank = 0;
		double lastValue = Double.MIN_VALUE;
		for (int i = 0; i < solutions.size(); i++) {
			Solution sol = solutions.get(i);
			if (sol.getFunctionValue() != lastValue) {
				rank++;
				lastValue = sol.getFunctionValue();
			}
			sum += rank;
			ranks[i] = rank;
		}

		while (list.size() < count) {
			int rnd = RAND.nextInt(sum);
			for (int i = 0; i < solutions.size(); i++) {
				Solution sol = solutions.get(i);
				rnd -= ranks[i];
				if (rnd <= 0) {
					list.add(sol.cloneMe());
					break;
				}
			}
		}

		Population result = new Population();
		result.setMax(pop.getMaxSolutions());
		result.setSolutions(list);
		return result;
	}

	@Override
	public String toString() {
		return "Rank Select";
	}
}
