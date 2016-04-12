package org.zigi.evolution.solution.value;

import java.util.Random;

public class NumericConstant extends GPFenotype {

	private static final Random RND = new Random();

	private Double value;
	private Double minValue = -1.0;
	private Double maxValue = 1.0;

	public NumericConstant(Double min, Double max) {
		super(0);
		this.minValue = min;
		this.maxValue = max;
	}

	public NumericConstant() {
		this(null, null);
	}

	public void evaluateRandom() {
		this.value = minValue + RND.nextDouble() * Math.abs(maxValue - minValue);
	}

	public GPFenotype cloneMe() {
		NumericConstant val = new NumericConstant(minValue, maxValue);
		if (value != null)
			val.setValue(new Double((Double) value));
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

	public Double getMinValue() {
		return minValue;
	}

	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}

	public Double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}

	@Override
	public String toString() {
		if (value == null)
			return "K";
		return String.valueOf(value);
	}

}
