package org.zigi.evolution.solution.value;

public class Prg3 extends GPFenotype {

	public Prg3() {
		super(3);
	}

	public Genotype cloneMe() {
		return new Prg3();
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
		return "PRG3";
	}
}
