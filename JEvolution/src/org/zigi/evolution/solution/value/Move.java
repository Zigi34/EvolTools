package org.zigi.evolution.solution.value;

public class Move extends GPFenotype {

	public Move() {
		super(0);
	}

	public Genotype cloneMe() {
		return new Move();
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

	}

	@Override
	public Object getValue() {
		return null;
	}

	@Override
	public String toString() {
		return "MOVE";
	}
}
