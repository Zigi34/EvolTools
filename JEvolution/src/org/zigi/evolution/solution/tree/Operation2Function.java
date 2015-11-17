package org.zigi.evolution.solution.tree;

import java.util.LinkedList;
import java.util.List;

public class Operation2Function extends NonTerminal {

	private static final List<Class<?>> CHILD_TYPES = new LinkedList<Class<?>>();
	private List<NodeValue> childs = new LinkedList<NodeValue>();

	static {
		CHILD_TYPES.add(NonTerminal.class);
		CHILD_TYPES.add(NonTerminal.class);
	}

	public Operation2Function() {
		this.name = "PRG2";
	}

	@Override
	public Boolean isTerminal() {
		return false;
	}

	@Override
	public Boolean isNonTerminal() {
		return true;
	}

	@Override
	public Class<?> getResultType() {
		return null;
	}

	@Override
	public List<Class<?>> getChildTypes() {
		return CHILD_TYPES;
	}

	@Override
	public NodeValue clone() {
		return new Operation2Function();
	}

	@Override
	public Object getValue() {
		childs.get(0).getValue();
		childs.get(1).getValue();
		return null;
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
		return String.format("(%s,%s)", getChilds().size() < 1 ? "_" : getChilds().get(0),
				getChilds().size() < 2 ? "_" : getChilds().get(1));
	}
}
