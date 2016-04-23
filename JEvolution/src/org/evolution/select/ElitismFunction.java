package org.evolution.select;

import java.util.List;

import org.evolution.solution.Solution;
import org.evolution.util.Population;

public abstract class ElitismFunction {
	public abstract List<Solution> select(Population pop, Integer max);
}
