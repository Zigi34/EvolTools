package org.evolution.services;

import java.util.LinkedList;
import java.util.List;

import org.evolution.algorithm.GeneticProgramming;
import org.evolution.listener.AlgorithmListener;
import org.evolution.model.AlgorithmModel;

public class AlgorithmService {

	private static AlgorithmService instance;
	private static AlgorithmModel selected;

	public AlgorithmService() {
	}

	public static AlgorithmService getInstance() {
		if (instance == null)
			instance = new AlgorithmService();
		return instance;
	}

	public List<AlgorithmModel> findAlgorithms() {
		List<AlgorithmModel> list = new LinkedList<AlgorithmModel>();
		GeneticProgramming alg = new GeneticProgramming();
		alg.addChangeListener(new AlgorithmListener());
		list.add(new AlgorithmModel(alg));
		return list;
	}

	public static AlgorithmModel getSelected() {
		if (selected == null)
			selected = getInstance().findAlgorithms().get(0);
		return selected;
	}

	public static void setSelected(AlgorithmModel algorithm) {
		AlgorithmService.selected = algorithm;
	}
}
