package org.zigi.evolution.solution;

import java.util.List;

import org.zigi.evolution.solution.value.GPFenotype;
import org.zigi.evolution.util.Cloneable;

public abstract class Solution implements Cloneable<Solution> {

	private Double fitness = 0.0;

	public abstract GPFenotype getGenotype(Integer index);

	public abstract List<GPFenotype> getGenotypes();

	public abstract void setGenotype(int index, GPFenotype value);

	public abstract void setGenotypes(List<GPFenotype> vals);

	public abstract void addGenotype(GPFenotype value);

	public abstract int size();

	public boolean isEvaluated() {
		return fitness != null;
	}

	public Double getFitness() {
		return fitness;
	}

	public void setFitness(Double fitness) {
		this.fitness = fitness;
	}

	public abstract String getGenotypeString();
}
