package org.zigi.evolution.solution.value;

public class SumFunction extends GPFenotype {

	public SumFunction() {
		super(2);
	}

	public GPFenotype cloneMe() {
		return new SumFunction();
	}

	@Override
	public boolean isTerminal() {
		return false;
	}

	@Override
	public String toString() {
		return "SUM";
	}
}
