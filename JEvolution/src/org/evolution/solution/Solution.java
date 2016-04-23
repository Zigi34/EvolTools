package org.evolution.solution;

import java.util.List;

import org.evolution.solution.type.GPFenotype;
import org.evolution.util.Cloneable;

public abstract class Solution implements Cloneable<Solution> {

	private Double rawFitness = 0.0;

	public abstract GPFenotype getGenotype(Integer index);

	public abstract List<GPFenotype> getGenotypes();

	public abstract void setGenotype(int index, GPFenotype value);

	public abstract void setGenotypes(List<GPFenotype> vals);

	public abstract void addGenotype(GPFenotype value);

	public abstract int size();

	public boolean isEvaluated() {
		return rawFitness != null;
	}

	public Double getFunctionValue() {
		return rawFitness;
	}

	public void setFunctionValue(Double fitness) {
		this.rawFitness = fitness;
	}

	public abstract String getGenotypeString();

	@Override
	public int hashCode() {
		String code = toString();
		return code.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null)
			return obj.toString().equals(toString());
		return false;
	}
}
