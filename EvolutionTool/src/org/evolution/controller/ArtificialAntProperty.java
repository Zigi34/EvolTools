package org.evolution.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.evolution.model.ProblemModel;
import org.evolution.problem.ArtificialAnt;
import org.evolution.services.ProblemService;
import org.evolution.solution.type.GPFenotype;
import org.evolution.solution.type.IfFoodAhead;
import org.evolution.solution.type.LeftDirection;
import org.evolution.solution.type.Move;
import org.evolution.solution.type.Prg2;
import org.evolution.solution.type.Prg3;
import org.evolution.solution.type.RightDirection;
import org.evolution.util.Utils;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.Callback;

public class ArtificialAntProperty extends AnchorPane {

	private static final Logger LOG = Logger.getLogger(ArtificialAntProperty.class);
	private static List<GPFenotype> allFenotype = new LinkedList<GPFenotype>();

	@FXML
	private ChoiceBox<GPFenotype> fenotypeCombo;

	@FXML
	private ListView<GPFenotype> selected;

	@FXML
	private Button createButton;

	@FXML
	private Button removeButton;

	@FXML
	private TextField minValue;

	@FXML
	private TextField maxValue;

	@FXML
	private Label minValueLabel;

	@FXML
	private Label maxValueLabel;

	private FenotypeProperty selectedFenotype;
	private FileChooser fileChooser = new FileChooser();

	static {
		allFenotype.add(new Move());
		allFenotype.add(new LeftDirection());
		allFenotype.add(new RightDirection());
		allFenotype.add(new IfFoodAhead());
		allFenotype.add(new Prg2());
		allFenotype.add(new Prg3());
	}

	public ArtificialAntProperty() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/ArtificialAntProperty.fxml"));
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
		if (model != null && model.getProblem() instanceof ArtificialAnt) {
			ArtificialAnt problem = (ArtificialAnt) model.getProblem();
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
			fenotypeCombo.valueProperty().addListener(new ChangeListener<GPFenotype>() {
				@Override
				public void changed(ObservableValue<? extends GPFenotype> observable, GPFenotype oldValue, GPFenotype newValue) {
					LOG.info("vybráno " + newValue);
					selectedFenotype = new FenotypeProperty(newValue);
				}
			});
			fenotypeCombo.getSelectionModel().selectFirst();

			// nastavení minimální a maximální hodnoty
			minValueLabel.setText("Minimální hodnota");
			maxValueLabel.setText("Maximální hodnota");
			minValue.setText(String.valueOf(problem.getMinFunctionValue()));
			maxValue.setText(String.valueOf(problem.getMaxFunctionValue()));
			minValue.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							try {
								problem.setMinFunctionValue(Double.parseDouble(newValue));
							} catch (Exception e) {
							}
						}
					});
				}
			});
			maxValue.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							try {
								problem.setMaxFunctionValue(Double.parseDouble(newValue));
							} catch (Exception e) {
							}
						}
					});
				}
			});
		}
	}
}
