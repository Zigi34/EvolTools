package org.evolution.controller;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.evolution.algorithm.GeneticProgramming;
import org.evolution.model.AlgorithmModel;
import org.evolution.model.CrossFunctionModel;
import org.evolution.model.MutateFunctionModel;
import org.evolution.model.PopulationModel;
import org.evolution.model.ProblemModel;
import org.evolution.model.SelectFunctionModel;
import org.evolution.select.SelectFunction;
import org.evolution.select.TournamentSelection;
import org.evolution.services.AlgorithmService;
import org.evolution.services.PopulationService;
import org.evolution.services.ProblemService;
import org.evolution.services.SelectFunctionService;
import org.evolution.services.Services;
import org.evolution.util.Population;
import org.evolution.util.Utils;

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
	private Label crossFunctionLabel;

	@FXML
	private ChoiceBox<CrossFunctionModel> crossFunction;

	@FXML
	private TitledPane basicProperty;

	@FXML
	private Label generationLabel;

	@FXML
	private TextField generation;

	@FXML
	private Label crossLabel;

	@FXML
	private Slider cross;

	@FXML
	private Label mutateLabel;

	@FXML
	private Slider mutate;

	@FXML
	private Label reproductionLabel;

	@FXML
	private Slider reproduction;

	@FXML
	private Label popLabel;

	@FXML
	private Slider popSlider;

	@FXML
	private ChoiceBox<Number> treeHeight;

	@FXML
	private Label tournamentSizeLabel;

	@FXML
	private TextField tournamentSize;

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
		tournamentSizeLabel.setVisible(false);
		tournamentSize.setVisible(false);
		selectFunctionLabel.setText(Utils.getLabel("select_function"));

		ObservableList<SelectFunctionModel> selectList = FXCollections.observableList(Services.selectFunctionService().findSelectFunctions());
		selectFunction.setItems(selectList);
		selectFunction.getSelectionModel().selectFirst();
		selectFunction.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<SelectFunctionModel>() {
			@Override
			public void changed(ObservableValue<? extends SelectFunctionModel> observable, SelectFunctionModel oldValue, SelectFunctionModel newValue) {
				LOG.info("Změna select funkce");
				SelectFunction select = newValue.getFunction();
				if (select instanceof TournamentSelection) {
					TournamentSelection tournament = (TournamentSelection) select;
					tournamentSizeLabel.setVisible(true);
					tournamentSizeLabel.setText("Jedinců v turnaji" + tournament.getTournamentSize());
					tournamentSize.setVisible(true);
					tournamentSize.setText(String.valueOf(tournament.getTournamentSize()));
				} else {
					tournamentSizeLabel.setVisible(false);
					tournamentSize.setVisible(false);
				}
				SelectFunctionService.setSelected(newValue);
			}
		});

		// nastavení velikosti turnaje
		tournamentSize.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				SelectFunction select = Services.selectFunctionService().getSelected().getFunction();
				if (select instanceof TournamentSelection) {
					TournamentSelection tournament = (TournamentSelection) select;
					Population pop = PopulationService.getSelected().getPopulation();
					try {
						Integer size = Integer.parseInt(tournamentSize.getText());
						if (size < pop.getMaxSolutions() && size > 1)
							tournament.setTournamentSize(size);
					} catch (Exception e) {
					}
				}
			}
		});

		// mutate function
		mutateFunctionLabel.setText(Utils.getLabel("mutate_function"));

		ObservableList<MutateFunctionModel> mutateList = FXCollections.observableList(Services.mutateFunctionService().findMutateFunctions());
		mutateFunction.setItems(mutateList);
		mutateFunction.getSelectionModel().selectFirst();
		mutateFunction.valueProperty().addListener(new ChangeListener<MutateFunctionModel>() {
			@Override
			public void changed(ObservableValue<? extends MutateFunctionModel> observable, MutateFunctionModel oldValue, MutateFunctionModel newValue) {
				GeneticProgramming gp = (GeneticProgramming) AlgorithmService.getSelected().getAlgorithm();
				gp.setMutate(newValue.getFunction());
				LOG.debug("Mutační funkce: " + newValue.getFunction());
			}
		});

		// mutate function
		crossFunctionLabel.setText(Utils.getLabel("cross_function"));

		ObservableList<CrossFunctionModel> crossList = FXCollections.observableList(Services.crossFunctionService().findCrossFunctions());
		crossFunction.setItems(crossList);
		crossFunction.getSelectionModel().selectFirst();
		crossFunction.valueProperty().addListener(new ChangeListener<CrossFunctionModel>() {
			@Override
			public void changed(ObservableValue<? extends CrossFunctionModel> observable, CrossFunctionModel oldValue, CrossFunctionModel newValue) {
				GeneticProgramming gp = (GeneticProgramming) AlgorithmService.getSelected().getAlgorithm();
				gp.setCross(newValue.getFunction());
				LOG.debug("Funkce křížení: " + newValue.getFunction());
			}
		});

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

		popLabel.setText("Velikost populace:");
		popSlider.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> source, Number oldValue, Number newValue) {
				popLabel.setText("Velikost populace: " + newValue.intValue());
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						PopulationModel model = PopulationService.getSelected();
						if (model != null) {
							model.getPopulation().setMaxSolutions(newValue.intValue());
							LOG.info("Nastavení populace na " + newValue);
						}
					}
				});
			}
		});

		// cross
		crossLabel.setText(Utils.getLabel("cross"));
		if (algModel.getAlgorithm() instanceof GeneticProgramming) {
			GeneticProgramming gp = (GeneticProgramming) algModel.getAlgorithm();
			cross.valueProperty().setValue(gp.getCrossProbrability());
		}
		cross.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (oldValue != newValue && algModel.getAlgorithm() instanceof GeneticProgramming) {
					GeneticProgramming gp = (GeneticProgramming) algModel.getAlgorithm();
					gp.setCrossProbrability(newValue.doubleValue());
					mutate.setValue(gp.getMutateProbability());
					reproduction.setValue(gp.getReproduceProbability());
				}
			}
		});

		// mutate
		mutateLabel.setText(Utils.getLabel("mutate"));
		if (algModel.getAlgorithm() instanceof GeneticProgramming) {
			GeneticProgramming gp = (GeneticProgramming) algModel.getAlgorithm();
			mutate.valueProperty().setValue(gp.getMutateProbability());
		}
		mutate.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (oldValue != newValue && algModel.getAlgorithm() instanceof GeneticProgramming) {
					GeneticProgramming gp = (GeneticProgramming) algModel.getAlgorithm();
					gp.setMutateProbability(newValue.doubleValue());
					cross.setValue(gp.getCrossProbrability());
					reproduction.setValue(gp.getReproduceProbability());
				}
			}
		});

		// reproduction
		reproductionLabel.setText(Utils.getLabel("reproduction"));
		if (algModel.getAlgorithm() instanceof GeneticProgramming) {
			GeneticProgramming gp = (GeneticProgramming) algModel.getAlgorithm();
			reproduction.valueProperty().setValue(gp.getReproduceProbability());
		}
		reproduction.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (oldValue != newValue && algModel.getAlgorithm() instanceof GeneticProgramming) {
					GeneticProgramming gp = (GeneticProgramming) algModel.getAlgorithm();
					gp.setReproduceProbability(newValue.doubleValue());
					cross.setValue(gp.getCrossProbrability());
					mutate.setValue(gp.getMutateProbability());
				}
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
