package org.evolution.solution.type;

import org.evolution.util.Cloneable;

public abstract class GPFenotype implements Cloneable<GPFenotype> {

	private int maxChilds = 0;

	public GPFenotype(int childs) {
		this.maxChilds = childs;
	}

	public abstract boolean isTerminal();

	public int getMaxChilds() {
		return maxChilds;
	}

	public void setMaxChilds(int maxChilds) {
		this.maxChilds = maxChilds;
	}

	public void setValue(Object o) {

	}

	public Object getValue() {
		return null;
	}

	public abstract String getName();
}
