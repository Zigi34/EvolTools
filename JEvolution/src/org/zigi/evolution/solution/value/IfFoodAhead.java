package org.zigi.evolution.solution.value;

public class IfFoodAhead extends GPFenotype {

	public IfFoodAhead() {
		super(2);
	}

	public Genotype cloneMe() {
		return new IfFoodAhead();
	}

	@Override
	public boolean isTerminal() {
		return false;
	}

	@Override
	public boolean isFunction() {
		return true;
	}

	@Override
	public void setValue(Object o) {

	}

	@Override
	public Object getValue() {
		return null;
	}

	@Override
	public String toString() {
		return "IF-FOOD-AHEAD";
	}
}
