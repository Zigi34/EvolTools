package org.zigi.evolution.algorithm;

import org.apache.log4j.Logger;
import org.zigi.evolution.cross.CrossFunction;
import org.zigi.evolution.cross.TreeCross;
import org.zigi.evolution.mutate.MutateFunction;
import org.zigi.evolution.mutate.TreeMutate;
import org.zigi.evolution.problem.TreeProblem;
import org.zigi.evolution.select.RankSelect;
import org.zigi.evolution.select.SelectFunction;
import org.zigi.evolution.util.Population;

public class GeneticProgramming extends EvolutionAlgorithm {

	private SelectFunction select;
	private CrossFunction cross;
	private MutateFunction mutate;
	private double mutateProbability = 0.1;
	private double crossProbrability = 0.8;

	public static final int POPULATION_SIZE = 20;

	public static final String CREATE_INIT_POPULATION_START = "CREATE_INIT_POPULATION_START";
	public static final String CREATE_INIT_POPULATION_END = "CREATE_INIT_POPULATION_END";
	public static final String EVALUATE_POPULATION_START = "EVALUATE_POPULATION_START";
	public static final String EVALUATE_POPULATION_END = "EVALUATE_POPULATION_END";
	public static final String CHECK_BEST_SOLUTION_START = "CHECK_BEST_SOLUTION_START";
	public static final String CHECK_BEST_SOLUTION_END = "CHECK_BEST_SOLUTION_END";
	public static final String MUTATE_SOLUTION_START = "MUTATE_SOLUTION_START";
	public static final String MUTATE_SOLUTION_END = "MUTATE_SOLUTION_END";
	public static final String CROSS_SOLUTION_START = "CROSS_SOLUTION_START";
	public static final String CROSS_SOLUTION_END = "CROSS_SOLUTION_END";
	public static final String NEW_POPULATION = "NEW_POPULATION";

	private static final Logger LOG = Logger.getLogger(GeneticProgramming.class);

	public GeneticProgramming() {
		setPopulationSize(POPULATION_SIZE);

		setCross(new TreeCross());
		setMutate(new TreeMutate());
		setSelect(new RankSelect());
	}

	public SelectFunction getSelect() {
		return select;
	}

	public void setSelect(SelectFunction select) {
		this.select = select;
	}

	public CrossFunction getCross() {
		return cross;
	}

	public void setCross(CrossFunction cross) {
		this.cross = cross;
	}

	public MutateFunction getMutate() {
		return mutate;
	}

	public void setMutate(MutateFunction mutate) {
		mutate.setProblem(getProblem());
		this.mutate = mutate;
	}

	public double getMutateProbability() {
		return mutateProbability;
	}

	public void setMutateProbability(double mutateProbability) {
		if (this.mutateProbability <= 1.0) {
			this.mutateProbability = mutateProbability;
			double sum = this.mutateProbability + this.crossProbrability;
			if (sum > 1.0) {
				this.crossProbrability = 1.0 - mutateProbability;
			}
		}
	}

	public double getCrossProbrability() {
		return crossProbrability;
	}

	public void setCrossProbrability(double crossProbrability) {
		if (this.crossProbrability <= 1.0) {
			this.crossProbrability = crossProbrability;
			double sum = this.mutateProbability + this.crossProbrability;
			if (sum > 1.0) {
				this.mutateProbability = 1.0 - crossProbrability;
			}
		}
	}

	public double getReproduceProbability() {
		return 1.0 - crossProbrability - mutateProbability;
	}

	public void setReproduceProbability(double value) {
		if (value <= 1.0) {
			double sum = this.crossProbrability + value;
			if (sum > 1.0) {
				this.crossProbrability = 1.0 - value;
				this.mutateProbability = 0.0;
			} else {
				this.mutateProbability = 1.0 - crossProbrability - value;
			}
		}
	}

	public void run() {
		try {
			setState(INIT_STATE);
			TreeProblem problem = (TreeProblem) getProblem();
			this.mutate.setProblem(problem);
			Population population = getPopulation();

			// pokud neni populace inicializovana, vegenerujeme pul napul
			setState(CREATE_INIT_POPULATION_START);
			for (int i = 0; i < (population.getMaxSolutions() / 2); i++)
				population.add(problem.randomFullTreeSolution());
			for (int i = (population.getMaxSolutions() / 2); i < population.getMaxSolutions(); i++)
				population.add(problem.randomGrowTreeSolution());
			setState(CREATE_INIT_POPULATION_END);

			// Util.logPopulation(population);

			// ohodnotime celou populaci
			setState(EVALUATE_POPULATION_START);
			problem.evaluate(population);
			setState(EVALUATE_POPULATION_END);

			// aktualizace nejlepsiho jedince
			setState(CHECK_BEST_SOLUTION_START);
			checkBestSolution(population);
			setState(CHECK_BEST_SOLUTION_END);

			while (getActualGeneration() < getGeneration() && !isTerminate()) {
				Population list = select.select(getPopulation(), getProblem(), population.getMaxSolutions());

				// LOG.info("SELECTED");
				// Util.logPopulation(list);

				long crossSize = Math.round(list.size() * crossProbrability);
				long mutateSize = Math.round(list.size() * mutateProbability);
				long reproduceSize = list.size() - (crossSize - mutateSize);

				// krizeni
				setState(CROSS_SOLUTION_START);
				cross.cross(list, 0, crossSize);
				setState(CROSS_SOLUTION_END);

				// LOG.info("CROSSED");
				// Util.logPopulation(list);

				// mutace
				setState(MUTATE_SOLUTION_START);
				mutate.mutate(list, crossSize, mutateSize);
				setState(MUTATE_SOLUTION_END);

				// LOG.info("MATETED");
				// Util.logPopulation(list);

				// ohodnocení nové populace
				setState(EVALUATE_POPULATION_START);
				problem.evaluate(list);
				setState(EVALUATE_POPULATION_END);

				// LOG.info("EVALUATED");
				// Util.logPopulation(list);

				setState(CHECK_BEST_SOLUTION_START);
				checkBestSolution(list);
				setState(CHECK_BEST_SOLUTION_END);

				// konec plneni nove populace a nahrazeni stare
				setPopulation(list);
				setState(NEW_POPULATION);

				// Util.logPopulation(list);

				increseActualGeneration();
			}

			setState(EvolutionAlgorithm.ALGORITHM_TERMINATING);
			setState(EvolutionAlgorithm.ALGORITHM_TERMINATED);
		} catch (Exception e) {
			LOG.error(e);
		}
	}
}
