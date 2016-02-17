package org.zigi.evolution.services;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import org.zigi.evolution.model.PopulationModel;
import org.zigi.evolution.model.ProblemModel;
import org.zigi.evolution.util.Population;

/**
 * Population manager
 * 
 * @author zigi
 *
 */
public class PopulationService {

	private static PopulationService instance;
	private static PopulationModel selected;
	private static List<PopulationModel> list;

	private PopulationService() {
		if (list == null) {
			list = new LinkedList<PopulationModel>();
			initialize(list);
		}
	}

	public static PopulationService getInstance() {
		if (instance == null)
			instance = new PopulationService();
		return instance;
	}

	/**
	 * Načítá všechny moduly pro výběrové funkce
	 * 
	 * @return
	 */
	public List<PopulationModel> findPopulations() {
		return list;
	}

	/**
	 * fill all population models
	 * 
	 * @param list
	 */
	private static void initialize(List<PopulationModel> list) {
		try {
			for (ProblemModel problem : Services.problemService().findProblemFunctions()) {
				Population pop = new Population();

				// randomly create solution
				IntStream.range(0, 10).forEachOrdered(n -> {
					pop.add(problem.getProblem().randomSolution());
				});

				list.add(new PopulationModel(pop));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Return selected population model
	 * 
	 * @return
	 */
	public static PopulationModel getSelected() {
		if (selected == null)
			selected = list.get(0);
		return selected;
	}

	public static void setSelected(PopulationModel selected) {
		PopulationService.selected = selected;
	}
}
