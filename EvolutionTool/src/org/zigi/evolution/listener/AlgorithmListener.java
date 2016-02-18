package org.zigi.evolution.listener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.log4j.Logger;
import org.zigi.evolution.algorithm.EvolutionAlgorithm;
import org.zigi.evolution.algorithm.GeneticProgramming;
import org.zigi.evolution.services.Services;
import org.zigi.evolution.solution.Solution;

public class AlgorithmListener implements PropertyChangeListener {

	private static final Logger LOG = Logger.getLogger(AlgorithmListener.class);

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String event = (String) evt.getNewValue();
		if (event.equals(EvolutionAlgorithm.ALGORITHM_STARTED))
			LOG.info("Algoritmus spuštěn");
		else if (event.equals(EvolutionAlgorithm.ALGORITHM_TERMINATED))
			LOG.info("Algoritmus zastaven");
		else if (event.equals(GeneticProgramming.MUTATE_SOLUTION_START)) {
			// LOG.info("Mutuji");
		} else if (event.equals(GeneticProgramming.CROSS_SOLUTION_START)) {
			// LOG.info("Křížím");
		} else if (event.equals(EvolutionAlgorithm.NEW_BEST_SOLUTION)) {
			EvolutionAlgorithm alg = Services.algorithmService().getSelected().getAlgorithm();
			Solution sol = alg.getBestSolution();
			if (sol != null)
				LOG.info("Nové lepší řešení " + sol.getFitness());
			else
				LOG.info("Nové lepší řešení");
		} else if (event.equals(GeneticProgramming.NEW_POPULATION)) {
			EvolutionAlgorithm alg = Services.algorithmService().getSelected().getAlgorithm();
			int generation = alg.getActualGeneration();
			if (generation % 50 == 0)
				LOG.info("Nová populace [" + generation + "]");
		}
	}
}
