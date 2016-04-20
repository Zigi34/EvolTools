package org.zigi.evolution.solution.value;

public class PowerFunction extends GPFenotype {

	public PowerFunction() {
		super(2);
	}

	public GPFenotype cloneMe() {
		return new PowerFunction();
	}

	@Override
	public boolean isTerminal() {
		return false;
	}

	@Override
	public String toString() {
		return "POW";
	}

	@Override
	public String getName() {
		return "";
	}
}
