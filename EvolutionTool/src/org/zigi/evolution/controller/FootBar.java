package org.zigi.evolution.controller;

import java.io.IOException;

import org.zigi.evolution.util.Utils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;

public class FootBar extends ToolBar {

	@FXML
	private Label title;

	@FXML
	private Label generation;

	@FXML
	private Label best;

	public FootBar() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/FootBar.fxml"));
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
		title.setText(Utils.getLabel("foot_title"));
		generation.setText(Utils.getLabel("foot_generation"));
		best.setText(Utils.getLabel("foot_best"));
	}

	public void setTitle(String value) {
		title.setText(value);
	}

	public String getTitle() {
		return title.getText();
	}

	public void setGeneration(String value) {
		generation.setText(value);
	}

	public String getGeneration() {
		return generation.getText();
	}

	public void setBest(String value) {
		this.best.setText(value);
	}

	public String getBest() {
		return best.getText();
	}

}
