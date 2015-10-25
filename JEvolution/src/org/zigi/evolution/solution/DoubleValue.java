package org.zigi.evolution.solution;

public class DoubleValue extends CloneableValue<DoubleValue> implements Valuable<Double> {

	private Double val;

	public DoubleValue(Double value) {
		this.val = value;
	}

	@Override
	public DoubleValue clone() {
		return new DoubleValue(val.doubleValue());
	}

	public Double getValue() {
		return val;
	}

	@Override
	public String toString() {
		return String.format("%.3f", val);
	}

}
