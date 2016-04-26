package org.evolution.model;

import org.evolution.cross.CrossFunction;

public class CrossFunctionModel {

	private CrossFunction function;

	public CrossFunctionModel(CrossFunction function) {
		this.function = function;
	}

	public CrossFunction getFunction() {
		return function;
	}

	public void setFunction(CrossFunction function) {
		this.function = function;
	}

	@Override
	public String toString() {
		return function.toString();
	}
}
