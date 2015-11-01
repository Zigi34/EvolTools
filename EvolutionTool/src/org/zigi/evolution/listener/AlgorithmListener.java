package org.zigi.evolution.listener;

import org.zigi.evolution.algorithm.Algorithm;
import org.zigi.evolution.controller.MainController;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class AlgorithmListener implements ChangeListener<Algorithm> {

	private MainController controller;

	public AlgorithmListener(MainController controller) {
		this.controller = controller;
	}

	public MainController getController() {
		return controller;
	}

	@Override
	public void changed(ObservableValue<? extends Algorithm> observable, Algorithm oldValue, Algorithm newValue) {

	}
}
