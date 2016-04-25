package org.evolution.main;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.evolution.algorithm.GeneticProgramming;
import org.evolution.cross.CrossFunction;
import org.evolution.cross.OnePointTreeCross;
import org.evolution.problem.ArtificialAnt;
import org.evolution.problem.RegressionProblem;
import org.evolution.select.SelectFunction;
import org.evolution.select.TournamentSelection;
import org.evolution.solution.Solution;
import org.evolution.solution.TreeSolution;
import org.evolution.solution.type.CosFunction;
import org.evolution.solution.type.DivideFunction;
import org.evolution.solution.type.MultiplyFunction;
import org.evolution.solution.type.NumericConstant;
import org.evolution.solution.type.PowerFunction;
import org.evolution.solution.type.SinFunction;
import org.evolution.solution.type.SubtractionFunction;
import org.evolution.solution.type.SumFunction;
import org.evolution.terminate.FitnessTerminate;
import org.evolution.util.Population;
import org.evolution.util.Position;

public class Main {

	private static final Random RAND = new Random();
	private static final Logger LOG = Logger.getLogger(Main.class);
	private static final DecimalFormat FITNESS_FORMAT = new DecimalFormat("#.####");

	public void crossTreeSolutions(TreeSolution sol1, TreeSolution sol2) {
		int index1 = RAND.nextInt(sol1.size());
		int index2 = RAND.nextInt(sol2.size());

		// samotné křížení
		TreeSolution.changeSubTree(sol1, index1, sol2, index2);
	}

