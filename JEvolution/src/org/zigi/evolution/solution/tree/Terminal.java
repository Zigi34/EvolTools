package org.zigi.evolution.solution.tree;

import java.util.List;

public abstract class Terminal extends NodeValue {

	@Override
	public Boolean isNonTerminal() {
		return false;
	}

	@Override
	public Boolean isTerminal() {
		return true;
	}

	@Override
	public List<NodeValue> getChilds() {
		return null;
	}

	@Override
	public List<Class<?>> getChildTypes() {
		return null;
	}

	@Override
	public Integer getChildCount() {
		return 0;
	}
}
