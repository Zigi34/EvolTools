package org.zigi.evolution.evaluate;

import java.util.Random;

import org.apache.log4j.Logger;
import org.zigi.evolution.Population;
import org.zigi.evolution.solution.tree.ConstantValue;
import org.zigi.evolution.solution.tree.NodeValue;
import org.zigi.evolution.solution.tree.TreeSolution;
import org.zigi.evolution.solution.tree.VariableValue;

public class ExpressionFunction extends EvaluateFunction<TreeSolution, NodeValue> {

	private static final Logger LOG = Logger.getLogger(ExpressionFunction.class);
	private static final Random RAND = new Random();

	@Override
	public Double evaluate(TreeSolution solution) {
		Double sum = 0.0;
		for (Double i = 0.0; i < 5.0; i += 0.1) {
			setTreeValue(solution, i);
			Double realValue = (Double) solution.getRoot().getValue();
			Double rightValue = rightValue(i);
			sum += (Math.abs(realValue - rightValue) * 0.1);
		}
		return 1.0 / sum;
	}

	@Override
	public void evaluate(Population<TreeSolution, NodeValue> population) {

	}

	private Double rightValue(Double value) {
		if (value != null) {
			return 3.0 * Math.sin(value - 10.0) + Math.cos(4 * value);
		}
		return 0.0;
	}

	private void setTreeValue(TreeSolution tree, Double xVal) {
		for (NodeValue node : tree.getChildNodes()) {
			LOG.info("Node:" + node);
			if (node instanceof VariableValue) {
				VariableValue var = (VariableValue) node;
				var.setValue(xVal);
			} else if (node instanceof ConstantValue) {
				ConstantValue cons = (ConstantValue) node;
				if (cons.getValue() == null)
					cons.setValue(RAND.nextInt(10));
			}
		}
	}
}
