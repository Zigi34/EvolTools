package org.zigi.evolution.mutate;

import java.util.List;

import org.zigi.evolution.Population;
import org.zigi.evolution.solution.CloneableValue;
import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.space.SolutionSpace;

public abstract class MutateFunction<T extends Solution<U>, U extends CloneableValue<U>> {
	protected SolutionSpace<T, U> space = null;

	public SolutionSpace<T, U> getSpace() {
		return space;
	}

	public void setSpace(SolutionSpace<T, U> space) {
		this.space = space;
	}

	public abstract void mutate(Population<T, U> population);

	public abstract void mutate(List<T> solutions);
}
