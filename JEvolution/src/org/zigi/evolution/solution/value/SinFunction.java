package org.zigi.evolution.solution.value;

public class SinFunction extends GPFenotype {

	public SinFunction() {
		super(1);
	}

	public GPFenotype cloneMe() {
		return new SinFunction();
	}

	@Override
	public boolean isTerminal() {
		return false;
	}

	@Override
	public String toString() {
		return "SIN";
	}
}
