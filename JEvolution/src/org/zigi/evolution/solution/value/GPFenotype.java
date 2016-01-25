package org.zigi.evolution.solution.value;

public abstract class GPFenotype extends Genotype {

	private int maxChilds = 0;

	public GPFenotype(int childs) {
		this.maxChilds = childs;
	}

	public abstract boolean isTerminal();

	public abstract boolean isFunction();

	public int getMaxChilds() {
		return maxChilds;
	}

	public void setMaxChilds(int maxChilds) {
		this.maxChilds = maxChilds;
	}

}
