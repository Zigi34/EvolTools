package org.zigi.evolution.solution.tree;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

public class NumberValue extends Terminal {
	private Number value;
	private static final List<NodeValue> CHILDS = new LinkedList<NodeValue>();
	private static final Logger LOG = Logger.getLogger(NumberValue.class);

	public NumberValue() {
	}

	@Override
	public String getName() {
		return String.valueOf(value.doubleValue());
	}

	public NumberValue(Number value) {
		LOG.info("V=:" + value);
		this.value = value;
	}

	public Integer getInt() {
		return value.intValue();
	}

	public Double getDouble() {
		return value.doubleValue();
	}

	public void setValue(Number value) {
		this.value = value;
	}

	public NumberValue add(NumberValue value) {
		return new NumberValue(this.value.doubleValue() + value.getDouble());
	}

	public NumberValue subtraction(NumberValue value) {
		return new NumberValue(this.value.doubleValue() - value.getDouble());
	}

	public NumberValue multiplication(NumberValue value) {
		return new NumberValue(this.value.doubleValue() * value.getDouble());
	}

	public NumberValue divide(NumberValue value) {
		return new NumberValue(this.value.doubleValue() / value.getDouble());
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
	public NodeValue clone() {
		return new NumberValue(new Double(value.doubleValue()));
	}

	@Override
	public Object getValue() {
		return new NumberValue(value);
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
