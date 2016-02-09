package org.zigi.evolution.model;

import org.zigi.evolution.mutate.MutateFunction;

public class MutateFunctionModel {

	private MutateFunction function;

	public MutateFunctionModel(MutateFunction function) {
		this.function = function;
	}

	public MutateFunction getFunction() {
		return function;
	}

	public void setFunction(MutateFunction function) {
		this.function = function;
	}

	@Override
	public String toString() {
		return function.toString();
	}
}
