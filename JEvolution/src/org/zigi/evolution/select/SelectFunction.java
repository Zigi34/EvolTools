package org.zigi.evolution.select;

import org.zigi.evolution.Population;
import org.zigi.evolution.solution.CloneableValue;
import org.zigi.evolution.solution.Solution;

public abstract class SelectFunction<T extends Solution<U>, U extends CloneableValue<U>> {
	public abstract Population<T, U> select(Population<T, U> population);
}
