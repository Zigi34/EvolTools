package org.zigi.evolution.solution.tree;

public abstract class NonTerminal extends NodeValue {

	@Override
	public Boolean isNonTerminal() {
		return true;
	}

	@Override
	public Boolean isTerminal() {
		return false;
	}
}
