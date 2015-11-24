package org.zigi.evolution.solution.tree;

import java.util.LinkedList;
import java.util.List;

public class SinusFunction extends NonTerminal {

	private static final List<Class<?>> CHILD_TYPES = new LinkedList<Class<?>>();
	private List<NodeValue> childs = new LinkedList<NodeValue>();

	static {
		CHILD_TYPES.add(NumberValue.class);
	}

	public SinusFunction() {
		this.name = "sin";
	}

	@Override
	public Object getValue() {
		if (childs.size() == 1) {
			return Math.sin((Double) childs.get(0).getValue());
		}
		return null;
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
		return NumberValue.class;
	}

	@Override
	public List<Class<?>> getChildTypes() {
		return CHILD_TYPES;
	}

	@Override
	public NodeValue clone() {
		return new SinusFunction();
	}

	@Override
	public Integer getChildCount() {
		return 1;
	}

	@Override
	public List<NodeValue> getChilds() {
		return childs;
	}

	@Override
	public String toString() {
		return String.format("sin(%s)", getChilds().size() < 1 ? "_" : getChilds().get(0));
	}
}
