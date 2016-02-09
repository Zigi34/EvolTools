package org.zigi.evolution.controller;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.zigi.evolution.model.MutateFunctionModel;
import org.zigi.evolution.model.SelectFunctionModel;
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

public class EvolutionToolAlgorithmProperty extends BorderPane {

	private static final Logger LOG = Logger.getLogger(EvolutionToolAlgorithmProperty.class);

	@FXML
	private Label selectFunctionLabel;

	@FXML
	private ChoiceBox<SelectFunctionModel> selectFunction;

	@FXML
	private Label mutateFunctionLabel;

	@FXML
	private ChoiceBox<MutateFunctionModel> mutateFunction;

	@FXML
	private TitledPane basicProperty;

	public EvolutionToolAlgorithmProperty() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/EvolutionToolAlgorithmProperty.fxml"));
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
		basicProperty.setText(Utils.getLabel("basic_property"));

		// select function
		selectFunctionLabel.setText(Utils.getLabel("select_function"));

		ObservableList<SelectFunctionModel> selectList = FXCollections.observableList(Services.selectFunctionService().findSelectFunctions());
		selectFunction.setItems(selectList);
		selectFunction.getSelectionModel().selectFirst();
		selectFunction.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<SelectFunctionModel>() {
			@Override
			public void changed(ObservableValue<? extends SelectFunctionModel> observable, SelectFunctionModel oldValue, SelectFunctionModel newValue) {
				LOG.info("Změna select funkce");
				Services.selectFunctionService().setSelected(newValue);
			}
		});

		// mutate function
		mutateFunctionLabel.setText(Utils.getLabel("mutate_function"));

		ObservableList<MutateFunctionModel> mutateList = FXCollections.observableList(Services.mutateFunctionService().findMutateFunctions());
		mutateFunction.setItems(mutateList);
		mutateFunction.getSelectionModel().selectFirst();
	}
}
