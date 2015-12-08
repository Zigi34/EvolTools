package org.zigi.evolution.algorithm;

import java.util.List;
import java.util.Random;

import org.zigi.evolution.cross.CrossFce;
import org.zigi.evolution.mutate.MutateFce;
import org.zigi.evolution.problem.Problem;
import org.zigi.evolution.select.BestSelectElitism;
import org.zigi.evolution.select.ElitismFce;
import org.zigi.evolution.select.RouleteWheelSelectFunction;
import org.zigi.evolution.select.SelectFce;
import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.solution.TreeSolution;
import org.zigi.evolution.util.Population;

public class GeneticProgramming extends EvolutionAlg {

	private int generation = 50;
	private SelectFce select = new RouleteWheelSelectFunction();
	private CrossFce cross;
	private MutateFce mutate;
	private ElitismFce elitism = new BestSelectElitism();

	private static final Random RAND = new Random();

	public SelectFce getSelect() {
		return select;
	}

	public void setSelect(SelectFce select) {
		this.select = select;
	}

	public CrossFce getCross() {
		return cross;
	}

	public void setCross(CrossFce cross) {
		this.cross = cross;
	}

	public MutateFce getMutate() {
		return mutate;
	}

	public void setMutate(MutateFce mutate) {
		this.mutate = mutate;
	}

	public int getGeneration() {
		return generation;
	}

	public void setGeneration(int generation) {
		this.generation = generation;
	}

	public void run() {
		Problem problem = getProblem();
		Population population = getPop();

		// pokud neni populace inicializovana, vegenerujeme
		if (population.size() == 0) {
			for (int i = 0; i < population.getMax(); i++)
				population.add(problem.randomSolution());
		}

		// ohodnotime celou populaci
		problem.evaluate(population);

		// aktualizace nejlepsiho jedince
		checkBestSolution(population);

		for (int i = 0; i < generation; i++) {
			// vytvorime si novou populaci
			Population newPopulation = new Population();
			newPopulation.setMax(getPop().getMax());

			// dokud neni populace plna, tak pokracujeme
			while (newPopulation.size() < newPopulation.getMax()) {
				// pokud chybi uz jeden jedinec, pouzijeme mutaci
				if (newPopulation.getMax() - newPopulation.size() == 1) {
					// mutace
					List<Solution> list = select.select(population, 1);
					Solution selected = list.get(0).cloneMe();
					mutate.mutate(selected);

					newPopulation.add(selected);
				} else {
					// nahodne vybereme bud mutaci nebo krizeni
					if (RAND.nextDouble() <= 0.5) {
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

						cross.cross(list);

						newPopulation.add(selected1);
						newPopulation.add(selected2);
					}

				}
			}

			// ohodnocení nové populace
			problem.evaluate(newPopulation);

			// nova populace
			Population nextPop = new Population();
			nextPop.setMax(newPopulation.getMax() + getPop().getMax());
			nextPop.addAll(getPop().getSolutions());
			nextPop.addAll(newPopulation.getSolutions());

			checkBestSolution(nextPop);

			List<Solution> list = elitism.select(nextPop, newPopulation.getMax());
			Population p = new Population();
			p.setMax(list.size());
			p.addAll(list);

			// konec plneni nove populace a nahrazeni stare
			setPop(p);

			double sumDeep = 0.0;
			for (Solution sol : getPop().getSolutions()) {
				TreeSolution tree = (TreeSolution) sol;
				sumDeep += tree.deep();
			}

			System.out.println("Velikost populace: " + getPop().size());
			System.out.println("Average deep: " + (sumDeep / newPopulation.size()));
			System.out.println("Průměrná fitness: " + getPop().getAverageFitness());
			System.out.println("Best solution: " + (getBestSolution().getFitness() * 91));
			System.out.println("\n");

			if (i % 10 == 0)
				System.out.println("[" + getBestSolution().getFitness() + "]: " + getBestSolution());
		}
	}
}
