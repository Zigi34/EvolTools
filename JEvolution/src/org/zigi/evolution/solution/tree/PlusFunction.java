package org.zigi.evolution.solution.tree;

public class PlusFunction extends Function {
	public PlusFunction() {
		this.name = "plus";
	}

	@Override
	public int getDescendantCount() {
		return 2;
	}
}
