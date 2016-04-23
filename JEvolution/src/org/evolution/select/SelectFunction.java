package org.evolution.select;

import org.evolution.problem.Problem;
import org.evolution.util.Population;

public abstract class SelectFunction {
	public abstract Population select(Population pop, Problem problem, int count);
}
