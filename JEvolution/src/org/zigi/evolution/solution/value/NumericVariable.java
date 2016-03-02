package org.zigi.evolution.solution.value;

public class NumericVariable extends GPFenotype {

	private String name;
	private Double value;

	public NumericVariable(String name) {
		super(0);
		this.name = name;
	}

	public NumericVariable(String name, Double value) {
		this(name);
		this.value = value;
	}

	public GPFenotype cloneMe() {
		NumericVariable val = new NumericVariable(getName());
		val.setValue(getValue());
		return val;
	}

	public String getName() {
		return name;
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
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NumericVariable) {
			NumericVariable variable = (NumericVariable) obj;
			if (variable.getName().equals(name))
				return true;
		}
		return false;
	}

	@Override
	public String toString() {
		if (value != null)
			return String.valueOf(value);
		return name;
	}

}
