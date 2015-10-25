package org.zigi.evolution.solution.tree;

public abstract class Function implements NonTerminal {
	protected String name;
	
	public abstract int getDescendantCount();
	
	public String getName() {
		return name;
	}
}
