package org.zigi.evolution.mutate;

import org.zigi.evolution.problem.Problem;
import org.zigi.evolution.solution.Solution;

public abstract class MutateFunction {
	private Problem problem;

	public abstract boolean mutate(Solution sol);

	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

}
