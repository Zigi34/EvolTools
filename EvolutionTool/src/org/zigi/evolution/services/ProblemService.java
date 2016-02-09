package org.zigi.evolution.services;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.zigi.evolution.model.ProblemModel;
import org.zigi.evolution.problem.ArtificialAnt;

public class ProblemService {

	private static ProblemService instance;
	private static ProblemModel selected;

	private ProblemService() {
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
		List<ProblemModel> list = new LinkedList<ProblemModel>();
		initialize(list);
		return list;
	}

	private static void initialize(List<ProblemModel> list) {
		try {
			ArtificialAnt problem = new ArtificialAnt();
			problem.setMaxHeight(6);
			problem.setMaxMoves(420);
			problem.setYard(new File("resource/data/artificial_ant"));
			list.add(new ProblemModel("Umělý mravenec", problem));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ProblemModel getSelected() {
		return selected;
	}

	public static void setSelected(ProblemModel selected) {
		ProblemService.selected = selected;
	}
}
