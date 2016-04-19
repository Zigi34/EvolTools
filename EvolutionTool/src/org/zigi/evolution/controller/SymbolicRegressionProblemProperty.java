package org.zigi.evolution.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.zigi.evolution.model.ProblemModel;
import org.zigi.evolution.problem.RegressionProblem;
import org.zigi.evolution.services.Services;
import org.zigi.evolution.solution.value.CosFunction;
import org.zigi.evolution.solution.value.DivideFunction;
import org.zigi.evolution.solution.value.GPFenotype;
import org.zigi.evolution.solution.value.MultiplyFunction;
import org.zigi.evolution.solution.value.NumericConstant;
import org.zigi.evolution.solution.value.RangedPowerFunction;
import org.zigi.evolution.solution.value.SinFunction;
import org.zigi.evolution.solution.value.SubtractionFunction;
import org.zigi.evolution.solution.value.SumFunction;
import org.zigi.evolution.util.Utils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

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
	private Button createButton;

	private FenotypeProperty selectedFenotype;

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

	private void initialize() {
		selected.getItems().clear();

		// tlačítko přidat
		createButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (selectedFenotype != null) {
					LOG.info("Přidávám " + selectedFenotype.generateFenotype());
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

		ProblemModel model = Services.problemService().getSelected();
		if (model != null && model.getProblem() instanceof RegressionProblem) {
			RegressionProblem problem = (RegressionProblem) model.getProblem();
			List<GPFenotype> problemFenotypes = problem.getFenotypes();

			for (GPFenotype item : allFenotype) {
				Object obj = Utils.getInstanceOfClass(item.getClass(), problemFenotypes);
				if (obj == null) {
					LOG.info(obj + " neobsahuje");
				} else {
					LOG.info(obj + " obsahuje");
				}

			}
		}
	}
}
