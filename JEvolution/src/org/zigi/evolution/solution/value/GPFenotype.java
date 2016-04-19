package org.zigi.evolution.solution.value;

import org.zigi.evolution.util.Cloneable;

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
}
