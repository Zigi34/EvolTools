package org.zigi.evolution.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.evolution.algorithm.EvolutionAlgorithm;
import org.evolution.algorithm.GeneticProgramming;
import org.evolution.solution.Solution;
import org.zigi.evolution.model.AlgorithmModel;
import org.zigi.evolution.services.Services;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;

public class FitnessChart extends AnchorPane {

	@FXML
	private NumberAxis xAxis;

	@FXML
	private NumberAxis yAxis;

	@FXML
	private LineChart<Integer, Double> popFitnessLine;

	private static final Logger LOG = Logger.getLogger(FitnessChart.class);
	private XYChart.Series<Integer, Double> series = new XYChart.Series<Integer, Double>();

	public FitnessChart() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/FitnessChart.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			LOG.error(exception);
		}

		initialize();
	}

	private void initialize() {
		series.setName("Fitness chart");
		xAxis.setLowerBound(0.0);
		xAxis.setTickUnit(1.0);
		yAxis.setLowerBound(0.0);
		yAxis.setUpperBound(1.0);
		yAxis.setTickUnit(0.01);
		popFitnessLine.getData().add(series);

		AlgorithmModel model = Services.algorithmService().getSelected();
		if (model != null) {
			EvolutionAlgorithm alg = model.getAlgorithm();
			LOG.info("Nastavuji odchytavani udalosti");
			alg.addChangeListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					// při vytvoření nové populace se zjistí a do grafu nastaví
					// fitness
					if (evt.getNewValue().equals(GeneticProgramming.NEW_POPULATION) && alg.getActualGeneration() % 25 == 0) {
						Solution bestSolution = alg.getBestSolution();
						Double bestFitness = alg.getProblem().getNormalizedFitness(bestSolution);
						Integer generation = alg.getActualGeneration();
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								LOG.info("gen:" + generation + " = " + bestFitness);
								series.getData().add(new XYChart.Data<Integer, Double>(generation, bestFitness));
							}
						});
					} else if (evt.getNewValue().equals(EvolutionAlgorithm.ALGORITHM_STARTED)) {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								series.getData().clear();
								LOG.info("Smazání grafu");
							}
						});
					}
				}
			});
		}
	}
}
