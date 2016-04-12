package org.zigi.evolution.select;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.util.Population;

public class TournamentSelection extends SelectFunction {

	private static final Random RAND = new Random();
	private int tournamentSize = 2;

	@Override
	public List<Solution> select(Population pop, int count) {
		List<Solution> list = new LinkedList<Solution>();

		for (int i = 0; i < count; i++) {
			double bestFittness = Double.MIN_VALUE;
			Solution bestSolution = null;

			for (int j = 0; j < tournamentSize; j++) {
				Solution solut = pop.getSolutions().get(RAND.nextInt(pop.size()));

				if (solut.getFitness() > bestFittness) {
					bestSolution = solut;
					bestFittness = solut.getFitness();
				}
			}

			list.add(bestSolution);
		}
		return list;
	}

	public int getTournamentSize() {
		return tournamentSize;
	}

	public void setTournamentSize(int tournamentSize) {
		this.tournamentSize = tournamentSize;
	}

	@Override
	public String toString() {
		return "Tournament Select";
	}

	@Override
	public List<Solution> select(Population pop, int count, double maxValue) {
		return select(pop, count);
	}

}
