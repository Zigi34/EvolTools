package org.zigi.evolution.solution.array;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.zigi.evolution.solution.CloneableValue;
import org.zigi.evolution.solution.Solution;

public class ArraySolution<T extends CloneableValue<T>> extends Solution<T> {

	private ArrayList<T> values = new ArrayList<T>();

	@Override
	public ArraySolution<T> clone() {
		ArraySolution<T> sol = new ArraySolution<T>();
		for (int i = 0; i < values.size(); i++) {
			sol.setValue(i, sol.getValue(i).clone());
		}
		return sol;
	}

	@Override
	public T getValue(Object key) {
		if (key != null && key instanceof Integer) {
			return values.get((Integer) key);
		}
		return null;
	}

	@Override
	public Collection<T> getValues() {
		return values;
	}

	@Override
	public Set<Object> getKeys() {
		Set<Object> keys = new HashSet<Object>(values.size());
		for (int i = 0; i < values.size(); i++)
			keys.add(i);
		return keys;
	}

	@Override
	public void setValue(Object key, T value) {
		if (key != null && key instanceof Integer) {
			values.set((Integer) key, value);
		}
	}

	@Override
	public int size() {
		return values.size();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (T value : values) {
			sb.append(value + ", ");
		}
		return sb.toString();
	}

	@Override
	public void addValue(T value) {
		values.add(value);
	}
}
