package org.zigi.evolution.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.zigi.evolution.EvolutionToolsApplication;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class PopulationFitnessController implements Initializable {

	private EvolutionToolsApplication window;

	@FXML
	private LineChart<Number, Number> popFitnessLine;

	@FXML
	private NumberAxis xAxis;

	@FXML
	private NumberAxis yAxis;

	public EvolutionToolsApplication getWindow() {
		return window;
	}

	public void setWindow(EvolutionToolsApplication window) {
		this.window = window;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		xAxis.setLowerBound(1.0);
		xAxis.setUpperBound(10.0);
		xAxis.setTickUnit(0.5);
		xAxis.setLabel("Number of generation");

		yAxis.setLowerBound(0.0);
		yAxis.setUpperBound(50.0);
		yAxis.setTickUnit(0.5);

		popFitnessLine.setTitle("Fitness progress in population");

		// defining a series
		XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
		series.setName("My portfolio");

		// populating the series with data
		series.getData().add(new XYChart.Data<Number, Number>(1.0, 23.0));
		series.getData().add(new XYChart.Data<Number, Number>(2.0, 14.0));
		series.getData().add(new XYChart.Data<Number, Number>(3.0, 15.0));
		series.getData().add(new XYChart.Data<Number, Number>(4.0, 24.0));
		series.getData().add(new XYChart.Data<Number, Number>(5.0, 34.0));
		series.getData().add(new XYChart.Data<Number, Number>(6.0, 36.0));
		series.getData().add(new XYChart.Data<Number, Number>(7.0, 22.0));

		popFitnessLine.getData().add(series);
	}
}
