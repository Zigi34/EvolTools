package org.zigi.evolution.select;

import java.util.List;

import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.util.Population;

public abstract class ElitismFunction {
	public abstract List<Solution> select(Population pop, Integer max);
}
