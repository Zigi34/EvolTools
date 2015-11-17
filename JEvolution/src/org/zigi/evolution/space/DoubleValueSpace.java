package org.zigi.evolution.space;

import java.util.Random;

import org.zigi.evolution.solution.DoubleValue;
import org.zigi.evolution.solution.array.ArraySolution;

public class DoubleValueSpace extends SolutionSpace<ArraySolution<DoubleValue>, DoubleValue> {

	private Random rand = new Random();

	@Override
	public ArraySolution<DoubleValue> randomSolution(int size) {
		ArraySolution<DoubleValue> solution = new ArraySolution<DoubleValue>();
		for (int i = 0; i < size; i++)
			solution.addChildNode(randomValue());
		return solution;
	}

	@Override
	public DoubleValue randomValue() {
		return new DoubleValue(rand.nextDouble());
	}

	@Override
	public String toString() {
		return "SPACE[Double Value Space]\n";
	}

}
