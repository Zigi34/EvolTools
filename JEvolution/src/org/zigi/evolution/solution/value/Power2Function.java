package org.zigi.evolution.solution.value;

public class Power2Function extends GPFenotype {

	public Power2Function() {
		super(1);
	}

	public GPFenotype cloneMe() {
		return new Power2Function();
	}

	@Override
	public boolean isTerminal() {
		return false;
	}

	@Override
	public String toString() {
		return "POW2";
	}

	@Override
	public String getName() {
		return "^2";
	}
}
