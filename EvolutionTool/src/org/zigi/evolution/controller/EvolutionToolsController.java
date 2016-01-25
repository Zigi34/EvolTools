package org.zigi.evolution.controller;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.zigi.evolution.EvolutionToolsApplication;
import org.zigi.evolution.algorithm.GeneticProgramming;
import org.zigi.evolution.model.InfoSource;
import org.zigi.evolution.model.SolutionModel;
import org.zigi.evolution.problem.ArtificialAnt;
import org.zigi.evolution.select.RankSelect;
import org.zigi.evolution.select.RouleteWheelSelect;
import org.zigi.evolution.select.SelectFunction;
import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.util.Population;
import org.zigi.evolution.util.Utils;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class EvolutionToolsController implements Initializable {

	private static final Logger LOG = Logger.getLogger(EvolutionToolsController.class);

	private EvolutionToolsApplication window;

	// algoritmus
	private GeneticProgramming evolAlgorithm = new GeneticProgramming();

	// selektory
	private List<SelectFunction> selectFunctionList = new LinkedList<SelectFunction>();

	@FXML
	private ListView<InfoSource> infoSourceList;

	@FXML
	private Label populationSizeLabel;

	@FXML
	private TextField populationSizeInput;

	@FXML
	private Label selectFunctionLabel;

	@FXML
	private ChoiceBox<SelectFunction> selectFunctionInput;

	@FXML
	private AnchorPane infoSourceContent;

	@FXML
	private TableView<SolutionModel> populationTable;

	public EvolutionToolsApplication getWindow() {
		return window;
	}

	public void setWindow(EvolutionToolsApplication window) {
		this.window = window;
	}

	@FXML
	public void exitApplication(ActionEvent event) {
		System.out.println("Stage is closing");
		Platform.exit();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// select funkce
		selectFunctionList.add(new RankSelect());
		selectFunctionList.add(new RouleteWheelSelect());
		selectFunctionLabel.setText(Utils.getLabel("select_function_label"));
		selectFunctionInput.setItems(FXCollections.observableList(selectFunctionList));

		// inicializace problemu
		ArtificialAnt problem = new ArtificialAnt();
		try {
			problem.setMaxHeight(6);
			problem.setMaxMoves(420);
			problem.setYard(new File("resource/data/artificial_ant"));
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e);
		}

		// algoritmus
		Population pop = new Population(20);
		for (int i = 0; i < pop.getMaxSolutions(); i++)
			pop.add(problem.randomGrowTreeSolution());
		evolAlgorithm.setPopulation(pop);

		// inicializace nastaveni maximalni velikosti populace
		populationSizeLabel.setText(Utils.getLabel("population_size_label"));
		populationSizeInput.setText(String.valueOf(evolAlgorithm.getPopulation().getMaxSolutions()));
		populationSizeInput.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try {
					evolAlgorithm.setPopulationSize(Integer.parseInt(newValue));
					LOG.info("Změna hodnoty populace na " + newValue);
				} catch (Exception e) {
					LOG.error(e);
				}
			}
		});

		// inicializace informacnich oken
		List<InfoSource> values = new LinkedList<InfoSource>();
		InfoSource source = new InfoSource("Population fitness", "/gui/population-fitness.fxml");
		values.add(source);
		infoSourceList.setItems(FXCollections.observableList(values));

		infoSourceList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<InfoSource>() {

			@Override
			public void changed(ObservableValue<? extends InfoSource> observable, InfoSource oldValue,
					InfoSource newValue) {
				System.out.println("Vybráno: " + newValue);
				infoSourceContent.getChildren().clear();
				infoSourceContent.getChildren().add(Utils.loadView(EvolutionToolsApplication.class, newValue.getGui()));
			}
		});

		// inicializace populacni tabulky
		final ObservableList<SolutionModel> data = FXCollections.observableArrayList();
		if (evolAlgorithm != null) {
			for (Solution sol : evolAlgorithm.getPopulation().getSolutions()) {
				data.add(new SolutionModel(sol));
			}
		}

		TableColumn<SolutionModel, String> fitnessCol = new TableColumn<SolutionModel, String>("Fitness");
		fitnessCol.setCellValueFactory(new PropertyValueFactory<SolutionModel, String>("fitness"));
		TableColumn<SolutionModel, String> genotypeCol = new TableColumn<SolutionModel, String>("Genotype");
		genotypeCol.setCellValueFactory(new PropertyValueFactory<SolutionModel, String>("genotype"));

		populationTable.setItems(data);
		populationTable.getColumns().add(fitnessCol);
		populationTable.getColumns().add(genotypeCol);

	}
}
