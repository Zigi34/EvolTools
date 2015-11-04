package org.zigi.evolution.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.zigi.evolution.Algorithm;
import org.zigi.evolution.cross.CrossFunction;
import org.zigi.evolution.cross.OnePointCross;
import org.zigi.evolution.listener.SelectFunctionListener;
import org.zigi.evolution.model.GeneticAlgorithmDTO;
import org.zigi.evolution.mutate.MutateFunction;
import org.zigi.evolution.mutate.SimpleMutate;
import org.zigi.evolution.select.RandomSelect;
import org.zigi.evolution.select.RouleteWheelSelect;
import org.zigi.evolution.select.SelectFunction;
import org.zigi.evolution.solution.DoubleValue;
import org.zigi.evolution.solution.array.ArraySolution;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

public class GeneticAlgorithmController implements Initializable {

	private Algorithm window;
	private GeneticAlgorithmDTO algorithm;

	@FXML
	private ComboBox<CrossFunction<ArraySolution<DoubleValue>, DoubleValue>> crossFunctionCB;

	@FXML
	private ComboBox<MutateFunction<ArraySolution<DoubleValue>, DoubleValue>> mutateFunctionCB;

	@FXML
	private ComboBox<SelectFunction<ArraySolution<DoubleValue>, DoubleValue>> selectFunctionCB;

	@FXML
	public void changeSelectFunction(ActionEvent event) {
		System.out.println("Select function changed");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<CrossFunction<ArraySolution<DoubleValue>, DoubleValue>> crossList = FXCollections
				.observableArrayList();
		crossList.add(new OnePointCross<>());
		crossFunctionCB.setItems(crossList);

		ObservableList<MutateFunction<ArraySolution<DoubleValue>, DoubleValue>> mutateList = FXCollections
				.observableArrayList();
		mutateList.add(new SimpleMutate<>());
		mutateFunctionCB.setItems(mutateList);

		ObservableList<SelectFunction<ArraySolution<DoubleValue>, DoubleValue>> selectList = FXCollections
				.observableArrayList();
		selectList.add(new RouleteWheelSelect<>());
		selectList.add(new RandomSelect<>());
		selectFunctionCB.setItems(selectList);
		selectFunctionCB.valueProperty()
				.addListener(new SelectFunctionListener<ArraySolution<DoubleValue>, DoubleValue>());
	}

	public Algorithm getWindow() {
		return window;
	}

	public void setWindow(Algorithm window) {
		this.window = window;
	}

	public GeneticAlgorithmDTO getAlgorithmDTO() {
		return algorithm;
	}

	public void setAlgorithmDTO(GeneticAlgorithmDTO algorithm) {
		this.algorithm = algorithm;
	}

	public void init() {
	}

	public void loadSetting() {
		if (algorithm != null) {

		}
	}

	@FXML
	public void exitApplication(ActionEvent event) {
		System.out.println("Stage is closing");
		Platform.exit();
	}
}
