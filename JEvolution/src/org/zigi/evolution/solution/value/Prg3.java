package org.zigi.evolution.solution.value;

public class Prg3 extends GPFenotype {

	public Prg3() {
		super(3);
	}

	public GPFenotype cloneMe() {
		return new Prg3();
	}

	@Override
	public boolean isTerminal() {
		return false;
	}

	@Override
	public String toString() {
		return "PRG3";
	}

	@Override
	public String getName() {
		return "PRG3";
	}
}
