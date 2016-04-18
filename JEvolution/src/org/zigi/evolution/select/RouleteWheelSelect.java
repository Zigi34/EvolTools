package org.zigi.evolution.select;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.util.Population;

/**
 * Výběr ruletovým kolem. Populace se nejprve seřadí podle fitness a každému
 * řešení se přiřadí výseč podle této hodnoty fitness na pomysleném kruhu.
 * Následně se náhodně vygeneruje číslo s maximální mezí odpovídající obvodu
 * (součet fitness hodnot jednotlivých řešení).
 * 
 * @author Zdeněk Gold
 *
 */
public class RouleteWheelSelect extends SelectFunction {

	private static final Random RAND = new Random();
	public static final double MIN_VALUE = 0.005;

	private double fitnessIncrement = MIN_VALUE;

	/**
	 * Hodnota navyšování pro selekci
	 * 
	 * @return
	 */
	public double getFitnessIncrement() {
		return fitnessIncrement;
	}

	/**
	 * Nastavení navýšení pro každé řešení ve výseči koláče
	 * 
	 * @param fitnessIncrement
	 */
	public void setFitnessIncrement(double fitnessIncrement) {
		this.fitnessIncrement = fitnessIncrement;
	}

	@Override
	public String toString() {
		return "Roulete Wheel Select";
	}

	@Override
	public Population select(Population pop, int count) {
		List<Solution> list = new LinkedList<Solution>();

		double max = 0.0;
		for (Solution sol : pop.getSolutions()) {
			max += Population.getNormalizedFitness(sol, pop);
		}

		for (int index = 0; index < count; index++) {
			double rnd = RAND.nextDouble() * max;
			double value = 0.0;
			for (Solution sol : pop.getSolutions()) {
				Double fitness = Population.getNormalizedFitness(sol, pop);
				value += fitness;
				if (value > rnd) {
					list.add(sol.cloneMe());
					break;
				}
			}
		}
		Population result = new Population();
		result.setBestFunctionValue(pop.getBestFunctionValue());
		result.setMax(pop.getMaxSolutions());
		result.setSumFunctionValue(pop.getSumFunctionValue());
		result.setWorstFunctionValue(pop.getWorstFunctionValue());
		result.setSolutions(list);
		return result;
	}
}
