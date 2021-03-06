package org.evolution.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.evolution.controller.fenotype.IFenotypeProperty;
import org.evolution.solution.type.CosFunction;
import org.evolution.solution.type.DivideFunction;
import org.evolution.solution.type.GPFenotype;
import org.evolution.solution.type.IfFoodAhead;
import org.evolution.solution.type.LeftDirection;
import org.evolution.solution.type.Move;
import org.evolution.solution.type.MultiplyFunction;
import org.evolution.solution.type.NumericConstant;
import org.evolution.solution.type.Prg2;
import org.evolution.solution.type.Prg3;
import org.evolution.solution.type.RangedPowerFunction;
import org.evolution.solution.type.RightDirection;
import org.evolution.solution.type.SinFunction;
import org.evolution.solution.type.SubtractionFunction;
import org.evolution.solution.type.SumFunction;

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
	private Label label1;

	@FXML
	private TextField text1;

	@FXML
	private Label label2;

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
		name.setText(fenotype.getName());
		label1.setVisible(false);
		label2.setVisible(false);
		if (fenotype.getClass().equals(SumFunction.class)) {
			text1.setVisible(false);
			text2.setVisible(false);
		} else if (fenotype.getClass().equals(SubtractionFunction.class)) {
			text1.setVisible(false);
			text2.setVisible(false);
		} else if (fenotype.getClass().equals(MultiplyFunction.class)) {
			text1.setVisible(false);
			text2.setVisible(false);
		} else if (fenotype.getClass().equals(DivideFunction.class)) {
			text1.setVisible(false);
			text2.setVisible(false);
		} else if (fenotype.getClass().equals(SinFunction.class)) {
			text1.setVisible(false);
			text2.setVisible(false);
		} else if (fenotype.getClass().equals(CosFunction.class)) {
			text1.setVisible(false);
			text2.setVisible(false);
		} else if (fenotype.getClass().equals(NumericConstant.class)) {
			NumericConstant cons = (NumericConstant) fenotype;
			label1.setVisible(true);
			label2.setVisible(true);
			text1.setVisible(true);
			text1.setText(String.valueOf(cons.getMinValue()));
			label1.setText("Minumum");
			text2.setVisible(true);
			text2.setText(String.valueOf(cons.getMaxValue()));
			label2.setText("Maximum");
		} else if (fenotype.getClass().equals(RangedPowerFunction.class)) {
			RangedPowerFunction rPow = (RangedPowerFunction) fenotype;
			label1.setVisible(true);
			label2.setVisible(true);
			text1.setVisible(true);
			text2.setVisible(true);
			text1.setText(String.valueOf(rPow.getMinIndex()));
			label1.setText("Minimum");
			text2.setText(String.valueOf(rPow.getMaxIndex()));
			label2.setText("Maximum");
		}
	}

	@Override
	public GPFenotype generateFenotype() {
		GPFenotype result = null;
		// result = fenotype.cloneMe();

		if (fenotype.getClass().equals(SumFunction.class)) {
			return new SumFunction();
		} else if (fenotype.getClass().equals(SubtractionFunction.class)) {
			return new SubtractionFunction();
		} else if (fenotype.getClass().equals(MultiplyFunction.class)) {
			return new MultiplyFunction();
		} else if (fenotype.getClass().equals(DivideFunction.class)) {
			return new DivideFunction();
		} else if (fenotype.getClass().equals(SinFunction.class)) {
			return new SinFunction();
		} else if (fenotype.getClass().equals(CosFunction.class)) {
			return new CosFunction();
		} else if (fenotype.getClass().equals(NumericConstant.class)) {
			return new NumericConstant(getValue1(), getValue2());
		} else if (fenotype.getClass().equals(RangedPowerFunction.class)) {
			return new RangedPowerFunction(getValue1(), getValue2(), true);
		} else if (fenotype.getClass().equals(Move.class)) {
			return new Move();
		} else if (fenotype.getClass().equals(LeftDirection.class)) {
			return new LeftDirection();
		} else if (fenotype.getClass().equals(RightDirection.class)) {
			return new RightDirection();
		} else if (fenotype.getClass().equals(IfFoodAhead.class)) {
			return new IfFoodAhead();
		} else if (fenotype.getClass().equals(Prg2.class)) {
			return new Prg2();
		} else if (fenotype.getClass().equals(Prg3.class)) {
			return new Prg3();
		}

		return result;
	}

	public String getLabel1() {
		return label1.getText();
	}

	public void setLabel1(String label1) {
		this.label1.setText(label1);
	}

	public Double getValue1() {
		try {
			return Double.parseDouble(text1.getText());
		} catch (Exception e) {
		}
		return null;
	}

	public void setValue1(Double value) {
		this.text1.setText(value.toString());
	}

	public String getLabel2() {
		return label2.getText();
	}

	public void setLabel2(String label2) {
		this.label2.setText(label2);
	}

	public Double getValue2() {
		try {
			return Double.parseDouble(text2.getText());
		} catch (Exception e) {
		}
		return null;
	}

	public void setText2(TextField text2) {
		this.text2 = text2;
	}

}
