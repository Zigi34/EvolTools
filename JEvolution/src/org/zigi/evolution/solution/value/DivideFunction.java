package org.zigi.evolution.solution.value;

public class DivideFunction extends GPFenotype {

	public DivideFunction() {
		super(2);
	}

	public GPFenotype cloneMe() {
		return new DivideFunction();
	}

	@Override
	public boolean isTerminal() {
		return false;
	}

	@Override
	public String toString() {
		return "DIV";
	}
}
