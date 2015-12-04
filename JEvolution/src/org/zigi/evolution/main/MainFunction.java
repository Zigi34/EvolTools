package org.zigi.evolution.main;

import org.apache.log4j.Logger;
import org.zigi.evolution.Population;
import org.zigi.evolution.algorithm.ga.GA;
import org.zigi.evolution.cross.OnePointCross;
import org.zigi.evolution.evaluate.ArtificialAnt;
import org.zigi.evolution.evaluate.DeJong1;
import org.zigi.evolution.evaluate.ExpressionFunction;
import org.zigi.evolution.mutate.SimpleMutate;
import org.zigi.evolution.select.RouleteWheelSelect;
import org.zigi.evolution.solution.DoubleValue;
import org.zigi.evolution.solution.array.ArraySolution;
import org.zigi.evolution.solution.tree.ConstantValue;
import org.zigi.evolution.solution.tree.CosinusFunction;
import org.zigi.evolution.solution.tree.IfFootAhead;
import org.zigi.evolution.solution.tree.Move;
import org.zigi.evolution.solution.tree.MultiplicationFunction;
import org.zigi.evolution.solution.tree.PRG2;
import org.zigi.evolution.solution.tree.PlusFunction;
import org.zigi.evolution.solution.tree.SinusFunction;
import org.zigi.evolution.solution.tree.TreeSolution;
import org.zigi.evolution.solution.tree.TurnLeft;
import org.zigi.evolution.solution.tree.TurnRight;
import org.zigi.evolution.solution.tree.VariableValue;
import org.zigi.evolution.space.DoubleValueSpace;

public class MainFunction {

	public static Logger log = Logger.getLogger(MainFunction.class);

	public static void main(String[] args) {
		// solutionTree();
		// gaAlgorithm();
		// artificialAnt();
	}

	private static void neco() {
		GA<DoubleValue> alg = new GA<DoubleValue>();

	}

	private static void artificialAnt() {
		TreeSolution solution = new TreeSolution();
		solution.addChildNode(new IfFootAhead());
		solution.addChildNode(new Move());
		solution.addChildNode(new PRG2());
		solution.addChildNode(new TurnRight());
		solution.addChildNode(new IfFootAhead());
		solution.addChildNode(new Move());
		solution.addChildNode(new PRG2());
		solution.addChildNode(new PRG2());
		solution.addChildNode(new TurnLeft());
		solution.addChildNode(new TurnLeft());
		solution.addChildNode(new IfFootAhead());
		solution.addChildNode(new Move());
		solution.addChildNode(new PRG2());
		solution.addChildNode(new TurnRight());
		solution.addChildNode(new Move());

		ArtificialAnt fce = new ArtificialAnt();
		fce.setMaxOperation(80);
		Integer[][] array = new Integer[6][6];
		for (int x = 0; x < array.length; x++)
			for (int y = 0; y < array[x].length; y++)
				array[x][y] = 0;
		array[0][1] = 1;
		array[1][1] = 1;
		array[1][2] = 1;
		array[2][3] = 1;
		array[3][3] = 1;
		array[4][5] = 1;
		array[5][2] = 1;
		array[5][4] = 1;
		array[5][5] = 1;
		fce.setArray(array);
		Double value = fce.evaluate(solution);
		log.info("Hodnota=" + value);
	}

	private static void solutionTree() {
		TreeSolution solution = new TreeSolution();
		solution.addChildNode(new PlusFunction());
		solution.addChildNode(new MultiplicationFunction());
		solution.addChildNode(new ConstantValue(100.0));
		solution.addChildNode(new SinusFunction());
		solution.addChildNode(new ConstantValue(2.5));
		solution.addChildNode(new CosinusFunction());
		solution.addChildNode(new VariableValue("x"));

		ExpressionFunction fce = new ExpressionFunction();
		Double value = fce.evaluate(solution);
		log.info("Výsledek: " + value);
	}

	private static void gaAlgorithm() {
		int populationSize = 20;
		int solutionSize = 5;
		int generation = 100;
		double mutateProbability = 0.1;
		double crossProbability = 0.75;

		GA<DoubleValue> alg = new GA<DoubleValue>();
		// Define problem
		alg.setFunction(new DeJong1());

		// set space
		DoubleValueSpace space = new DoubleValueSpace();
		alg.setSpace(space);

		// set select
		alg.setSelect(new RouleteWheelSelect<DoubleValue>());

		// set mutate operation
		SimpleMutate<DoubleValue> mutate = new SimpleMutate<DoubleValue>();
		mutate.setMutateProbability(mutateProbability);
		alg.setMutate(mutate);

		// set cross operation
		OnePointCross<DoubleValue> cross = new OnePointCross<DoubleValue>();
		cross.setCrossProbability(crossProbability);
		alg.setCross(cross);

		// init population
		Population<ArraySolution<DoubleValue>, DoubleValue> population = new Population<ArraySolution<DoubleValue>, DoubleValue>(
				populationSize, solutionSize);
		for (int i = 0; i < populationSize; i++)
			population.add(space.randomSolution(population.getSolutionSize()));

		// set population
		alg.setPopulation(population);

		// generation
		alg.setGeneration(generation);

		log.debug(alg);
		// set cross operation
		alg.start();
	}

}
