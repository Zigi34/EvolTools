package org.evolution.solution.type;

import java.util.Random;

public class NumericConstant extends GPFenotype {

	private static final Random RND = new Random();

	private Double value;
	private Double minValue = -1.0;
	private Double maxValue = 1.0;
	private boolean district = false;

	public NumericConstant(Double min, Double max) {
		super(0);
		this.minValue = min;
		this.maxValue = max;
	}

	public NumericConstant(Double min, Double max, boolean district) {
		this(min, max);
		this.district = district;
	}

	public NumericConstant() {
		this(null, null);
	}

	public void evaluateRandom() {
		this.value = minValue + RND.nextDouble() * Math.abs(maxValue - minValue);
		if (district)
			this.value = (double) Math.round(this.value);
	}

	public GPFenotype cloneMe() {
		NumericConstant val = new NumericConstant(minValue, maxValue, district);
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

	@Override
	public String getName() {
		return "k";
	}

}
