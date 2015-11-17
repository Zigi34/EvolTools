package org.zigi.evolution.mutate;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.zigi.evolution.Population;
import org.zigi.evolution.solution.CloneableValue;
import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.solution.array.ArraySolution;
import org.zigi.evolution.space.SolutionSpace;

/**
 * Projde vsechny zadane jedince a u kazdeho projde vsechny genomy. Kazdy genom
 * potom muze nahodne zmutovanim pozmenit.
 * 
 * @author Zdenek Gold
 *
 * @param <T>
 *            typ jedince
 */
public class SimpleMutate<T extends CloneableValue<T>> extends MutateFunction<ArraySolution<T>, T> {
	private Double mutateProbability = 0.2;
	private Random rand = new Random();
	private static Logger log = Logger.getLogger(SimpleMutate.class);

	@Override
	public void mutate(Population<ArraySolution<T>, T> population) {
		mutate(population.getSolutions());
	}

	@Override
	public void mutate(List<ArraySolution<T>> solutions) {
		List<Integer> keys = new LinkedList<Integer>();
		for (Solution<T> item : solutions) {
			keys.clear();

			for (T key : item.getChildNodes()) {
				log.info(key + ", ");
			}

			for (Integer index = 0; index < item.getChildNodes().size(); index++) {
				if (rand.nextDouble() < mutateProbability) {
					keys.add(index);
				}
			}

			for (Integer index = 0; index < item.getChildNodes().size(); index++) {
				if (keys.contains(index))
					item.setChildNode(index, space.randomValue());
			}
		}
	}

	public SolutionSpace<ArraySolution<T>, T> getSpace() {
		return space;
	}

	public void setSpace(SolutionSpace<ArraySolution<T>, T> space) {
		this.space = space;
	}

	public Double getMutateProbability() {
		return mutateProbability;
	}

	public void setMutateProbability(Double mutateProbability) {
		this.mutateProbability = mutateProbability;
	}

	@Override
	public String toString() {
		return String.format("MUTATE[Simple Mutate] (prob: %.3f)\n", getMutateProbability());
	}
}