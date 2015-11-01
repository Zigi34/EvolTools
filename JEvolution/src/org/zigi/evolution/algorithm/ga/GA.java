package org.zigi.evolution.algorithm.ga;

import java.util.List;

import org.apache.log4j.Logger;
import org.zigi.evolution.Population;
import org.zigi.evolution.algorithm.Algorithm;
import org.zigi.evolution.algorithm.EA;
import org.zigi.evolution.cross.CrossFunction;
import org.zigi.evolution.exception.EvolutionException;
import org.zigi.evolution.mutate.MutateFunction;
import org.zigi.evolution.select.SelectFunction;
import org.zigi.evolution.solution.CloneableValue;
import org.zigi.evolution.solution.array.ArraySolution;
import org.zigi.evolution.util.TimeMeasure;

public class GA<T extends CloneableValue<T>> extends EA<ArraySolution<T>, T> {

	protected MutateFunction<ArraySolution<T>, T> mutate;
	protected CrossFunction<ArraySolution<T>, T> cross;
	protected SelectFunction<ArraySolution<T>, T> select;

	private static Logger log = Logger.getLogger(GA.class);

	public void run() {
		TimeMeasure measure = TimeMeasure.start();

		getFunction().evaluate(getPopulation());
		// log.debug("Population\n" + getPopulation());

		updateBestSolution();
		log.debug(String.format("BEST SOLUTION: %s\n", getBestSolution()));

		for (int i = 0; i < getGeneration(); i++) {
			try {
				Population<ArraySolution<T>, T> selected = select.select(getPopulation());
				// log.debug("Selected\n" + selected);

				cross.cross(selected);
				// log.debug("Crossed\n" + selected);

				mutate.mutate(selected);
				// log.debug("Mutated\n" + selected);

				Population<ArraySolution<T>, T> concated = getPopulation().concat(selected);
				// log.debug("Concated\n" + concated);

				getFunction().evaluate(concated);
				// log.debug("Evaluated\n" + concated);

				Population<ArraySolution<T>, T> bestSelected = concated.bestSolutions();
				// log.debug("Best selected\n" + bestSelected);

				setPopulation(bestSelected);

				updateBestSolution();
				log.debug(String.format("BEST SOLUTION: %s", getBestSolution()));
			} catch (EvolutionException ee) {
				log.error(ee);
			}
		}
		log.debug("Measured: " + measure.stop(TimeMeasure.MILLIS_DELAY) + " ms");
	}

	public MutateFunction<ArraySolution<T>, T> getMutate() {
		return mutate;
	}

	public void setMutate(MutateFunction<ArraySolution<T>, T> mutate) {
		this.mutate = mutate;
		this.mutate.setSpace(getSpace());
	}

	public CrossFunction<ArraySolution<T>, T> getCross() {
		return cross;
	}

	public void setCross(CrossFunction<ArraySolution<T>, T> cross) {
		this.cross = cross;
	}

	public SelectFunction<ArraySolution<T>, T> getSelect() {
		return select;
	}

	public void setSelect(SelectFunction<ArraySolution<T>, T> select) {
		this.select = select;
	}

	@Override
	public List<ArraySolution<T>> getResults() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return "Genetic algorithm";
	}

	public Algorithm defaultAlgorithm() {
		// TODO Auto-generated method stub
		return null;
	}
}
