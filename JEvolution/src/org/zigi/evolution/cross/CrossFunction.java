package org.zigi.evolution.cross;

import org.zigi.evolution.Population;
import org.zigi.evolution.exception.CrossException;
import org.zigi.evolution.solution.CloneableValue;
import org.zigi.evolution.solution.Solution;

public abstract class CrossFunction<T extends Solution<U>, U extends CloneableValue<U>> {
	public abstract void cross(Population<T, U> population) throws CrossException;
}
