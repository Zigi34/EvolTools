package org.zigi.evolution.evaluate;

import org.zigi.evolution.Population;
import org.zigi.evolution.solution.CloneableValue;
import org.zigi.evolution.solution.Solution;

public abstract class EvaluateFunction<T extends Solution<U>, U extends CloneableValue<U>> {
	public abstract Double evaluate(T solution);

	public abstract void evaluate(Population<T, U> population);
}
