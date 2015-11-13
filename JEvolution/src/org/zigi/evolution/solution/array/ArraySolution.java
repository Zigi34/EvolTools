package org.zigi.evolution.solution.array;

import java.util.ArrayList;
import java.util.Collection;

import org.zigi.evolution.solution.CloneableValue;
import org.zigi.evolution.solution.Solution;

public class ArraySolution<T extends CloneableValue<T>> extends Solution<T> {

	private ArrayList<T> values = new ArrayList<T>();

	@Override
	public ArraySolution<T> clone() {
		ArraySolution<T> sol = new ArraySolution<T>();
		for (int i = 0; i < values.size(); i++) {
			sol.addValue(values.get(i).clone());
		}
		sol.setFitness(getFitness());
		return sol;
	}

	@Override
	public T getValue(Integer key) {
		return values.get(key);
	}

	@Override
	public Collection<T> getValues() {
		return values;
	}

	@Override
	public void setValue(Integer key, T value) {
		values.set(key, value);
	}

	@Override
	public int size() {
		return values.size();
	}

	@Override
	public void addValue(T value) {
		values.add(value);
	}
}
