package org.evolution.controller;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.evolution.EvolutionTool;
import org.evolution.model.ProblemModel;
import org.evolution.problem.RegressionProblem;
import org.evolution.services.ProblemService;
import org.evolution.solution.type.CosFunction;
import org.evolution.solution.type.DivideFunction;
import org.evolution.solution.type.GPFenotype;
import org.evolution.solution.type.MultiplyFunction;
import org.evolution.solution.type.NumericConstant;
import org.evolution.solution.type.RangedPowerFunction;
import org.evolution.solution.type.SinFunction;
import org.evolution.solution.type.SubtractionFunction;
import org.evolution.solution.type.SumFunction;
import org.evolution.util.Utils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.Callback;

public class SymbolicRegressionProblemProperty extends AnchorPane {

	private static final Logger LOG = Logger.getLogger(SymbolicRegressionProblemProperty.class);
	private static List<GPFenotype> allFenotype = new LinkedList<GPFenotype>();

	@FXML
	private ChoiceBox<GPFenotype> fenotypeCombo;

	@FXML
	private AnchorPane fenotypeProperty;

	@FXML
	private ListView<GPFenotype> selected;

	@FXML
	private TextField datasetPath;

	@FXML
	private Button fileButton;

	@FXML
	private Button createButton;

	@FXML
	private Button removeButton;

	private FenotypeProperty selectedFenotype;
	private FileChooser fileChooser = new FileChooser();

	static {
		allFenotype.add(new SumFunction());
		allFenotype.add(new SubtractionFunction());
		allFenotype.add(new MultiplyFunction());
		allFenotype.add(new DivideFunction());
		allFenotype.add(new SinFunction());
		allFenotype.add(new CosFunction());
		allFenotype.add(new NumericConstant());
		allFenotype.add(new RangedPowerFunction());
	}

	public SymbolicRegressionProblemProperty() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/SymbolicRegressionProperty.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			LOG.error(exception);
		}

		initialize();
	}

	/**
	 * Třída reprezentující zobrazení fenotypů
	 * 
	 * @author Zdeněk Gold
	 *
	 */
	class FenotypeCell extends ListCell<GPFenotype> {
		@Override
		protected void updateItem(GPFenotype item, boolean empty) {
			super.updateItem(item, empty);
			if (empty || item == null) {
				setText(null);
				setGraphic(null);
			} else {
				setText(item.getName());
			}
		}
	}

	private void initialize() {
		fileChooser.setTitle("Vyber soubor");

		ProblemModel model = ProblemService.getSelected();
		selected.getItems().clear();
		if (model != null && model.getProblem() instanceof RegressionProblem) {
			RegressionProblem problem = (RegressionProblem) model.getProblem();
			List<GPFenotype> problemFenotypes = problem.getFenotypes();

			// inicializace tabulky fenotypu podle problemu
			for (GPFenotype item : allFenotype) {
				Object obj = Utils.getInstanceOfClass(item.getClass(), problemFenotypes);
				if (obj == null) {

				} else {
					selected.getItems().add(item);
				}
			}

			// seznam použitých elementů
			selected.setCellFactory(new Callback<ListView<GPFenotype>, ListCell<GPFenotype>>() {
				@Override
				public ListCell<GPFenotype> call(ListView<GPFenotype> param) {
					return new FenotypeCell();
				}
			});

			// tlačítko přidat
			createButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if (selectedFenotype != null) {
						GPFenotype fenotype = selectedFenotype.generateFenotype();
						LOG.info("Přidávám " + fenotype);
						selected.getItems().add(fenotype);
						problem.addFenotype(fenotype);
					}
				}
			});

			// tlačítko odebrat
			removeButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if (selected.getSelectionModel().getSelectedItem() != null) {
						GPFenotype fenotype = selected.getSelectionModel().getSelectedItem();
						LOG.info("Odebírám " + fenotype);
						selected.getItems().remove(fenotype);
						problem.removeFenotype(fenotype);
					}
				}
			});

			// vyber fenotype
			fenotypeCombo.getItems().addAll(allFenotype);
			fenotypeCombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<GPFenotype>() {
				@Override
				public void changed(ObservableValue<? extends GPFenotype> observable, GPFenotype oldValue, GPFenotype newValue) {
					LOG.info("vybráno " + newValue);
					fenotypeProperty.getChildren().clear();
					selectedFenotype = new FenotypeProperty(newValue);
					fenotypeProperty.getChildren().add(selectedFenotype);
				}
			});
			fenotypeCombo.getSelectionModel().selectFirst();

			// cesta k datasetu
			datasetPath.setText(problem.getDatasetPath());

			// výběr datasetu
			fileButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					File file = fileChooser.showOpenDialog(EvolutionTool.getStage());
					if (file.exists()) {
						problem.setDatasetPath(file.toString());
						datasetPath.setText(file.toString());
					}
				}
			});
		}
	}
}
