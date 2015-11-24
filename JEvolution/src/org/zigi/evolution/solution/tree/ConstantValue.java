package org.zigi.evolution.solution.tree;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

public class ConstantValue extends NumberValue {
	private static final List<NodeValue> CHILDS = new LinkedList<NodeValue>();
	private static final Logger LOG = Logger.getLogger(ConstantValue.class);

	public ConstantValue(Double value) {
		this.value = value;
	}

	@Override
	public NodeValue clone() {
		// TODO Auto-generated method stub
		return null;
	}

}
