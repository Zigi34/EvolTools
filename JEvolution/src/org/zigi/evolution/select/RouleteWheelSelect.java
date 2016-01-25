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

	@Override
	public List<Solution> select(Population sols, int count) {
		List<Solution> list = new LinkedList<Solution>();

		// seznam reseni ze kterych si muzeme nahodne jedno vybrat
		List<Solution> forSelect = new LinkedList<Solution>();
		forSelect.addAll(sols.getSolutions());

		double max = 0.0;
		for (Solution sol : sols.getSolutions()) {
			if (sol.isEvaluated()) {
				max += sol.getFitness() + MIN_VALUE;
			}
		}

		for (int i = 0; i < count; i++) {
			double rnd = RAND.nextDouble() * max;
			double value = 0.0;
			Solution solut = null;
			for (Solution sol : forSelect) {
				value += sol.getFitness() + MIN_VALUE;
				if (value > rnd) {
					solut = sol;
					break;
				}
			}

			// vybereme
			list.add(solut);

			// odstranime jedince z dalsiho vyberu (zamezeni dvojiteho vyberu
			// stejneho jedince)
			max -= (solut.getFitness() + MIN_VALUE);
			forSelect.remove(solut);
		}
		return list;
	}

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
}
