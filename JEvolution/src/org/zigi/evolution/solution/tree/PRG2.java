package org.zigi.evolution.solution.tree;

import java.util.LinkedList;
import java.util.List;

public class PRG2 extends NonTerminal {

	private static final List<Class<?>> CHILD_TYPES = new LinkedList<Class<?>>();
	private List<NodeValue> childs = new LinkedList<NodeValue>();

	static {
		CHILD_TYPES.add(NodeValue.class);
		CHILD_TYPES.add(NodeValue.class);
	}

	public PRG2() {
		this.name = "PRG2";
	}

	@Override
	public Class<?> getResultType() {
		return NonTerminal.class;
	}

	@Override
	public List<Class<?>> getChildTypes() {
		return CHILD_TYPES;
	}

	@Override
	public NodeValue clone() {
		return new PRG2();
	}

	@Override
	public Integer getChildCount() {
		return 2;
	}

	@Override
	public List<NodeValue> getChilds() {
		return childs;
	}

	@Override
	public String toString() {
		int size = getChilds().size();
		if (size == 0)
			return "PRG2(_,_)";
		else if (size == 1)
			return String.format("PRG2(%s,_)", getChilds().get(0));
		return String.format("PRG2(%s,%s)", getChilds().get(0), getChilds().get(1));
	}
}
