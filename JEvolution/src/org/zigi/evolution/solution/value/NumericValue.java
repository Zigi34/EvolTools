package org.zigi.evolution.solution.value;

/**
 * Represented like number of result applyed by operation with number. It is not
 * a random value on beginning generating tree solution
 * 
 * @author zigi
 *
 */
public class NumericValue extends GPFenotype {

	private Double value;

	public NumericValue() {
		super(0);
	}

	public NumericValue(Double value) {
		this();
		this.value = value;
	}

	public GPFenotype cloneMe() {
		NumericValue val = new NumericValue();
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

	@Override
	public String getName() {
		return "";
	}

}
