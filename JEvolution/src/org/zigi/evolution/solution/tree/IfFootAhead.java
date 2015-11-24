package org.zigi.evolution.solution.tree;

import java.util.LinkedList;
import java.util.List;

public class IfFootAhead extends NonTerminal {

	private static final List<Class<?>> CHILD_TYPES = new LinkedList<Class<?>>();
	private List<NodeValue> childs = new LinkedList<NodeValue>();

	static {
		CHILD_TYPES.add(NodeValue.class);
		CHILD_TYPES.add(NodeValue.class);
	}

	public IfFootAhead() {
		this.name = "IFA";
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
		return new IfFootAhead();
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
			return "IFA(_,_)";
		else if (size == 1)
			return String.format("IFA(%s,_)", getChilds().get(0));
		else
			return String.format("IFA(%s,%s)", getChilds().get(0), getChilds().get(1));
	}
}
