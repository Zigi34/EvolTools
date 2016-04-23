package org.evolution.solution;

import java.util.Comparator;

public class SolutionComparator implements Comparator<Solution> {

	private boolean minProblem;

	public SolutionComparator(boolean isMinProblem) {
		minProblem = isMinProblem;
	}

	public int compare(Solution o1, Solution o2) {
		if (minProblem)
			return o2.getFunctionValue().compareTo(o1.getFunctionValue());
		else
			return o1.getFunctionValue().compareTo(o2.getFunctionValue());
	}

}
