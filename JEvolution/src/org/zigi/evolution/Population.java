package org.zigi.evolution;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.zigi.evolution.solution.CloneableValue;
import org.zigi.evolution.solution.Solution;

public class Population<T extends Solution<U>, U extends CloneableValue<U>> implements Collection<T> {
	protected List<T> solutions = new LinkedList<T>();
	protected int solutionSize;
	protected int maxSolutions = 0;
	private static Logger log = Logger.getLogger(Population.class);

	public Population() {

	}

	public Population(int maxPopulations, int solutionSize) {
		this.maxSolutions = maxPopulations;
		this.solutionSize = solutionSize;
	}

	public Population(Population<T, U> population) {
		this.solutionSize = population.getSolutionSize();
		this.maxSolutions = population.getMaxSolutions();
	}

	public Population<T, U> bestSolutions() {
		Collections.sort(solutions, new Comparator<T>() {
			public int compare(T o1, T o2) {
				return o2.getFitness().compareTo(o1.getFitness());
			}
		});

		Population<T, U> pop = new Population<T, U>(this);

		for (int i = 0; i < maxSolutions; i++) {
			pop.add(solutions.get(i));
		}
		return pop;
	}

	public Population<T, U> concat(Population<T, U> population) {
		Population<T, U> pop = new Population<T, U>(population);
		for (T sol : population.getSolutions()) {
			pop.add(sol);
		}
		for (T sol : getSolutions()) {
			pop.add(sol);
		}
		return pop;
	}

	public void setMaxSolutions(int max) {
		this.maxSolutions = max;
	}

	public int getMaxSolutions() {
		return maxSolutions;
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

	public int getSolutionSize() {
		return solutionSize;
	}

	public void setSolutionSize(int solutionSize) {
		this.solutionSize = solutionSize;
	}

	public boolean add(T solution) {
		return solutions.add(solution);
	}

	public int size() {
		return solutions.size();
	}

	public boolean isEmpty() {
		return solutions.isEmpty();
	}

	public boolean contains(Object o) {
		return solutions.contains(o);
	}

	public Iterator<T> iterator() {
		return solutions.iterator();
	}

	public Object[] toArray() {
		return solutions.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return solutions.toArray(a);
	}

	public boolean remove(Object o) {
		return solutions.remove(o);
	}

	public boolean containsAll(Collection<?> c) {
		return solutions.containsAll(c);
	}

	public boolean addAll(Collection<? extends T> c) {
		return solutions.addAll(c);
	}

	public boolean removeAll(Collection<?> c) {
		return solutions.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return solutions.retainAll(c);
	}

	public void clear() {
		solutions.clear();
	}

	public Population<T, U> clone() {
		Population<T, U> pop = new Population<T, U>();
		pop.setSolutionSize(solutionSize);
		for (T solution : solutions) {
			pop.add(solution);
		}
		return pop;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Population\n" + StringUtils.repeat("-", 50) + "\n");
		for (Solution<U> item : solutions) {
			sb.append(item + "\n");
		}
		return sb.toString();
	}
}
