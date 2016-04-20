package org.zigi.evolution.solution.value;

public class MultiplyFunction extends GPFenotype {

	public MultiplyFunction() {
		super(2);
	}

	public GPFenotype cloneMe() {
		return new MultiplyFunction();
	}

	@Override
	public boolean isTerminal() {
		return false;
	}

	@Override
	public String toString() {
		return "MUL";
	}

	@Override
	public String getName() {
		return "*";
	}
}
