package org.evolution.solution.type;

public class RightDirection extends GPFenotype {

	public RightDirection() {
		super(0);
	}

	public GPFenotype cloneMe() {
		return new RightDirection();
	}

	@Override
	public boolean isTerminal() {
		return true;
	}

	@Override
	public String toString() {
		return "RIGHT";
	}

	@Override
	public String getName() {
		return "RIGHT";
	}

}
