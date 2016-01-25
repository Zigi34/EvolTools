package org.zigi.evolution.solution.value;

public class NumberGenotyp extends Genotype {

	private Double value;

	public Genotype cloneMe() {
		NumberGenotyp val = new NumberGenotyp();
		val.setValue(getValue());
		return val;
	}

	public NumberGenotyp() {

	}

	public NumberGenotyp(Double val) {
		this.value = val;
	}

	@Override
	public void setValue(Object o) {
		if (o instanceof Double) {
			value = (Double) o;
		}
	}

	@Override
	public Object getValue() {
		return value;
	}
}
