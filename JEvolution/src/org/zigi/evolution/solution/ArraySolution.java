package org.zigi.evolution.solution;

import java.util.LinkedList;
import java.util.List;

import org.zigi.evolution.solution.value.Genotype;

public class ArraySolution extends Solution {

	private List<Genotype> vals = new LinkedList<Genotype>();

	public Solution cloneMe() {
		ArraySolution sol = new ArraySolution();
		for (Genotype gen : sol.getGenotypes()) {
			sol.addGenotype(gen.cloneMe());
		}
		return sol;
	}

	@Override
	public Genotype getGenotype(Integer index) {
		return vals.get(index);
	}

	@Override
	public List<Genotype> getGenotypes() {
		return vals;
	}

	@Override
	public void setGenotypes(List<Genotype> vals) {
		vals.clear();
		vals.addAll(vals);
	}

	@Override
	public void addGenotype(Genotype value) {
		vals.add(value);
	}

	@Override
	public int size() {
		return vals.size();
	}

	@Override
	public void setGenotype(int index, Genotype value) {
		// TODO Auto-generated method stub

	}
}
