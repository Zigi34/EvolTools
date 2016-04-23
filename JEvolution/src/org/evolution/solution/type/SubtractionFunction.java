package org.evolution.solution.type;

public class SubtractionFunction extends GPFenotype {

	public SubtractionFunction() {
		super(2);
	}

	public GPFenotype cloneMe() {
		return new SubtractionFunction();
	}

	@Override
	public boolean isTerminal() {
		return false;
	}

	@Override
	public String toString() {
		return "SUB";
	}

	@Override
	public String getName() {
		return "-";
	}
}
