package org.zigi.evolution.select;

import java.util.LinkedList;
import java.util.List;

import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.util.Population;

public class BestSelectElitism extends ElitismFce {

	@Override
	public List<Solution> select(Population pop, Integer max) {

		// seradi reseni od nejhorsiho po nejlepsi
		pop.sort();
		int index = pop.size() - max;
		List<Solution> list = new LinkedList<Solution>();
		for (int i = index; i < pop.size(); i++)
			list.add(pop.getSolutions().get(i));
		return list;
	}

}
