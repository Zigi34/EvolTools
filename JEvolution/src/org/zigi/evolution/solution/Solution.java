package org.zigi.evolution.solution;

import java.util.Collection;
import java.util.Set;

public abstract class Solution<T extends CloneableValue<T>> {

	private Double fitness;

	public abstract T getValue(Object key);

	public abstract Collection<T> getValues();

	public abstract Set<Object> getKeys();

	public abstract void setValue(Object key, T value);

	public abstract void addValue(T value);

	public abstract int size();

	public abstract Solution<T> clone();

	public Double getFitness() {
		return fitness;
	}

	public void setFitness(Double fitness) {
		this.fitness = fitness;
	}
}
