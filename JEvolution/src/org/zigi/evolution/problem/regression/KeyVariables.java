package org.zigi.evolution.problem.regression;

import java.util.LinkedList;
import java.util.List;

/**
 * Class represent input function variables
 * 
 * @author zigi
 *
 */
public class KeyVariables {
	private List<Double> keys = new LinkedList<Double>();
	private int maxKeys = 0;

	public KeyVariables(int size) {
		this.maxKeys = size;
	}

	public KeyVariables(Double... keys) {
		this(keys.length);
		for (Double key : keys) {
			addKey(key);
		}
	}

	public void addKey(Double key) {
		if (keys.size() < maxKeys)
			keys.add(key);
	}

	public void addKey(int index, Double value) {
		if (keys.get(index) != null)
			keys.add(index, value);
	}

	public int getDimension() {
		return maxKeys;
	}

	/**
	 * Vraci hodnotu klice pod indexem
	 * 
	 * @param index
	 *            index klice
	 * @return
	 */
	public Double getKey(Integer index) {
		return keys.get(index);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("(");
		for (int i = 0; i < keys.size() - 1; i++)
			sb.append(String.format("%s,", keys.get(i)));
		sb.append(keys.get(keys.size() - 1));
		sb.append(")");
		return sb.toString();
	}
}
