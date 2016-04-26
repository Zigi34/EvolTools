package org.evolution.services;

import java.util.LinkedList;
import java.util.List;

import org.evolution.cross.OnePointTreeCross;
import org.evolution.cross.TreeCross;
import org.evolution.model.CrossFunctionModel;

public class CrossFunctionService {

	private static CrossFunctionService instance;
	private static CrossFunctionModel selected;

	private CrossFunctionService() {
	}

	public static CrossFunctionService getInstance() {
		if (instance == null)
			instance = new CrossFunctionService();
		return instance;
	}

	/**
	 * Nacteni vsech modulu mutacni funkce
	 * 
	 * @return
	 */
	public List<CrossFunctionModel> findCrossFunctions() {
		List<CrossFunctionModel> list = new LinkedList<CrossFunctionModel>();
		list.add(new CrossFunctionModel(new OnePointTreeCross()));
		list.add(new CrossFunctionModel(new TreeCross()));
		return list;
	}

	public static CrossFunctionModel getSelected() {
		return selected;
	}

	public static void setSelected(CrossFunctionModel selected) {
		CrossFunctionService.selected = selected;
	}
}
