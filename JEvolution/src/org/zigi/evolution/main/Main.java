package org.zigi.evolution.main;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Random;

import org.apache.log4j.Logger;
import org.zigi.evolution.algorithm.GeneticProgramming;
import org.zigi.evolution.problem.ArtificialAnt;
import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.solution.TreeSolution;

public class Main {

	private static final Random RAND = new Random();
	private static final Logger LOG = Logger.getLogger(Main.class);

	/**
	 * Provadi krizeni mezi dvema stromy
	 * 
	 * @param sol1
	 *            prvni reseni
	 * @param sol2
	 *            druhe reseni
	 */
	public void crossTreeSolutions(TreeSolution sol1, TreeSolution sol2) {
		int index1 = RAND.nextInt(sol1.size());
		int index2 = RAND.nextInt(sol2.size());

		// samotné křížení
		TreeSolution.changeSubTree(sol1, index1, sol2, index2);
	}

	public void artificialAnt() {
		try {
			ArtificialAnt problem = new ArtificialAnt();
			// problem.setMaxHeight(6);
			// problem.setMaxMoves(420);
			// problem.setYard(new File("resources/artificial_ant"));
			// LOG.info("Drobečků: " + problem.getCrumbs());

			GeneticProgramming alg = new GeneticProgramming();
			alg.setProblem(problem);
			alg.addChangeListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					LOG.info(evt.getNewValue());
				}
			});
			alg.start();

			Solution best = alg.getBestSolution();
			System.out.println("BEST: " + best.toString());
			// int[][] path = problem.getPath(best);

			// print best path
			// ArtificialAnt.printPath(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Náhodné generování stromu požadované hloubky
	 * 
	 * @return
	 */
	public static TreeSolution randomTree(ArtificialAnt problem) {
		return (TreeSolution) problem.randomSolution();
	}

	public static void main(String[] args) {
		Main test = new Main();
		test.artificialAnt();
	}

}
