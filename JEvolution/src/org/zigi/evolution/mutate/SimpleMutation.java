package org.zigi.evolution.mutate;

import java.util.Random;

import org.zigi.evolution.problem.Problem;
import org.zigi.evolution.solution.Solution;

public class SimpleMutation extends MutateFce {

	private Random ran = new Random();
	private Problem problem;

	public SimpleMutation(Problem problem) {
		this.problem = problem;
	}

	@Override
	public void mutate(Solution sol) {
		Integer randIdx = ran.nextInt(sol.size());
		sol.setGenotype(randIdx, problem.randomGenotype());
	}

	public Problem getSpace() {
		return problem;
	}

	public void setSpace(Problem space) {
		this.problem = space;
	}
}
