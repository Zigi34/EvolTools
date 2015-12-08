package org.zigi.evolution.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.solution.value.Cloneable;

public class Population implements Cloneable<Population>, Comparator<Solution> {

	private List<Solution> solutions = new LinkedList<Solution>();
	private int max;

	public static final int DEFAULT_SIZE = 20;

	public Population() {
		this(DEFAULT_SIZE);
	}

	public Population(int size) {
		this.max = size;
	}

	public void add(Solution sol) {
		if (solutions.size() < max)
			solutions.add(sol);
	}

	public void addAll(List<Solution> solutions) {
		if ((this.solutions.size() + solutions.size()) <= max)
			this.solutions.addAll(solutions);
	}

	public void clear() {
		solutions.clear();
	}

	public void sort() {
		Collections.sort(solutions, this);
	}

	/**
	 * Vrací průměrnou hodnotu fitness v populaci
	 * 
	 * @return
	 */
	public Double getAverageFitness() {
		Double sum = 0.0;
		for (Solution sol : solutions) {
			if (sol.getFitness() == null)
				return null;
			else
				sum += sol.getFitness();
		}
		return sum / size();
	}

	/**
	 * Vrací seznam všech řešení
	 * 
	 * @return
	 */
	public List<Solution> getSolutions() {
		return solutions;
	}

	public void setSolutions(List<Solution> solutions) {
		this.solutions = solutions;
	}

	/**
	 * Maximální počet řešení v populaci
	 * 
	 * @return
	 */
	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	/**
	 * Počet řešení v populaci
	 * 
	 * @return
	 */
	public int size() {
		return solutions.size();
	}

	public Population cloneMe() {
		Population pop = new Population();
		for (Solution sol : solutions) {
			pop.add(sol.cloneMe());
		}
		return pop;
	}

	public int compare(Solution o1, Solution o2) {
		return o1.getFitness().compareTo(o2.getFitness());
	}
}
