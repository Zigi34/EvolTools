package org.zigi.evolution.solution.value;

public class Prg2 extends GPFenotype {

	public Prg2() {
		super(2);
	}

	public Genotype cloneMe() {
		return new Prg2();
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
		return "PRG2";
	}
}
