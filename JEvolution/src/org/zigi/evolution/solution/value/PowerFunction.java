package org.zigi.evolution.solution.value;

public class PowerFunction extends GPFenotype {

	private Double value;

	public PowerFunction() {
		super(1);
	}

	public GPFenotype cloneMe() {
		return new PowerFunction();
	}

	@Override
	public boolean isTerminal() {
		return false;
	}

	@Override
	public void setValue(Object o) {
		if (o instanceof Double)
			this.value = (Double) o;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "POW";
	}
}
