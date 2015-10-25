package org.zigi.evolution;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.zigi.evolution.solution.CloneableValue;
import org.zigi.evolution.solution.Solution;

public class Population<T extends Solution<U>, U extends CloneableValue<U>> {
	protected List<T> solutions = new LinkedList<T>();
	protected int maxSolutions = 5;
	protected int solutionSize;

	public Population<T, U> bestSolutions(Population<T, U> population, int count) {
		List<T> solutions = population.getSolutions();
		Collections.sort(solutions, new Comparator<T>() {
			public int compare(T o1, T o2) {
				return o2.getFitness().compareTo(o1.getFitness());
			}
		});

		Population<T, U> pop = new Population<T, U>();
		pop.setMaxSolutions(population.getMaxSolutions());
		pop.setSolutionSize(population.getSolutionSize());

		for (int i = 0; i < count; i++) {
			pop.add(solutions.get(i));
		}

		return pop;
	}

	public List<T> getSolutions() {
		return solutions;
	}

	public T getSolution(int index) {
		return solutions.get(index);
	}

	public void setSolutions(List<T> populace) {
		this.solutions = populace;
	}

	public int getMaxSolutions() {
		return maxSolutions;
	}

	public void setMaxSolutions(int max) {
		this.maxSolutions = max;
	}

	public int getSolutionSize() {
		return solutionSize;
	}

	public void setSolutionSize(int solutionSize) {
		this.solutionSize = solutionSize;
	}

	public void add(T solution) {
		solutions.add(solution);
	}

	public int size() {
		return solutions.size();
	}

	public Population<T, U> concat(Population<T, U> population) {
		Population<T, U> pop = new Population<T, U>();
		for (T sol : population.getSolutions()) {
			pop.add(sol);
		}
		for (T sol : getSolutions()) {
			pop.add(sol);
		}
		return pop;
	}

	public Population<T, U> clone() {
		Population<T, U> pop = new Population<T, U>();
		pop.setMaxSolutions(maxSolutions);
		pop.setSolutionSize(solutionSize);
		for (T solution : solutions) {
			pop.add(solution);
		}
		return pop;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Solution<U> item : solutions) {
			sb.append(item + "\n");
		}
		return sb.toString();
	}
}
