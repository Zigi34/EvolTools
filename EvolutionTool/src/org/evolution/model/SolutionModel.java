package org.evolution.model;

import org.evolution.solution.Solution;

import javafx.beans.property.SimpleStringProperty;

public class SolutionModel {

	private SimpleStringProperty fitness;
	private SimpleStringProperty genotype;

	public SolutionModel(Solution sol) {
		this.fitness = new SimpleStringProperty(String.valueOf(sol.getFunctionValue()));
		this.genotype = new SimpleStringProperty(sol.getGenotypeString());
	}

	public String getFitness() {
		return fitness.get();
	}

	public void setFitness(String fitness) {
		this.fitness.set(fitness);
	}

	public String getGenotype() {
		return genotype.get();
	}

	public void setGenotype(String genotype) {
		this.genotype.set(genotype);
	}

}
