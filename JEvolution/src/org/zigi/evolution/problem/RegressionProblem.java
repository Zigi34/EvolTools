package org.zigi.evolution.problem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.zigi.evolution.problem.regression.KeyVariables;
import org.zigi.evolution.solution.Solution;
import org.zigi.evolution.solution.TreeSolution;
import org.zigi.evolution.solution.value.CosFunction;
import org.zigi.evolution.solution.value.DivideFunction;
import org.zigi.evolution.solution.value.GPFenotype;
import org.zigi.evolution.solution.value.MultiplyFunction;
import org.zigi.evolution.solution.value.Node;
import org.zigi.evolution.solution.value.NumberGenotyp;
import org.zigi.evolution.solution.value.NumericConstant;
import org.zigi.evolution.solution.value.NumericVariable;
import org.zigi.evolution.solution.value.PowerFunction;
import org.zigi.evolution.solution.value.SinFunction;
import org.zigi.evolution.solution.value.SubtractionFunction;
import org.zigi.evolution.solution.value.SumFunction;
import org.zigi.evolution.util.Population;

public class RegressionProblem extends TreeProblem {
	private HashMap<KeyVariables, Double> dataset = new HashMap<>();
	private Integer dimension;
	private static final String VARIABLE_NAMES = "abcdefghijklmnopqrstuvwxyz";
	private static final Random RAND = new Random();

	public static final Logger LOG = Logger.getLogger(RegressionProblem.class);

	public RegressionProblem() {

	}

	/**
	 * Load training dataset from file
	 * 
	 * @param file
	 *            file with dataset
	 * @param splitter
	 *            splitting character
	 */
	public void loadDataset(File file, String splitter) {
		if (file == null) {
			LOG.error("File is null");
			return;
		} else if (!file.exists()) {
			LOG.error("File is not exist");
			return;
		}

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(splitter);
				if (this.dimension == null)
					this.dimension = values.length - 1;

				KeyVariables key = new KeyVariables(this.dimension);
				for (int i = 0; i < values.length - 1; i++) {
					Double keyValue = Double.parseDouble(values[i]);
					key.addKey(keyValue);
				}
				dataset.put(key, Double.parseDouble(values[values.length - 1]));
			}
			br.close();
		} catch (FileNotFoundException e) {
			LOG.error(e);
		} catch (IOException e) {
			LOG.error(e);
		}

		// pridame proměnné do seznamu terminalu
		for (int i = 0; i < dimension; i++)
			addFenotype(new NumericVariable(String.valueOf(VARIABLE_NAMES.charAt(i))));

	}

	/**
	 * Aplikuje operaci vybraného uzlu s operátory v podobě podřízených uzlů
	 * 
	 * @param functionNode
	 * @return
	 */
	private Double applyFunction(Node functionNode) {
		if (functionNode == null)
			return null;
		GPFenotype function = functionNode.getValue();
		if (function instanceof SumFunction) {
			List<Node> childs = functionNode.getChilds();
			Double var1 = (Double) childs.get(0).getValue().getValue();
			Double var2 = (Double) childs.get(1).getValue().getValue();
			return var1 + var2;
		} else if (function instanceof SubtractionFunction) {
			List<Node> childs = functionNode.getChilds();
			Double var1 = (Double) childs.get(0).getValue().getValue();
			Double var2 = (Double) childs.get(1).getValue().getValue();
			return var1 - var2;
		} else if (function instanceof DivideFunction) {
			List<Node> childs = functionNode.getChilds();
			Double var1 = (Double) childs.get(0).getValue().getValue();
			Double var2 = (Double) childs.get(1).getValue().getValue();
			return var1 / var2;
		} else if (function instanceof MultiplyFunction) {
			List<Node> childs = functionNode.getChilds();
			Double var1 = (Double) childs.get(0).getValue().getValue();
			Double var2 = (Double) childs.get(1).getValue().getValue();
			return var1 * var2;
		} else if (function instanceof PowerFunction) {
			List<Node> childs = functionNode.getChilds();
			Double var1 = (Double) childs.get(0).getValue().getValue();
			Double var2 = (Double) childs.get(1).getValue().getValue();
			return Math.pow(var1, var2);
		} else if (function instanceof SinFunction) {
			List<Node> childs = functionNode.getChilds();
			Double var1 = (Double) childs.get(0).getValue().getValue();
			return Math.sin(var1);
		} else if (function instanceof CosFunction) {
			List<Node> childs = functionNode.getChilds();
			Double var1 = (Double) childs.get(0).getValue().getValue();
			return Math.cos(var1);
		} else if (function instanceof NumericConstant) {
			return (Double) function.getValue();
		} else if (function instanceof NumberGenotyp) {
			return (Double) function.getValue();
		}
		return null;
	}

	@Override
	public Double evaluate(Solution sol) {
		if (sol instanceof TreeSolution) {
			TreeSolution originalTree = (TreeSolution) sol.cloneMe();

			Double difference = 0.0;

			for (KeyVariables key : dataset.keySet()) {
				TreeSolution tree = (TreeSolution) originalTree.cloneMe();
				List<NumericVariable> variables = variables(tree);
				for (int i = 0; i < variables.size(); i++)
					variables.get(i).setValue(key.getKey(i));

				List<Node> leaves = null;
				while (!(leaves = tree.leaveNodes()).isEmpty() && tree.getNodes().size() > 1) {
					// LOG.info(tree);

					for (Node item : leaves) {
						Node parent = item.getParent();
						if (parent == null)
							break;

						// testing for leaves
						boolean valid = true;
						for (Node node : parent.getChilds()) {
							if (!node.isLeaf()) {
								valid = false;
								break;
							}
						}

						if (valid) {
							// get numeric value
							Double val = applyFunction(parent);
							tree.removeSubTree(parent);
							tree.addGenotype(new NumericConstant(val));
							break;
						}
					}
				}
				Double result = (Double) tree.getRoot().getValue().getValue();
				difference += Math.abs(result - dataset.get(key));
			}
			return difference;
		}
		return null;
	}

	@Override
	public void evaluate(Population pop) {
		for (Solution sol : pop.getSolutions())
			evaluate(sol);
	}

	/**
	 * Vraci seznam promennych, ktere se ve strome vyskytuji
	 * 
	 * @param tree
	 * @return
	 */
	public List<NumericVariable> variables(TreeSolution tree) {
		List<NumericVariable> list = new LinkedList<NumericVariable>();
		for (Node node : tree.deepNodes()) {
			if (node.getValue() instanceof NumericVariable && !list.contains(node.getValue()))
				list.add((NumericVariable) node.getValue());
		}
		return list;
	}
}
