package org.zigi.evolution.select;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.zigi.evolution.problem.Problem;
import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.util.Population;

public class TournamentSelection extends SelectFunction {

	private static final Random RAND = new Random();
	private int tournamentSize = 2;

	@Override
	public Population select(Population pop, Problem problem, int count) {
		List<Solution> list = new LinkedList<Solution>();
		for (int i = 0; i < count; i++) {
			double bestFittness = Double.MIN_VALUE;
			Solution bestSolution = null;

			for (int j = 0; j < tournamentSize; j++) {
				Solution solut = pop.getSolutions().get(RAND.nextInt(pop.size()));

				Double fitness = problem.getNormalizedFitness(solut);
				if (fitness > bestFittness || bestSolution == null) {
					bestSolution = solut;
					bestFittness = fitness;
				}
			}
			list.add(bestSolution.cloneMe());
		}
		Population result = new Population();
		result.setSolutions(list);

		return result;
	}

	public int getTournamentSize() {
		return tournamentSize;
	}

	public void setTournamentSize(int tournamentSize) {
		this.tournamentSize = tournamentSize;
	}

	@Override
	public String toString() {
		return "tournament(" + tournamentSize + ")";
	}
}
