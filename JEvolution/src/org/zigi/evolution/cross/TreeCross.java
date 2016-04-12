package org.zigi.evolution.cross;

import java.util.List;
import java.util.Random;

import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.solution.TreeSolution;

public class TreeCross extends CrossFunction {

	private static final Random RAND = new Random();

	@Override
	public boolean cross(List<Solution> solutions) {
		TreeSolution sol1 = (TreeSolution) solutions.get(0);
		TreeSolution sol2 = (TreeSolution) solutions.get(1);

		int height1 = sol1.height();
		int height2 = sol2.height();
		if (height1 <= 0 || height2 <= 0)
			return false;

		int cross1 = RAND.nextInt(sol1.size() - 1) + 1;
		int cross2 = RAND.nextInt(sol2.size() - 1) + 1;

		return TreeSolution.changeSubTree(sol1, cross1, sol2, cross2);
	}
}
