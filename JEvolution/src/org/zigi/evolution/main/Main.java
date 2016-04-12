package org.zigi.evolution.main;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import org.apache.log4j.Logger;
import org.zigi.evolution.algorithm.GeneticProgramming;
import org.zigi.evolution.algorithm.terminate.FitnessTerminate;
import org.zigi.evolution.cross.CrossFunction;
import org.zigi.evolution.cross.OnePointTreeCross;
import org.zigi.evolution.problem.ArtificialAnt;
import org.zigi.evolution.problem.RegressionProblem;
import org.zigi.evolution.select.TournamentSelection;
import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.solution.TreeSolution;
import org.zigi.evolution.solution.value.CosFunction;
import org.zigi.evolution.solution.value.DivideFunction;
import org.zigi.evolution.solution.value.MultiplyFunction;
import org.zigi.evolution.solution.value.NumericConstant;
import org.zigi.evolution.solution.value.PowerFunction;
import org.zigi.evolution.solution.value.RangedPowerFunction;
import org.zigi.evolution.solution.value.SinFunction;
import org.zigi.evolution.solution.value.SubtractionFunction;
import org.zigi.evolution.solution.value.SumFunction;
import org.zigi.evolution.util.Population;

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

	public void sextic() {

		try {
			RegressionProblem problem = new RegressionProblem();
			problem.loadDataset(new File("resources/quintic"), ";");
			problem.setMaxHeight(6);
			problem.addFenotype(new SumFunction());
			problem.addFenotype(new SubtractionFunction());
			problem.addFenotype(new MultiplyFunction());
			// problem.addFenotype(new DivideFunction());
			problem.addFenotype(new RangedPowerFunction(2.0, 5.0, true));
			problem.addFenotype(new NumericConstant(-4.0, 4.0));

			GeneticProgramming gp = new GeneticProgramming();
			gp.addChangeListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					if (evt.getNewValue().equals(GeneticProgramming.ALGORITHM_TERMINATED)) {
						LOG.info(String.format("(%s) BEST: %s", gp.getActualGeneration(), gp.getBestSolution()));
						// Util.logPopulation(gp.getPopulation());
					} else if (evt.getNewValue().equals(GeneticProgramming.NEW_BEST_SOLUTION)) {
						LOG.info(String.format("(%s) BEST: %s", gp.getActualGeneration(), gp.getBestSolution()));
					} else if (evt.getNewValue().equals(GeneticProgramming.EVALUATE_POPULATION_END)) {
						LOG.info("generation: " + gp.getActualGeneration());
					}
				}
			});
			// gp.addTerminateFunction(new FitnessTerminate(1.0));
			Population pop = new Population(500);
			gp.setPopulation(pop);
			gp.setProblem(problem);
			gp.setGeneration(100);
			TournamentSelection select = new TournamentSelection();
			select.setTournamentSize(10);
			gp.setSelect(select);
			CrossFunction cross = new OnePointTreeCross();
			gp.setCross(cross);
			gp.start();
		} catch (Exception e) {
			LOG.error(e);
		}
	}

	public void regression() {
		try {
			RegressionProblem problem = new RegressionProblem();
			problem.loadDataset(new File("resources/regression"), ";");
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

	/**
	 * Náhodné generování stromu požadované hloubky
	 * 
	 * @return
	 */
	public static TreeSolution randomTree(ArtificialAnt problem) {
		return (TreeSolution) problem.randomSolution();
	}

	public static void main(String[] args) throws IOException {
		Main test = new Main();
		// test.artificialAnt();
		// test.regression();
		test.sextic();

	}

	/**
	 * The extension of the given file name is replaced with "ptx". If the file
	 * with the resulting name does not exist, it is compiled from the given
	 * file using NVCC. The name of the PTX file is returned.
	 *
	 * @param cuFileName
	 *            The name of the .CU file
	 * @return The name of the PTX file
	 * @throws IOException
	 *             If an I/O error occurs
	 */
	private static String preparePtxFile(String cuFileName) throws IOException {
		int endIndex = cuFileName.lastIndexOf('.');
		if (endIndex == -1) {
			endIndex = cuFileName.length() - 1;
		}
		String ptxFileName = cuFileName.substring(0, endIndex + 1) + "ptx";
		File ptxFile = new File(ptxFileName);
		if (ptxFile.exists()) {
			return ptxFileName;
		}

		File cuFile = new File(cuFileName);
		if (!cuFile.exists()) {
			throw new IOException("Input file not found: " + cuFileName);
		}
		String modelString = "-m" + System.getProperty("sun.arch.data.model");
		String command = "nvcc " + modelString + " -ptx " + cuFile.getPath() + " -o " + ptxFileName;

		System.out.println("Executing\n" + command);
		Process process = Runtime.getRuntime().exec(command);

		String errorMessage = new String(toByteArray(process.getErrorStream()));
		String outputMessage = new String(toByteArray(process.getInputStream()));
		int exitValue = 0;
		try {
			exitValue = process.waitFor();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new IOException("Interrupted while waiting for nvcc output", e);
		}

		if (exitValue != 0) {
			System.out.println("nvcc process exitValue " + exitValue);
			System.out.println("errorMessage:\n" + errorMessage);
			System.out.println("outputMessage:\n" + outputMessage);
			throw new IOException("Could not create .ptx file: " + errorMessage);
		}

		System.out.println("Finished creating PTX file");
		return ptxFileName;
	}

	/**
	 * Fully reads the given InputStream and returns it as a byte array
	 *
	 * @param inputStream
	 *            The input stream to read
	 * @return The byte array containing the data from the input stream
	 * @throws IOException
	 *             If an I/O error occurs
	 */
	private static byte[] toByteArray(InputStream inputStream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte buffer[] = new byte[8192];
		while (true) {
			int read = inputStream.read(buffer);
			if (read == -1) {
				break;
			}
			baos.write(buffer, 0, read);
		}
		return baos.toByteArray();
	}
}
