package org.zigi.evolution.select;

import java.util.List;

import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.util.Population;

public abstract class SelectFunction {
	public abstract List<Solution> select(Population pop, int count);
}