	public void artificialAnt() {
		int maxHeight = 6;
		int maxMoves = 440;
		double mutateProb = 0.05;
		double crossProb = 0.95;
		int generation = 100;
		int populationSize = 500;
		int tournamentSize = 6;

		try {
			ArtificialAnt problem = new ArtificialAnt();
			problem.setMaxHeight(maxHeight);
			problem.setMaxMoves(maxMoves);
			problem.setYard(new File("resources/artificial_ant"));
			problem.setMinFunctionValue(0.0);
			problem.setMaxFunctionValue(89.0);
			GeneticProgramming alg = new GeneticProgramming();
			alg.setGeneration(generation);
			alg.setMutateProbability(mutateProb);
			alg.setCrossProbrability(crossProb);
			Population pop = new Population(populationSize);
			alg.setPopulation(pop);
			alg.setProblem(problem);
			TournamentSelection select = new TournamentSelection();
			select.setTournamentSize(tournamentSize);
			alg.setSelect(select);
			CrossFunction cross = new OnePointTreeCross();
			alg.setCross(cross);
			alg.addChangeListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					if (evt.getNewValue().equals(GeneticProgramming.NEW_POPULATION)) {
						GeneticProgramming gp = (GeneticProgramming) evt.getSource();
						LOG.info(gp.getActualGeneration() + " - (" + problem.getNormalizedFitness(gp.getBestSolution())
								+ ") " + gp.getBestSolution());
					} else if (evt.getNewValue().equals(GeneticProgramming.ALGORITHM_TERMINATED)) {
						GeneticProgramming gp = (GeneticProgramming) evt.getSource();
						List<Position> path = problem.generatePath(gp.getBestSolution());
						int[][] yard = new int[32][32];
						for (Position pos : path) {
							yard[pos.getY()][pos.getX()] = 1;
							// LOG.info(pos);
						}
						StringBuilder sb = new StringBuilder();
						sb.append("\n");
						for (int i = 0; i < 32; i++) {
							for (int j = 0; j < 32; j++) {
								if (yard[i][j] == 1)
									sb.append("x");
								else
									sb.append(".");
							}
							sb.append("\n");
						}
						LOG.info(sb.toString());
					}

				}
			});
			alg.start();

			Solution best = alg.getBestSolution();
			System.out.println("BEST: " + best.toString());
			// int[][] path = problem.getPath(best);

			// print best path
			// ArtificialAnt.printPath(path);
		} catch (Exception e) {
			LOG.error(e);
		}
	}

	public void sextic() {
		try {
			// LOG.info(
			// "problem;best-function;squared-error;fitness;population-size;max-generation;tree-height;select-function;cross-probability;mutate-probability;cross-function;max-error;min-error");
			List<Map<Integer, Double>> stat = new LinkedList<Map<Integer, Double>>();
			StringBuilder bests = new StringBuilder();

			int tests = 20;
			int maxGen = 500;
			int popSize = 200;
			int height = 6;
			int tournamentSize = 10;

			for (int i = 0; i < tests; i++) {
				Map<Integer, Double> st = new HashMap<Integer, Double>();
				RegressionProblem problem = new RegressionProblem();
				problem.setDatasetPath("resources/quintic");
				problem.setMaxHeight(height);

				GeneticProgramming gp = new GeneticProgramming();
				gp.setMutateProbability(0.28);
				gp.setCrossProbrability(0.7);
				gp.addChangeListener(new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (evt.getNewValue().equals(GeneticProgramming.ALGORITHM_TERMINATED)) {

							Solution best = gp.getBestSolution();
							Double funVal = best.getFunctionValue();
							Population pop = gp.getPopulation();
							Double fitness = problem.getNormalizedFitness(best);
							int sols = pop.getMaxSolutions();
							int gen = gp.getGeneration();
							int height = problem.getMaxHeight();
							SelectFunction select = gp.getSelect();
							Double mutateProb = gp.getMutateProbability();
							double crossProb = gp.getCrossProbrability();
							CrossFunction cross = gp.getCross();
							bests.append("=" + RegressionProblem.printFunction((TreeSolution) best) + ";");
						} else if (evt.getNewValue().equals(GeneticProgramming.NEW_BEST_SOLUTION)) {

						} else if (evt.getNewValue().equals(GeneticProgramming.NEW_POPULATION)) {
							st.put(gp.getActualGeneration(), problem.getNormalizedFitness(gp.getBestSolution()));
							// LOG.info(gp.getActualGeneration());
							LOG.info(String.format("%s;%s;%s;%s", gp.getActualGeneration(),
									gp.getBestSolution().getFunctionValue(),
									problem.getNormalizedFitness(gp.getBestSolution()), gp.getBestSolution()));
						}
					}
				});
				Population pop = new Population(popSize);
				problem.setMinFunctionValue(0.0);
				problem.setMaxFunctionValue(2.0);
				gp.setPopulation(pop);
				gp.setProblem(problem);
				gp.setGeneration(maxGen);
				TournamentSelection select = new TournamentSelection();
				select.setTournamentSize(tournamentSize);
				gp.setSelect(select);
				CrossFunction cross = new OnePointTreeCross();
				gp.setCross(cross);

				gp.run();
				stat.add(st);
			}

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < maxGen; i++) {
				sb.append(i + ";");
				for (int j = 0; j < stat.size(); j++) {
					sb.append(stat.get(j).get(i) + ";");
				}
				sb.append("\n");
			}

			printToFile("1", sb.toString());
			printToFile("2", bests.toString());
		} catch (Exception e) {
			LOG.error(e);
		}

	}

	private void printToFile(String path, String data) throws IOException {
		File file = new File(path);
		FileWriter sw = new FileWriter(file);
		sw.write(data);
		sw.close();
	}

	public void regression() {
		try {
			RegressionProblem problem = new RegressionProblem();
			problem.setDatasetPath("resources/quintic");
			problem.addFenotype(new SinFunction());
			problem.addFenotype(new CosFunction());
			problem.addFenotype(new SumFunction());
			problem.addFenotype(new SubtractionFunction());
			problem.addFenotype(new MultiplyFunction());
			problem.addFenotype(new PowerFunction());
			problem.addFenotype(new DivideFunction());
			// problem.addFenotype(new NumericValue());
			NumericConstant const1 = new NumericConstant(-2.0, 10.0);
			problem.addFenotype(const1);

			int index = 1;
			for (; index < 20; index++) {
				GeneticProgramming gp = new GeneticProgramming();
				gp.addChangeListener(new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (evt.getNewValue().equals(GeneticProgramming.ALGORITHM_TERMINATED)) {
							LOG.info(String.format("(%s) BEST: %s", gp.getActualGeneration(), gp.getBestSolution()));
						}
					}
				});
				gp.addTerminateFunction(new FitnessTerminate(20.0));
				Population pop = new Population(200);
				gp.setPopulation(pop);
				gp.setProblem(problem);
				gp.setGeneration(1000);
				gp.start();
			}
		} catch (Exception e) {
			LOG.error(e);
		}
	}

	public static TreeSolution randomTree(ArtificialAnt problem) {
		return (TreeSolution) problem.randomSolution();
	}

	public static void main(String[] args) throws IOException {
		Main test = new Main();
		// test.artificialAnt();
		// test.regression();
		test.sextic2();
	}

	public void sextic2() {
		try {
			int maxGen = 500;
			int popSize = 20;
			int height = 6;
			int tournamentSize = 2;

			RegressionProblem problem = new RegressionProblem();
			problem.setDatasetPath("resources/quintic");
			problem.setMaxHeight(height);

			GeneticProgramming gp = new GeneticProgramming();
			gp.setMutateProbability(0.05);
			gp.setCrossProbrability(0.85);
			gp.addChangeListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					if (evt.getNewValue().equals(GeneticProgramming.ALGORITHM_TERMINATED)) {
						Solution best = gp.getBestSolution();
						Double funVal = best.getFunctionValue();
						Population pop = gp.getPopulation();
						Double fitness = problem.getNormalizedFitness(best);
						int sols = pop.getMaxSolutions();
						int gen = gp.getGeneration();
						int height = problem.getMaxHeight();
						SelectFunction select = gp.getSelect();
						Double mutateProb = gp.getMutateProbability();
						double crossProb = gp.getCrossProbrability();
						CrossFunction cross = gp.getCross();
					} else if (evt.getNewValue().equals(GeneticProgramming.NEW_BEST_SOLUTION)) {
						LOG.info(gp.getBestSolution());
					} else if (evt.getNewValue().equals(GeneticProgramming.NEW_POPULATION)) {
						LOG.info(String.format("%s;%s;%s;%s", gp.getActualGeneration(),
								gp.getBestSolution().getFunctionValue(),
								problem.getNormalizedFitness(gp.getBestSolution()), gp.getBestSolution()));
					}
				}
			});
			Population pop = new Population(popSize);
			problem.setMinFunctionValue(0.0);
			problem.setMaxFunctionValue(2.0);
			gp.setPopulation(pop);
			gp.setProblem(problem);
			// gp.setGeneration(maxGen);
			// TournamentSelection select = new TournamentSelection();
			// select.setTournamentSize(tournamentSize);
			// gp.setSelect(select);
			CrossFunction cross = new OnePointTreeCross();
			gp.setCross(cross);

			gp.run();

		} catch (Exception e) {
			LOG.error(e);
		}

	}
}
