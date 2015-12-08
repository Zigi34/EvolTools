package org.zigi.evolution.cross;

import java.util.List;
import java.util.Random;

import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.solution.TreeSolution;

public class TreeCrossFunction extends CrossFce {

	private static final Random RAND = new Random();

	@Override
	public void cross(List<Solution> solutions) {
		TreeSolution sol1 = (TreeSolution) solutions.get(0);
		TreeSolution sol2 = (TreeSolution) solutions.get(1);

		int cross1 = RAND.nextInt(sol1.size());
		int cross2 = RAND.nextInt(sol2.size());

		TreeSolution.changeSubTree(sol1, cross1, sol2, cross2);
	}
}
