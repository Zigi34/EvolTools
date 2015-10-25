package org.zigi.evolution.solution;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class TreeSolution<T extends CloneableValue<T>> extends Solution<T> {

	private Node<T> root;
	private List<Node<T>> nodes = new LinkedList<Node<T>>();

	public void connectTo(T where, T item) {
		if (nodes.size() == 0)
			root = new Node<T>(item);
		else if (nodes.contains(where) && !nodes.contains(item)) {

		}
	}

	@Override
	public Solution<T> clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T getValue(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<T> getValues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Object> getKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValue(Object key, T value) {
		// TODO Auto-generated method stub

	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addValue(T value) {
		// TODO Auto-generated method stub

	}
}
