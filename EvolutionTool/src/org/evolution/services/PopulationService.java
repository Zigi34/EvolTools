package org.evolution.services;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.evolution.model.PopulationModel;
import org.evolution.model.ProblemModel;
import org.evolution.util.Population;

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

	private static final Logger LOG = Logger.getLogger(PopulationService.class);

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

				/*
				 * // randomly create solution IntStream.range(0,
				 * 10).forEachOrdered(n -> {
				 * pop.add(problem.getProblem().randomSolution()); });
				 */
				list.add(new PopulationModel(pop));
			}
		} catch (Exception e) {
			LOG.error(e);
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
