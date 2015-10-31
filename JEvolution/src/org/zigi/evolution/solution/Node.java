package org.zigi.evolution.solution;

import java.util.HashSet;
import java.util.Set;

public class Node<T extends CloneableValue<T>> extends CloneableValue<Node<T>> {
	private Node<T> parent;
	private T value;
	private Set<Node<T>> nodes = new HashSet<Node<T>>();

	public Node(T value) {
		this.value = value;
	}

	public Node() {

	}

	public Node<T> getParent() {
		return parent;
	}

	public void setParent(Node<T> parent) {
		this.parent = parent;
	}

	public Set<Node<T>> getNodes() {
		return nodes;
	}

	public void connectTo(T value) {
		nodes.add(new Node<T>());
	}

	public void setValue(T value) {
		this.value = value;
	}

	public T getValue() {
		return value;
	}

	@Override
	public Node<T> clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Node<T> child : nodes) {
			sb.append(child);
		}
		return String.format("Node(val: %s)[parent: %s]\n", getValue(), sb.toString());
	}

}
