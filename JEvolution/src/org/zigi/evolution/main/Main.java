package org.zigi.evolution.main;

import java.util.Random;

import org.zigi.evolution.algorithm.GeneticProgramming;
import org.zigi.evolution.cross.TreeCrossFunction;
import org.zigi.evolution.mutate.TreeMutate;
import org.zigi.evolution.problem.ArtificialAnt;
import org.zigi.evolution.select.RankSelectFunction;
import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.solution.TreeSolution;
import org.zigi.evolution.solution.value.IfFoodAhead;
import org.zigi.evolution.solution.value.LeftDirection;
import org.zigi.evolution.solution.value.Move;
import org.zigi.evolution.solution.value.Prg2;
import org.zigi.evolution.solution.value.Prg3;
import org.zigi.evolution.solution.value.RightDirection;

public class Main {

	private static final Random RAND = new Random();

	public void crossTreeSolutions(TreeSolution sol1, TreeSolution sol2) {
		System.out.println("První strom");
		System.out.println(sol1);

		System.out.println("Druhý strom");
		System.out.println(sol2);

		int index1 = RAND.nextInt(sol1.size());
		int index2 = RAND.nextInt(sol2.size());
		System.out
				.println(String.format("Křízení %s uzlu v prvním stromě s %s uzlem ve druhém stromě", index1, index2));

		// samotné křížení
		TreeSolution.changeSubTree(sol1, index1, sol2, index2);

		System.out.println("První strom");
		System.out.println(sol1);

		System.out.println("Druhý strom");
		System.out.println(sol2);
	}

	public void artificialAnt() {
		try {
			int width = 32;
			int height = 32;
			int[][] array = new int[height][width];
			array[0][0] = 1;
			array[0][1] = 1;
			array[0][2] = 1;
			array[0][3] = 1;
			array[1][3] = 1;
			array[2][3] = 1;
			array[2][25] = 1;
			array[2][26] = 1;
			array[2][27] = 1;
			array[3][3] = 1;
			array[3][24] = 1;
			array[3][29] = 1;
			array[4][3] = 1;
			array[4][24] = 1;
			array[4][29] = 1;
			array[5][3] = 1;
			array[5][4] = 1;
			array[5][5] = 1;
			array[5][6] = 1;
			array[5][8] = 1;
			array[5][9] = 1;
			array[5][10] = 1;
			array[5][11] = 1;
			array[5][12] = 1;
			array[5][21] = 1;
			array[5][22] = 1;
			array[6][12] = 1;
			array[6][29] = 1;
			array[7][12] = 1;
			array[8][12] = 1;
			array[8][20] = 1;
			array[9][12] = 1;
			array[9][20] = 1;
			array[9][29] = 1;
			array[10][12] = 1;
			array[10][20] = 1;
			array[11][20] = 1;
			array[12][12] = 1;
			array[12][29] = 1;
			array[13][12] = 1;
			array[14][12] = 1;
			array[14][20] = 1;
			array[14][26] = 1;
			array[14][27] = 1;
			array[14][28] = 1;
			array[15][12] = 1;
			array[15][20] = 1;
			array[15][23] = 1;
			array[16][17] = 1;
			array[17][16] = 1;
			array[18][12] = 1;
			array[18][16] = 1;
			array[18][24] = 1;
			array[19][12] = 1;
			array[19][16] = 1;
			array[19][27] = 1;
			array[20][12] = 1;
			array[21][12] = 1;
			array[21][16] = 1;
			array[22][12] = 1;
			array[22][26] = 1;
			array[23][23] = 1;
			array[24][3] = 1;
			array[24][4] = 1;
			array[24][7] = 1;
			array[24][8] = 1;
			array[24][9] = 1;
			array[24][10] = 1;
			array[24][11] = 1;
			array[24][16] = 1;
			array[25][1] = 1;
			array[25][16] = 1;
			array[26][1] = 1;
			array[26][16] = 1;
			array[27][1] = 1;
			array[27][16] = 1;
			array[28][1] = 1;
			array[28][8] = 1;
			array[28][9] = 1;
			array[28][10] = 1;
			array[28][11] = 1;
			array[28][12] = 1;
			array[28][13] = 1;
			array[28][14] = 1;
			array[29][1] = 1;
			array[29][7] = 1;
			array[30][7] = 1;
			array[31][2] = 1;
			array[31][3] = 1;
			array[31][4] = 1;
			array[31][5] = 1;

			ArtificialAnt problem = new ArtificialAnt();
			problem.setMaxDeepSize(6);
			problem.setMaxMoves(440);
			problem.setArray(array);

			problem.addFenotype(new LeftDirection());
			problem.addFenotype(new RightDirection());
			problem.addFenotype(new Move());
			problem.addFenotype(new IfFoodAhead());
			problem.addFenotype(new Prg2());
			problem.addFenotype(new Prg3());

			Solution sol = problem.randomSolution();

			GeneticProgramming alg = new GeneticProgramming();
			alg.setPopulationSize(60);
			alg.setGeneration(200);
			alg.setProblem(problem);
			alg.setCross(new TreeCrossFunction());
			alg.setMutate(new TreeMutate(problem));
			alg.setSelect(new RankSelectFunction());
			alg.start();

			Solution best = alg.getBestSolution();
			System.out.println("[" + best.getFitness() + "]" + best);
			System.out.println("\n");
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
		// test.crossTreeSolutions(sol1, sol2);

		// ArtificialAnt problem = new ArtificialAnt();
		// TreeSolution tree1 = randomTree(problem);
		// TreeSolution tree2 = randomTree(problem);

		// cross for 2 trees
		// test.crossTreeSolutions(tree1, tree2);
		test.artificialAnt();
	}

}
