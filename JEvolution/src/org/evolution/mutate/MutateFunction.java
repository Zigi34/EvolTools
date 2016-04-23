package org.evolution.mutate;

import java.util.List;

import org.evolution.problem.Problem;
import org.evolution.solution.Solution;

public abstract class MutateFunction {
	private Problem problem;

	public abstract void mutate(List<Solution> sol, long offset, long size);

	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

}
