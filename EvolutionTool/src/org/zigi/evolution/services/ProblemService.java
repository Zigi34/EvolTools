package org.zigi.evolution.services;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.zigi.evolution.model.AlgorithmModel;
import org.zigi.evolution.model.ProblemModel;
import org.zigi.evolution.problem.ArtificialAnt;
import org.zigi.evolution.problem.RegressionProblem;

public class ProblemService {

	private static ProblemService instance;
	private static ProblemModel selected;
	private static List<ProblemModel> list;
	private static final Logger LOG = Logger.getLogger(ProblemService.class);

	private ProblemService() {
		if (list == null) {
			list = new LinkedList<ProblemModel>();
			initialize(list);
		}
	}

	public static ProblemService getInstance() {
		if (instance == null)
			instance = new ProblemService();
		return instance;
	}

	/**
	 * Načítá všechny moduly pro výběrové funkce
	 * 
	 * @return
	 */
	public List<ProblemModel> findProblemFunctions() {
		return list;
	}

	private static void initialize(List<ProblemModel> list) {
		try {
			ArtificialAnt problem = new ArtificialAnt();
			problem.setMaxHeight(6);
			problem.setMaxMoves(420);
			problem.setYard(new File("resource/data/artificial_ant"));
			list.add(new ProblemModel("Umělý mravenec", problem));

			RegressionProblem problem2 = new RegressionProblem();
			list.add(new ProblemModel("Symbolická regrese", problem2));
		} catch (Exception e) {

		}
	}

	public static ProblemModel getSelected() {
		if (selected == null)
			selected = list.get(0);
		return selected;
	}

	public static void setSelected(ProblemModel selected) {
		ProblemService.selected = selected;
		// setting of problem will be call set problem in algorithm
		AlgorithmModel alg = Services.algorithmService().getSelected();
		if (alg != null) {
			alg.getAlgorithm().setProblem(selected.getProblem());
		}
	}
}
