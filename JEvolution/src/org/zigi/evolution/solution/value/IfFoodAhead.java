package org.zigi.evolution.solution.value;

public class IfFoodAhead extends GPFenotype {

	public IfFoodAhead() {
		super(2);
	}

	public GPFenotype cloneMe() {
		return new IfFoodAhead();
	}

	@Override
	public boolean isTerminal() {
		return false;
	}

	@Override
	public String toString() {
		return "IF-FOOD-AHEAD";
	}

	@Override
	public String getName() {
		return "IFA";
	}
}
