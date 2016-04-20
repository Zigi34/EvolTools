package org.zigi.evolution.solution.value;

public class LeftDirection extends GPFenotype {

	public LeftDirection() {
		super(0);
	}

	public GPFenotype cloneMe() {
		return new LeftDirection();
	}

	@Override
	public boolean isTerminal() {
		return true;
	}

	@Override
	public String toString() {
		return "LEFT";
	}

	@Override
	public String getName() {
		return "LEFT";
	}
}
