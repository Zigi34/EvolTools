package org.evolution.mutate;

import java.util.List;

import org.evolution.problem.Problem;
import org.evolution.solution.Solution;

public abstract class MutateFunction {

	public abstract void mutate(List<Solution> sol, Problem problem, long offset, long size);
}
