package org.zigi.evolution.solution.value;

public class LeftDirection extends GPFenotype {

	public LeftDirection() {
		super(0);
	}

	public Genotype cloneMe() {
		return new LeftDirection();
	}

	@Override
	public boolean isTerminal() {
		return true;
	}

	@Override
	public boolean isFunction() {
		return false;
	}

	@Override
	public void setValue(Object o) {
		// nic nedelej
	}

	@Override
	public Object getValue() {
		return null;
	}

	@Override
	public String toString() {
		return "LEFT";
	}
}
