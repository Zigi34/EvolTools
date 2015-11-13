package org.zigi.evolution.solution.tree;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.zigi.evolution.solution.Solution;

public class TreeSolution extends Solution<NodeValue> {

	private NodeValue root;
	private List<NodeValue> values = new LinkedList<NodeValue>();
	private static final Logger LOG = Logger.getLogger(TreeSolution.class);

	@Override
	public NodeValue getValue(Integer key) {
		return values.get(key);
	}

	@Override
	public Collection<NodeValue> getValues() {
		return values;
	}

	@Override
	public void setValue(Integer key, NodeValue value) {
		values.set(key, value);
	}

	@Override
	public void addValue(NodeValue value) {
		if (root == null) {
			root = value;
		} else {
			for (NodeValue node : deepWalk()) {
				if (node.isAnyEmptyChild()) {
					node.addChild(value);
					break;
				}
			}
		}
		values.add(value);
	}

	public List<NodeValue> deepWalk() {
		List<NodeValue> values = new LinkedList<NodeValue>();
		deepWalk(values, root);
		LOG.info("DEEP WALK");
		for (NodeValue node : values) {
			LOG.info(node.getName());
		}
		return values;
	}

	private void deepWalk(List<NodeValue> nodes, NodeValue actual) {
		if (actual != null) {
			nodes.add(actual);
			if (actual.getChilds() != null) {
				for (NodeValue node : actual.getChilds()) {
					deepWalk(nodes, node);
				}
			}
		}
	}

	@Override
	public int size() {
		return values.size();
	}

	@Override
	public Solution<NodeValue> clone() {
		// klonování řešení
		return null;
	}

	public NodeValue getRoot() {
		return root;
	}

	public void setRoot(NodeValue root) {
		this.root = root;
	}

	@Override
	public String toString() {
		if (root != null)
			return root.toString();
		return "";
	}
}