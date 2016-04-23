package org.zigi.evolution.model;

import org.evolution.util.Population;

/**
 * Population model object
 * 
 * @author zigi
 *
 */
public class PopulationModel {
	private Population pop;

	public PopulationModel(Population pop) {
		this.pop = pop;
	}

	public Population getPopulation() {
		return pop;
	}

	public void setPopulation(Population population) {
		this.pop = population;
	}
}
