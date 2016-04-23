package org.zigi.evolution.model;

import org.evolution.problem.Problem;

public class ProblemModel {
	private Problem problem;
	private String name;

	public ProblemModel(Problem problem) {
		this.problem = problem;
	}

	public ProblemModel(String name, Problem problem) {
		this.problem = problem;
		this.name = name;
	}

	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	@Override
	public String toString() {
		if (name == null)
			return problem.toString();
		return name;
	}
}
