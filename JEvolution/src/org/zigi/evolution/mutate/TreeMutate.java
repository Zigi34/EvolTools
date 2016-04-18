package org.zigi.evolution.mutate;

import java.util.List;
import java.util.Random;

import org.zigi.evolution.problem.TreeProblem;
import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.solution.TreeSolution;

public class TreeMutate extends MutateFunction {

	// private TreeProblem problem;
	private static final Random RAND = new Random();

	public TreeMutate() {
	}

	@Override
	public void mutate(List<Solution> sol, long offset, long size) {
		for (long i = offset; i < offset + size; i++) {
			TreeSolution solution = (TreeSolution) sol.get((int) i);
			int rnd = RAND.nextInt(solution.size());

			// odstraneni podstromu od urciteho uzlu
			solution.removeSubTree(solution.getNode(rnd));

			// doplneni chybejici casti stromu nahodnym vygenerovanim
			TreeProblem problem = (TreeProblem) getProblem();
			problem.randomGrowTreeSolution(solution);
		}
	}

	@Override
	public String toString() {
		return "Tree mutation";
	}
}
