package org.evolution.services;

import java.util.LinkedList;
import java.util.List;

import org.evolution.model.SelectFunctionModel;
import org.evolution.select.RankSelect;
import org.evolution.select.RouleteWheelSelect;
import org.evolution.select.TournamentSelection;

public class SelectFunctionService {

	private static SelectFunctionModel selected = new SelectFunctionModel(new RankSelect());
	private static SelectFunctionService instance;

	private SelectFunctionService() {

	}

	/**
	 * Načítá všechny moduly pro výběrové funkce
	 * 
	 * @return
	 */
	public List<SelectFunctionModel> findSelectFunctions() {
		List<SelectFunctionModel> list = new LinkedList<SelectFunctionModel>();
		list.add(new SelectFunctionModel(new RankSelect()));
		list.add(new SelectFunctionModel(new RouleteWheelSelect()));
		list.add(new SelectFunctionModel(new TournamentSelection()));
		return list;
	}

	public static SelectFunctionService getInstance() {
		if (instance == null)
			instance = new SelectFunctionService();
		return instance;
	}

	public static SelectFunctionModel getSelected() {
		return selected;
	}

	public static void setSelected(SelectFunctionModel selected) {
		SelectFunctionService.selected = selected;
	}

}
