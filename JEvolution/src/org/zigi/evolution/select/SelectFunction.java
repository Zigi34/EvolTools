package org.zigi.evolution.select;

import org.zigi.evolution.problem.Problem;
import org.zigi.evolution.util.Population;

public abstract class SelectFunction {
	public abstract Population select(Population pop, Problem problem, int count);
}
