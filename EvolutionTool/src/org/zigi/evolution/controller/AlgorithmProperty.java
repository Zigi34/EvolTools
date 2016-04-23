package org.zigi.evolution.controller;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.zigi.evolution.algorithm.GeneticProgramming;
import org.zigi.evolution.model.AlgorithmModel;
import org.zigi.evolution.model.MutateFunctionModel;
import org.zigi.evolution.model.PopulationModel;
import org.zigi.evolution.model.ProblemModel;
import org.zigi.evolution.model.SelectFunctionModel;
import org.zigi.evolution.services.AlgorithmService;
import org.zigi.evolution.services.PopulationService;
import org.zigi.evolution.services.ProblemService;
import org.zigi.evolution.services.SelectFunctionService;
import org.zigi.evolution.services.Services;
import org.zigi.evolution.util.Utils;

import javafx.application.Platform;
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

public class AlgorithmProperty extends BorderPane {

	private static final Logger LOG = Logger.getLogger(AlgorithmProperty.class);

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

	@FXML
	private Label popLabel;

	@FXML
	private Slider popSlider;

	@FXML
	private ChoiceBox<Number> treeHeight;

	public AlgorithmProperty() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/AlgorithmProperty.fxml"));
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
		Services.populationService();
		Services.algorithmService();
		AlgorithmModel algModel = AlgorithmService.getSelected();

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
				SelectFunctionService.setSelected(newValue);
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
					AlgorithmService.getSelected().getAlgorithm().setGeneration(Integer.parseInt(newValue));
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

		popLabel.setText("Velikost populace:");
		popSlider.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> source, Number oldValue, Number newValue) {
				popLabel.setText("Velikost populace: " + newValue.intValue());
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						Services.populationService();
						PopulationModel model = PopulationService.getSelected();
						if (model != null) {
							model.getPopulation().setMax(newValue.intValue());
							LOG.info("Nastavení populace na " + newValue);
						}
					}
				});
			}
		});

		// výška stromu
		ProblemModel problemModel = ProblemService.getSelected();
		treeHeight.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				problemModel.getProblem().setMaxHeight(newValue.intValue());
				LOG.info("Maximální výška stromu nastavena na " + newValue.intValue());
			}
		});
		for (Integer i = 1; i <= 20; i++)
			treeHeight.getItems().add(i);
		treeHeight.getSelectionModel().select(5);
	}
}
