package org.evolution.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.evolution.algorithm.EvolutionAlgorithm;
import org.evolution.algorithm.GeneticProgramming;
import org.evolution.model.AlgorithmModel;
import org.evolution.problem.Problem;
import org.evolution.problem.RegressionProblem;
import org.evolution.problem.regression.KeyVariables;
import org.evolution.services.Services;
import org.evolution.solution.Solution;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class ResultChart extends AnchorPane {

	private static final Logger LOG = Logger.getLogger(ResultChart.class);

	@FXML
	private NumberAxis xAxis;

	@FXML
	private NumberAxis yAxis;

	@FXML
	private LineChart<Double, Double> result;

	@FXML
	private Label solutionTitle;

	@FXML
	private Label generationTitle;

	@FXML
	private Label fitnessTitle;

	private XYChart.Series<Double, Double> optimal = new XYChart.Series<Double, Double>();
	private XYChart.Series<Double, Double> best = new XYChart.Series<Double, Double>();

	public ResultChart() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/ResultChart.fxml"));
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
		optimal.setName("Optimální řešení");
		best.setName("Nejlepší nalezené řešení");
		xAxis.setLowerBound(0.0);
		xAxis.setTickUnit(1.0);
		yAxis.setLowerBound(0.0);
		yAxis.setUpperBound(1.0);
		yAxis.setTickUnit(0.01);

		AlgorithmModel model = Services.algorithmService().getSelected();
		if (model != null) {
			EvolutionAlgorithm alg = model.getAlgorithm();
			result.getData().add(optimal);
			result.getData().add(best);
			LOG.info("Nastavuji odchytavani udalosti");
			alg.addChangeListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					// při vytvoření nové populace se zjistí a do grafu nastaví
					// fitness
					Problem problem = alg.getProblem();
					if (evt.getNewValue().equals(GeneticProgramming.NEW_BEST_SOLUTION)) {
						Solution bestSolution = alg.getBestSolution();
						LOG.info("Lepší: " + bestSolution);
						Double bestFitness = alg.getProblem().getNormalizedFitness(bestSolution);
						Integer generation = alg.getActualGeneration();
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								LOG.info("nové řešení:" + generation + " = " + bestFitness);
								best.getData().clear();
								if (problem instanceof RegressionProblem) {
									// Regrese
									RegressionProblem reg = (RegressionProblem) problem;
									for (KeyVariables variable : reg.getDataset().keySet()) {
										Double xVal = variable.getKey(0);
										Double val = reg.getFunctionValue(variable, bestSolution);
										LOG.info("x=" + xVal + ",y=" + val);
										best.getData().add(new XYChart.Data<Double, Double>(xVal, val));
									}
								}
								solutionTitle.setText(bestSolution.getGenotypeString());
								generationTitle.setText("Generation: " + generation);
								fitnessTitle.setText("Fitness: " + bestFitness);
							}
						});
					} else if (evt.getNewValue().equals(EvolutionAlgorithm.ALGORITHM_STARTED)) {
						LOG.info("Počátek algoritmu");
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								if (problem instanceof RegressionProblem) {
									// Regrese
									RegressionProblem reg = (RegressionProblem) problem;
									for (KeyVariables variable : reg.getDataset().keySet()) {
										Double xVal = variable.getKey(0);
										optimal.getData().add(new XYChart.Data<Double, Double>(xVal, reg.getDataset().get(variable)));
									}
									best.getData().clear();
									solutionTitle.setText("");
									generationTitle.setText("Generation: 0");
									fitnessTitle.setText("Fitness: 0.0");
								}
							}
						});
					}
				}
			});
		}
	}
}
