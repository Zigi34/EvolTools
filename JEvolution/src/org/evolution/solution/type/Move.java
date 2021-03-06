package org.evolution.solution.type;

public class Move extends GPFenotype {

	public Move() {
		super(0);
	}

	public GPFenotype cloneMe() {
		return new Move();
	}

	@Override
	public boolean isTerminal() {
		return true;
	}

	@Override
	public String toString() {
		return "MOVE";
	}

	@Override
	public String getName() {
		return "MOVE";
	}
}
