package org.zigi.evolution.solution.tree;

public class Move extends Terminal {

	@Override
	public Class<?> getResultType() {
		return Terminal.class;
	}

	@Override
	public NodeValue clone() {
		return null;
	}

	@Override
	public String toString() {
		return "MOVE";

	}
}
