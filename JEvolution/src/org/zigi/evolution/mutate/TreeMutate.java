package org.zigi.evolution.mutate;

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
	public boolean mutate(Solution sol) {
		TreeSolution solution = (TreeSolution) sol;
		int rnd = RAND.nextInt(solution.size());

		// odstraneni podstromu od urciteho uzlu
		solution.removeSubTree(solution.getNode(rnd));

		// doplneni chybejici casti stromu nahodnym vygenerovanim
		TreeProblem problem = (TreeProblem) getProblem();
		problem.randomGrowTreeSolution(solution);
		return true;
	}

	@Override
	public String toString() {
		return "Tree mutation";
	}
}
