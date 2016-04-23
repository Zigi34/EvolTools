package org.evolution.cross;

import java.util.Random;

import org.evolution.solution.TreeSolution;
import org.evolution.util.Population;

public class TreeCross extends CrossFunction {

	private static final Random RAND = new Random();

	@Override
	public void cross(Population solutions, long offset, long size) {
		for (int i = (int) offset; i < size; i += 2) {
			TreeSolution sol1 = (TreeSolution) solutions.get(i);
			TreeSolution sol2 = (TreeSolution) solutions.get(i + 1);

			int height1 = sol1.height();
			int height2 = sol2.height();
			if (height1 <= 0 || height2 <= 0)
				return;

			int cross1 = RAND.nextInt(sol1.size());
			int cross2 = RAND.nextInt(sol2.size());

			TreeSolution.changeSubTree(sol1, cross1, sol2, cross2);
		}
	}
}
