package org.zigi.evolution.controller;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.zigi.evolution.algorithm.GeneticProgramming;
import org.zigi.evolution.model.AlgorithmModel;
import org.zigi.evolution.model.MutateFunctionModel;
import org.zigi.evolution.model.PopulationModel;
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
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
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

	@FXML
	private Label generationLabel;

	@FXML
	private TextField generation;

	@FXML
	private Label crossMutateLabel;

	@FXML
	private Slider crossMutate;

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
		PopulationModel popModel = Services.populationService().getSelected();
		AlgorithmModel algModel = Services.algorithmService().getSelected();

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

		// generation
		generationLabel.setText(Utils.getLabel("generation"));

		generation.setText(String.valueOf(algModel.getAlgorithm().getGeneration()));
		generation.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try {
					Services.algorithmService().getSelected().getAlgorithm().setGeneration(Integer.parseInt(newValue));
					LOG.debug("Change generation count to " + newValue);
				} catch (Exception e) {
					LOG.warn("Invalid value for generation count " + newValue);
				}
			}
		});

		// cross-mutate
		crossMutateLabel.setText(Utils.getLabel("cross_mutate"));

		if (algModel.getAlgorithm() instanceof GeneticProgramming) {
			GeneticProgramming gp = (GeneticProgramming) algModel.getAlgorithm();
			crossMutate.valueProperty().setValue(gp.getMutateProbability());
		}
		crossMutate.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (algModel.getAlgorithm() instanceof GeneticProgramming) {
					GeneticProgramming gp = (GeneticProgramming) algModel.getAlgorithm();
					gp.setMutateProbability(newValue.doubleValue());
				}
			}
		});
	}
}
