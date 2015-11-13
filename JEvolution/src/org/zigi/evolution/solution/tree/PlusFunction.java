package org.zigi.evolution.solution.tree;

import java.util.LinkedList;
import java.util.List;

public class PlusFunction extends NonTerminal {

	private static final List<Class<?>> CHILD_TYPES = new LinkedList<Class<?>>();
	private List<NodeValue> childs = new LinkedList<NodeValue>();

	static {
		CHILD_TYPES.add(NumberValue.class);
		CHILD_TYPES.add(NumberValue.class);
	}

	public PlusFunction() {
		this.name = "+";
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
		return new PlusFunction();
	}

	@Override
	public Object getValue() {
		NumberValue val1 = (NumberValue) childs.get(0);
		NumberValue val2 = (NumberValue) childs.get(1);
		return new NumberValue((Number) val1.add(val2).getValue());
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
		return String.format("(%s+%s)", getChilds().size() < 1 ? "_" : getChilds().get(0),
				getChilds().size() < 2 ? "_" : getChilds().get(1));
	}
}
