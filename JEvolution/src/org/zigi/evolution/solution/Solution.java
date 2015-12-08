package org.zigi.evolution.solution;

import java.util.List;

import org.zigi.evolution.solution.value.Cloneable;
import org.zigi.evolution.solution.value.Genotype;

public abstract class Solution implements Cloneable<Solution> {

	private Double fitness = 0.0;

	public abstract Genotype getGenotype(Integer index);

	public abstract List<Genotype> getGenotypes();

	public abstract void setGenotype(int index, Genotype value);

	public abstract void setGenotypes(List<Genotype> vals);

	public abstract void addGenotype(Genotype value);

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
}
