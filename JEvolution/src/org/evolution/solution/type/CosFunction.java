package org.evolution.solution.type;

public class CosFunction extends GPFenotype {

	public CosFunction() {
		super(1);
	}

	public GPFenotype cloneMe() {
		return new CosFunction();
	}

	@Override
	public boolean isTerminal() {
		return false;
	}

	@Override
	public String toString() {
		return "COS";
	}

	@Override
	public String getName() {
		return "cos()";
	}
}
