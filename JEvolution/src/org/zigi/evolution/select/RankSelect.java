package org.zigi.evolution.select;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.solution.SolutionComparator;
import org.zigi.evolution.util.Population;

/**
 * Selekce hodnot na základě pořadí fitness hodnot.
 * 
 * @author Zdeněk Gold
 *
 */
public class RankSelect extends SelectFunction {

	private static final Random RAND = new Random();

	@Override
	public List<Solution> select(Population sols, int count) {
		int[] ranks = new int[sols.size()];

		List<Solution> list = new LinkedList<Solution>();

		List<Solution> solutions = new LinkedList<Solution>();
		solutions.addAll(sols.getSolutions());

		Collections.sort(solutions, new SolutionComparator());

		int sum = 0;
		int rank = 0;
		double lastValue = Double.MIN_VALUE;
		for (int i = 0; i < solutions.size(); i++) {
			Solution sol = solutions.get(i);
			if (sol.getFitness() != lastValue) {
				rank++;
				lastValue = sol.getFitness();
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
					list.add(sol);
					sum -= ranks[i];
					ranks[i] = 0;
					break;
				}
			}
		}
		return list;
	}

	@Override
	public String toString() {
		return "Rank Select";
	}

	@Override
	public List<Solution> select(Population pop, int count, double maxValue) {
		// TODO Auto-generated method stub
		return null;
	}
}
