package org.zigi.evolution.solution.value;

public class Prg2 extends GPFenotype {

	public Prg2() {
		super(2);
	}

	public GPFenotype cloneMe() {
		return new Prg2();
	}

	@Override
	public boolean isTerminal() {
		return false;
	}

	@Override
	public String toString() {
		return "PRG2";
	}

	@Override
	public String getName() {
		return "PRG2";
	}
}
