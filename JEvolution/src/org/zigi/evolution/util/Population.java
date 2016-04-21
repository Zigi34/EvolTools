package org.zigi.evolution.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.zigi.evolution.solution.Solution;

public class Population implements Cloneable<Population>, Comparator<Solution>, List<Solution> {

	private List<Solution> solutions = new LinkedList<Solution>();
	private int maxSolutions;
	private Double sumFunctionValue = 0.0;

	public static final int DEFAULT_SIZE = 20;

	public Population() {
		this(DEFAULT_SIZE);
	}

	public Population(int size) {
		this.maxSolutions = size;
	}

	public void addAll(List<Solution> solutions) {
		if ((this.solutions.size() + solutions.size()) <= maxSolutions)
			this.solutions.addAll(solutions);
	}

	public void clear() {
		solutions.clear();
	}

	public void sort() {
		Collections.sort(solutions, this);
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
	public int getMaxSolutions() {
		return maxSolutions;
	}

	public void setMax(int max) {
		this.maxSolutions = max;
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
		Double val1 = o1.getFunctionValue();
		Double val2 = o2.getFunctionValue();
		if (val1.isNaN())
			val1 = Double.MIN_VALUE;
		if (val2.isNaN())
			val2 = Double.MIN_VALUE;
		return val1.compareTo(val2);
	}

	public Double getSumFunctionValue() {
		return sumFunctionValue;
	}

	public void setSumFunctionValue(Double sumFunctionValue) {
		this.sumFunctionValue = sumFunctionValue;
	}

	@Override
	public boolean isEmpty() {
		return solutions.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return solutions.contains(o);
	}

	@Override
	public Iterator<Solution> iterator() {
		return solutions.iterator();
	}

	@Override
	public Object[] toArray() {
		return solutions.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return solutions.toArray(a);
	}

	@Override
	public boolean add(Solution e) {
		if (solutions.size() < maxSolutions) {
			return solutions.add(e);
		}
		return false;
	}

	@Override
	public boolean remove(Object o) {
		return solutions.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return solutions.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends Solution> c) {
		return solutions.addAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return solutions.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return solutions.retainAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends Solution> c) {
		return solutions.addAll(index, c);
	}

	@Override
	public Solution get(int index) {
		return solutions.get(index);
	}

	@Override
	public Solution set(int index, Solution element) {
		return solutions.set(index, element);
	}

	@Override
	public void add(int index, Solution element) {
		solutions.add(index, element);
	}

	@Override
	public Solution remove(int index) {
		return solutions.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return solutions.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return solutions.lastIndexOf(o);
	}

	@Override
	public ListIterator<Solution> listIterator() {
		return solutions.listIterator();
	}

	@Override
	public ListIterator<Solution> listIterator(int index) {
		return solutions.listIterator(index);
	}

	@Override
	public List<Solution> subList(int fromIndex, int toIndex) {
		return solutions.subList(fromIndex, toIndex);
	}
}
