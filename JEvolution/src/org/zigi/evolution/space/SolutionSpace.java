package org.zigi.evolution.space;

import org.zigi.evolution.solution.CloneableValue;
import org.zigi.evolution.solution.Solution;

public abstract class SolutionSpace<T extends Solution<U>, U extends CloneableValue<U>> {
	private T classType;

	public abstract T randomSolution(int size);

	public abstract U randomValue();

	public T getType() {
		return classType;
	}
}
