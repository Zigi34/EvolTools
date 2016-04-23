package org.evolution.select;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.evolution.solution.Solution;
import org.evolution.util.Population;

public class BestElitism extends ElitismFunction {

	private static final Logger LOG = Logger.getLogger(BestElitism.class);

	@Override
	public List<Solution> select(Population pop, Integer max) {

		// seradi reseni od nejhorsiho po nejlepsi
		pop.sort();
		int index = pop.size() - max;
		List<Solution> list = new LinkedList<Solution>();
		for (int i = index; i < pop.size(); i++)
			list.add(pop.getSolutions().get(i).cloneMe());
		return list;
	}

}
