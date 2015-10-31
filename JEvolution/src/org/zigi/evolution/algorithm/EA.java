package org.zigi.evolution.algorithm;

import java.util.List;

import org.zigi.evolution.Population;
import org.zigi.evolution.evaluate.EvaluateFunction;
import org.zigi.evolution.solution.CloneableValue;
import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.space.SolutionSpace;

public abstract class EA<T extends Solution<U>, U extends CloneableValue<U>> implements Algorithm, Runnable {
	private Population<T, U> population = new Population<T, U>();
	protected EvaluateFunction<T, U> function;
	protected SolutionSpace<T, U> space;
	protected T best;

	private int generation = 3;

	private Thread t = new Thread(this);

	public EvaluateFunction<T, U> getFunction() {
		return function;
	}

	public void init() {

	}

	public void setFunction(EvaluateFunction<T, U> function) {
		this.function = function;
	}

	public SolutionSpace<T, U> getSpace() {
		return space;
	}

	public void setSpace(SolutionSpace<T, U> space) {
		this.space = space;
	}

	public Population<T, U> getPopulation() {
		return population;
	}

	public void setPopulation(Population<T, U> populace) {
		this.population = populace;
	}

	public int getGeneration() {
		return generation;
	}

	public void setGeneration(int generation) {
		this.generation = generation;
	}

	public T getBestSolution() {
		return best;
	}

	public void updateBestSolution() {
		Double fitness = 0.0;
		T best = null;
		for (T solution : getPopulation()) {
			if (solution.getFitness() > fitness) {
				fitness = solution.getFitness();
				best = solution;
			}
		}
		setBestSolution(best);
	}

	public void setBestSolution(T best) {
		this.best = best;
	}

	public abstract List<T> getResults();

	public void start() {
		init();
		t.start();
	}

	public void stop() {
		try {
			t.join(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		t.interrupt();
	}
}
