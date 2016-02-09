package org.zigi.evolution.controller;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.zigi.evolution.model.ProblemModel;
import org.zigi.evolution.services.Services;
import org.zigi.evolution.util.Utils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;

public class EvolutionToolProblemProperty extends BorderPane {

	private static final Logger LOG = Logger.getLogger(EvolutionToolProblemProperty.class);

	@FXML
	private Label selectProblemLabel;

	@FXML
	private ChoiceBox<ProblemModel> problemFunction;

	@FXML
	private TitledPane selectProblemPane;

	public EvolutionToolProblemProperty() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/EvolutionToolProblemProperty.fxml"));
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
		// basic property
		selectProblemPane.setText(Utils.getLabel("select_problem_pane"));

		// select function
		selectProblemLabel.setText(Utils.getLabel("select_problem"));

		ObservableList<ProblemModel> selectList = FXCollections.observableList(Services.problemService().findProblemFunctions());
		problemFunction.setItems(selectList);
		problemFunction.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ProblemModel>() {
			@Override
			public void changed(ObservableValue<? extends ProblemModel> observable, ProblemModel oldValue, ProblemModel newValue) {
				Services.problemService().setSelected(newValue);
				LOG.info("Změna problému");
			}
		});
		problemFunction.getSelectionModel().selectFirst();
	}
}
