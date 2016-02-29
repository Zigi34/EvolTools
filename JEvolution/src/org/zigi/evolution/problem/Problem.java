package org.zigi.evolution.problem;

import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.solution.value.GPFenotype;
import org.zigi.evolution.util.Population;

public abstract class Problem {
	public abstract Solution randomSolution();

	public abstract GPFenotype randomGenotype();

	public abstract Double evaluate(Solution sol);

	public abstract void evaluate(Population pop);
}
