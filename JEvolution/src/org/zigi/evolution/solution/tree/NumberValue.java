package org.zigi.evolution.solution.tree;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

public abstract class NumberValue extends Terminal {
	private static final List<NodeValue> CHILDS = new LinkedList<NodeValue>();
	private static final Logger LOG = Logger.getLogger(NumberValue.class);

	public NumberValue() {
	}

	public NumberValue(Number value) {
		this.value = value;
	}

	public Integer getInt() {
		Number val = (Number) value;
		return val.intValue();
	}

	public Double getDouble() {
		Number val = (Number) value;
		return val.doubleValue();
	}

	public void setValue(Number value) {
		this.value = value;
	}

	@Override
	public Boolean isTerminal() {
		return true;
	}

	@Override
	public Boolean isNonTerminal() {
		return false;
	}

	@Override
	public Class<?> getResultType() {
		return NumberValue.class;
	}

	@Override
	public List<Class<?>> getChildTypes() {
		return null;
	}

	@Override
	public Integer getChildCount() {
		return 0;
	}

	@Override
	public List<NodeValue> getChilds() {
		return CHILDS;
	}

	@Override
	public String toString() {
		return String.format("%.3f", value);
	}
}
