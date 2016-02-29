package org.zigi.evolution.solution.value;

import java.util.Random;

public class NumericConstant extends GPFenotype {

	private Double value;
	private Double minValue = -1.0;
	private Double maxValue = 1.0;
	private static final Random RAND = new Random();

	public NumericConstant() {
		super(0);
		this.value = RAND.nextDouble() * (Math.abs(maxValue - minValue)) + minValue;
	}

	public NumericConstant(Double value) {
		this();
		this.value = value;
	}

	public GPFenotype cloneMe() {
		NumericConstant val = new NumericConstant();
		val.setValue(getValue());
		return val;
	}

	@Override
	public void setValue(Object o) {
		if (o instanceof Double)
			value = (Double) o;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public boolean isTerminal() {
		return true;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

}
