package org.zigi.evolution.solution;

import java.util.Collection;

public abstract class Solution<T extends CloneableValue<T>> {

	private Double fitness;

	public abstract T getValue(Integer key);

	public abstract Collection<T> getValues();

	public abstract void setValue(Integer key, T value);

	public abstract void addValue(T value);

	public abstract int size();

	public abstract Solution<T> clone();

	public Double getFitness() {
		return fitness;
	}

	public void setFitness(Double fitness) {
		this.fitness = fitness;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[" + String.format("%.3f", fitness) + "] ");
		for (T key : getValues()) {
			sb.append(key + " ");
		}
		return sb.toString();
	}
}
