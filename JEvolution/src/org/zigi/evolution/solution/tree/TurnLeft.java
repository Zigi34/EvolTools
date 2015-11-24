package org.zigi.evolution.solution.tree;

public class TurnLeft extends Terminal {

	@Override
	public Class<?> getResultType() {
		return Terminal.class;
	}

	@Override
	public NodeValue clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return "TURN LEFT";
	}

}
