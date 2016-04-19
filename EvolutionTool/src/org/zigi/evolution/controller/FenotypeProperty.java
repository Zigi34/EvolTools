package org.zigi.evolution.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.zigi.evolution.controller.fenotype.IFenotypeProperty;
import org.zigi.evolution.solution.value.GPFenotype;
import org.zigi.evolution.solution.value.SumFunction;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class FenotypeProperty extends AnchorPane implements IFenotypeProperty {

	private static final Logger LOG = Logger.getLogger(FenotypeProperty.class);
	private static List<GPFenotype> allGenotype = new LinkedList<GPFenotype>();

	@FXML
	private Label name;

	@FXML
	private TextField text1;

	@FXML
	private TextField text2;

	private GPFenotype fenotype;

	public FenotypeProperty(GPFenotype fenotype) {
		this.fenotype = fenotype;

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/FenotypeProperty.fxml"));
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
		name.setText(fenotype.toString());
		if (fenotype.getClass().equals(SumFunction.class)) {
			text1.setVisible(false);
			text2.setVisible(false);
		}
	}

	@Override
	public GPFenotype generateFenotype() {
		GPFenotype result = null;
		if (fenotype.getClass().equals(SumFunction.class)) {
			return new SumFunction();
		}
		return result;
	}

}
