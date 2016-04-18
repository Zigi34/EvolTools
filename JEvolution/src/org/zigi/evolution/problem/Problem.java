package org.zigi.evolution.problem;

import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.solution.value.GPFenotype;
import org.zigi.evolution.util.Population;

public abstract class Problem {
	public boolean isMinProblem = true;

	public abstract Solution randomSolution();

	public abstract GPFenotype randomGenotype();

	public abstract void evaluate(Population pop);

	public boolean isMinProblem() {
		return isMinProblem;
	}

}
