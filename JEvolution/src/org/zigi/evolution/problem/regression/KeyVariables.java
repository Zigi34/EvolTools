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
}
