package org.zigi.evolution.select;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.solution.SolutionComparator;
import org.zigi.evolution.util.Population;

public class RankSelectFunction extends SelectFce {

	private static final Random RAND = new Random();

	@Override
	public List<Solution> select(Population sols, int count) {
		List<Solution> list = new LinkedList<Solution>();

		List<Solution> solutions = new LinkedList<Solution>();
		solutions.addAll(sols.getSolutions());

		Collections.sort(solutions, new SolutionComparator());

		while (list.size() < count) {
			int sum = 0;
			int increment = 1;
			for (Solution sol : solutions) {
				sum += increment;
				increment++;
			}

			int rndIndex = RAND.nextInt(sum);
			increment = 1;
			for (Solution sol : solutions) {
				rndIndex -= increment;
				increment++;
				if (rndIndex <= 0) {
					list.add(sol);
					solutions.remove(sol);
					break;
				}
			}
		}
		return list;
	}
}
