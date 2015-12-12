package org.zigi.evolution.algorithm;

import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.zigi.evolution.cross.CrossFunction;
import org.zigi.evolution.mutate.MutateFunction;
import org.zigi.evolution.problem.TreeProblem;
import org.zigi.evolution.select.BestElitism;
import org.zigi.evolution.select.ElitismFunction;
import org.zigi.evolution.select.RouleteWheelSelect;
import org.zigi.evolution.select.SelectFunction;
import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.util.Population;

public class GeneticProgramming extends EvolutionAlgorithm {

	private int generation = 50;
	private SelectFunction select = new RouleteWheelSelect();
	private CrossFunction cross;
	private MutateFunction mutate;
	private ElitismFunction elitism = new BestElitism();

	private static final Random RAND = new Random();

	public static final String CREATE_INIT_POPULATION_START = "CREATE_INIT_POPULATION_START";
	public static final String CREATE_INIT_POPULATION_END = "CREATE_INIT_POPULATION_END";

	private static final Logger LOG = Logger.getLogger(GeneticProgramming.class);

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
		this.mutate = mutate;
	}

	public int getGeneration() {
		return generation;
	}

	public void setGeneration(int generation) {
		this.generation = generation;
	}

	public void run() {
		setState(INIT_STATE);
		TreeProblem problem = (TreeProblem) getProblem();
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
		problem.evaluate(population);

		// aktualizace nejlepsiho jedince
		checkBestSolution(population);

		for (int i = 0; i < generation; i++) {
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
					mutate.mutate(selected);

					newPopulation.add(selected);
				} else {
					// nahodne vybereme bud mutaci nebo krizeni
					if (RAND.nextDouble() <= 0.7) {
						// mutace
						List<Solution> list = select.select(population, 1);
						Solution selected = list.get(0).cloneMe();
						mutate.mutate(selected);

						newPopulation.add(selected);
					} else {
						// krizeni
						List<Solution> list = select.select(population, 2);
						Solution selected1 = list.get(0).cloneMe();
						Solution selected2 = list.get(1).cloneMe();

						list.clear();

						list.add(selected1);
						list.add(selected2);

						if (!cross.cross(list)) {
							mutate.mutate(selected1);
							mutate.mutate(selected2);
						}
						newPopulation.add(selected1);
						newPopulation.add(selected2);
					}

				}
			}

			// ohodnocení nové populace
			problem.evaluate(newPopulation);

			// nova populace
			Population nextPop = new Population();
			nextPop.setMax(newPopulation.getMaxSolutions() + getPopulation().getMaxSolutions());
			nextPop.addAll(getPopulation().getSolutions());
			nextPop.addAll(newPopulation.getSolutions());

			checkBestSolution(nextPop);

			List<Solution> list = elitism.select(nextPop, newPopulation.getMaxSolutions());
			Population p = new Population();
			p.setMax(list.size());
			p.addAll(list);

			// konec plneni nove populace a nahrazeni stare
			setPopulation(p);
		}
	}
}
