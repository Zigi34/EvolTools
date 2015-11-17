package org.zigi.evolution.solution;

import java.util.Collection;

public abstract class Solution<T extends CloneableValue<T>> {

	private Double fitness;

	public abstract T getChildNode(Integer key);

	public abstract Collection<T> getChildNodes();

	public abstract void setChildNode(Integer key, T value);

	public abstract void addChildNode(T value);

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
		for (T key : getChildNodes()) {
			sb.append(key + " ");
		}
		return sb.toString();
	}
}
