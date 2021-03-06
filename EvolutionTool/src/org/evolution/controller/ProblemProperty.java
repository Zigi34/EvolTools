package org.evolution.controller;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.evolution.model.ProblemModel;
import org.evolution.problem.ArtificialAnt;
import org.evolution.problem.RegressionProblem;
import org.evolution.services.Services;
import org.evolution.util.Utils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class ProblemProperty extends AnchorPane {

	private static final Logger LOG = Logger.getLogger(ProblemProperty.class);

	@FXML
	private Label selectProblemLabel;

	@FXML
	private ChoiceBox<ProblemModel> problemFunction;

	private AnchorPane problemPane;

	public ProblemProperty() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/ProblemProperty.fxml"));
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

		// select function
		selectProblemLabel.setText(Utils.getLabel("select_problem"));

		ObservableList<ProblemModel> selectList = FXCollections.observableList(Services.problemService().findProblemFunctions());
		problemFunction.setItems(selectList);
		problemFunction.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ProblemModel>() {
			@Override
			public void changed(ObservableValue<? extends ProblemModel> observable, ProblemModel oldValue, ProblemModel newValue) {
				Services.problemService().setSelected(newValue);
				LOG.info("Změna problému");

				try {
					if (newValue.getProblem() instanceof RegressionProblem) {
						if (problemPane != null)
							getChildren().remove(problemPane);
						SymbolicRegressionProperty panel = new SymbolicRegressionProperty();
						AnchorPane.setTopAnchor(panel, 64.0);
						AnchorPane.setLeftAnchor(panel, 14.0);
						AnchorPane.setRightAnchor(panel, 14.0);
						AnchorPane.setBottomAnchor(panel, 14.0);
						panel.setPrefHeight(-1.0);
						panel.setPrefWidth(-1.0);
						problemPane = panel;
						getChildren().add(panel);
					} else if (newValue.getProblem() instanceof ArtificialAnt) {
						if (problemPane != null)
							getChildren().remove(problemPane);
						ArtificialAntProperty panel = new ArtificialAntProperty();
						AnchorPane.setTopAnchor(panel, 64.0);
						AnchorPane.setLeftAnchor(panel, 14.0);
						AnchorPane.setRightAnchor(panel, 14.0);
						AnchorPane.setBottomAnchor(panel, 14.0);
						panel.setPrefHeight(-1.0);
						panel.setPrefWidth(-1.0);
						problemPane = panel;
						getChildren().add(panel);
					}
				} catch (Exception e) {
					LOG.error(e);
				}
			}
		});
		problemFunction.getSelectionModel().selectFirst();
	}
}
