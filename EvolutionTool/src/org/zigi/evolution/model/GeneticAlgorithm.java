package org.zigi.evolution.model;

import org.zigi.evolution.algorithm.EA;
import org.zigi.evolution.controller.MainController;
import org.zigi.evolution.solution.DoubleValue;
import org.zigi.evolution.solution.array.ArraySolution;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;

public class GeneticAlgorithm extends EvolutionAlgorithm<ArraySolution<DoubleValue>, DoubleValue> {

	public GeneticAlgorithm(MainController controller, EA<ArraySolution<DoubleValue>, DoubleValue> algorithm) {
		super(controller, algorithm);
	}

	@Override
	public void initPane() {
		TabPane pane = getController().getSettingPane();

		for (int i = 0; i < 5; i++) {
			Tab tab = new Tab();
			tab.setText("Tab" + i);
			HBox hbox = new HBox();
			hbox.getChildren().add(new Label("Tab" + i));
			hbox.setAlignment(Pos.CENTER);
			tab.setContent(hbox);
			pane.getTabs().add(tab);
		}
	}

}
