package org.evolution.model;

import org.evolution.select.SelectFunction;

public class SelectFunctionModel {

	private SelectFunction function;

	public SelectFunctionModel(SelectFunction function) {
		this.function = function;
	}

	public SelectFunction getFunction() {
		return function;
	}

	@Override
	public String toString() {
		return function.toString();
	}
}
