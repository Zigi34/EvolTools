package org.zigi.evolution.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.AnchorPane;

public class EvolutionToolFitnessChart extends AnchorPane {

	@FXML
	private NumberAxis xAxis;

	@FXML
	private NumberAxis yAxis;

	public EvolutionToolFitnessChart() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/EvolutionToolFitnessChart.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}

		initialize();
	}

	private void initialize() {

	}
}