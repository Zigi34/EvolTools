package org.zigi.evolution.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.zigi.evolution.Algorithm;
import org.zigi.evolution.listener.AlgorithmListener;
import org.zigi.evolution.model.AlgorithmDTO;
import org.zigi.evolution.util.Utils;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

public class AlgorithmController implements Initializable {

	private Algorithm window;

	@FXML
	private ComboBox<AlgorithmDTO<?, ?>> algorithms;

	@FXML
	private TabPane pane;

	@FXML
	private Tab algorithmTab;

	@FXML
	private AnchorPane settingPane;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		List<AlgorithmDTO<?, ?>> algs = Utils.createAlgorithmList(this);
		ObservableList<AlgorithmDTO<?, ?>> list = FXCollections.observableArrayList();
		list.addAll(algs);

		algorithms.setItems(list);
		algorithms.valueProperty().addListener(new AlgorithmListener(this));
	}

	public Algorithm getWindow() {
		return window;
	}

	public void setWindow(Algorithm mainApp) {
		this.window = mainApp;
	}

	public TabPane getPane() {
		return pane;
	}

	public Tab getAlgorithmTab() {
		return algorithmTab;
	}

	public AnchorPane getSettingPane() {
		return settingPane;
	}

	public void setSettingPane(AnchorPane pane) {
		settingPane = pane;
	}

	@FXML
	public void exitApplication(ActionEvent event) {
		System.out.println("Stage is closing");
		Platform.exit();
	}
}
