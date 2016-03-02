package org.zigi.evolution.solution.value;

public class NumericConstant extends GPFenotype {

	private Double value;
	private Double minValue = -1.0;
	private Double maxValue = 1.0;
	private String name;

	public NumericConstant(String name, Double min, Double max) {
		super(0);
		this.name = name;
		this.minValue = min;
		this.maxValue = max;
	}

	public NumericConstant(String name) {
		this(name, null, null);
	}

	public GPFenotype cloneMe() {
		NumericConstant val = new NumericConstant(name, minValue, maxValue);
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

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NumericConstant) {
			NumericConstant cons = (NumericConstant) obj;
			return cons.getName().equals(name);
		}
		return false;
	}

	@Override
	public String toString() {
		if (value == null)
			return name;
		return String.valueOf(value);
	}

}
