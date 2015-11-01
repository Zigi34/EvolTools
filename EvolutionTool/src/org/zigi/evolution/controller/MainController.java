package org.zigi.evolution.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.zigi.evolution.Main;
import org.zigi.evolution.algorithm.Algorithm;
import org.zigi.evolution.listener.AlgorithmListener;
import org.zigi.evolution.util.Utils;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;

public class MainController implements Initializable {

	private Main window;

	@FXML
	private ComboBox<Algorithm> algorithmList;

	@FXML
	private TabPane settingPane;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		List<Algorithm> algorithms = Utils.createAlgorithmList();
		ObservableList<Algorithm> list = FXCollections.observableArrayList();
		list.addAll(algorithms);
		algorithmList.setItems(list);
		algorithmList.valueProperty().addListener(new AlgorithmListener(this));
	}

	public Main getMainApp() {
		return window;
	}

	public void setMainApp(Main mainApp) {
		this.window = mainApp;
	}

	public TabPane getSettingPane() {
		return settingPane;
	}

	@FXML
	public void exitApplication(ActionEvent event) {
		System.out.println("Stage is closing");
		Platform.exit();
	}
}
