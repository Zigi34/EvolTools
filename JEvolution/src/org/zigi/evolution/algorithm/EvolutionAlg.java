package org.zigi.evolution.algorithm;

import org.zigi.evolution.problem.Problem;
import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.util.Population;

public abstract class EvolutionAlg implements Runnable {

	private Population pop;
	private boolean terminate;
	private Problem problem;
	private Solution bestSolution;

	public void start() throws Exception {
		terminate = false;

		if (problem == null)
			throw new Exception("Problem není nastavený");

		if (pop == null)
			fillPopulation();

		run();
	}

	private void fillPopulation() {
		pop = new Population();
		for (int i = 0; i < pop.getMax(); i++)
			pop.add(problem.randomSolution());
	}

	public void stop() {
		terminate = true;
	}

	public Population getPop() {
		return pop;
	}

	public void setPop(Population pop) {
		this.pop = pop;
	}

	public void setPopulationSize(int size) {
		pop = new Population(size);
	}

	public boolean isTerminate() {
		return terminate;
	}

	public void setTerminate(boolean terminate) {
		this.terminate = terminate;
	}

	public void restart() throws Exception {
		terminate = false;
		start();
	}

	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	public Solution getBestSolution() {
		return bestSolution;
	}

	/**
	 * Prochazi celou populaci a hleda reseni, ktera jsou lepsi nez aktualne
	 * nejlepsi reseni. Pokud takove reseni nalezne, nahradi posledni nejlepsi
	 * reseni
	 * 
	 * @param population
	 */
	public void checkBestSolution(Population population) {
		if (this.bestSolution == null)
			this.bestSolution = population.getSolutions().get(0);

		for (Solution solution : population.getSolutions()) {
			if (solution.getFitness() != null && solution.getFitness() > this.bestSolution.getFitness()) {
				this.bestSolution = solution.cloneMe();
				System.out.println("Best: " + this.bestSolution.getClass());
			}
		}
	}

}
