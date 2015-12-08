package org.zigi.evolution.solution;

import java.util.Comparator;

public class SolutionComparator implements Comparator<Solution> {

	public int compare(Solution o1, Solution o2) {
		return o1.getFitness().compareTo(o2.getFitness());
	}

}
