package org.zigi.evolution.algorithm;

import java.util.List;
import java.util.Random;

import org.zigi.evolution.cross.CrossFunction;
import org.zigi.evolution.cross.TreeCross;
import org.zigi.evolution.mutate.MutateFunction;
import org.zigi.evolution.mutate.TreeMutate;
import org.zigi.evolution.problem.TreeProblem;
import org.zigi.evolution.select.BestElitism;
import org.zigi.evolution.select.ElitismFunction;
import org.zigi.evolution.select.RankSelect;
import org.zigi.evolution.select.SelectFunction;
import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.util.Population;

public class GeneticProgramming extends EvolutionAlgorithm {

	private SelectFunction select;
	private CrossFunction cross;
	private MutateFunction mutate;
	private ElitismFunction elitism = new BestElitism();
	private double mutateProbability = 0.7;

	private static final Random RAND = new Random();

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

	public GeneticProgramming() {
		setPopulationSize(400);

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
		this.mutateProbability = mutateProbability;
	}

	public void run() {
		setState(INIT_STATE);
		TreeProblem problem = (TreeProblem) getProblem();
		this.mutate.setProblem(problem);
		Population population = getPopulation();

		// pokud neni populace inicializovana, vegenerujeme pul napul metodou
		// GROW a FULL
		if (population.size() == 0) {
			setState(CREATE_INIT_POPULATION_START);
			for (int i = 0; i < (population.getMaxSolutions() / 2); i++)
				population.add(problem.randomFullTreeSolution());
			for (int i = (population.getMaxSolutions() / 2); i < population.getMaxSolutions(); i++)
				population.add(problem.randomGrowTreeSolution());
			setState(CREATE_INIT_POPULATION_END);
		}

		// ohodnotime celou populaci
		setState(EVALUATE_POPULATION_START);
		problem.evaluate(population);
		setState(EVALUATE_POPULATION_END);

		// aktualizace nejlepsiho jedince
		setState(CHECK_BEST_SOLUTION_START);
		checkBestSolution(population);
		setState(CHECK_BEST_SOLUTION_END);

		while (getActualGeneration() < getGeneration() && !isTerminate()) {
			// vytvorime si novou populaci
			Population newPopulation = new Population();
			newPopulation.setMax(getPopulation().getMaxSolutions());

			// dokud neni populace plna, tak pokracujeme
			while (newPopulation.size() < newPopulation.getMaxSolutions()) {
				// pokud chybi uz jeden jedinec, pouzijeme mutaci
				if (newPopulation.getMaxSolutions() - newPopulation.size() == 1) {
					// mutace
					List<Solution> list = select.select(population, 1);
					Solution selected = list.get(0).cloneMe();
					setState(MUTATE_SOLUTION_START);
					mutate.mutate(selected);
					setState(MUTATE_SOLUTION_END);

					newPopulation.add(selected);
				} else {
					// nahodne vybereme bud mutaci nebo krizeni
					if (RAND.nextDouble() <= mutateProbability) {
						// mutace
						List<Solution> list = select.select(population, 1);
						Solution selected = list.get(0).cloneMe();
						setState(MUTATE_SOLUTION_START);
						mutate.mutate(selected);
						setState(MUTATE_SOLUTION_END);

						newPopulation.add(selected);
					} else {
						// krizeni
						List<Solution> list = select.select(population, 2);
						Solution selected1 = list.get(0).cloneMe();
						Solution selected2 = list.get(1).cloneMe();

						list.clear();

						list.add(selected1);
						list.add(selected2);

						setState(CROSS_SOLUTION_START);
						if (!cross.cross(list)) {
							mutate.mutate(selected1);
							mutate.mutate(selected2);
						}
						setState(CROSS_SOLUTION_END);
						newPopulation.add(selected1);
						newPopulation.add(selected2);
					}

				}
			}

			// ohodnocení nové populace
			setState(EVALUATE_POPULATION_START);
			problem.evaluate(newPopulation);
			setState(EVALUATE_POPULATION_END);

			// nova populace
			Population nextPop = new Population();
			nextPop.setMax(newPopulation.getMaxSolutions() + getPopulation().getMaxSolutions());
			nextPop.addAll(getPopulation().getSolutions());
			nextPop.addAll(newPopulation.getSolutions());

			setState(CHECK_BEST_SOLUTION_START);
			checkBestSolution(nextPop);
			setState(CHECK_BEST_SOLUTION_END);

			List<Solution> list = elitism.select(nextPop, newPopulation.getMaxSolutions());
			Population p = new Population();
			p.setMax(list.size());
			p.addAll(list);

			// konec plneni nove populace a nahrazeni stare
			setPopulation(p);
			setState(NEW_POPULATION);

			increseActualGeneration();
		}

		setState(EvolutionAlgorithm.ALGORITHM_TERMINATING);
		setState(EvolutionAlgorithm.ALGORITHM_TERMINATED);
	}
}
