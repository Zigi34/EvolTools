package org.evolution.services;

import java.util.LinkedList;
import java.util.List;

import org.evolution.model.MutateFunctionModel;
import org.evolution.mutate.TreeMutate;

public class MutateFunctionService {

	private static MutateFunctionService instance;
	private static MutateFunctionModel selected;

	private MutateFunctionService() {
	}

	public static MutateFunctionService getInstance() {
		if (instance == null)
			instance = new MutateFunctionService();
		return instance;
	}

	/**
	 * Nacteni vsech modulu mutacni funkce
	 * 
	 * @return
	 */
	public List<MutateFunctionModel> findMutateFunctions() {
		List<MutateFunctionModel> list = new LinkedList<MutateFunctionModel>();
		list.add(new MutateFunctionModel(new TreeMutate()));
		return list;
	}

	public static MutateFunctionModel getSelected() {
		return selected;
	}

	public static void setSelected(MutateFunctionModel selected) {
		MutateFunctionService.selected = selected;
	}
}
